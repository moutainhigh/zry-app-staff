package com.zhongmei.analysis;

import android.content.Context;

import com.zhongmei.analysis.check.DataCheckManager;
import com.zhongmei.analysis.convert.DataConvertManager;
import com.zhongmei.analysis.store.DataStoreManager;

import java.io.File;

/**
 * Created by demo on 2018/12/15
 */
public class DataMobAgent {
    /**
     * 将传入的关心的性能数据进行存储
     *
     * @param context 上下文
     * @param data    相关的性能数据
     */
    public static <T> void onEvent(Context context, PerformanceActionData data) {
        // 数据不符合规范，直接返回
        if (!DataCheckManager.checkDataValid(data)) {
            return;
        }
        // 对数据进行转换，得到我们真正想存入的数据类型
        T realData = DataConvertManager.getValidStoreData(data);
        // 数据存储数据
        DataStoreManager.storeData(context, realData);
    }

    /**
     * 删除指定日期的文件
     *
     * @param context
     * @param dataFormat 数据格式；例：2017-05-09
     */
    public static <T> void deleteDataContent(Context context, T dataFormat) {
        DataStoreManager.deleteData(context, dataFormat);
    }

    /**
     * 获取指定的数据文件
     *
     * @param context 上下文
     * @return 获取的文件对象，找不到的情况下得到Null
     */
    public static File getDataFormat(Context context, String dateFormat) {
        return DataStoreManager.getDataFile(context, dateFormat);
    }

    /**
     * 将所有数据强制写入
     *
     * @param context 上下文
     * @Param listener 数据操作回调
     */
    public static <T> void writeAllData(Context context, DataHandleListener<T> listener) {
        DataStoreManager.writeAllData(context, listener);
    }
}
