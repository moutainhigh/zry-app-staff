package com.zhongmei.yunfu.util;

import java.math.BigDecimal;


public class Decimal extends BigDecimal {


    private static final long serialVersionUID = 1L;

    private Decimal(BigDecimal value) {
        super(MathDecimal.toTrimZeroString(value));
    }

    @Override
    public String toString() {
        return toPlainString();
    }


    public static Decimal valueOf(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return new Decimal(value);
    }
}
