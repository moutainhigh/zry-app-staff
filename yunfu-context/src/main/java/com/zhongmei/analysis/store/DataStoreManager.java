package com.zhongmei.analysis.store;

import android.content.Context;

import com.zhongmei.analysis.DataHandleListener;

import java.io.File;

/**
 * Created by demo on 2018/12/15
 */
public class DataStoreManager {
    private static IDataStore sDataStoreEngine;

    /**
     * 对传入的数据进行转换，然后进行存储
     *
     * @param context 上下文
     * @param data    需要储存的数据
     * @param <T>
     */
    public static <T> void storeData(Context context, T data) {
        getDataStoreEngine().realStoreData(context, data);
    }

    /**
     * 删除制定日期的文件
     *
     * @param context    上下文
     * @param dateFormat 日期格式：****-**-**；例：2017-05-09
     */
    public static <T> void deleteData(Context context, T dateFormat) {
        getDataStoreEngine().deleteStoreData(context, dateFormat);
    }

    /**
     * 获得指定格式的文件
     *
     * @param context    上下文
     * @param dateFormat 指定格式 例："2017-05-10"
     * @return
     */
    public static File getDataFile(Context context, String dateFormat) {
        if (getDataStoreEngine() instanceof SdcardDataStore) {
            return ((SdcardDataStore) getDataStoreEngine()).getDataFormat(context, dateFormat);
        }
        return null;
    }

    /**
     * 强制将内存中的数据写入
     *
     * @param context  上下文
     * @param listener 写入成功或者失败的回调
     */
    public static void writeAllData(Context context, DataHandleListener listener) {
        getDataStoreEngine().writeAllData(context, listener);
    }

    /*
     * 创建存储数据的对象，目前默认的是使用Sdcard进行存储
     */
    private synchronized static IDataStore getDataStoreEngine() {
        if (sDataStoreEngine == null) {
            sDataStoreEngine = new SdcardDataStore();
        }
        return sDataStoreEngine;
    }

    public static void setDataStoreEngine(IDataStore sDataStoreEngine) {
        DataStoreManager.sDataStoreEngine = sDataStoreEngine;
    }
}
