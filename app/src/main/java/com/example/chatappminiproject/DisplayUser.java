package com.example.chatappminiproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatappminiproject.Model.Users;
import com.example.chatappminiproject.adapter.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayUser extends AppCompatActivity {

    CircleImageView profile_pic;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    final int SETTINGS_ACTIVITY = 1;

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Users> mUsers;

    private EditText ed_Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_display_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recyclerView = findViewById(R.id.rc_displayusers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        profile_pic = findViewById(R.id.profile_image);
        username = findViewById(R.id.usernamehome);
        ed_Search = findViewById(R.id.ed_search);

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(DisplayUser.this, mUsers, true);
        recyclerView.setAdapter(userAdapter);

        readUsers();

        ed_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                username.setText(users.getUsername());
                Glide.with(getApplicationContext()).load(users.getImageUrl()).into(profile_pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchUsers(String search){


        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(search)
                .endAt(search+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    mUsers.add(users);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers() {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(ed_Search.getText().toString().equals("")){
                     mUsers.clear();
                    for (DataSnapshot usersnapshot : snapshot.getChildren()){
                        Users user = usersnapshot.getValue(Users.class);

                        assert user != null;
                        assert firebaseUser !=null;

                        if (!user.getId().equals(firebaseUser.getUid())){
                        mUsers.add(user);
                        }

                    }
                    userAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

  private void status(String status){

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);

    }
    @Override
    protected void onResume(){
        super.onResume();
        status("Online");
    }

    protected void onPause(){
        super.onPause();
        status("Offline");
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
            case R.id.homepage:
                startActivity(new Intent(DisplayUser.this, homepage.class));
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DisplayUser.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;

            case R.id.friend:
                Intent intent2displayuser = new Intent(DisplayUser.this, DisplayUser.class);
                startActivity(intent2displayuser);
                break;

            case R.id.aboutus:
                Intent intent2aboutus = new Intent(DisplayUser.this, AboutUs.class);
                startActivity(intent2aboutus);
                break;

            case R.id.own_moment:
                Intent intent = new Intent(DisplayUser.this, OwnMoment.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.setting:
                startActivityForResult(new Intent(DisplayUser.this, SettingsPreference.class), SETTINGS_ACTIVITY);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SETTINGS_ACTIVITY){
            this.recreate();
        }
    }

    public void setTheme(){
        SharedPreferences SP=PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if(SP.getString("color_choices","Teal").equals("Teal")){
            setTheme(R.style.TealTheme);
        }else {
            setTheme(R.style.DeepPurpleTheme);
        }
    }

}