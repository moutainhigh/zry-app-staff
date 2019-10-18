package com.zhongmei.bty.basemodule.monitor.shopmanager.handover.entitys;

import com.zhongmei.bty.basemodule.database.entity.shopmanager.CashHandover;

import java.util.ArrayList;
import java.util.List;


public class HandoverData extends CashHandover {
    private static final long serialVersionUID = 1L;

    private boolean editable;

    private boolean haveUnpaid;
    private boolean haveUnClearAccount;
    private Long bizDate;        private List<HandoverItem> cashHandoverItems;

    public List<HandoverItem> getCashHandoverItems() {
        return cashHandoverItems == null ? cashHandoverItems = new ArrayList<>() : cashHandoverItems;
    }

    public void setCashHandoverItems(List<HandoverItem> cashHandoverItems) {
        this.cashHandoverItems = cashHandoverItems;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isHaveUnpaid() {
        return haveUnpaid;
    }

    public void setHaveUnpaid(boolean haveUnpaid) {
        this.haveUnpaid = haveUnpaid;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isHaveUnClearAccount() {
        return haveUnClearAccount;
    }

    public void setHaveUnClearAccount(boolean haveUnClearAccount) {
        this.haveUnClearAccount = haveUnClearAccount;
    }

    public Long getBizDate() {
        return bizDate;
    }

    public void setBizDate(Long bizDate) {
        this.bizDate = bizDate;
    }
}
