package com.example.chatappminiproject.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.chatappminiproject.MainActivity;
import com.example.chatappminiproject.PostMomentActivity;
import com.example.chatappminiproject.R;
import com.example.chatappminiproject.homepage;


public class MomentsFragment extends Fragment {

    ImageButton addicon;
    TextView textaddmoment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moments, container, false);

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

        return view;
    }
}