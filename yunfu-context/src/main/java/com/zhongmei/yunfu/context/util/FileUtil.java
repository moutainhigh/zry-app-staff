package com.zhongmei.yunfu.context.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.zhongmei.yunfu.context.base.BaseApplication;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class FileUtil {

    private static final String LOCAL_ROOT_PATH = getRootDirectory()
            + "files/";

    public static final String LOCAL_IMAGE_PATH = LOCAL_ROOT_PATH + "image/";

    private static final String LOCAL_LOG_PATH = LOCAL_ROOT_PATH + "log/";

    private static final String LOCAL_LICENSE_PATH = LOCAL_ROOT_PATH
            + "license/";

    public static final String DEFAULT_LICENSE_FILE = "shop.key";

    public static final String LOCAL_PRINTER_NAME = "calm_settings.db";

    public static final String LOCAL_PRINTER_PATH = LOCAL_ROOT_PATH
            + "databases/" + LOCAL_PRINTER_NAME;

    public static final String COPY_PATH = getRootDirectory()
            + "calm2/";

    public static final String LOCAL_LOG_COPY_FOLDER = "logs/";

    public static final String LOCAL_DATEBASE_COPY_FOLDER = "databases/";

    public static final String LOCAL_LOG_COPY_PATH = COPY_PATH
            + LOCAL_LOG_COPY_FOLDER;

    public static final String LOCAL_DATEBASE_COPY_PATH = COPY_PATH
            + LOCAL_DATEBASE_COPY_FOLDER;

    public static final String LOCAL_DATABASE_PATH = LOCAL_ROOT_PATH
            + "databases/";

    public static final String LOACL_CALM_DATABASE_NAME = "yun_fu.db";

    public static String getRootPath() {
        return LOCAL_ROOT_PATH;
    }


    public static String getCamlDBPath() {
        return LOCAL_ROOT_PATH + LOCAL_DATEBASE_COPY_FOLDER + LOACL_CALM_DATABASE_NAME;
    }


    public static String getDBPath() {
        return LOCAL_ROOT_PATH + LOCAL_DATEBASE_COPY_FOLDER;
    }


    public static boolean checkdbExsit(String name) {
        File dbFile = new File(LOCAL_DATABASE_PATH + name);
        if (dbFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getLocalLicensePath(Context context) {
        if (checkSDCard()) {
            File file = new File(LOCAL_LICENSE_PATH);
            if (!file.exists()) {
                file.mkdir();
            }
            return LOCAL_LICENSE_PATH;
        } else {
            return context.getCacheDir().getPath() + "/";
        }
    }

    public static String getLocalExDatabaseFilePath(Context context,
                                                    String dbName) {
        File dbPath = new File(LOCAL_DATABASE_PATH);
        if (!dbPath.exists()) {
            dbPath.mkdirs();
        }
        return dbPath + "/" + dbName;
    }

    public static boolean deleteLocalLicensePath(Context context) {
        if (checkSDCard()) {
            File file = new File(LOCAL_LICENSE_PATH);
            if (!file.exists()) {
                return true;
            }
            File[] files = file.listFiles();
            for (File f : files) {
                f.delete();
            }
            return file.delete();
        } else {
            return new File(context.getCacheDir().getPath() + "/").delete();
        }
    }

    public static String getLocalLogpath(Context context) {
        if (checkSDCard()) {
            File file = new File(LOCAL_LOG_PATH);
            if (!file.exists()) {
                file.mkdir();
            }
            return LOCAL_LOG_PATH;
        } else {
            return context.getCacheDir().getPath() + "/";
        }
    }

    public static void clearLocalImages(Context context) {
        String path = getLocalImagePath(context);
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    f.delete();
                }
            }
        }
    }


    public static String getLocalFileName(String url) {
        if (url == null || url.trim().length() == 0) {
            return null;
        }
        if (url.startsWith("http://")) {
            url = url.substring("http://".length());
        } else if (url.startsWith("https://")) {
            url = url.substring("https://".length());
        }

        if (url == null || url.trim().length() == 0) {
            return null;
        }
        if (url.lastIndexOf("/") != -1) {
            String fullname = url.substring(url.lastIndexOf("/") + 1);
            return fullname;
        }
        return null;
    }


    public static String getLocalImagePath(Context context) {
        return getLocalRootPath(context);
    }


    private static boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    private static String getLocalRootPath(Context context) {
        if (checkSDCard()) {
            File file = new File(LOCAL_IMAGE_PATH);
            if (!file.exists()) {
                file.mkdir();
            }
            return LOCAL_IMAGE_PATH;
        } else {
            return context.getCacheDir().getPath() + "/";
        }
    }


    public static String getLocalImageNameByUrl(Context context, String url) {
        return getLocalImagePath(context) + getLocalFileName(url);
    }


    public static boolean checkFileExist(String paramString) {
        return new File(paramString).exists();
    }


    private static String getRootDirectory() {
        String rootpath = null;
        File externalFilesDir = BaseApplication.sInstance.getExternalFilesDir(null);
        if (externalFilesDir != null) {
            rootpath = externalFilesDir.getParent();
        }

        if (rootpath == null) {
            rootpath = Environment.getExternalStorageDirectory() + "/Android/data/com.zhongmei.bty/";
        }

        if (!rootpath.endsWith("/")) {
            rootpath += "/";
        }
        return rootpath;
    }

    public static String getRootDirectory(String subDir) {
        File homeDir = BaseApplication.sInstance.getFilesDir();
        if (Environment.isExternalStorageEmulated()) {
            homeDir = BaseApplication.sInstance.getExternalFilesDir(null).getParentFile();
        }

        subDir = subDir == null ? "" : subDir;
        return new File(homeDir, "calm/" + subDir).getPath() + File.separator;
    }


    public static boolean deleteFile(String paramString) {
        File f = new File(paramString);
        if (f.exists()) {
            return f.delete();
        }
        return false;
    }


    public static void saveToSDCardOrRAM(Context context, String filename,
                                         InputStream content, String filePath) throws Exception {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file = new File(filePath + filename);
            if (!file.exists()) {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = content.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            content.close();
            outStream.close();
        } else {
            FileOutputStream outStream = context.openFileOutput(filename,
                    Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = content.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            content.close();
            outStream.close();
        }
    }

    public static String readFromSDCardOrRAM(Context context, String fileName,
                                             String filePath) throws Exception {
        FileInputStream fis = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file = new File(filePath + fileName);
            if (!file.exists()) {
                return null;
            }
            fis = new FileInputStream(file);
        } else {
            fis = context.openFileInput(fileName);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        StringBuffer buff = new StringBuffer();
        String line = null;
        while ((line = in.readLine()) != null) {
            buff.append(line);
            buff.append("\n");
        }
        String s = buff.toString();
        if (s.length() > 0) {
            s = s.substring(0, s.length() - 1);
        }
        in.close();
        return s;
    }


    public static void saveToSDCardOrRAM(Context context, String filename,
                                         String content, String filePath) throws Exception {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file = new File(filePath + filename);
            if (!file.exists()) {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(new FileOutputStream(file));
            out.write(content);
            out.close();
        } else {
            PrintWriter out = new PrintWriter(context.openFileOutput(filename,
                    Context.MODE_PRIVATE));
            out.write(content);
            out.close();
        }
    }

    public static InputStream Bitmap2IS(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        return sbs;
    }

    public static boolean checkFileExistFromSDCardOrRAM(Context context,
                                                        String fileName, String filePath) throws Exception {
        FileInputStream fis = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file = new File(filePath + fileName);
            if (!file.exists()) {
                return false;
            } else {
                return true;
            }
        } else {
            fis = context.openFileInput(fileName);
            if (fis.available() > 0) {
                return true;
            } else {
                return false;
            }
        }

    }

    public static InputStream readInputStreamFromSDCardOrRAM(Context context,
                                                             String fileName, String filePath) throws Exception {
        FileInputStream fis = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file = new File(filePath + fileName);
            if (!file.exists()) {
                return null;
            }
            fis = new FileInputStream(file);
        } else {
            fis = context.openFileInput(fileName);
        }

        return fis;
    }
}
