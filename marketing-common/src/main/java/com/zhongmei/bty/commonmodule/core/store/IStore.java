package com.zhongmei.bty.commonmodule.core.store;


public interface IStore {

    boolean getBoolean(String key, boolean defaultValue);

    void putBoolean(String key, boolean value);

    int getInt(String key, int defaultValue);

    void putInt(String key, int Value);

    float getFloat(String key, float defaultValue);

    void putFloat(String key, float Value);

    long getLong(String key, long defaultValue);

    void putLong(String key, long Value);

    String getString(String key, String defaultValue);

    void putString(String key, String Value);

    void commit();
}
