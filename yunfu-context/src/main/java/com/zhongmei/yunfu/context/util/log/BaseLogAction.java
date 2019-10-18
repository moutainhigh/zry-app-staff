package com.zhongmei.yunfu.context.util.log;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public abstract class BaseLogAction {
    public static final String TAG = BaseLogAction.class.getSimpleName();
    public static final String S_BRAND_NAME = "zhongmei";

    private boolean canChangeFolderName;
    protected boolean saveSdcard;
    private boolean displayCommand;
    protected String folderPath;
    protected String logPath;

    public BaseLogAction() {
        canChangeFolderName = true;
        saveSdcard = false;
        displayCommand = false;
        this.folderPath = (Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                S_BRAND_NAME + File.separator + "logs" + File.separator + "default");
        this.logPath = (this.folderPath + File.separator + getCurrentDate() + ".log");
    }

    public void putLogData(LogData log) {
        if (this.displayCommand) {
            Log.i(this.getClass().getSimpleName(), log.getTime() + ":" + log.getTag() + ":" + log.getMsg());
        }
        putStrLog(log.toString());
    }

    private String getCurrentDate() {
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        return format1.format(new Date());
    }

    public void setSaveSdcard(boolean save) {
        this.saveSdcard = save;
    }

    public void setDisplayCommand(boolean displayCommand) {
        this.displayCommand = displayCommand;
    }

    public void setFolderName(String folderName) {
        if (TextUtils.isEmpty(folderName)) {
            folderName = "default";
        }
        this.canChangeFolderName = false;
        this.folderPath = (Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + S_BRAND_NAME
                + File.separator + "logs" + File.separator + folderName);
        this.logPath = (this.folderPath + File.separator + getCurrentDate() + ".log");
    }

    public boolean canChangeFolderName() {
        return this.canChangeFolderName;
    }

    public abstract void putStrLog(String logStr);
    public abstract void destroy(); }
