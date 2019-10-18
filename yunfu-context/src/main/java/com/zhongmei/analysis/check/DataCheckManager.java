package com.zhongmei.analysis.check;



public class DataCheckManager {
    private static IDataCheck sDataCheckEngine;


    public static <T> boolean checkDataValid(T data) {
        IDataCheck dataCheck = getDataCheckEngine();
        return dataCheck.isDataValid(data);
    }

        private static IDataCheck getDataCheckEngine() {
        if (sDataCheckEngine == null) {
            sDataCheckEngine = new PerformanceDataCheck();
        }
        return sDataCheckEngine;
    }
}
