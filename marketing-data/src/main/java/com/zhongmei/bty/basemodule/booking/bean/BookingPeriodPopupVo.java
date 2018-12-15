package com.zhongmei.bty.basemodule.booking.bean;

import com.zhongmei.bty.commonmodule.database.entity.Period;

/**
 * 预订时间
 */
public class BookingPeriodPopupVo {
    private boolean isSelect;

    private Period period;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }
}
