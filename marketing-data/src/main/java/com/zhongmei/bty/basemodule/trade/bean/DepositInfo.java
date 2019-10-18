package com.zhongmei.bty.basemodule.trade.bean;

import java.io.Serializable;
import java.math.BigDecimal;


public class DepositInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int TYPE_BY_PEOPLE = 1;

    public static final int TYPE_BY_TRADE = 2;

    public static final int TYPE_BY_CUSTME = 3;
    private int type;
    private BigDecimal value;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
