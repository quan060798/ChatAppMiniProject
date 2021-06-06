package com.example.chatappminiproject.Fragments;

import com.example.chatappminiproject.Notification.MyResponse;
import com.example.chatappminiproject.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAMDli-rA:APA91bEeK8wYyOBu1oq7D6xPWQC8AYLB4Y8EVTkZJFngJESNYO1WZJoD6lHAj_BXRPHHMMdYgUuSlMAYqKfRfJ7ft1dUamNz6K_qAHpLJYFuI0TOebzrsewasxw7O2EP0Fr93CGVKokY"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
