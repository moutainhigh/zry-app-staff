package com.zhongmei.analysis.check;

import android.text.TextUtils;

import com.zhongmei.analysis.PerformanceActionData;


/**
 * Created by demo on 2018/12/15
 */

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
