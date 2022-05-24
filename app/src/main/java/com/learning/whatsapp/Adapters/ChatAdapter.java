package com.learning.whatsapp.Adapters;

//Create Two ViewHolder one for the receiver message and another for the sender message

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.learning.whatsapp.Models.Messages;
import com.learning.whatsapp.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<Messages> messages;
    Context context;
    String recId; //--------------------------------to delete chat

    //Give Two view model different key_type

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    //--------Create this constructor after

    public ChatAdapter(ArrayList<Messages> messages, Context context, String recId) {
        this.messages = messages;
        this.context = context;
        this.recId = recId;
    }

    public ChatAdapter(ArrayList<Messages> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }


    //Decide if message comes from the sender or receiver and show based on View_Type

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){                                                               //Inflate sender ViewModel
            View view = LayoutInflater.from(context).inflate(R.layout.sender_bg,parent,false);
            return new SenderViewModel(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.reciever_bg,parent,false);
            return new ReceiverViewModel(view);
        }
    }

//below method used in adapter if more than one view types are included in recycler view

    @Override
    public int getItemViewType(int position) {

        if(messages.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())) {       //Check if message comes from sender or receiver
            return SENDER_VIEW_TYPE;
        }else{
            return  RECEIVER_VIEW_TYPE;
        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messages messages1 = messages.get(position);

        //to delete message from the chat recyclerview

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recId;        //To get to the senderRoom node in database changes also made after in the cht details activity

                                database.getReference().child("chats").child(senderRoom)
                                        .child(messages1.getMsgID())
                                        .setValue(null);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        }).show();



                return false;
            }
        });


        //To check message comes from which viewModel
        if(holder.getClass()==SenderViewModel.class){           //getClass firebase function if it's equal sender set message into holder
            ((SenderViewModel)holder).senderMsg.setText(messages1.getMessage());

        }
        else{
            ((ReceiverViewModel)holder).receiverMsg.setText(messages1.getMessage());  //set receiver message
        }


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ReceiverViewModel extends RecyclerView.ViewHolder{

        TextView receiverMsg,receiverTime;

        //There are two different View Model Sender and Receiver

        public ReceiverViewModel(@NonNull View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.receiver_message);
            receiverTime = itemView.findViewById(R.id.receiver_time);
        }
    }

    public class SenderViewModel extends RecyclerView.ViewHolder{

        TextView senderMsg,senderTime;
        public SenderViewModel(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.sender_msg);
            senderTime = itemView.findViewById(R.id.sender_time);
        }
    }
}
