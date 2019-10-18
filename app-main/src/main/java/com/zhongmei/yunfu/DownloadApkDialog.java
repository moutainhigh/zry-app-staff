package com.zhongmei.yunfu;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.zhongmei.yunfu.context.util.AppUtils;
import com.zhongmei.yunfu.context.data.VersionInfo;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.ui.view.DownloadHintDialog;

import java.io.File;
import java.math.BigDecimal;

import de.greenrobot.event.EventBus;


public class DownloadApkDialog {

    private static final String TAG = DownloadApkDialog.class.getSimpleName();

    private DownloadManager mDownloadManager;

    private Thread mListenDownloadStatusThread;

    private boolean isDownloading;

    private String mUrl;

    private VersionInfo mInfo;

    private DownloadHintDialog mDialogFragment;

    private FragmentActivity mContext;

    private long mDownloadId;
    private View.OnClickListener cancelListener;

    public static DownloadApkDialog createDownload(FragmentActivity context, VersionInfo info) {
        return new DownloadApkDialog(context, info, null);
    }

    public static DownloadApkDialog createDownload(FragmentActivity context, VersionInfo info, View.OnClickListener cancelListener) {
        return new DownloadApkDialog(context, info, cancelListener);
    }

    private DownloadApkDialog(final FragmentActivity context, VersionInfo info, final View.OnClickListener cancelListener) {
        mContext = context;
        this.cancelListener = cancelListener;
        DownloadHintDialog.DownloadHintDialogBuilder builder = new DownloadHintDialog.DownloadHintDialogBuilder(MainApplication.getInstance())
                .message(context.getString(R.string.update_downloading)).max(100);

        if (!info.isForce()) {
            builder.negativeText(R.string.cancel)
                    .negativeLisnter(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            callCancelListener(v);
                            mDownloadManager.remove(mDownloadId);
                        }
                    });
        }
        mDialogFragment = builder.build();
        mDialogFragment.setCancelable(false);
        mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        mInfo = info;
        mUrl = info.getDownloadUrl();
            }

    public DownloadApkDialog star() {
        return star(false);
    }

    public DownloadApkDialog star(boolean isRetry) {
        if (!isRetry) {
            show();
        }
        try {
            if (mDownloadId > 0) {
                mDownloadManager.remove(mDownloadId);
            }
            mDownloadId = download(mUrl);
            if (!isRetry) {
                EventBus.getDefault().register(this);
            }
            isDownloading = true;
            listenDownloadStatus(mDownloadId);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
                        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDialogFragment.setMessage(mContext.getString(R.string.update_download_failed));
                    mDialogFragment.btn_positive.setVisibility(View.VISIBLE);
                    mDialogFragment.btn_negative.setVisibility(View.GONE);
                    mDialogFragment.btn_positive.setText(R.string.common_submit);
                    mDialogFragment.btn_positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            callCancelListener(v);
                            if (mDownloadId > 0) {
                                mDownloadManager.remove(mDownloadId);
                            }
                            if (cancelListener != null) {
                                cancelListener.onClick(v);
                            }
                        }
                    });
                }
            }, 300);
        }
        return this;
    }

    private void callCancelListener(View v) {
        if (cancelListener != null) {
            cancelListener.onClick(v);
        }
    }

    public void dismiss() {
        isDownloading = false;
        EventBus.getDefault().unregister(this);
        mDialogFragment.dismissAllowingStateLoss();
    }

    public void show() {
        mDialogFragment.show(mContext.getSupportFragmentManager(), "DownloadApkDialog");
    }


    public void onEventMainThread(final EventDownloadStatus event) {
        if (event.isError) {
            mDialogFragment.setMessage(mContext.getString(R.string.update_retry_message));
            mDialogFragment.btn_positive.setVisibility(View.VISIBLE);
            mDialogFragment.btn_positive.setText(R.string.login_retry);
            mDialogFragment.btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDownloadManager = (DownloadManager) mContext.getSystemService(
                            Context.DOWNLOAD_SERVICE);
                    star(true);
                    mDialogFragment.btn_positive.setVisibility(View.GONE);
                }
            });
            if (!mInfo.isForce()) {
                mDialogFragment.btn_negative.setVisibility(View.VISIBLE);
                mDialogFragment.btn_negative.setBackgroundResource(R.drawable.beauty_btn_dialog_cancel_selector);
                mDialogFragment.btn_negative.setText(R.string.cancel);
                mDialogFragment.btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        callCancelListener(v);
                        if (mDownloadId > 0) {
                            mDownloadManager.remove(mDownloadId);
                        }
                    }
                });
            } else {
                mDialogFragment.btn_negative.setVisibility(View.VISIBLE);
                mDialogFragment.btn_negative.setText(R.string.go_check_network);
                mDialogFragment.btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
                        mContext.startActivity(wifiSettingsIntent);
                    }
                });
            }
            mDialogFragment.btn_negative.setBackgroundResource(R.drawable.beauty_btn_dialog_cancel_selector);
            return;
        }

        if (event.isSuccess) {
            mDialogFragment.setProgress(100L);
            mDialogFragment.setMessage(mContext.getString(R.string.update_download_success));
            mDialogFragment.btn_positive.setVisibility(View.VISIBLE);
            mDialogFragment.btn_positive.setText(R.string.common_submit);
            mDialogFragment.btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtils.installAPK(mContext, event.localUrl);
                }
            });
            AppUtils.installAPK(mContext, event.localUrl);
            return;
        }


        mDialogFragment.setMessage(mContext.getString(R.string.update_download_detail));
        if (event.totalBytes > 0)  {
            mDialogFragment.setProgress(event.downloadBytes * 100 / event.totalBytes);
        }
    }

    private float formatDownloadByteCount(long count) {
        BigDecimal b = new BigDecimal(count / 1024 / 1024);
        b.setScale(2, BigDecimal.ROUND_HALF_UP);
        return b.floatValue();
    }

    public long download(String url) throws Exception {
        Uri resource = Uri.parse(url);
        Request request = new Request(resource);
        request.setAllowedOverRoaming(false);
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap
                .getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);
                request.setShowRunningNotification(true);
        request.setVisibleInDownloadsUi(true);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/kry." + mInfo.getVersionCode() + ".apk");
        file.delete();
                request.setDestinationInExternalPublicDir("/download/", "kry." + mInfo.getVersionCode() + ".apk");
        request.setTitle(mContext.getString(R.string.download_request_title));
        mDownloadManager = (DownloadManager) mContext.getSystemService(
                Context.DOWNLOAD_SERVICE);
        return mDownloadManager.enqueue(request);
    }

    private void listenDownloadStatus(final long id) {
        if (mListenDownloadStatusThread != null) {
            mListenDownloadStatusThread.interrupt();
        }
        mListenDownloadStatusThread = new Thread() {
            public void run() {
                setName("download status");
                while (isDownloading) {
                    queryDownloadStatus(id);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mListenDownloadStatusThread.start();
    }

    private void queryDownloadStatus(long id) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);

        Cursor c = mDownloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            long downloadBytes = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            long totalBytes = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            String localUrl = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            EventDownloadStatus eventDownloadStatus = new EventDownloadStatus();
            switch (status) {
                case DownloadManager.STATUS_PENDING:
                case DownloadManager.STATUS_RUNNING:
                    isDownloading = true;
                    eventDownloadStatus.downloadBytes = downloadBytes;
                    eventDownloadStatus.totalBytes = totalBytes;
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    eventDownloadStatus.localUrl = localUrl;
                    eventDownloadStatus.isSuccess = true;
                    isDownloading = false;
                    break;
                case DownloadManager.STATUS_PAUSED:
                case DownloadManager.STATUS_FAILED:
                    eventDownloadStatus.isError = true;
                    isDownloading = false;
                    break;
            }
            EventBus.getDefault().post(eventDownloadStatus);
        }
        c.close();
    }

    public static class EventDownloadStatus {
        public long downloadBytes;
        public long totalBytes;
        public String localUrl;
        public boolean isSuccess;
        public boolean isError;

        public EventDownloadStatus() {

        }
    }
}
