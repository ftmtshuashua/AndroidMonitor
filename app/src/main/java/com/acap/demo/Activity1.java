package com.acap.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.acap.wfma.interior.Logs;

import java.text.MessageFormat;


public class Activity1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);
        findViewById(R.id.view_FinishSelf).setOnClickListener(v -> finish());
        findViewById(R.id.view_StartActivity2).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        findViewById(R.id.view_ComplianceActivity).setOnClickListener(v -> startActivity(new Intent(this, ComplianceActivity.class)));
    }


}