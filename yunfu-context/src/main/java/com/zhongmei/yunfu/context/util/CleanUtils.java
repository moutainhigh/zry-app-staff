package com.zhongmei.yunfu.context.util;

import android.content.Context;

import java.io.File;



public final class CleanUtils {

    private CleanUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    public static boolean cleanInternalCache(Context context) {
        return FileUtils.deleteFilesInDir(context.getCacheDir());
    }


    public static boolean cleanInternalFiles(Context context) {
        return FileUtils.deleteFilesInDir(context.getFilesDir());
    }


    public static boolean cleanInternalDbs(Context context) {
        return FileUtils.deleteFilesInDir(context.getFilesDir().getParent() + File.separator + "databases");
    }


    public static boolean cleanInternalDbByName(Context context, String dbName) {
        return context.deleteDatabase(dbName);
    }


    public static boolean cleanInternalSP(Context context) {
        return FileUtils.deleteFilesInDir(context.getFilesDir().getParent() + File.separator + "shared_prefs");
    }


    public static boolean cleanExternalCache(Context context) {
        return SDCardUtils.isSDCardEnable() && FileUtils.deleteFilesInDir(context.getExternalCacheDir());
    }


    public static boolean cleanCustomCache(String dirPath) {
        return FileUtils.deleteFilesInDir(dirPath);
    }


    public static boolean cleanCustomCache(File dir) {
        return FileUtils.deleteFilesInDir(dir);
    }
}
