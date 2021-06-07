package com.example.chatappminiproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PostMomentActivity extends AppCompatActivity {
    Uri imguri;
    String myUrl = "";
    StorageTask uploadTask;
    StorageReference storageReference;

    ImageView momentimage;
    Button post, back, choose;
    EditText descript;

    final int SETTINGS_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_post_moment);

        momentimage = findViewById(R.id.moment_photo);
        post = findViewById(R.id.button_post);
        back = findViewById(R.id.button_cancel);
        descript = findViewById(R.id.moment_description);
        choose = findViewById(R.id.button_addmomentimage);

        storageReference = FirebaseStorage.getInstance().getReference("Moments_Photo");


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostMomentActivity.this, homepage.class);
                startActivity(intent);
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMoment();
            }
        });


    }

    private void uploadMoment() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting");
        progressDialog.show();

        if (imguri != null){
            StorageReference file = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imguri));
            uploadTask = file.putFile(imguri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return file.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        myUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Moments");
                        String momentid = reference.push().getKey();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("momentid", momentid);
                        hashMap.put("momentimageurl", myUrl);
                        hashMap.put("description", descript.getText().toString());
                        hashMap.put("poster", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                        String dateText = simpleDateFormat.format(calendar.getTime());
                        hashMap.put("uploadtime", dateText);
                        reference.child(momentid).setValue(hashMap);
                        System.out.println("Moment IMage link: " + myUrl);
                        progressDialog.dismiss();

                        startActivity(new Intent(PostMomentActivity.this, homepage.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(PostMomentActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostMomentActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension (Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== 3 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imguri = data.getData();
            momentimage.setImageURI(imguri);
        }
        if(requestCode == SETTINGS_ACTIVITY){
            this.recreate();
        }
    }

    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"),3);
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
                startActivityForResult(new Intent(PostMomentActivity.this, SettingsPreference.class), SETTINGS_ACTIVITY);
                break;
        }
        return false;
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