package com.learning.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.learning.whatsapp.Adapters.FragmentsAdapter;
import com.learning.whatsapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        //set fragment manager and tablayout
        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));                    //Fragment adapter for all three fragments in main activity on viewPager
        binding.tablayout.setupWithViewPager(binding.viewPager);                                            //setup viewPager of tablayout

    }

    //To get back to home after pressing back button at start activity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {             //createOptionmenu use to creae menu
        MenuInflater inflater = getMenuInflater();              //Use MenuInflater
        inflater.inflate(R.menu.menu,menu);                        //Use inflater to inflate your menu xml file into contentView
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {                  //onOptionsItemSelected
        switch(item.getItemId()){

            case R.id.setting:
                Intent intent2 = new Intent(MainActivity.this,Settings_Activity.class);
                startActivity(intent2);
                break;

            case R.id.logout:
                auth.signOut();                                 //call auth.signOut method to  logout as an user
                Intent intent = new Intent(MainActivity.this,SignIn.class);
                startActivity(intent);
                break;
            case R.id.groupChat:
                Intent intent1 = new Intent(MainActivity.this,GroupChatActivity.class);
                startActivity(intent1);
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}