package com.learning.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.whatsapp.Adapters.ChatAdapter;
import com.learning.whatsapp.Models.Messages;
import com.learning.whatsapp.databinding.ActivityChatDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        //Fields those we want to save in the firebase database
        final String senderId = auth.getUid();                //To get the current user id through authentication
        String receiverId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");               //Use these getExtra methods to ge data from the userAdapter
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.userNameChat.setText(userName);

        Picasso.get().load(profilePic).placeholder(R.drawable.profile).into(binding.profileImageChat);

        binding.backArrowSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        // MessageModel = messages
        //message = msg
        //model = model

        //set chat adapter for the chat recycler view For more details visit chat adapter
        final ArrayList<Messages> messages = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messages,this,receiverId);
        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        //onCLick Listner on send btn and to store text message what's in the edit text
        //save it using message model constructor

        final String senderRoom = senderId + receiverId; // To set the node in the firebase
        final String receiverRoom = receiverId + senderId;  //To set the node for the receiver chat in the firebase dataase


        //This portion is for showing message in the chat recyclerView after store chat data in the database
        database.getReference().child("chats")
                        .child(senderRoom)          //Use sender room because we show the particular sender his/her message in chat details
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        messages.clear();                                           //clear list to show only last message from the database

                                        for(DataSnapshot snapshot1 : snapshot.getChildren()){           //get data from the firebase through addValueEventListener

                                            //Whenever you want to retrieve data from the firebase create instance of that class
                                            Messages model = snapshot1.getValue(Messages.class);         //get data in the message class from the snapshot.getValue method
                                            model.setMsgID(snapshot1.getKey());    //set messageId that helps you in delete msg

                                            messages.add(model);
                                        }
                                        chatAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });



        binding.btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Drawable warning = (Drawable)getResources().getDrawable(R.drawable.ic_for_error);       //-----------------this is for experiment
//                //if message is empty cannot be send
                if(binding.etTextMsg.getText().toString().isEmpty()){
                    binding.etTextMsg.setError("Enter message");
                    return;
                }

                //----------------Store message in database
               String msg =  binding.etTextMsg.getText().toString();
               final Messages model = new Messages(senderId,msg);       // give final keyword to instance to stay same at every time we used create message instance to store the message and who send it in the database child(chats)
               model.setTimeStamp(new Date().getTime());                  //to store the time stamp of the message
                binding.etTextMsg.setText("");

                database.getReference().child("chats")
                        .child(senderRoom)                              //set value in the sender node in the firebase database
                        .push()                 //Use push when you want to add another node in database [like senderRoom]                             //push method use to set data in the database
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                 database.getReference().child("chats")         //set value in the receiver node in the firebase database
                                         .child(receiverRoom)
                                         .push()
                                         .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                             public void onSuccess(Void unused) {

                                             }
                                         });
                            }
                        });
            }
        });

    }
}