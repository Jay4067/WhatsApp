package com.learning.whatsapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learning.whatsapp.Adapters.UsersAdapter;
import com.learning.whatsapp.Models.Users;
import com.learning.whatsapp.R;
import com.learning.whatsapp.databinding.FragmentChatBinding;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    FragmentChatBinding binding;                    //create fragment binding first

    public ChatFragment() {

    }

    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase database;                      //Use database to put details of the users from the database into this fragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();

        // Inflate the layout for this fragment
        binding= FragmentChatBinding.inflate(inflater, container, false);       //<------------(1)

        UsersAdapter adapter = new UsersAdapter(list, getContext());
        binding.chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        //To get data from the database
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {     //addValueEventListener
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot :snapshot.getChildren()){             //DataReference through dataSnapshot
                    Users users = dataSnapshot.getValue(Users.class);               //get value in the user through snapshot
                    users.setUserId(dataSnapshot.getKey());                         //setUserId because send data from the userAdapter to chat detail activity need to define by userid ||  set receiverId base on this method in chatDetail Activity

                    //------------->To remove the user which is login from the recyclerView
                    if(!users.getUserId().equals(FirebaseAuth.getInstance().getUid())) {
                        list.add(users);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return binding.getRoot();                                                           //<-------(2)
    }
}