package com.acap.demo.base;

import android.app.Application;
import android.content.Context;

import com.acap.wfma.WFMA;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/10/13 14:38
 * </pre>
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        WFMA.start();
    }
}
