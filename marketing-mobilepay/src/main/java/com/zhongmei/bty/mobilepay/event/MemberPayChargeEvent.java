package com.zhongmei.bty.mobilepay.event;

import java.math.BigDecimal;


public class MemberPayChargeEvent {
    public final static int BALANCE_CHANGE_TYPE_CHARGE = 1;    public final static int BALANCE_CHANGE_TYPE_CONSUME = 0;
    private int mType = 0;
    private BigDecimal mValueCardBalance = new BigDecimal(0);
    public BigDecimal getmValueCardBalance() {
        return mValueCardBalance;
    }

    public void setmValueCardBalance(BigDecimal mValueCardBalance) {
        this.mValueCardBalance = mValueCardBalance;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }
}
