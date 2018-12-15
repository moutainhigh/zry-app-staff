package com.zhongmei.bty.commonmodule.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by demo on 2018/12/15
 */

public class DecimalFormatUtil {

    public static String formateInventoryNumber(BigDecimal numnber) {
        if (numnber == null) {
            numnber = BigDecimal.ZERO;
        }
        DecimalFormat df = new DecimalFormat("###########0.#####");
        return df.format(numnber);
    }

    public static String formateInventoryMoney(BigDecimal amount) {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
        DecimalFormat df = new DecimalFormat("###########0.00###");
        return df.format(amount);
    }
}
