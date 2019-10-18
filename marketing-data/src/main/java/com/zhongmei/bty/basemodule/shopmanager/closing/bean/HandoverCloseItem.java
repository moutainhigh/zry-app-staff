package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import com.zhongmei.bty.basemodule.database.entity.shopmanager.CashHandoverItem;


public class HandoverCloseItem extends CashHandoverItem {

    private static final long serialVersionUID = 1L;

    Long handoverDate;
    String handoverUserName;
    public Long getHandoverDate() {
        return handoverDate;
    }

    public void setHandoverDate(Long handoverDate) {
        this.handoverDate = handoverDate;
    }

    public String getHandoverUserName() {
        return handoverUserName;
    }

    public void setHandoverUserName(String handoverUserName) {
        this.handoverUserName = handoverUserName;
    }


}
