package com.zhongmei.bty.basemodule.monitor.shopmanager.handover.entitys;

import com.zhongmei.bty.basemodule.database.entity.shopmanager.CashHandover;

import java.util.ArrayList;
import java.util.List;

/**
 * 交接返回数据
 */
public class HandoverData extends CashHandover {
    private static final long serialVersionUID = 1L;

    private boolean editable;

    private boolean haveUnpaid;//是否有未支付订单

    private boolean haveUnClearAccount;//是否有未清账的数据

    private Long bizDate;//营业时间
    //	 收款明细
    private List<HandoverItem> cashHandoverItems;

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
