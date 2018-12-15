package com.zhongmei.bty.basemodule.discount.bean.salespromotion;

import java.io.Serializable;

public class SalesPromotionValidityPeriod implements Serializable {
    //开始日期
    private String startDate;
    //结束日期
    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
