package com.zhongmei.analysis.convert;

import com.zhongmei.analysis.PerformanceActionData;



public class DataConvertManager {
    private static IDataConvert sDataConvertEngine;

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
