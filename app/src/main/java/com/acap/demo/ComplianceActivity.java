package com.acap.demo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;

import com.acap.demo.utils.AndroidId;
import com.acap.demo.utils.GetMac;
import com.acap.demo.utils.PackageList;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/11/12 23:43
 * </pre>
 *
 * @author A·Cap
 */
public class ComplianceActivity extends Activity {

    private interface Action {
        void call() throws Throwable;
    }

    private void setButton(int id, String txt, Action runnable) {
        Button viewById = findViewById(id);
        viewById.setText(txt);
        viewById.setOnClickListener(v -> {
            try {
                runnable.call();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compliance);

        setButton(R.id.view_1, "获得Mac", () -> GetMac.getLocalMacAddressFromIp());
        setButton(R.id.view_2, "获得AndroidId", () -> AndroidId.getAndroidId(this));
        setButton(R.id.view_2, "获得PakcageList", () -> PackageList.getList(this));
    }

    private void getMacAddress() {
        tryCatch(() -> GetMac.getNewMac());
        tryCatch(() -> GetMac.getLocalMacAddressFromIp());
        tryCatch(() -> GetMac.tryGetWifiMac(this));

        tryCatch(() -> AndroidId.getAndroidId(this));
        tryCatch(() -> AndroidId.getUUID());
        tryCatch(() -> AndroidId.getIMEI(this));
        tryCatch(() -> AndroidId.getIMSI(this));

        tryCatch(() -> PackageList.getList(this));

        new Dialog(this).show();
//        Lifecycle
    }


    private void tryCatch(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
        }
    }

    @FunctionalInterface
    public interface Runnable {
        void run() throws Exception;
    }

}
