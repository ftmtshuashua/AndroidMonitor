package com.acap.compliance;

import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.acap.compliance.interior.Logs;
import com.acap.hook.HookModel;
import com.acap.hook.OnBeforeHookedMethod;
import com.swift.sandhook.xposedcompat.XposedCompat;

import java.net.NetworkInterface;
import java.util.UUID;

/**
 * <pre>
 * Tip:
 *      合规检测
 *
 * Created by ACap on 2021/11/13 14:09
 * </pre>
 *
 * @author A·Cap
 */
public class Compliance {

    private static boolean mInit = false;

    private Compliance() {
    }

    /**
     * Start the monitor, It tells you who shut down your activity
     */
    public static void start() {
        if (mInit) {
            throw new IllegalStateException("The monitor is started!");
        }
        mInit = true;
        Logs.i("Monitor starting ,Privacy compliance!");

        try {
            init();
        } catch (Throwable e) {
            Logs.e(e);
        }

        moinitorMac();
        moinitorAndroidId();
        moinitorOaid();
        moinitorPackageList();
        moinitorGps();
    }

    private static void init() throws Throwable {
        XposedCompat.classLoader = ClassLoader.getSystemClassLoader();
    }

    private static void moinitorMac() {
        new HookModel(WifiInfo.class).setMethod("getMacAddress")
                .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.call("Mac -> WifiInfo.getMacAddress()"));
        new HookModel(NetworkInterface.class).setMethod("getHardwareAddress")
                .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.call("Mac -> NetworkInterface.getHardwareAddress()"));
        new HookModel(WifiManager.class).setMethod("getScanResults")
                .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.call("Mac -> WifiManager.getScanResults()"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            new HookModel(BluetoothLeScanner.class).setMethod("startScan", ScanCallback.class)
                    .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.call("Mac -> BluetoothLeScanner.startScan()"));
        }
    }

    private static void moinitorAndroidId() {
        new HookModel(Settings.System.class).setMethod("getString", ContentResolver.class, String.class)
                .findAndHookMethod((OnBeforeHookedMethod) param -> {
                    if (Settings.System.ANDROID_ID.equals(param.args[1])) {
                        Logs.call("AndroidId -> Settings.System.getString(Settings.System.ANDROID_ID)");
                    }
                });
        new HookModel(UUID.class).setMethod("randomUUID")
                .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.call("AndroidId -> UUID.randomUUID()"));
        new HookModel(TelephonyManager.class).setMethod("getImei")
                .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.call("AndroidId -> TelephonyManager.getImei()"));
        new HookModel(TelephonyManager.class).setMethod("getDeviceId")
                .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.call("AndroidId -> TelephonyManager.getDeviceId()"));
        new HookModel(TelephonyManager.class).setMethod("getMeid")
                .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.call("AndroidId -> TelephonyManager.getMeid()"));
        new HookModel(TelephonyManager.class).setMethod("getSimSerialNumber")
                .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.call("AndroidId -> TelephonyManager.getSimSerialNumber()"));
        new HookModel(TelephonyManager.class).setMethod("getSubscriberId") //IMSI
                .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.call("AndroidId -> TelephonyManager.getSubscriberId()"));
    }

    private static void moinitorOaid() {
        try {
            new HookModel("com.bun.miitmdid.core.MdidSdkHelper")
                    .setMethod("InitSdk", Context.class, boolean.class, HookModel.getClass("com.bun.miitmdid.interfaces.IIdentifierListener"))
                    .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.call("Oaid -> MdidSdkHelper.InitSdk()"));
        } catch (Throwable e) {
            Logs.e(e);
        }
    }


    private static void moinitorPackageList() {
        try {
            new HookModel("android.app.ApplicationPackageManager")
                    .setMethod("getInstalledPackages", int.class)
                    .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.call("PacakgeList -> PackageManager.getInstalledPackages()"));
        } catch (Throwable e) {
            Logs.e(e);
        }
    }

    private static void moinitorGps() {
    }
}
