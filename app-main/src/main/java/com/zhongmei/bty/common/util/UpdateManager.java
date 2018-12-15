package com.zhongmei.bty.common.util;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.util.ToastUtil;

public class UpdateManager {

    private static final String TAG = UpdateManager.class.getSimpleName();

    private static UpdateManager sUpdateManager;
    private DownloadManager mDownloadManager;
    public static String DOWNLOAD_ID = "download_id";

    public UpdateManager(Context context) {
        mDownloadManager = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public static synchronized UpdateManager getInstance() {
        if (sUpdateManager == null) {
            sUpdateManager = new UpdateManager(MainApplication.getInstance());
        }
        return sUpdateManager;
    }

    @Deprecated
    public void init(Context context) {
		/*mDownloadManager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);*/
    }

    public void download() {
        if (ShopInfoCfg.getInstance().getAppVersionInfo().hasUpdate()) {
            String updateUrl = ShopInfoCfg.getInstance().getAppVersionInfo().getUpdateUrl();
            if (!TextUtils.isEmpty(updateUrl) && URLUtil.isValidUrl(updateUrl)) {
                if (UpdateManager.getInstance().isDownloadRunning()) {
                    ToastUtil.showLongToast(R.string.is_loading);
                    return;
                } else {
                    ToastUtil.showLongToast(R.string.start_load_packet);
                    try {
                        download(updateUrl);
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                        ToastUtil.showLongToast(R.string.load_manager_not_open);
                    }

                }
            } else {
                Resources res = MainApplication.getInstance().getResources();
                ToastUtil.showLongToast(String.format(res.getString(R.string.error_level_up_address_format), updateUrl));
            }
        }
    }

	/*public void downloadPrint() {
		if (MainApplication.getInstance().getVersionInfo().hasUpdate()) {
			String updateUrl = MainApplication.getInstance().getPrintVersionInfo()
					.get(VersionInfo.UPDATE_URL_KEY);
			if (!TextUtils.isEmpty(updateUrl) && URLUtil.isValidUrl(updateUrl)) {
				if (UpdateManager.getInstance().isDownloadRunning()) {
					ToastUtil.showLongToast(R.string.is_loading);
					return;
				} else {
					ToastUtil.showLongToast(R.string.start_load_packet);
					try {
						download(updateUrl);
					} catch (Exception e) {
						Log.e(TAG, "", e);
						ToastUtil.showLongToast(R.string.load_manager_not_open);
					}

				}
			} else {
				Resources res = MainApplication.getInstance().getResources();
				ToastUtil.showLongToast(String.format(res.getString(R.string.error_level_up_address_format), updateUrl));
			}
		}
	}*/

    public void download(String url) {

        Uri resource = Uri.parse(url);
        Request request = new Request(resource);
//		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
//				| Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap
                .getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);
        request.setShowRunningNotification(true);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir("/download/",
                "newVersion.apk");
        request.setTitle("zhongmei update the new version");

        long id = mDownloadManager.enqueue(request);
        SharedPreferenceUtil.getSpUtil().putLong(DOWNLOAD_ID, id);
    }

    public boolean isDownloadRunning() {
        long downloadiId = SharedPreferenceUtil.getSpUtil().getLong(
                DOWNLOAD_ID, 0);
        if (downloadiId == 0) {
            return false;
        }
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadiId);
        boolean b = false;
        Cursor c = mDownloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    int status = c.getInt(c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS));
                    Log.d(TAG, "DownloadRuning status"
                            + status);
                    if (status == DownloadManager.STATUS_RUNNING) {
                        b = true;
                    }
                }
            } finally {
                c.close();
            }
        }
        return b;
    }

    public void queryDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(SharedPreferenceUtil.getSpUtil().getLong(
                DOWNLOAD_ID, 0));
        Cursor c = mDownloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    int status = c.getInt(c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (status) {
                        case DownloadManager.STATUS_PAUSED:
                        case DownloadManager.STATUS_PENDING:
                        case DownloadManager.STATUS_RUNNING:
                            break;
                        case DownloadManager.STATUS_SUCCESSFUL:

                            SharedPreferenceUtil.getSpUtil().remove(DOWNLOAD_ID);
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setDataAndType(
                                    Uri.parse("file://"
                                            + c.getString(c
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME))),
                                    "application/vnd.android.package-archive");
                            MainApplication.getInstance().startActivity(i);
                            break;
                        case DownloadManager.STATUS_FAILED:
                            mDownloadManager.remove(SharedPreferenceUtil.getSpUtil()
                                    .getLong(DOWNLOAD_ID, 0));
                            SharedPreferenceUtil.getSpUtil().remove(DOWNLOAD_ID);
                            break;
                    }
                }
            } finally {
                c.close();
            }
        }
    }
}
