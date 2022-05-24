package com.learning.whatsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.whatsapp.ChatDetailActivity;
import com.learning.whatsapp.Models.Users;
import com.learning.whatsapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {       //<---(2) extends with RecyclerView.


    ArrayList<Users> list;
    Context context;

    public UsersAdapter(ArrayList<Users> list, Context context) {                               //take list as a parameter
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);
    return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Users users = list.get(position);                           //to get position in the recyclerView and also help to set the receiverId through putExtra method
        Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.profile)
                .into(holder.image);
         holder.userName.setText(users.getUserName());

         //--------Added to show last message on the screen on user chatFragment
        FirebaseDatabase.getInstance().getReference().child("chats")        //used to  get last message from the firebase database
                        .child(FirebaseAuth.getInstance().getUid() + users.getUserId())     //get chat messages from senderId + receiverId node
                                .orderByChild("timestamp")                                  //generally in firebase data entry enters in ascending first come first
                .limitToLast(1)           //--------------show only last message                                                  //we need only one message
                        .addListenerForSingleValueEvent(new ValueEventListener() {          //so we use SingleValueEvent Listner
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChildren()){                                 //put condition to check node is not null
                                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                        holder.lastMessage.setText(snapshot1.child("message").getValue().toString());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

         holder.userParent.setOnClickListener(new View.OnClickListener() {          //to click on the user details
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(context, ChatDetailActivity.class);
                 intent.putExtra("userId",users.getUserId());                   //give details from this adapter to chat details activity
                 intent.putExtra("profilePic",users.getProfilePic());
                 intent.putExtra("userName",users.getUserName());

                 context.startActivity(intent);
             }
         });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{            //In any RecView adapter first create ViewHolder

        ImageView image;
        TextView userName,lastMessage;
        LinearLayout userParent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            userParent = itemView.findViewById(R.id.userParent);
        }
    }
}
