package com.zhongmei.yunfu.context.util;

import android.content.Context;
import android.text.TextUtils;

import java.util.Set;

/**
 * @Date：2014年11月4日 上午11:44:15
 * @Description: sp的操作工具类
 * @Version: 1.0
 */
public class SharedPreferenceUtil {

    /**
     * @date：2014年11月4日 上午11:44:27
     * @Description:上下文
     */
    private Context mContext;

    /**
     * @date：2014年11月4日 下午4:15:07
     * @Description:TODO
     */
    private static SharedPreferenceUtil instance;

    /*
     * 存储sharedPreference的文件名
     */
    private static String sFileName;

    /**
     * @param context
     * @Constructor
     * @Description 构造方法
     */
    private SharedPreferenceUtil(Context context) {
        this.mContext = context;
    }

    /**
     * @Title: init
     * @Description: 初始化sp对象
     * @Param @param context TODO
     * @Return void 返回类型
     */
    public static void init(Context context, String fileName) {
        if (TextUtils.isEmpty(fileName) || context == null) {
            throw new RuntimeException("fileName or context is empty, can't initial sharedPreferenceUtils");
        }
        sFileName = fileName;
        instance = new SharedPreferenceUtil(context.getApplicationContext());
    }

    /**
     * @Title: getInstance
     * @Description: 获取实例对象
     * @Param @return TODO
     * @Return SharedPreferenceUtil 返回类型
     */
    public static SharedPreferenceUtil getSpUtil() {
        if (instance == null) {
            throw new IllegalStateException("context not initialized");
        } else {
            return instance;
        }
    }

    /**
     * @Title: putStringSet
     * @Description: 存储set数组
     * @Param @param key
     * @Param @param values
     * @Param @return TODO
     * @Return boolean 返回类型
     */
    public boolean putStringSet(String key, Set<String> values) {
        return mContext
                .getSharedPreferences(sFileName,
                        Context.MODE_PRIVATE).edit().putStringSet(key, values)
                .commit();
    }

    /**
     * @Title: putString
     * @Description: 存储string
     * @Param @param key
     * @Param @param value
     * @Param @return TODO
     * @Return boolean 返回类型
     */
    public boolean putString(String key, String value) {
        return mContext
                .getSharedPreferences(sFileName,
                        Context.MODE_PRIVATE).edit().putString(key, value)
                .commit();
    }

    /**
     * @Title: putLong
     * @Description: 存储long
     * @Param @param key
     * @Param @param value
     * @Param @return TODO
     * @Return boolean 返回类型
     */
    public boolean putLong(String key, long value) {
        return mContext
                .getSharedPreferences(sFileName,
                        Context.MODE_PRIVATE).edit().putLong(key, value)
                .commit();
    }

    /**
     * @Title: putBoolean
     * @Description: 存储boolean
     * @Param @param key
     * @Param @param value
     * @Param @return TODO
     * @Return boolean 返回类型
     */
    public boolean putBoolean(String key, boolean value) {
        return mContext
                .getSharedPreferences(sFileName,
                        Context.MODE_PRIVATE).edit().putBoolean(key, value)
                .commit();
    }

    /**
     * @Title: putInt
     * @Description: 存储int
     * @Param @param key
     * @Param @param value
     * @Param @return TODO
     * @Return boolean 返回类型
     */
    public boolean putInt(String key, int value) {
        return mContext
                .getSharedPreferences(sFileName,
                        Context.MODE_PRIVATE).edit().putInt(key, value)
                .commit();
    }

    /**
     * @Title: getString
     * @Description: 获取string
     * @Param @param key
     * @Param @param defValue
     * @Param @return TODO
     * @Return String 返回类型
     */
    public String getString(String key, String defValue) {
        return mContext.getSharedPreferences(sFileName,
                Context.MODE_PRIVATE).getString(key, defValue);
    }

    /**
     * @Title: getLong
     * @Description: 获取long
     * @Param @param key
     * @Param @param defValue
     * @Param @return TODO
     * @Return long 返回类型
     */
    public long getLong(String key, long defValue) {
        return mContext.getSharedPreferences(sFileName,
                Context.MODE_PRIVATE).getLong(key, defValue);
    }

    /**
     * @Title: getBoolean
     * @Description: 获取boolean
     * @Param @param key
     * @Param @param defValue
     * @Param @return TODO
     * @Return boolean 返回类型
     */
    public boolean getBoolean(String key, boolean defValue) {
        return mContext.getSharedPreferences(sFileName,
                Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    /**
     * @Title: getInt
     * @Description: 获取int
     * @Param @param key
     * @Param @param defValue
     * @Param @return TODO
     * @Return int 返回类型
     */
    public int getInt(String key, int defValue) {
        return mContext.getSharedPreferences(sFileName,
                Context.MODE_PRIVATE).getInt(key, defValue);
    }

    /**
     * @Title: getStringSet
     * @Description: 获取数组
     * @Param @param key
     * @Param @param values
     * @Param @return TODO
     * @Return Set<String> 返回类型
     */
    public Set<String> getStringSet(String key, Set<String> values) {
        return mContext.getSharedPreferences(sFileName,
                Context.MODE_PRIVATE).getStringSet(key, values);
    }

    /**
     * @Title: remove
     * @Description: 移除某一个键值对
     * @Param @param key
     * @Param @return TODO
     * @Return boolean 返回类型
     */
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

    /**
     * @Title: clear
     * @Description: 清空所有的sp
     * @Param @return TODO
     * @Return boolean 返回类型
     */
    public boolean clear() {
        return mContext
                .getSharedPreferences(sFileName,
                        Context.MODE_PRIVATE).edit().clear().commit();
    }
}
