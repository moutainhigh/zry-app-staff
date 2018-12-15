package com.zhongmei.bty.cashier.dal;

import android.content.Context;

/**
 * Created by demo on 2018/12/15
 */
public abstract class BaseDal implements Dal {

    protected Context context;

    protected void onCreate(Context context) {
        this.context = context;
    }

}
