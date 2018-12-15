package com.zhongmei.analysis.convert;

import com.zhongmei.analysis.PerformanceActionData;

/**
 * Created by demo on 2018/12/15
 */

public class DataConvertManager {
    private static IDataConvert sDataConvertEngine;

    // 先对原始数据进行转换
    public static <T> T getValidStoreData(PerformanceActionData data) {
        IDataConvert dataConvert = getDataConvertEngine();
        return (T) dataConvert.convertData(data);
    }

    private static IDataConvert getDataConvertEngine() {
        if (sDataConvertEngine == null) {
            sDataConvertEngine = new PerformanceStoreDataConvert();
        }
        return sDataConvertEngine;
    }
}
