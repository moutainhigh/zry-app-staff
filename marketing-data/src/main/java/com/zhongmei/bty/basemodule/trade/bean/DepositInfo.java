package com.zhongmei.bty.basemodule.trade.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Date： 2016/7/19
 * @Description:押金内容
 * @Version: 1.0
 */
public class DepositInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int TYPE_BY_PEOPLE = 1;

    public static final int TYPE_BY_TRADE = 2;

    public static final int TYPE_BY_CUSTME = 3; //自定义

    private int type;//押金类型 1按人计算押金(默认) 2按订单算押金

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
