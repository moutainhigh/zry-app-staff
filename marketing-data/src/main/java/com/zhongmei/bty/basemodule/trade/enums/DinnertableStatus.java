package com.zhongmei.bty.basemodule.trade.enums;


public enum DinnertableStatus {


    UNISSUED(0),


    ISSUED(1),


    SERVING(2),


    EMPTY(3),


    DONE(4);

    public final int value;

    private DinnertableStatus(int value) {
        this.value = value;
    }

}
