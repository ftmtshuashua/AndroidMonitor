package com.acap.fix;

import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.acap.fix.hook.HookModel;
import com.acap.fix.hook.OnBeforeHookedMethod;
import com.acap.fix.utils.LogUtils;

import java.net.NetworkInterface;
import java.util.UUID;

/**
 * <pre>
 * Tip:
 *      隐私合规监测
 *
 * Created by A·Cap on 2021/10/27 15:08
 * </pre>
 */
public class AppPrivacyComplianceMonitor {
    public static boolean mIsStack = false;

    public static final void init() {
        //Mac监测
        new HookModel(WifiInfo.class).setMethod("getMacAddress")
                .findAndHookMethod((OnBeforeHookedMethod) param -> print("隐私合规监测 -> Call WifiInfo.getMacAddress()"));
        new HookModel(NetworkInterface.class).setMethod("getHardwareAddress")
                .findAndHookMethod((OnBeforeHookedMethod) param -> print("隐私合规监测 -> Call NetworkInterface.getHardwareAddress()"));
        new HookModel(WifiManager.class).setMethod("getScanResults")
                .findAndHookMethod((OnBeforeHookedMethod) param -> print("隐私合规监测 -> Call WifiManager.getScanResults()"));
        new HookModel(BluetoothLeScanner.class).setMethod("startScan", ScanCallback.class)
                .findAndHookMethod((OnBeforeHookedMethod) param -> print("隐私合规监测 -> Call BluetoothLeScanner.startScan()"));
//        BluetoothDevice.ACTION_FOUND

        //Andorid ID
        new HookModel(Settings.System.class).setMethod("getString", ContentResolver.class, String.class)
                .findAndHookMethod((OnBeforeHookedMethod) param -> {
                    if (Settings.System.ANDROID_ID.equals(param.args[1])) {
                        print("隐私合规监测 -> Call Settings.System.getString(Settings.System.ANDROID_ID)");
                    }
                });
        new HookModel(UUID.class).setMethod("randomUUID")
                .findAndHookMethod((OnBeforeHookedMethod) param -> print("隐私合规监测 -> Call UUID.randomUUID()"));
        new HookModel(TelephonyManager.class).setMethod("getImei")
                .findAndHookMethod((OnBeforeHookedMethod) param -> print("隐私合规监测 -> Call TelephonyManager.getImei()"));
        new HookModel(TelephonyManager.class).setMethod("getDeviceId")
                .findAndHookMethod((OnBeforeHookedMethod) param -> print("隐私合规监测 -> Call TelephonyManager.getDeviceId()"));
        new HookModel(TelephonyManager.class).setMethod("getMeid")
                .findAndHookMethod((OnBeforeHookedMethod) param -> print("隐私合规监测 -> Call TelephonyManager.getMeid()"));
        new HookModel(TelephonyManager.class).setMethod("getSimSerialNumber")
                .findAndHookMethod((OnBeforeHookedMethod) param -> print("隐私合规监测 -> Call TelephonyManager.getSimSerialNumber()"));
        new HookModel(TelephonyManager.class).setMethod("getSubscriberId") //IMSI
                .findAndHookMethod((OnBeforeHookedMethod) param -> print("隐私合规监测 -> Call TelephonyManager.getSubscriberId()"));


        //应用列表
        try {
            new HookModel(Class.forName("android.app.ApplicationPackageManager")).setMethod("getInstalledPackages", int.class)
                    .findAndHookMethod((OnBeforeHookedMethod) param -> print("隐私合规监测 -> Call PackageManager.getInstalledPackages()"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //OAID
        try {
            new HookModel(Class.forName("  com.bun.miitmdid.core.MdidSdkHelper")).setMethod("InitSdk", Context.class, boolean.class, Class.forName("com.bun.miitmdid.interfaces.IIdentifierListener"))
                    .findAndHookMethod((OnBeforeHookedMethod) param -> print("隐私合规监测 -> Call MdidSdkHelper.InitSdk()"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //位置信息
    }

    private static void print(String msg) {
        if (mIsStack) {
            LogUtils.trim(new Exception(msg));
        }else{
            LogUtils.e(msg);
        }
    }

}
