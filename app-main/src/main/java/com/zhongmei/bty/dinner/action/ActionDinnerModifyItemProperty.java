package com.zhongmei.bty.dinner.action;

import java.math.BigDecimal;


public class ActionDinnerModifyItemProperty {

        private String uuid;

        private BigDecimal selectedQty;

    public ActionDinnerModifyItemProperty(String uuid, BigDecimal selectedQty) {
        this.uuid = uuid;
        this.selectedQty = selectedQty;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getSelectedQty() {
        return selectedQty;
    }

    public void setSelectedQty(BigDecimal selectedQty) {
        this.selectedQty = selectedQty;
    }

}
