package com.zhongmei.bty.basemodule.discount.bean.salespromotion;

import java.io.Serializable;

public class SalesPromotionRuleLimitPeriod implements Serializable {
        private String startPeriod;
        private String endPeriod;

    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public String getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }
}
