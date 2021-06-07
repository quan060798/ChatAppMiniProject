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
import com.example.chatappminiproject.Model.Chatlist;
import com.example.chatappminiproject.Model.Users;
import com.example.chatappminiproject.Notification.Token;
import com.example.chatappminiproject.R;
import com.example.chatappminiproject.adapter.AllChatAdapter;
import com.example.chatappminiproject.adapter.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AllChatAdapter allChatAdapter;
    private List<Users> mUsers;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    private List<Chatlist> userslist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.chat_recylcer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userslist = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userslist.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Chatlist chatlist = snapshot1.getValue(Chatlist.class);
                    userslist.add(chatlist);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updatToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }

    private void updatToken (String token)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }

    private void chatList() {
        mUsers = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Users users = dataSnapshot.getValue(Users.class);
                    for (Chatlist chatlist : userslist)
                    {

                        if (users.getId() != null && users.getId().equals(chatlist.getId()))
                        {
                            mUsers.add(users);
                            System.out.println("Testing m users add");
                        }
                    }
                }
                allChatAdapter = new AllChatAdapter(getContext(), mUsers,true);
                recyclerView.setAdapter(allChatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}