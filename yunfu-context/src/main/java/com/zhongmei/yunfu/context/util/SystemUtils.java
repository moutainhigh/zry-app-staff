package com.zhongmei.yunfu.context.util;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.context.Constant;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Created by demo on 2018/12/15
 */

public class SystemUtils {
    private static final String TAG = SystemUtils.class.getSimpleName();

    public static String getMacAddress() {
        // 先从sp中去mac地址。
        SharedPreferenceUtil spUtil = SharedPreferenceUtil.getSpUtil();
        String mMacAddress = spUtil.getString(CommonConstant.SP_DEVICE_ID, "");
        if (TextUtils.isEmpty(mMacAddress)) {
            mMacAddress = MacAddressUtil.getMacAddress(BaseApplication.getInstance());
            spUtil.putString(CommonConstant.SP_DEVICE_ID, mMacAddress);
        }

        return mMacAddress;
    }

    /**
     * @Title: genOnlyIdentifier
     * @Description: 产生一个唯一性的标识
     * @Param @return TODO
     * @Return String 返回类型
     */
    public static String genOnlyIdentifier() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * @Title: getVersionCode
     * @Description: 获取版本编号
     * @Param @return TODO
     * @Return String 返回类型
     */
    public static String getVersionCode() {
        return getVersionCode(null);
    }

    public static String getVersionCode(String packageName) {
        PackageInfo info = null;
        try {
            if (packageName == null) {
                info = BaseApplication.sInstance.getPackageManager().getPackageInfo(BaseApplication.sInstance.getPackageName(), 0);
            } else {
                info = BaseApplication.sInstance.getPackageManager().getPackageInfo(packageName, 0);
            }
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
        return String.valueOf(info.versionCode);
    }

    /**
     * @Title: getVersionName
     * @Description: 获取版本名称
     * @Param @return TODO
     * @Return String 返回类型
     */
    public static String getVersionName() {
        PackageInfo info = null;
        try {
            info = BaseApplication.sInstance.getPackageManager().getPackageInfo(BaseApplication.sInstance.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "", e);
            return "";
        }
        return info.versionName;
    }

    /**
     * @Title: getSystemType
     * @Description: 获取系统类型
     * @Param @return TODO
     * @Return String 返回类型
     */
    public static String getSystemType() {
        return "android";
    }

    /**
     * @Title: getAppType
     * @Description: 获取app类型
     * @Param @return TODO
     * @Return String 返回类型
     */
    public static String getAppType() {
        return "5";
    }

    /**
     * 生成一个交易单号。生成规则：
     * https://tower.im/projects/af99e92345b6418cb22715d48c139e59/uploads/a42c5e82710d45bea5dae62fab295927
     *
     * @return
     */
    public static String getBillNumber() {
        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmssSSS", Locale.getDefault());
        return "101" + df.format(new Date()) + ShopInfoCfg.getInstance().getTabletNumberFormat();
    }

    public static String getPrinterServer() {
        return "http://"
                + SharedPreferenceUtil.getSpUtil().getString(Constant.SP_PRINT_IP_ADDRESS, "192.168.1.1")
                + ":8080/";
    }

    public static void setAutoTime(ContentResolver resolver) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            //系统不让更改，待定
//			android.provider.Settings.Global.putInt(resolver, android.provider.Settings.Global.AUTO_TIME, 1);
        } else {
            Settings.System.putInt(resolver, Settings.System.AUTO_TIME, 1);
        }
    }

    public static void setTime1224(ContentResolver resolver) {
        String timeFormat = Settings.System.getString(resolver, Settings.System.TIME_12_24);
        if (timeFormat.equals("12")) {
            Settings.System.putString(resolver, Settings.System.TIME_12_24, "24");
        }
    }

    public static void exitPrintServer() {
        ActivityManager am = (ActivityManager) BaseApplication.sInstance.getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses("com.demo.print");
    }

    public static void deleteRootDir() {
        String rootpath = Environment.getExternalStorageDirectory().getPath();
        if (!rootpath.endsWith("/")) {
            rootpath += "/";
        }
        File file = new File(rootpath + "zhongmei/");
        deleteAllFiles(file);
    }

    public static void deleteAllinpayDir() {
        String rootpath = Environment.getExternalStorageDirectory().getPath();
        if (!rootpath.endsWith("/")) {
            rootpath += "/";
        }
        File file = new File(rootpath + "ALLINPAY/");
        deleteAllFiles(file);
    }

    /**
     * 删除目录下所有文件
     *
     * @param root
     */
    public static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }
            }
    }

    public static void delete(File file) {
        if (!file.exists())
            return;
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    /**
     * 获取程序流量总接受量，单位为KB
     */
    public long getTotalRxKB() {
        long rxBytes = TrafficStats.getTotalRxBytes();//全部接收的字节数
        return byte2KB(rxBytes);
    }

    /**
     * 获取程序流量总上传量，单位为KB
     */
    public long getTotalTxKB() {
        long txBytes = TrafficStats.getTotalTxBytes();//全部上传的字节数
        return byte2KB(txBytes);
    }

    private long byte2KB(long bytes) {
        return bytes / 1024;
    }

    /**
     * 获取每个app的上传和下载流量
     */
    public void getAppBytes() {
        Context context = BaseApplication.sInstance;
        //拿到包管理者
        PackageManager pm = context.getPackageManager();
        //拿到包的信息  PackageInfo是系统的一个类
        List<PackageInfo> info = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
                | PackageManager.GET_PERMISSIONS);
        List<Map<String, String>> appInfo = new ArrayList<>();//用于存放App的名称，上传和下载的字节
        /**
         * 通过看它有没有网络权限权限
         */
        for (PackageInfo temp : info) {
            String permissions[] = temp.requestedPermissions;    //拿到该包的权限
            if (permissions != null && permissions.length > 0) {   //如果有权限
                for (String p : permissions) {
                    if (p.equals("android.permission.INTERNET")) {//是否有网络权限
                        String appName = temp.applicationInfo.loadLabel(pm).toString();       //App名称
                        long appMrb = TrafficStats.getUidRxBytes(temp.applicationInfo.uid);//App接收的字节
                        long appMtb = TrafficStats.getUidTxBytes(temp.applicationInfo.uid);//App发送的字节
                        String str = appName + "下载：" + byte2KB(appMrb) + "上传:" + byte2KB(appMtb);
                        Map<String, String> map = new HashMap<>();
                        map.put("info", str);
                        appInfo.add(map);
                    }
                }
            }
        }
    }

}
