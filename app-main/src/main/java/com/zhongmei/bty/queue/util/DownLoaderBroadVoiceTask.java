package com.zhongmei.bty.queue.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

import com.zhongmei.bty.commonmodule.database.entity.CommercialQueueConfigFile;

/**
 * 广播语音下载
 */
public class DownLoaderBroadVoiceTask extends AsyncTask<Void, Integer, Long> {
    private final String TAG = "DownLoaderTask";

    private final ProgressDialog mDialog;

    private final List<CommercialQueueConfigFile> fileList;

    private static final String path = "broadcast";

    /**
     * @param url     下载地址
     * @param dirPath 子文件夹名称
     * @param name    文件名
     * @param context
     */
    public DownLoaderBroadVoiceTask(List<CommercialQueueConfigFile> fileList, Context context) {
        super();
        this.fileList = fileList;
        if (context != null) {
            mDialog = new ProgressDialog(context);
        } else {
            mDialog = null;
        }
        File dbPath = new File(QueueFileUtil.getPath() + path);
        if (!dbPath.exists()) {
            dbPath.mkdirs();
        }
    }

    @Override
    protected void onPreExecute() {
        if (mDialog != null) {
            mDialog.setTitle("Downloading...");
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    onCancelDownload();
                }
            });
            mDialog.show();
        }
    }

    protected void onCancelDownload() {
        cancel(true);
    }

    @Override
    protected Long doInBackground(Void... params) {
        // TODO Auto-generated method stub
        return download();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (mDialog == null)
            return;
        if (values.length > 1) {
            int contentLength = values[1];
            if (contentLength == -1) {
                mDialog.setIndeterminate(true);
            } else {
                mDialog.setMax(contentLength);
            }
        } else {
            mDialog.setProgress(values[0]);
        }
    }

    @Override
    protected void onPostExecute(Long result) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 进度条总数
     */
    private int totalProgess = 0;
    /**
     * 当前文件进度
     */
    private int mProgress = 0;

    private long download() {
        totalProgess = 0;
        publishProgress(0, 100);
        int bytesCopied = 0;
        int perFileLength = 100 / fileList.size();
        int index = 0;
        for (CommercialQueueConfigFile file : fileList) {
            URLConnection connection = null;
            try {
                mProgress = 0;
                URL mUrl = new URL(QueueFileUtil.convertUtf(file.getUrl()));
                connection = mUrl.openConnection();
                int length = connection.getContentLength();
                File mFile = new File(QueueFileUtil.getPath() + path, file.getName());
                totalProgess = perFileLength * index;
                publishProgress(totalProgess);
                index++;
                if (mFile.exists() && length == mFile.length()) {
                    Log.d(TAG, "file " + mFile.getName() + " already exits!!");
                    continue;
                }
                ProgressReportingOutputStream mOutputStream = new ProgressReportingOutputStream(mFile, length);
                bytesCopied = copy(connection.getInputStream(), mOutputStream);
                if (bytesCopied != length && length != -1) {
                    Log.e(TAG, "Download incomplete bytesCopied=" + bytesCopied + ", length" + length);
                }
                mOutputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "", e);
            }
        }

        return bytesCopied;
    }

    private int copy(InputStream input, OutputStream output) {
        byte[] buffer = new byte[1024 * 8];
        BufferedInputStream in = new BufferedInputStream(input, 1024 * 8);
        BufferedOutputStream out = new BufferedOutputStream(output, 1024 * 8);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, 1024 * 8)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return count;
    }

    private final class ProgressReportingOutputStream extends FileOutputStream {
        private int length;

        public ProgressReportingOutputStream(File file, int length)
                throws FileNotFoundException {
            super(file);
            this.length = length;
        }

        @Override
        public void write(byte[] buffer, int byteOffset, int byteCount) throws IOException {
            super.write(buffer, byteOffset, byteCount);
            mProgress += byteCount;
            int addProgess = mProgress * 100 / length / fileList.size();
            publishProgress(totalProgess + addProgess);
        }

    }

    /**
     * 显示
     */
    public void showDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

}