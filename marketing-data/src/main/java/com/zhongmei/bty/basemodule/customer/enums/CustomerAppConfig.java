package com.zhongmei.bty.basemodule.customer.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 顾客配置
 * Created by demo on 2018/12/15
 */
public class CustomerAppConfig {

    @IntDef({CustomerBussinessType.DINNER, CustomerBussinessType.BEAUTY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerBussinessType {
        int DINNER = 1; // 正餐
        int BEAUTY = 2;// 美业
    }
}
