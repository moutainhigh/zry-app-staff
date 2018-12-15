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

/**
 * Created by demo on 2018/12/15
 * 用来记录打印相关的日志，其它的日志不要用这个
 */
@SuppressLint("SimpleDateFormat")
public class CLog {
    private static LogAction mLogAction;

    public static final String FOLDER_NAME = "cashier";
    public static final String FS_TAG_KEY = "CashierFSData";//正餐日志标志
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String mCurrentDate;
    private static boolean versionWrited = false;

    static {
        mLogAction = new LogAction(1);
        //如果构建
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
        //如果是新的一天，重新生成一个日志文件
        if (!versionWrited || !TextUtils.equals(mCurrentDate, getCurrentDate())) {
            mCurrentDate = getCurrentDate();
            mLogAction.setFolderName(FOLDER_NAME);
            writeLog("客户端版本号: ", BuildConfig.VERSION_NAME);
            versionWrited = true;
        }

        writeLog(tag, msg);
    }

    /**
     * debug日志打印
     *
     * @param tag    tag标签，区分日志类型
     * @param format 格式化字符串，包含%s占位符
     * @param param  格式化字符串中的参数部分
     */
    public static void d(String tag, String format, Object... param) {
        d(tag, String.format(format, param));
    }

    /**
     * 日志打印
     *
     * @param tag    tag标签，区分日志类型
     * @param format 格式化字符串，包含%s占位符
     * @param param  格式化字符串中的参数部分
     */
    public static void v(String tag, String format, Object... param) {
        v(tag, String.format(format, param));
    }

    /**
     * 普通日志打印
     *
     * @param tag    tag标签，区分日志类型
     * @param format 格式化字符串，包含%s占位符
     * @param param  格式化字符串中的参数部分
     */
    public static void i(String tag, String format, Object... param) {
        i(tag, String.format(format, param));
    }

    /**
     * 警告信息日志打印，例如数据异常导致的正常流程终止
     *
     * @param tag    tag标签，区分日志类型
     * @param format 格式化字符串，包含%s占位符
     * @param param  格式化字符串中的参数部分
     */
    public static void w(String tag, String format, Object... param) {
        w(tag, String.format(format, param));
    }

    /**
     * 异常信息，例如exception中出现的关键记录
     *
     * @param tag    tag标签，区分日志类型
     * @param format 格式化字符串，包含%s占位符
     * @param param  格式化字符串中的参数部分
     */
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