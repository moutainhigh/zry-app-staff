package com.zhongmei.analysis;

import android.content.Context;

import com.zhongmei.analysis.check.DataCheckManager;
import com.zhongmei.analysis.convert.DataConvertManager;
import com.zhongmei.analysis.store.DataStoreManager;

import java.io.File;


public class DataMobAgent {

    public static <T> void onEvent(Context context, PerformanceActionData data) {
                if (!DataCheckManager.checkDataValid(data)) {
            return;
        }
                T realData = DataConvertManager.getValidStoreData(data);
                DataStoreManager.storeData(context, realData);
    }


    public static <T> void deleteDataContent(Context context, T dataFormat) {
        DataStoreManager.deleteData(context, dataFormat);
    }


    public static File getDataFormat(Context context, String dateFormat) {
        return DataStoreManager.getDataFile(context, dateFormat);
    }


    public static <T> void writeAllData(Context context, DataHandleListener<T> listener) {
        DataStoreManager.writeAllData(context, listener);
    }
}
