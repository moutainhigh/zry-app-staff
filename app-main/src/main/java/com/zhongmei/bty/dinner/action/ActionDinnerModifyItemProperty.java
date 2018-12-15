package com.zhongmei.bty.dinner.action;

import java.math.BigDecimal;

/**
 * 修改菜品属性，发送该action让属性界面进行同步数据
 */
public class ActionDinnerModifyItemProperty {

    // 菜品uuid
    private String uuid;

    // 修改后的菜品数量
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
