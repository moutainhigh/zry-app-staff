package com.zhongmei.analysis.convert;


import com.zhongmei.analysis.PerformanceActionData;

/**
 * Created by demo on 2018/12/15
 */
public class PerformanceStoreDataConvert implements IDataConvert<PerformanceActionData> {

    // 一个数据转换的例子，该类不需要对数据进行转换，所以直接返回
    @Override
    public PerformanceActionData convertData(PerformanceActionData data) {
        return data;
    }
}
