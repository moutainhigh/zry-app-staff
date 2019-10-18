package com.zhongmei.bty.cashier.dal;

import android.content.Context;


public abstract class BaseDal implements Dal {

    protected Context context;

    protected void onCreate(Context context) {
        this.context = context;
    }

}
