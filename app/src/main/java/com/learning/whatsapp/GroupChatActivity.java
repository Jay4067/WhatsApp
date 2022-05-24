package com.learning.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.whatsapp.Adapters.ChatAdapter;
import com.learning.whatsapp.Models.Messages;
import com.learning.whatsapp.databinding.ActivityGroupChatBinding;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {


    ActivityGroupChatBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.backArrowSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(GroupChatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //Use same adapter as chat Details activity
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final ArrayList<Messages> messages = new ArrayList<>();
        final ChatAdapter adapter = new ChatAdapter(messages,this);

        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.userNameChat.setText("Friends Group");      //for set general name "Friends Group"

        binding.chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager manager =  new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(manager);

        database.getReference().child("Group Chat")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for(DataSnapshot snapshot1 :snapshot.getChildren()){
                            Messages model = snapshot1.getValue(Messages.class);
                            messages.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        //same as chatDetails activity only add fro one node
        binding.btSend.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                Drawable warning = (Drawable)getResources().getDrawable(R.drawable.ic_for_error);       //-----------------this is for experiment
                //if message is empty cannot be send
                if(binding.etTextMsg.getText().toString().isEmpty()){
                    binding.etTextMsg.setError("Enter message",warning);
                }


                final String message = binding.etTextMsg.getText().toString();
                final  Messages model = new Messages(senderId,message);
                model.setTimeStamp(new Date().getTime());
                binding.etTextMsg.setText("");

                database.getReference().child("Group Chat")
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });

            }
        });
    }
}