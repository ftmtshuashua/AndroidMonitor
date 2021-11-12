package com.acap.demo.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 * <pre>
 * Tip:
 *      获得Mac地址
 *
 * Created by A·Cap on 2021/10/27 15:18
 * </pre>
 */
public class GetMac {

    /**
     * 通过网络接口取
     *
     * @return
     */
    public static String getNewMac() throws Exception {
        ArrayList<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
        for (NetworkInterface nif : all) {
            if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
            byte[] macBytes = nif.getHardwareAddress();
            if (macBytes == null) {
                return null;
            }
            StringBuilder res1 = new StringBuilder();
            for (byte b : macBytes) {
                res1.append(String.format("%02X:", b));
            }
            if (res1.length() != 0) {
                res1.deleteCharAt(res1.length() - 1);
            }
            return res1.toString();
        }
        return null;
    }


    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    public static String getLocalMacAddressFromIp() throws SocketException {
        String strMacAddr = null;
        //获得IpD地址
        InetAddress ip = getLocalInetAddress();
        byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            if (i != 0) {
                buffer.append(':');
            }
            String str = Integer.toHexString(b[i] & 0xFF);
            buffer.append(str.length() == 1 ? 0 + str : str);
        }
        strMacAddr = buffer.toString().toUpperCase();
        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() throws SocketException {
        InetAddress ip = null;
        //列举
        Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
        while (en_netInterface.hasMoreElements()) {//是否还有元素
            NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
            Enumeration<InetAddress> en_ip = ni.getInetAddresses();//得到一个ip地址的列举
            while (en_ip.hasMoreElements()) {
                ip = en_ip.nextElement();
                if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                    break;
                else
                    ip = null;
            }
            if (ip != null) {
                break;
            }
        }
        return ip;
    }


    /**
     * 通过WiFiManager获取mac地址
     *
     * @param context
     * @return
     */
    public static String tryGetWifiMac(Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();
        if (wi == null || wi.getMacAddress() == null) {
            return null;
        }
        if ("02:00:00:00:00:00".equals(wi.getMacAddress().trim())) {
            return null;
        } else {
            return wi.getMacAddress().trim();
        }
    }
}
