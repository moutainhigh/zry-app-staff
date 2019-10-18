package com.zhongmei.analysis.store;

import android.content.Context;

import com.zhongmei.analysis.DataHandleListener;

import java.io.File;


public class DataStoreManager {
    private static IDataStore sDataStoreEngine;


    public static <T> void storeData(Context context, T data) {
        getDataStoreEngine().realStoreData(context, data);
    }


    public static <T> void deleteData(Context context, T dateFormat) {
        getDataStoreEngine().deleteStoreData(context, dateFormat);
    }


    public static File getDataFile(Context context, String dateFormat) {
        if (getDataStoreEngine() instanceof SdcardDataStore) {
            return ((SdcardDataStore) getDataStoreEngine()).getDataFormat(context, dateFormat);
        }
        return null;
    }


    public static void writeAllData(Context context, DataHandleListener listener) {
        getDataStoreEngine().writeAllData(context, listener);
    }


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
