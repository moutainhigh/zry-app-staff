package com.zhongmei.analysis.store;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhongmei.analysis.DataHandleListener;
import com.zhongmei.analysis.StatisticsHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by demo on 2018/12/15
 */
public class SdcardDataStore<T> implements IDataStore<T, String> {
    public static final String SDCARD_DIR_NAME = "statistics";
    public static final String SDCARD_FILE_NAME = "performance_";
    private static final long TIME_SLOT = 5 * 60 * 1000; // 5分钟
    private static final int SIZE_SLOT = 20; // 20条数据

    private WeakReference<Context> mContextWeakRef;
    private LinkedBlockingQueue<T> mLinkedBlockingQueue = new LinkedBlockingQueue<>();

    private Runnable saveRunnable = new Runnable() {
        @Override
        public void run() {
            if (mContextWeakRef == null) {
                return;
            }
            Context context = mContextWeakRef.get();
            if (context != null) {
                saveData(context);
            }
        }
    };

    public SdcardDataStore() {
        startRunnable();
    }

    @Override
    public void deleteStoreData(final Context context, final String dataFormat) {
        StatisticsHandler.getInstance().commit(new Runnable() {
            @Override
            public void run() {
                File file = getFile(context, dataFormat);
                try {
                    if (file != null && file.exists()) {
                        file.delete();
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private File getFile(Context context, String dataFormat) {
        if (context == null) {
            return null;
        }
        final File sdcardDir = context.getExternalFilesDir(null);
        File dir = new File(sdcardDir + File.separator + SDCARD_DIR_NAME);
        File file = new File(dir + File.separator + SDCARD_FILE_NAME + dataFormat);
        return file;
    }

    @Override
    public void realStoreData(final Context context, final T data) {
        mContextWeakRef = new WeakReference<Context>(context);
        StatisticsHandler.getInstance().commit(new Runnable() {
            @Override
            public void run() {
                mLinkedBlockingQueue.add(data);
                if (shouldSaveData()) {
                    saveData(context);
                }
            }
        });
    }

    @Override
    public void writeAllData(Context context, DataHandleListener listener) {
        if (mLinkedBlockingQueue.size() == 0) {
            if (listener != null) {
                listener.onSuccess(null);
            }
            return;
        }
        String realContent = getContent();
        storeActionData(context, realContent, listener);
    }

    File getDataFormat(Context context, String dataFormat) {
        File file = getFile(context, dataFormat);
        try {
            if (file != null && file.exists()) {
                return file;
            }
        } catch (Exception e) {
        }
        return null;
    }

    private void saveData(final Context context) {
        StatisticsHandler.getInstance().commit(new Runnable() {
            @Override
            public void run() {
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }
                if (mLinkedBlockingQueue.size() == 0) {
                    return;
                }
                String realContent = getContent();
                storeActionData(context, realContent, null);
            }
        });
    }

    private String getContent() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        StringBuilder content = new StringBuilder();
        while (!mLinkedBlockingQueue.isEmpty()) {
            T data = mLinkedBlockingQueue.poll();
            String dataStr = gson.toJson(data);
            content.append(dataStr);
            content.append("\n");
        }
        return content.toString();
    }

    private boolean shouldSaveData() {
        // 存储的条数超过20条
        int size = mLinkedBlockingQueue.size();
        if (size >= SIZE_SLOT) {
            return true;
        }
        return false;
    }

    private void storeActionData(Context context, final String content, final DataHandleListener listener) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (listener != null) {
                listener.onFailure();
            }
            return;
        }
        final File sdcardDir = context.getExternalFilesDir(null);
        if (sdcardDir == null) {
            if (listener != null) {
                listener.onFailure();
            }
            return;
        }
        StatisticsHandler.getInstance().commit(new Runnable() {
            @Override
            public void run() {
                File dir = new File(sdcardDir + File.separator + SDCARD_DIR_NAME);
                File file = new File(dir + File.separator + SDCARD_FILE_NAME + getCurrentDate());
                File folder = new File(sdcardDir.getAbsolutePath());

                try {
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(file, true);
                    byte[] bytes = content.getBytes();
                    out.write(bytes);
                    out.close();
                    if (listener != null) {
                        listener.onSuccess(file);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onFailure();
                    }
                    e.printStackTrace();
                } finally {
                    startRunnable();
                }
            }
        });
    }

    private void startRunnable() {
        StatisticsHandler.getInstance().remove(saveRunnable);
        StatisticsHandler.getInstance().commitDelayed(saveRunnable, TIME_SLOT);
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new java.util.Date());
    }
}
