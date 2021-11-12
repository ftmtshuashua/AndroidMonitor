package com.acap.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class Activity1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);
        findViewById(R.id.view_FinishSelf).setOnClickListener(v -> finish());
        findViewById(R.id.view_StartActivity2).setOnClickListener(v -> startActivity(new Intent(this, Activity2.class)));
    }


}