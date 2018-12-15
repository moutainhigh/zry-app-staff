package com.zhongmei.bty.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.FileUtil;
import com.zhongmei.yunfu.context.util.SystemUtils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UploadFile {
    private static final String TAG = UploadFile.class.getSimpleName();

    private static final String ZIP_FILE_PATH = "/zip/";

    private static final String LOG_FILE_PATH = "/files/";

    private static final int MAX_UPLOAD_LENGTH = 5 * 1024 * 1024;// 5M

    public static boolean uploadLogFile(Context context, String url, File file)
            throws NameNotFoundException, ClientProtocolException, IOException {
        ArrayList<File> fileList = new ArrayList<File>();
//		SharedPreferenceUtil mSharedPreferenceUtil = new SharedPreferenceUtil(
//				context);
        PackageInfo info = context.getPackageManager().getPackageInfo(
                context.getPackageName(), 0);

        boolean res = false;

        // int fileLength = (int) file.length();
        int sepCount = 0;
        // if (fileLength > MAX_UPLOAD_LENGTH) {
        // sepCount = fileLength / MAX_UPLOAD_LENGTH + 1;
        // }

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        MultipartEntity mpEntity = new MultipartEntity(); // 文件传输

        if (sepCount == 0) {
            fileList.add(file);
            Log.d(TAG, "Upload log file>>" + file.getAbsolutePath() + " ("
                    + file.length() + " byte)");
            ContentBody cbFile = new FileBody(file);
            mpEntity.addPart("fileData", cbFile);
        } else {// 拆分上传
            fileList.add(file);
            String zipPath = FileUtil.getLocalLogpath(context) + ZIP_FILE_PATH;
            FileInputStream in = new FileInputStream(zipPath + file.getName()
                    + ".zip");
            for (int i = 0; i < sepCount; i++) {
                File sepZipFile = new File(zipPath + file.getName()
                        + ".zip.part" + i);
                fileList.add(sepZipFile);
                ZipOutputStream out = new ZipOutputStream(
                        new BufferedOutputStream(new FileOutputStream(
                                sepZipFile)));
                byte[] buffer = new byte[MAX_UPLOAD_LENGTH];
                int realReadLength = in.read(buffer);
                ZipEntry zipEntry = new ZipEntry(file.getName());
                out.putNextEntry(zipEntry);
                out.write(buffer, 0, realReadLength);
                out.close();
                Log.d(TAG, "Upload log file>>" + sepZipFile.getAbsolutePath()
                        + " (" + sepZipFile.length() + " byte)");
                ContentBody cbFile = new FileBody(sepZipFile);
                mpEntity.addPart("fileData" + i, cbFile);
            }
            in.close();
        }

        httpPost.addHeader("shopID", ShopInfoCfg.getInstance().shopId);
        httpPost.addHeader("fileName", file.getName());
        httpPost.addHeader("fileLength", String.valueOf(file.length()));
        httpPost.addHeader("versionName", info.versionName);
        httpPost.setEntity(mpEntity);
        Log.d(TAG, "ttpClient.execute=");
        HttpResponse response = httpClient.execute(httpPost);
        int iStatusCode = response.getStatusLine().getStatusCode();
        Log.d(TAG, "StatusCode=" + iStatusCode);
        if (iStatusCode == 200) {
            Iterator<File> its = fileList.iterator();
            while (its.hasNext()) {
                its.next().delete();
            }
            fileList.clear();
            res = true;
        } else {
            res = false;
        }
        return res;
    }

    public static boolean uploadDbFile(Context context, String url, File file)
            throws NameNotFoundException, ClientProtocolException, IOException {
        Boolean res = false;
        MainApplication app = (MainApplication) context.getApplicationContext();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss"); // 初始化Formatter的转换格式。
        String hms = formatter.format(System.currentTimeMillis());

        String newFilePathTemp = ShopInfoCfg.getInstance().shopId
                + "_"
                + (SystemUtils.getMacAddress().replace(":", ""))
                + "_" + hms + "_";

        byte[] buffer = new byte[2046];

        String path = FileUtil.getLocalLogpath(context) + LOG_FILE_PATH;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        PackageInfo info = context.getPackageManager().getPackageInfo(
                context.getPackageName(), 0);

        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(url);
        MultipartEntity mpEntity = new MultipartEntity();

        File newFile = new File(path + newFilePathTemp + file.getName());

        if (newFile.exists()) {
            newFile.delete();
        }
        newFile.createNewFile();

        BufferedOutputStream os = new BufferedOutputStream(
                new FileOutputStream(newFile));
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(
                file));

        int btyeLen = 0;
        while ((btyeLen = is.read(buffer)) > 0) {
            os.write(buffer, 0, btyeLen);
        }
        ContentBody cbFile = new FileBody(newFile);
        mpEntity.addPart("fileData", cbFile);
        is.close();
        os.flush();
        os.close();

        httpPost.addHeader("shopID", ShopInfoCfg.getInstance().shopId);
        httpPost.addHeader("fileName", newFile.getName());
        httpPost.addHeader("fileLength", newFile.length() + "");
        if (info != null) {
            httpPost.addHeader("versionName", info.versionName);
        }
        httpPost.setEntity(mpEntity);

        HttpResponse response = httpClient.execute(httpPost);

        // HttpStatus.SC_OK表示连接成功
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            res = true;
        } else {
            res = false;
            Log.d("dengpan", "dengpan=================网络未连接,错误码："
                    + response.getStatusLine().getStatusCode());
        }
        newFile.delete();
        return res;

    }
}
