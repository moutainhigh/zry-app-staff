package com.zhongmei.bty.common.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.zhongmei.bty.common.qiniuupload.QiniuUploadHandler;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.data.operates.UploadOperates;
import com.zhongmei.bty.data.operates.message.content.TokenReq;
import com.zhongmei.bty.data.operates.message.content.TokenResp;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class UploadLogUtil {

    private final static String TAG = UploadLogUtil.class.getSimpleName();
    public final static String ACTION_UPLOAD_LOG = "action_upload_log";
    private static int intervalTime = 24 * 60 * 60 * 1000;
    private static UploadManager uploadManager = new UploadManager();

    public static void startUploadLogAlarm(Context context) {
                AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

                Intent intent = new Intent(context, UploadLogUtil.class);
        intent.setAction(ACTION_UPLOAD_LOG);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long triggerAtTime = calendar.getTimeInMillis();

                manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime,
                intervalTime, pendingIntent);
    }

    public static void uploadCashierLog() {
        if (ShopInfoCfg.getInstance().shopId == null) {
            return;
        }

        File file = new File(CLog.getPath() + CLog.FOLDER_NAME);
        if (file.exists() && file.isDirectory() && file.list().length > 0) {
            UploadOperates operates = OperatesFactory.create(UploadOperates.class);
            operates.requestToken(new TokenReq(1), getTokenListner);
        }
    }

    private static ResponseListener getTokenListner = new ResponseListener<TokenResp>() {

        @Override
        public void onResponse(ResponseObject<TokenResp> response) {
            if (ResponseObject.isOk(response) && response.getContent() != null
                    && (!TextUtils.isEmpty(response.getContent().getData()))) {

                String token = response.getContent().getData();
                upload(token);
            } else {
                ToastUtil.showLongToast(R.string.upload_fail);
                Log.e(TAG, "请求token失败：" + response.getMessage());
            }
        }

        @Override
        public void onError(VolleyError error) {
            ToastUtil.showLongToast(R.string.upload_fail);
            Log.e(TAG, "请求token失败：" + error.getMessage());
        }
    };

    private static void upload(String token) {
        List<FileAndNewNameMapItem> fileList = findLogFiles(CLog.getPath() + CLog.FOLDER_NAME);
        String dir = getEnvironmentDir();
        if (Utils.isNotEmpty(fileList)) {
            for (final FileAndNewNameMapItem item : fileList) {
                uploadManager.put(item.getFile(), dir + item.getNewFilePath(), token, new UpCompletionHandler() {
                    @Override
                    public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                        if (responseInfo.isOK()) {
                                                        item.getFile().delete();
                        }
                    }
                }, null);
            }
        }
    }

    private static String getEnvironmentDir() {
        String result = ShopInfoCfg.getInstance().getServerKey();
        if (result.contains("test")) {
            result = "testerp/";
        } else if (result.contains("dev")) {
            result = "deverp/";
        } else if (result.contains("gld")) {
            result = "glderp/";
        } else {
            result = "erp/";
        }
        return result;
    }

    private static List<FileAndNewNameMapItem> findLogFiles(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            String prefix = ShopInfoCfg.getInstance().shopId + "_"
                    + SystemUtils.getMacAddress().replace(":", "") + "_cashier_";
            String suffix = "_" + System.currentTimeMillis() + ".log";
            List<FileAndNewNameMapItem> fileList = new ArrayList<FileAndNewNameMapItem>();            File[] fs = file.listFiles();

            if (fs == null || fs.length == 0) {
                return null;
            }

                        Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long nowDate = calendar.getTime().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            for (File f : fs) {
                String fileName = f.getName().substring(0, f.getName().indexOf("."));
                try {
                                        long fileDate = dateFormat.parse(fileName).getTime();
                    if (fileDate < nowDate) {
                        fileList.add(new FileAndNewNameMapItem(f, prefix + fileName + suffix));
                    }
                } catch (ParseException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            return fileList;
        }
        return null;
    }

    private static class FileAndNewNameMapItem {
        private String newFileName;

        private File file;

        public FileAndNewNameMapItem(File file, String newFilePath) {
            this.newFileName = newFilePath;
            this.file = file;
        }

        public String getNewFilePath() {
            return newFileName;
        }

        public void setNewFilePath(String newFilePath) {
            this.newFileName = newFilePath;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }
    }

    private static void uploadPrintLogFile(List<FileAndNewNameMapItem> listFiles, String token) {
        String dir = getEnvironmentDir();
        for (final FileAndNewNameMapItem item : listFiles) {
            QiniuUploadHandler.doUpload(item.getFile(), dir + item.getNewFilePath(), token, new UpCompletionHandler() {
                @Override
                public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                    if (responseInfo.isOK()) {
                        Log.e(TAG, "上传打印日志成功：" + item.newFileName);
                    } else {
                        Log.e(TAG, "上传打印日志失败：" + item.newFileName);
                    }
                }
            }, null);
        }
    }



    private static List<FileAndNewNameMapItem> findUploadLogFile(String path, String name
            , String date) {
        String tempNewName = SystemUtils.getMacAddress().replace(":", "") + "_" + System.currentTimeMillis();
        String newName = ShopInfoCfg.getInstance().shopId + "_";

        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            List<FileAndNewNameMapItem> fileList = new ArrayList<>();            File[] fs = file.listFiles();

            if (fs == null || fs.length == 0) {
                return null;
            }

            for (File f : fs) {
                String fileName = f.getName().substring(0, f.getName().lastIndexOf("."));
                if (date.equals(fileName)) {
                    fileList.add(new FileAndNewNameMapItem(f, newName + f.getName() + "_" + name + "_" + tempNewName));
                }
            }
            return fileList;
        }
        return null;
    }

    private static String getExternalStorageDirectory() {
        String rootpath = Environment.getExternalStorageDirectory().getPath();
        if (!rootpath.endsWith("/")) {
            rootpath += "/";
        }
        return rootpath;
    }
}
