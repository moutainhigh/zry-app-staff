package com.zhongmei.bty.takeout.sender.event;

import java.math.BigDecimal;

import com.zhongmei.bty.commonmodule.event.EventBase;

/**
 *

 *
 */
public class SelectItemEvent extends EventBase {
    public final boolean isSelect;

    public final BigDecimal count;

    public final BigDecimal amount;

    public SelectItemEvent(boolean isSelect, BigDecimal count, BigDecimal amount) {
        this.isSelect = isSelect;
        this.count = count;
        this.amount = amount;
    }

}
