package com.example.chatappminiproject.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chatappminiproject.MainActivity;
import com.example.chatappminiproject.Moments;
import com.example.chatappminiproject.PostMomentActivity;
import com.example.chatappminiproject.R;
import com.example.chatappminiproject.adapter.OtherMomentAdapter;
import com.example.chatappminiproject.homepage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MomentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private OtherMomentAdapter otherMomentAdapter;
    private List<Moments> momentsList;
    private List<String> friendlist;
    ImageButton addicon;
    TextView textaddmoment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moments, container, false);
        recyclerView = view.findViewById(R.id.other_recyler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        momentsList = new ArrayList<>();
        otherMomentAdapter = new OtherMomentAdapter(getContext(), momentsList);
        recyclerView.setAdapter(otherMomentAdapter);
        System.out.println("MOMENT FRAGMENT");
        addicon = view.findViewById(R.id.addmomenticon);
        textaddmoment = view.findViewById(R.id.tv_addmoment);

        addicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostMomentActivity.class);
                startActivity(intent);
            }
        });

        textaddmoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostMomentActivity.class);
                startActivity(intent);
            }
        });
        checkFriend();
        return view;
    }

    private void checkFriend(){
        friendlist = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Friend").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isFriend");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendlist.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    friendlist.add(snapshot1.getKey());

                }

                readMoments();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMoments() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Moments");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                momentsList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Moments moments = snapshot1.getValue(Moments.class);
                    for (String id : friendlist)
                    {
                        if (moments.getPoster().equals(id))
                        {
                            momentsList.add(moments);

                        }
                    }
                }

                otherMomentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}