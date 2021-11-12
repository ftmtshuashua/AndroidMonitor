package com.acap.demo;

import android.app.Activity;
import android.os.Bundle;

/**
 * <pre>
 * Tip:
 *
 * Created by A·Cap on 2021/10/13 15:31
 * </pre>
 */
public class Activity2 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        findViewById(R.id.view_FinishSelf).setOnClickListener(v -> finish());
    }

}
