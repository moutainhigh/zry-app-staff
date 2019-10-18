package com.zhongmei.bty.basemodule.customer.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class CustomerAppConfig {

    @IntDef({CustomerBussinessType.DINNER, CustomerBussinessType.BEAUTY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerBussinessType {
        int DINNER = 1;         int BEAUTY = 2;    }
}
