package com.example.chatappminiproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatappminiproject.Model.Chat;
import com.example.chatappminiproject.Model.Users;
import com.example.chatappminiproject.R;
import com.example.chatappminiproject.adapter.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Users> users;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    private List<String> userslist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.chat_recylcer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userslist = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userslist.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Chat chat = snapshot1.getValue(Chat.class);

                    if(chat.getSender().equals(firebaseUser.getUid()))
                    {
                        userslist.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(firebaseUser.getUid()))
                    {
                        userslist.add(chat.getSender());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void readChats(){
        users = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Users musers = snapshot1.getValue(Users.class);

                    for (String id : userslist)
                    {
                        if(musers.getId().equals(id))
                        {
                            if (users.size()!=0)
                            {
                                for (Users users1 : users)
                                {
                                    if (!musers.getId().equals(users1.getId()))
                                    {
                                        users.add(musers);
                                    }
                                }
                            }
                            else
                            {
                                users.add(musers);
                            }
                        }
                    }
                }

                userAdapter = new UserAdapter(getContext(), users, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}