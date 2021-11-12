package com.acap.demo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/10/27 15:59
 * </pre>
 */
public class PackageList {

    public static void getList(Context context){
        context.getPackageManager().getInstalledPackages(0);
    }
}
