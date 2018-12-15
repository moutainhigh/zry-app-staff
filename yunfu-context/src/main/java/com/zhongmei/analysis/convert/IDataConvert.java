package com.zhongmei.analysis.convert;

import com.zhongmei.analysis.PerformanceActionData;

/**
 * Created by demo on 2018/12/15
 */
public interface IDataConvert<T> {
    T convertData(PerformanceActionData data);
}
