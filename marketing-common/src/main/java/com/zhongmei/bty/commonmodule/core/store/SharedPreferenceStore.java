package com.zhongmei.bty.commonmodule.core.store;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 简单存储的SharedPreference实现类
 */
public class SharedPreferenceStore implements IStore {

    private SharedPreferences sharedPreferences;

    public SharedPreferenceStore(Context context, String name) {
        this(context, name, Context.MODE_APPEND);
    }

    public SharedPreferenceStore(Context context, String name, int mode) {
        this.sharedPreferences = context.getSharedPreferences(name, mode);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }

    @Override
    public void putBoolean(String key, boolean value) {
        if (sharedPreferences != null) {
            sharedPreferences.edit().putBoolean(key, value).apply();
        }
    }

    @Override
    public int getInt(String key, int defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    @Override
    public void putInt(String key, int value) {
        if (sharedPreferences != null) {
            sharedPreferences.edit().putInt(key, value).apply();
        }
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getFloat(key, defaultValue);
        }
        return defaultValue;
    }

    @Override
    public void putFloat(String key, float value) {
        if (sharedPreferences != null) {
            sharedPreferences.edit().putFloat(key, value).apply();
        }
    }

    @Override
    public long getLong(String key, long defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getLong(key, defaultValue);
        }
        return defaultValue;
    }

    @Override
    public void putLong(String key, long value) {
        if (sharedPreferences != null) {
            sharedPreferences.edit().putLong(key, value).apply();
        }
    }

    @Override
    public String getString(String key, String defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, defaultValue);
        }
        return defaultValue;
    }

    @Override
    public void putString(String key, String value) {
        if (sharedPreferences != null) {
            sharedPreferences.edit().putString(key, value).apply();
        }
    }

    @Override
    public void commit() {
        if (sharedPreferences != null) {
            sharedPreferences.edit().apply();
        }
    }
}
