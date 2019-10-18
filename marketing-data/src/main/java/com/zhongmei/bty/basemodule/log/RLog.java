package com.zhongmei.bty.basemodule.log;

import android.os.Environment;
import android.text.TextUtils;

import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.log.LogData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RLog {
    public static final String FOLDER_NAME = "calm_retail";
    public static final String RETAIL_KEY_TAG = "RETAIL_LOG";
        public static final String DISH_KEY_TAG = "DISH_LOG";
    public static final String BAKERY_KEY_TAG = "BAKERY_LOG";

    private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    private static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static String sCurrentDate;
    private static RetailLogAction sLogAction;
    private static boolean versionWrite = false;

    static {
        sLogAction = RetailLogAction.getInstance();
        sLogAction.setDisplayCommand(true);
                sLogAction.setSaveSdcard(true);
        sLogAction.setFolderName(FOLDER_NAME);

        sCurrentDate = getCurrentDate();
        sLogAction.initLogThread();
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


    public static String getCurrentDate() {
        return format.format(new Date());
    }

    private static void writeLog(String tag, String msg) {
        if (!TextUtils.isEmpty(msg) && !TextUtils.isEmpty(tag)) {
            LogData log = new LogData();
            log.setTag(tag);
            log.setMsg(msg);
            log.setTime(format1.format(new Date()));
            log.setType(10);
            sLogAction.putLogData(log);
        }
    }

    private static void log(String tag, String msg) {
        if (!versionWrite || !TextUtils.equals(sCurrentDate, getCurrentDate())) {
            sCurrentDate = getCurrentDate();
            sLogAction.setFolderName(FOLDER_NAME);
                        versionWrite = true;
        }
        writeLog(tag, msg);
    }

    public static String environmentName() {
        String result = ShopInfoCfg.getInstance().getServerKey();
        if (result.contains("test")) {
            result = "testRetail";
        } else if (result.contains("dev")) {
            result = "devRetail";
        } else if (result.contains("gld")) {
            result = "gldRetail";
        } else {
            result = "Retail";
        }
        return result;
    }

        public static void logFlush() {
        for (int i = 0; i < 10; i++) {
            writeEmptyLog();
        }
    }

    public static void writeEmptyLog() {
        sLogAction.putStrLog("");
    }

    public static String rootPath() {
        String rootPath = Environment.getExternalStorageDirectory().getPath();
        if (!rootPath.endsWith("/")) {
            rootPath += "/";
        }
        return rootPath;
    }
}