package com.example.chatappminiproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class FingerPrint extends AppCompatActivity {

    private TextView authstat;
    private Button authbtn, passbtn;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    public Intent intent;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null)
        {
            Intent intent = new Intent(FingerPrint.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);

        authstat = findViewById(R.id.AuthStat);
        authbtn = findViewById(R.id.authbutton);
        passbtn = findViewById(R.id.usepassword);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(FingerPrint.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                authstat.setText("Authentication Error: " + errString);
                Toast.makeText(FingerPrint.this, "Authentication Error: " + errString, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(FingerPrint.this, "Authenticate Succeed", Toast.LENGTH_SHORT).show();
                intent = new Intent(FingerPrint.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                authstat.setText("Failed Authenticate");
                Toast.makeText(FingerPrint.this, "Authentication FAiled", Toast.LENGTH_SHORT).show();
                intent = new Intent(FingerPrint.this, FingerPrint.class);
                startActivity(intent);
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric Authentication").setSubtitle("Login Using Your Fingerprint").setNegativeButtonText("Use App Pass").build();
        authbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

        passbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(FingerPrint.this, MainActivity.class));
            }
        });

    }
}