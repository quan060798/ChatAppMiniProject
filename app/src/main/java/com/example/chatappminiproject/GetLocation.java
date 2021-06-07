package com.example.chatappminiproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static androidx.constraintlayout.motion.widget.Debug.getLocation;

public class GetLocation extends AppCompatActivity {

    Button btn_getlocation, share;
    TextView tv1, tv2, tv3, tv4, tv5;
    public String locationlink, userid;
    FusedLocationProviderClient fusedLocationProviderClient;
    Intent intentgetuserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        btn_getlocation = findViewById(R.id.btn_getlocation);
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tv4 = findViewById(R.id.tv_4);
        tv5 = findViewById(R.id.tv_5);
        share = findViewById(R.id.btn_sendlocation);
        intentgetuserid = getIntent();
        userid = intentgetuserid.getStringExtra("userid");
        if (locationlink == null)
        {
            share.setVisibility(View.GONE);
        }

        requestPermission();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btn_getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(GetLocation.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        Location location = task.getResult();
                        System.out.println(location.toString());
                        if (location != null) {
                            try {
                                Geocoder geocoder = new Geocoder(GetLocation.this, Locale.getDefault());

                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                tv1.setText(Html.fromHtml("<font color='#6200EE'><b>Latitude : </b><bt></font>" + addresses.get(0).getLatitude()));
                                tv2.setText(Html.fromHtml("<font color='#6200EE'><b>Longitude : </b><bt></font>" + addresses.get(0).getLongitude()));
                                tv3.setText(Html.fromHtml("<font color='#6200EE'><b>Country : </b><bt></font>" + addresses.get(0).getCountryName()));
                                tv4.setText(Html.fromHtml("<font color='#6200EE'><b>Locality : </b><bt></font>" + addresses.get(0).getLocality()));
                                tv5.setText(Html.fromHtml("<font color='#6200EE'><b>Address : </b><bt></font>" + addresses.get(0).getAddressLine(0)));
                                locationlink = "https://maps.google.com/?q=" + addresses.get(0).getLatitude() + "," + addresses.get(0).getLongitude();
                                share.setVisibility(View.VISIBLE);
                                System.out.println(locationlink);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });




        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetLocation.this, MessageActivity.class);
                intent.putExtra("location", locationlink);
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });
    }

    private  void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[] {ACCESS_FINE_LOCATION},1);
    }
}


