package com.zhongmei.analysis.convert;

import com.zhongmei.analysis.PerformanceActionData;


public interface IDataConvert<T> {
    T convertData(PerformanceActionData data);
}
