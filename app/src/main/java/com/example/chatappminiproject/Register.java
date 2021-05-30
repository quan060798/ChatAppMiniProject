package com.example.chatappminiproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private Button btnregister, btnchoose;
    private ImageView profilepic;
    private EditText username, phone, email, password, confirmpass;

    private FirebaseAuth mAuth;
    DatabaseReference reference;

    StorageReference storageReference;
    //private static final int IMAGE_REQUEST = 1;
    private Uri imageuri;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        storageReference = FirebaseStorage.getInstance().getReference("Images");
        mAuth = FirebaseAuth.getInstance();

        btnregister = findViewById(R.id.register);
        username = findViewById(R.id.username);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        confirmpass = findViewById(R.id.pass2);

        btnchoose = findViewById(R.id.choose);
        profilepic = findViewById(R.id.profilepic);

        btnchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_phone = phone.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_confirmpass = confirmpass.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_phone) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_confirmpass)) {
                    Toast.makeText(Register.this, "All field are required", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(Register.this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
                } else if (txt_phone.length() < 6 || txt_phone.length() > 12 ) {
                    Toast.makeText(Register.this, "The phone number is invalid.", Toast.LENGTH_SHORT).show();
                } else if (!txt_password.equals(txt_confirmpass)) {
                    Toast.makeText(Register.this, "The password is different.", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()) {
                    Toast.makeText(getApplicationContext(), "Please provide valid email", Toast.LENGTH_SHORT).show();
                } else
                    register(txt_username, txt_email, txt_phone, txt_password, txt_confirmpass);

                if(uploadTask !=null && uploadTask.isInProgress())
                {
                    Toast.makeText(Register.this, "Uploading", Toast.LENGTH_LONG).show();
                } else
                    Fileuploader();
            }
        });

    }

    private String getExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void Fileuploader() {
        StorageReference Ref = storageReference.child(System.currentTimeMillis()+ "." + getExtension(imageuri));

        uploadTask = Ref.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Uri downloadUrl = Uri.parse(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                        //Toast.makeText(Register.this, "Image Uploaded successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageuri = data.getData();
            profilepic.setImageURI(imageuri);
        }
    }

    private void register (String username, String email, String phone, String password, String confirmpass){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("phone", phone);
                            hashMap.put("email", email);
                            hashMap.put("imageURL", String.valueOf(imageuri));

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent (Register.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });

                            Toast.makeText(Register.this, "Registration done.",Toast.LENGTH_SHORT).show();
                            Intent intent2main = new Intent(Register.this, MainActivity.class);
                            startActivity(intent2main);

                        } else {
                            Toast.makeText(Register.this, "You cannot register with this email or phone.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



        /*register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(username.getText().length()!=0 && phone.getText().length()!=0 && email.getText().length()!=0 && password.getText().length()!=0 && confirmpass.getText().length()!=0 ){

                    String pword = password.getText().toString();
                    String cpword = confirmpass.getText().toString();

                    if (!pword.equals(cpword) )
                    {
                        Toast.makeText(getApplicationContext(), "The password is different",Toast.LENGTH_SHORT).show();
                    }
                    else
                        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                            Toast.makeText(getApplicationContext(), "Please provide valid email",Toast.LENGTH_SHORT).show();
                        else {
                        Toast.makeText(getApplicationContext(), "Registration Success! Please verify your email",Toast.LENGTH_LONG).show();
                        Intent intent2main = new Intent(Register.this, MainActivity.class);
                        startActivity(intent2main);
                    }

                }
                else
                    Toast.makeText(getApplicationContext(), "You need to fill in all info",Toast.LENGTH_SHORT).show();
            }
        });


        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        String userName = username.getText().toString();
                        String Phone = phone.getText().toString();
                        String Email = email.getText().toString();
                        String Password = password.getText().toString();

                        if (task.isSuccessful()) {
                            User user = new User(userName, Phone, Email, Password);
                        }
                    }

                });*/

}
