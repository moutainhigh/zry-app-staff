package com.zhongmei.analysis.check;

/**
 * Created by demo on 2018/12/15
 */

public class DataCheckManager {
    private static IDataCheck sDataCheckEngine;

    /**
     * 检测传入的数据是否符合要求
     *
     * @param data 实际的数据
     * @param <T>  数据类型
     * @return
     */
    public static <T> boolean checkDataValid(T data) {
        IDataCheck dataCheck = getDataCheckEngine();
        return dataCheck.isDataValid(data);
    }

    // 目前只有这一种检测，先留下这一个方法，后边可以再添加
    private static IDataCheck getDataCheckEngine() {
        if (sDataCheckEngine == null) {
            sDataCheckEngine = new PerformanceDataCheck();
        }
        return sDataCheckEngine;
    }
}
