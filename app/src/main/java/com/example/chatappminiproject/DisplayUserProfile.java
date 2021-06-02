package com.example.chatappminiproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatappminiproject.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayUserProfile extends AppCompatActivity {

    CircleImageView profile_pic, otheruser_pic;
    TextView username, other_username, phone, email;
    Button addfriend;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    String userid;

    Intent intent;

    private Context mContext;
    private List<Users> mUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_profile);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        profile_pic = findViewById(R.id.profile_image);
        username = findViewById(R.id.usernamehome);
        otheruser_pic = findViewById(R.id.userprofile);
        other_username = findViewById(R.id.otherusername);
        phone = findViewById(R.id.tv_displayphone);
        email = findViewById(R.id.tv_displayemail);
        addfriend = findViewById(R.id.btn_addfriend);

        intent = getIntent();
         userid = intent.getStringExtra("userid");

        userInfo();

        checkFriend();




        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                username.setText(users.getUsername());
                Glide.with(DisplayUserProfile.this).load(users.getImageUrl()).into(profile_pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = addfriend.getText().toString();

                if(btn.equals("Add Friend")){
                    FirebaseDatabase.getInstance().getReference().child("Friend").child(firebaseUser.getUid()).child("isFriend").child(userid).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Friend").child(userid).child("UserFriend").child(firebaseUser.getUid()).setValue(true);
                }else if(btn.equals("UnFriend")){
                    FirebaseDatabase.getInstance().getReference().child("Friend").child(firebaseUser.getUid()).child("isFriend").child(userid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Friend").child(userid).child("UserFriend").child(firebaseUser.getUid()).removeValue();

                }
            }
        });

    }

    private void userInfo(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(DisplayUserProfile.this == null){
                    return;
                }

                    Users users = snapshot.getValue(Users.class);




                Picasso.get().load(users.getImageUrl()).into(otheruser_pic);
                other_username.setText(users.getUsername());
                phone.setText(users.getPhone());
                email.setText(users.getEmail());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkFriend(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Friend").child(firebaseUser.getUid()).child("isFriend");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userid).exists()){
                    addfriend.setText("UnFriend");
                }else
                    addfriend.setText("Add Friend");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DisplayUserProfile.this, MainActivity.class));
                break;

            case R.id.friend:
                Intent intent2displayuser = new Intent(DisplayUserProfile.this, DisplayUser.class);
                startActivity(intent2displayuser);
                break;

            case R.id.own_moment:
                Intent intent = new Intent(DisplayUserProfile.this, OwnMoment.class);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }


}