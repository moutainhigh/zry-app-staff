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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

/**
 * 下载叫号文件
 */
public class DownLoaderCallVoiceTask extends AsyncTask<Void, Integer, Long> {
    private final String TAG = DownLoaderCallVoiceTask.class.getSimpleName();

    private URL mUrl;

    private File mFile;

    private final ProgressDialog mDialog;

    private int mProgress = 0;

    /**
     * 叫号语音包下载
     *
     * @param url
     * @param context
     */
    public DownLoaderCallVoiceTask(String url, Context context)
            throws Exception {
        super();
        if (context != null) {
            mDialog = new ProgressDialog(context);
        } else {
            mDialog = null;
        }
        // 删除原来的文件
        File dbPath = new File(QueueFileUtil.getPath());
        if (!dbPath.exists()) {
            dbPath.mkdirs();
        }
        mUrl = new URL(url);
        String fileName = new File(mUrl.getFile()).getName();
        mFile = new File(dbPath, fileName);
        Log.i(TAG, "out=" + mFile);

    }

    @Override
    protected void onPreExecute() {
        if (mDialog != null) {
            mDialog.setTitle("Downloading...");
            mDialog.setMessage(mFile.getName());
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    // // TODO Auto-generated method stub
                    // cancel(true);
                }
            });
            mDialog.show();
        }
    }

    @Override
    protected Long doInBackground(Void... params) {
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
        if (isCancelled())
            return;
        onDownLoadFinished(mFile);
    }

    /**
     * 下载完成
     *
     * @param file
     */
    protected void onDownLoadFinished(File file) {

    }

    /**
     * 文件异常
     *
     * @param file
     */
    protected void onDownLoadException() {

    }

    /**
     * 下载
     *
     * @return
     */
    private long download() {
        URLConnection connection = null;
        int bytesCopied = 0;
        try {
            connection = mUrl.openConnection();
            int length = connection.getContentLength();
            if (mFile.exists() && length == mFile.length()) {
                Log.d(TAG, "file " + mFile.getName() + " already exits!!");
                return 0l;
            }
            ProgressReportingOutputStream mOutputStream = new ProgressReportingOutputStream(mFile);
            publishProgress(0, length);
            bytesCopied = copy(connection.getInputStream(), mOutputStream);
            if (bytesCopied != length && length != -1) {
                Log.e(TAG, "Download incomplete bytesCopied=" + bytesCopied + ", length" + length);
            }
            mOutputStream.close();
        } catch (IOException e) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            cancel(true);
            onDownLoadException();
            Log.w(TAG, "", e);
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
            Log.w(TAG, "", e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.w(TAG, "", e);
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.w(TAG, "", e);
            }
        }
        return count;
    }

    private final class ProgressReportingOutputStream extends FileOutputStream {
        public ProgressReportingOutputStream(File file)
                throws FileNotFoundException {
            super(file);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void write(byte[] buffer, int byteOffset, int byteCount) throws IOException {
            // TODO Auto-generated method stub
            super.write(buffer, byteOffset, byteCount);
            mProgress += byteCount;
            publishProgress(mProgress);
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