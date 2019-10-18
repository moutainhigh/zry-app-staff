package com.zhongmei.bty.common.util;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.text.TextUtils;

import com.zhongmei.yunfu.BuildConfig;
import com.zhongmei.yunfu.context.util.log.BaseLogAction;
import com.zhongmei.yunfu.context.util.log.LogAction;
import com.zhongmei.yunfu.context.util.log.LogData;

import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressLint("SimpleDateFormat")
public class CLog {
    private static LogAction mLogAction;

    public static final String FOLDER_NAME = "cashier";
    public static final String FS_TAG_KEY = "CashierFSData";    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String mCurrentDate;
    private static boolean versionWrited = false;

    static {
        mLogAction = new LogAction(1);
                if ("release".equals(BuildConfig.BUILD_TYPE)) {
            mLogAction.setDisplayCommand(false);
        } else {
            mLogAction.setDisplayCommand(true);
        }
        setSaveLog(true, FOLDER_NAME);
        mCurrentDate = getCurrentDate();
    }

    public static String getPath() {
        String rootPath = Environment.getExternalStorageDirectory().getPath();
        if (!rootPath.endsWith("/")) {
            rootPath += "/";
        }
        return rootPath + BaseLogAction.S_BRAND_NAME + "/logs/";
    }

    private static void writeLog(String tag, String msg) {
        if (!TextUtils.isEmpty(msg) && !TextUtils.isEmpty(tag)) {
            writeLog(tag, msg, 10);
        }
    }

    private static void writeLog(String tag, String msg, int type) {
        LogData log = new LogData();
        log.setTag(tag);
        log.setMsg(msg);
        log.setTime(getTimeFormatter());
        log.setType(type);
        mLogAction.putLog(log);
    }

    private static String getTimeFormatter() {
        return timeFormat.format(new Date());
    }

    private static void setSaveLog(boolean save, String folderName) {
        mLogAction.setSaveSdcard(save);
        if (mLogAction.canChangeFolderName()) {
            mLogAction.setFolderName(folderName);
        }
    }

    public static void destory() {
        mLogAction.destroy();
    }

    private static void log(String tag, String msg) {
                if (!versionWrited || !TextUtils.equals(mCurrentDate, getCurrentDate())) {
            mCurrentDate = getCurrentDate();
            mLogAction.setFolderName(FOLDER_NAME);
            writeLog("客户端版本号: ", BuildConfig.VERSION_NAME);
            versionWrited = true;
        }

        writeLog(tag, msg);
    }


    public static void d(String tag, String format, Object... param) {
        d(tag, String.format(format, param));
    }


    public static void v(String tag, String format, Object... param) {
        v(tag, String.format(format, param));
    }


    public static void i(String tag, String format, Object... param) {
        i(tag, String.format(format, param));
    }


    public static void w(String tag, String format, Object... param) {
        w(tag, String.format(format, param));
    }


    public static void e(String tag, String format, Object... param) {
        e(tag, String.format(format, param));
    }

    public static void d(String tag, String msg) {
        log("[d]" + tag, msg);
    }

    public static void v(String tag, String msg) {
        log("[v]" + tag, msg);
    }

    public static void i(String tag, String msg) {
        log("[i]" + tag, msg);
    }

    public static void w(String tag, String msg) {
        log("[w]" + tag, msg);
    }

    public static void e(String tag, String msg) {
        log("[e]" + tag, msg);
    }

    private static String getCurrentDate() {
        return dateFormat.format(new Date());
    }
}