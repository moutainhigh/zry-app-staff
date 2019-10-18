package com.zhongmei.yunfu.context.util;

import android.content.Context;
import android.text.TextUtils;

import java.util.Set;


public class SharedPreferenceUtil {


    private Context mContext;


    private static SharedPreferenceUtil instance;


    private static String sFileName;


    private SharedPreferenceUtil(Context context) {
        this.mContext = context;
    }


    public static void init(Context context, String fileName) {
        if (TextUtils.isEmpty(fileName) || context == null) {
            throw new RuntimeException("fileName or context is empty, can't initial sharedPreferenceUtils");
        }
        sFileName = fileName;
        instance = new SharedPreferenceUtil(context.getApplicationContext());
    }


    public static SharedPreferenceUtil getSpUtil() {
        if (instance == null) {
            throw new IllegalStateException("context not initialized");
        } else {
            return instance;
        }
    }


    public boolean putStringSet(String key, Set<String> values) {
        return mContext
                .getSharedPreferences(sFileName,
                        Context.MODE_PRIVATE).edit().putStringSet(key, values)
                .commit();
    }


    public boolean putString(String key, String value) {
        return mContext
                .getSharedPreferences(sFileName,
                        Context.MODE_PRIVATE).edit().putString(key, value)
                .commit();
    }


    public boolean putLong(String key, long value) {
        return mContext
                .getSharedPreferences(sFileName,
                        Context.MODE_PRIVATE).edit().putLong(key, value)
                .commit();
    }


    public boolean putBoolean(String key, boolean value) {
        return mContext
                .getSharedPreferences(sFileName,
                        Context.MODE_PRIVATE).edit().putBoolean(key, value)
                .commit();
    }


    public boolean putInt(String key, int value) {
        return mContext
                .getSharedPreferences(sFileName,
                        Context.MODE_PRIVATE).edit().putInt(key, value)
                .commit();
    }


    public String getString(String key, String defValue) {
        return mContext.getSharedPreferences(sFileName,
                Context.MODE_PRIVATE).getString(key, defValue);
    }


    public long getLong(String key, long defValue) {
        return mContext.getSharedPreferences(sFileName,
                Context.MODE_PRIVATE).getLong(key, defValue);
    }


    public boolean getBoolean(String key, boolean defValue) {
        return mContext.getSharedPreferences(sFileName,
                Context.MODE_PRIVATE).getBoolean(key, defValue);
    }


    public int getInt(String key, int defValue) {
        return mContext.getSharedPreferences(sFileName,
                Context.MODE_PRIVATE).getInt(key, defValue);
    }


    public Set<String> getStringSet(String key, Set<String> values) {
        return mContext.getSharedPreferences(sFileName,
                Context.MODE_PRIVATE).getStringSet(key, values);
    }


    public boolean remove(String key) {
        return mContext
                .getSharedPreferences(sFileName,
                        Context.MODE_PRIVATE).edit().remove(key).commit();
    }

    public boolean contains(String key) {
        return mContext
                .getSharedPreferences(sFileName,
                        Context.MODE_PRIVATE).contains(key);
    }


    public boolean clear() {
        return mContext
                .getSharedPreferences(sFileName,
                        Context.MODE_PRIVATE).edit().clear().commit();
    }
}
