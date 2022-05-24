package com.learning.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.learning.whatsapp.Models.Users;
import com.learning.whatsapp.databinding.ActivitySignUpBinding;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;                      //use to bind view attribute to that context for that add library in the gradle file
    private FirebaseAuth auth;                           //create firebase auth
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());       //binding data to view
        setContentView(binding.getRoot());              //change Context to binding.getRoot()

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog  = new ProgressDialog(SignUp.this);
        progressDialog.setTitle("creating Account");
        progressDialog.setMessage("We're creating your account");

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {        //for signup using email and password
            @Override
            public void onClick(View view) {

                progressDialog.show();                                  //onClick show
                auth.createUserWithEmailAndPassword                                             //method
                        (binding.etEmail.getText().toString(), binding.etPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {        //AuthResult gives if any errors occur during program

                                progressDialog.dismiss();           //onComplete if successful dismiss dialog

                                if(task.isSuccessful()){
//                                    FirebaseUser User = auth.getCurrentUser();          //Same work as String id work on line 61
                                    Users user = new Users(binding.etUserName.getText().toString(),         //create constructor to store details of user in database
                                            binding.etEmail.getText().toString(),binding.etPassword.getText().toString());

                                    String id= task.getResult().getUser().getUid();             //TO get UserId from the firebase database
                                    database.getReference().child("Users").child(id).setValue(user);

                                    Toast.makeText(SignUp.this, "User Created Successful", Toast.LENGTH_SHORT).show();
//                                    Intent intent= new Intent(SignUp.this,SignIn.class);
//                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        binding.txtAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this,SignIn.class);
                startActivity(intent);
            }
        });
    }
}