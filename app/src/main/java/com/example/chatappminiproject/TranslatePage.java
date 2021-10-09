package com.example.chatappminiproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.common.MLApplication;
import com.huawei.hms.mlsdk.translate.MLTranslatorFactory;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslateSetting;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslator;

public class TranslatePage extends AppCompatActivity {

    TextView ori, output;
    Button translate;
    MLRemoteTranslator mlRemoteTranslator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_page);
        Intent intent = getIntent();
        String oritext = intent.getStringExtra("oritext");
        ori = findViewById(R.id.originaltext);
        output = findViewById(R.id.outputtext);
        translate = findViewById(R.id.translatebutton);
        ori.setText(oritext);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateFuncion(ori.getText().toString());
            }
        });
    }

    private void translateFuncion(String toString) {
        MLApplication.getInstance().setApiKey("CwEAAAAAUW8YAuFmJN3Mb3JlgbgdsQ0HGP5HwG+jzBZ96XyfmA/Jt+cGeIIixjoJE8/DisA0FldIka27CiLIV7w9U7jwGbTCBB0=");
        MLRemoteTranslateSetting setting = new MLRemoteTranslateSetting.Factory()
                .setSourceLangCode("en")
                .setTargetLangCode("zh")
                .create();
        mlRemoteTranslator = MLTranslatorFactory.getInstance().getRemoteTranslator(setting);
        final Task<String> task = mlRemoteTranslator.asyncTranslate(toString);
        task.addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                output.setText(s);
            }
        });
    }
}