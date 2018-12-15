package com.zhongmei.yunfu.context.util.log;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class LogAction {
    private int maxChangeSize = 10;//最大缓存日志条数
    private boolean canChangeFolderName;
    private boolean saveSdcard;
    private boolean displayCommand;
    private String folderPath;
    private String logPath;
    private AtomicInteger currentCacheSize = new AtomicInteger();//日志缓存计数
    private StringBuffer logCacheSB = new StringBuffer();//日志缓存
    private Timer timer;//主要用于执行一分钟一次检查
    private TimerTask timerTask;//主要用于执行空闲时间中存储内存缓存中的日志
    public static LogAction instance;
    private static final Executor THREAD_POOL_EXECUTOR = Executors.newFixedThreadPool(1);

    /*public static LogAction getInstance() {
        if (instance == null) {
            instance = new LogAction();
        }
        return instance;
    }*/

    public LogAction() {
        this.saveSdcard = false;
        this.displayCommand = false;
        this.canChangeFolderName = true;
        this.folderPath = (Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + BaseLogAction.S_BRAND_NAME + File.separator + "logs" + File.separator + "default");
        this.logPath = (this.folderPath + File.separator + getCurrentDate() + ".log");
    }

    public LogAction(int maxChangeSize) {
        this();
        this.maxChangeSize = maxChangeSize;
    }

    public void putLog(LogData log) {
        if (this.displayCommand) {
            Log.i(BaseLogAction.S_BRAND_NAME, log.getTime() + ":" + log.getTag() + ":" + log.getMsg());
        }
        if (this.saveSdcard) {
            if (currentCacheSize.get() > maxChangeSize) {
                writeFileSdcardFile(logCacheSB.toString());
                logCacheSB.delete(0, logCacheSB.length());
                currentCacheSize.getAndSet(0);
            }
            logCacheSB.append(log.toString());
            currentCacheSize.getAndIncrement();
            beginCountDown();
        }
    }

    //在最后一次调用日志后启动任务（在空闲时间将缓存中的日志写到文件中）
    private synchronized void beginCountDown() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (currentCacheSize.get() > 0) {
                    writeFileSdcardFile(logCacheSB.toString());
                    logCacheSB.delete(0, logCacheSB.length());
                    currentCacheSize.getAndSet(0);
                }
            }
        };
        timer.schedule(timerTask, 60 * 1000, 60 * 1000);
    }

    //写数据到SD中的文件
    private void writeFileSdcardFile(final String content) {
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                File file = new File(LogAction.this.logPath);
                File folder = new File(LogAction.this.folderPath);
                try {
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(file, true);
                    byte[] bytes = content.getBytes();
                    out.write(bytes);
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getCurrentDate() {
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        return format1.format(new Date());
    }

    public String getLogFileName() {
        return this.logPath;
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
        this.folderPath = (Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + BaseLogAction.S_BRAND_NAME + File.separator + "logs" + File.separator + folderName);
        this.logPath = (this.folderPath + File.separator + getCurrentDate() + ".log");
    }

    public boolean canChangeFolderName() {
        return this.canChangeFolderName;
    }

    public void destroy() {
        instance = null;
        System.gc();
    }
}