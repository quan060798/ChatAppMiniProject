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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatappminiproject.Model.Users;
import com.example.chatappminiproject.adapter.OwnMomentAdpater;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OwnMoment extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OwnMomentAdpater ownMomentAdpater;

    CircleImageView profile_pic;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference, momentreference;
    private List<Moments> momentsList;
    Button back;
    final int SETTINGS_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_own_moment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        profile_pic = findViewById(R.id.profile_image);
        username = findViewById(R.id.usernamehome);
        back = findViewById(R.id.goback);

        recyclerView = findViewById(R.id.own_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        momentsList = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        momentreference = FirebaseDatabase.getInstance().getReference("Moments");
        momentreference.orderByChild("poster").equalTo(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot momentSnapshot : snapshot.getChildren())
                {
                    Moments moments = momentSnapshot.getValue(Moments.class);
                    momentsList.add(moments);
                }

                ownMomentAdpater = new OwnMomentAdpater(OwnMoment.this, momentsList);
                recyclerView.setAdapter(ownMomentAdpater);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OwnMoment.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnMoment.this, homepage.class);
                startActivity(intent);
                finish();
            }
        });

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
                startActivity(new Intent(OwnMoment.this, homepage.class));
                break;

            case R.id.friend:
                Intent intent2displayuser = new Intent(OwnMoment.this, DisplayUser.class);
                startActivity(intent2displayuser);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(OwnMoment.this, MainActivity.class));
                finish();
            case R.id.own_moment:
                Toast.makeText(this, "Moments", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OwnMoment.this, OwnMoment.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.setting:
                startActivityForResult(new Intent(OwnMoment.this, SettingsPreference.class), SETTINGS_ACTIVITY);
                break;
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
        SharedPreferences SP= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if(SP.getString("color_choices","Teal").equals("Teal")){
            setTheme(R.style.TealTheme);
        }else {
            setTheme(R.style.DeepPurpleTheme);
        }
    }
}