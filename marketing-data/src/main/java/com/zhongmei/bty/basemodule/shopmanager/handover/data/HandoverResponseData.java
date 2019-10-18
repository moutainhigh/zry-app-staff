package com.zhongmei.bty.basemodule.shopmanager.handover.data;

import com.zhongmei.bty.basemodule.monitor.shopmanager.handover.entitys.HandoverData;
import com.zhongmei.bty.basemodule.monitor.shopmanager.handover.entitys.HandoverItem;

import java.util.List;


public class HandoverResponseData {


    private List<HandoverData> cashHandovers;


    private List<HandoverItem> cashHandoverItems;

    public List<HandoverData> getCashHandovers() {
        return cashHandovers;
    }

    public void setCashHandovers(List<HandoverData> cashHandovers) {
        this.cashHandovers = cashHandovers;
    }

    public List<HandoverItem> getCashHandoverItems() {
        return cashHandoverItems;
    }

    public void setCashHandoverItems(List<HandoverItem> cashHandoverItems) {
        this.cashHandoverItems = cashHandoverItems;
    }


}
