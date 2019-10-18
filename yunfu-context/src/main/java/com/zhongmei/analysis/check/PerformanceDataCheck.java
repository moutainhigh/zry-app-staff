package com.zhongmei.analysis.check;

import android.text.TextUtils;

import com.zhongmei.analysis.PerformanceActionData;




public class PerformanceDataCheck implements IDataCheck<PerformanceActionData> {

    @Override
    public boolean isDataValid(PerformanceActionData data) {
        if (data == null) {
            return false;
        }
        if (TextUtils.isEmpty(data.getMacAddress()) || TextUtils.isEmpty(data.getShopId())
                || TextUtils.isEmpty(data.getCode())) {
            return false;
        }
        return true;
    }
}
