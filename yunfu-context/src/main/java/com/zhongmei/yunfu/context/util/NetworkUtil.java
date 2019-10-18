package com.zhongmei.yunfu.context.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.context.R;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;

public class NetworkUtil {

    private final static String TAG = NetworkUtil.class.getSimpleName();

    private HashMap<String, String> paramsMap = null;


    private Context mContext;

    public NetworkUtil(Context context) {
        this.mContext = context;
    }


    public void addParams(String strParamName, String strParamValue) {
        if (paramsMap == null)
            paramsMap = new HashMap<String, String>();
        paramsMap.put(strParamName, strParamValue);
    }

    public void setParams(HashMap<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    @SuppressWarnings("deprecation")


    private boolean isUsedWifi() {
        WifiManager wifiManager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
        boolean isUsedWifi = false;        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            isUsedWifi = true;
        }
        if (isUsedWifi && wifiManager.isWifiEnabled()) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean isNetworkConnected() {
        Context context = BaseApplication.sInstance;
        if (context == null) {
            Log.i(TAG, context.getString(R.string.commonmodule_network_error_context_null));
            return false;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        if (networkInfo != null && networkInfo.length > 0) {
            for (int i = 0; i < networkInfo.length; i++) {
                                if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }

        Log.i(TAG, "网络未连接！");
        return false;
    }


    public static void saveIpAddress(Context context) {
        String ipAddress = getIp(context);
        if (!TextUtils.isEmpty(ipAddress)) {
            SpHelper.getDefault().saveIpAddress(ipAddress);
        }
    }


    private static String getIp(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());                return ipAddress;
            }

        }
        return null;
    }

    private static String intIP2StringIP(int ipAddress) {
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }

}
