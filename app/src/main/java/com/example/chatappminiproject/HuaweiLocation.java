package com.example.chatappminiproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.common.ResolvableApiException;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.HWLocation;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationResult;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsResponse;
import com.huawei.hms.location.LocationSettingsStatusCodes;
import com.huawei.hms.location.SettingsClient;

import java.util.List;

import static android.content.ContentValues.TAG;

public class HuaweiLocation extends AppCompatActivity {

    Intent intentgetuserid;
    public String userid, locationtext;
    Button btn_start, btn_stop, btn_last, btn_address, send;
    TextView result;
    // Define a location provider client.
    private FusedLocationProviderClient fusedLocationProviderClient;
    // Define a device setting client.
    private SettingsClient settingsClient;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huawei_location);
        dynamicPermission();
        intentgetuserid = getIntent();
        userid = intentgetuserid.getStringExtra("userid");
        btn_start = findViewById(R.id.startupdatelocation_btn);
        btn_stop = findViewById(R.id.stopupdatelocation_btn);
        btn_last = findViewById(R.id.getlast_btn);
        btn_address = findViewById(R.id.getaddress_btn);
        send = findViewById(R.id.sendaddressorlocation_btn);
        result = findViewById(R.id.location_result);
        send.setVisibility(View.GONE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
// Create a settingsClient object.
        btn_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastKnownLocation();
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopUpdateLocation();
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdateLocation();
            }
        });

        btn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddress();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLocationMessage();
            }
        });
    }

    private void sendLocationMessage() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null)
                        {
                            return;
                        }
                        String locationlink = "https://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
                        Intent intent = new Intent(HuaweiLocation.this, MessageActivity.class);
                        intent.putExtra("location", locationlink);
                        intent.putExtra("userid", userid);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void getAddress() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setNeedAddress(true);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationProviderClient.getLastLocationWithAddress(mLocationRequest)
                .addOnSuccessListener(new OnSuccessListener<HWLocation>() {
                    @Override
                    public void onSuccess(HWLocation hwLocation) {
                        if (hwLocation != null)
                        {
                            locationtext = "Latitude:" + hwLocation.getLatitude() + "\n Longitude:" + hwLocation.getLongitude();
                            locationtext += "\n City: " + hwLocation.getCity();
                            locationtext += "\n CountryCode:" + hwLocation.getCountryCode();
                            locationtext += "\n PostalCode:" + hwLocation.getPostalCode();
                            result.setText(locationtext);
                        }
                    }
                });
    }

    private void startUpdateLocation() {
        settingsClient = LocationServices.getSettingsClient(this);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        mLocationRequest = new LocationRequest();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Toast.makeText(HuaweiLocation.this, "Successfully Set Location", Toast.LENGTH_SHORT).show();
                        fusedLocationProviderClient
                                .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(HuaweiLocation.this, "YEs", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                int StatusCode = ((ApiException) e).getStatusCode();
                switch (StatusCode){
                    case  LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException rae = (ResolvableApiException) e;
                            rae.startResolutionForResult(HuaweiLocation.this, 0);
                        } catch (IntentSender.SendIntentException sendIntentException) {

                        }
                        break;
                }
            }
        });
        mLocationRequest.setNeedAddress(true);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallback = new LocationCallback(){
            public void onLocationResult(LocationResult locationResult)
            {
                if (locationResult != null){
                        locationtext = "Latitude:" + locationResult.getLastLocation().getLatitude()+
                                "\nLongitude:" + locationResult.getLastLocation().getLongitude();
                    Log.d(TAG, locationtext);
                    result.setText(locationtext);
                }
            }
        };
    }

    private void stopUpdateLocation() {
        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        locationtext = "Successfully stopped Location Updates";
                        Log.d(TAG, locationtext);
                        result.setText(locationtext);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(HuaweiLocation.this, "Failed Stopped Get Location", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getLastKnownLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null)
                        {
                            return;
                        }
                        locationtext = "Lattitude: " + location.getLatitude()+"\n Longitude: " + location.getLongitude();
                        Log.d(TAG, locationtext);
                        result.setText(locationtext);
                        send.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void dynamicPermission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(TAG, "sdk < 28 Q");
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] strings =
                        {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                ActivityCompat.requestPermissions(this, strings, 1);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    "android.permission.ACCESS_BACKGROUND_LOCATION") != PackageManager.PERMISSION_GRANTED){
                String[] strings = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"};
                ActivityCompat.requestPermissions(this, strings, 2);
            }
        }

    }
}