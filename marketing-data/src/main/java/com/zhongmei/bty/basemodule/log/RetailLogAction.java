package com.zhongmei.bty.basemodule.log;

import android.text.TextUtils;

import com.zhongmei.yunfu.context.util.log.BaseLogAction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


public class RetailLogAction extends BaseLogAction {
    public static final String TAG = BaseLogAction.class.getName();
    private static final int S_PRINT_MAX_SIZE = 10;
    private static final int S_INTERVAL_TIME = 2 * 1000 * 60;
    private static final int S_MAX_BYTE_SIZE = 50000;

    private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private ExecutorService POOL = Executors.newFixedThreadPool(1);
    private static RetailLogAction instance = new RetailLogAction();
    private volatile long currDate = 0;

    public static RetailLogAction getInstance() {
        return instance;
    }

    public void initLogThread() {
        currDate = System.currentTimeMillis();
        POOL.execute(new LogThread());
    }

    @Override
    public void putStrLog(String logStr) {
        if (this.saveSdcard) {
            queue.add(logStr);
        }
    }

    @Override
    public void destroy() {

    }

    private class LogThread extends Thread {
        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            String str;
            int count = 0;
            try {
                while ((str = queue.take()) != null) {
                    count++;
                    if (!TextUtils.isEmpty(str)) {
                        sb.append(str);
                    }
                    int lenSize = sb.length();
                    if (count >= S_PRINT_MAX_SIZE || (System.currentTimeMillis() - currDate) > S_INTERVAL_TIME
                            || (lenSize >= S_MAX_BYTE_SIZE)) {
                        if (!TextUtils.isEmpty(sb.toString())) {
                            FileOutputStream out = null;
                            File file = new File(RetailLogAction.this.logPath);
                            File folder = new File(RetailLogAction.this.folderPath);
                            try {
                                if (!(folder.exists() || folder.mkdirs())) continue;
                                if (!(file.exists() || file.createNewFile())) continue;
                                out = new FileOutputStream(file, true);
                                out.write(sb.toString().getBytes());
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (out != null) {
                                    try {
                                        out.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        count = 0;
                        sb.delete(0, sb.length());
                        currDate = System.currentTimeMillis();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}