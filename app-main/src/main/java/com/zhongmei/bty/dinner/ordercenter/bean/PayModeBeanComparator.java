package com.zhongmei.bty.dinner.ordercenter.bean;

import java.io.Serializable;
import java.util.Comparator;

public class PayModeBeanComparator implements Comparator<PayModeBean>, Serializable {

    @Override
    public int compare(PayModeBean bean1, PayModeBean bean2) {
        int result = bean1.getValue() - bean2.getValue();
        return result;
    }

}
