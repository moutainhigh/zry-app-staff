package com.zhongmei.bty.mobilepay.event;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */
public class MemberPayChargeEvent {
    public final static int BALANCE_CHANGE_TYPE_CHARGE = 1;//充值
    public final static int BALANCE_CHANGE_TYPE_CONSUME = 0;//消费

    private int mType = 0;//操作类别

    private BigDecimal mValueCardBalance = new BigDecimal(0);// 会员余额

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
