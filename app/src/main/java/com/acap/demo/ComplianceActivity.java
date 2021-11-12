package com.acap.demo;

import android.app.Activity;
import android.app.Dialog;

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
