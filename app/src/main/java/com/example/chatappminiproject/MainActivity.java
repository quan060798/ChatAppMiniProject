package com.example.chatappminiproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button login, register;
    FirebaseUser firebaseUser;

    final int SETTINGS_ACTIVITY = 1;
    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            Intent intent = new Intent(MainActivity.this, homepage.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.btnlogin);
        register = findViewById(R.id.btnregister);





        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2login = new Intent(MainActivity.this, Login.class);
                startActivity(intent2login);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2register = new Intent(MainActivity.this, Register.class);
                startActivity(intent2register);
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

            case R.id.setting:
                startActivityForResult(new Intent(MainActivity.this, SettingsPreference.class), SETTINGS_ACTIVITY);
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