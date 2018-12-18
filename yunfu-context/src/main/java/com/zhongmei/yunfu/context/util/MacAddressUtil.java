package com.zhongmei.yunfu.context.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.net.NetworkInterface;

public class MacAddressUtil {

    /**
     * android 6.0及以上、7.0以下 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        // 如果是6.0以下，直接通过wifimanager获取
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            String macAddress0 = getMacAddress0(context);
            if (!TextUtils.isEmpty(macAddress0)) {
                return macAddress0;
            }
        }

        String macSerial = "";
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName("wlan0");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("eth0");
                if ("MuMu".equalsIgnoreCase(Build.MODEL)) {
                    networkInterface = NetworkInterface.getByName("eth1");
                }
            }

            if (networkInterface != null) {
                byte[] addressByte = networkInterface.getHardwareAddress();
                StringBuilder sb = new StringBuilder();
                for (byte b : addressByte) {
                    sb.append(String.format("%02X:", b));
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }

                macSerial = sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("----->" + "NetInfoManager", "getMacAddress:" + e.toString());
        }
        return macSerial;
    }

    private static String getMacAddress0(Context context) {
        if (isAccessWifiStateAuthorized(context)) {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = null;
            try {
                wifiInfo = wifiMgr.getConnectionInfo();
                return wifiInfo.getMacAddress();
            } catch (Exception e) {
                Log.e("----->" + "NetInfoManager", "getMacAddress0:" + e.toString());
            }
        }
        return "";

    }

    /**
     * Check whether accessing wifi state is permitted
     *
     * @param context
     * @return
     */
    private static boolean isAccessWifiStateAuthorized(Context context) {
        if (PackageManager.PERMISSION_GRANTED == context
                .checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE")) {
            Log.e("----->" + "NetInfoManager",
                    "isAccessWifiStateAuthorized:" + "access wifi state is enabled");
            return true;
        }
        return false;
    }
}
