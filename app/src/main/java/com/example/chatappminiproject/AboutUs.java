package com.example.chatappminiproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AboutUs extends AppCompatActivity {

    ImageButton call,email,facebook;
    TextView chatemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        call = findViewById(R.id.callaboutus);
        email = findViewById(R.id.emailaboutus);
        facebook = findViewById(R.id.facebook);
        chatemail= findViewById(R.id.tv_email);



        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentcall = new Intent(Intent.ACTION_DIAL);
                intentcall.setData(Uri.parse("tel:0125971468"));
                startActivity(intentcall);
            }
        });



        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=100069326701000"));
                startActivity(intent1);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to = chatemail.getText().toString();

                Intent intent2email = new Intent(Intent.ACTION_VIEW);
                intent2email.setType("text/plain");
                intent2email.setType("message/rfc822");
                intent2email.setData(Uri.parse("mailto:"+ to));

                intent2email.putExtra(Intent.EXTRA_SUBJECT, " " );

                startActivity(Intent.createChooser(intent2email,"Send Email"));
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
                startActivity(new Intent(AboutUs.this, homepage.class));
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AboutUs.this, MainActivity.class));
                break;

            case R.id.friend:
                Intent intent2displayuser = new Intent(AboutUs.this, DisplayUser.class);
                startActivity(intent2displayuser);
                break;

            case R.id.aboutus:
                Intent intent2aboutus = new Intent(AboutUs.this, AboutUs.class);
                startActivity(intent2aboutus);
                break;

            case R.id.own_moment:
                Intent intent = new Intent(AboutUs.this, OwnMoment.class);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }


}


