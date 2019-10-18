package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.enums.InvoiceSource;
import com.zhongmei.bty.basemodule.trade.enums.InvoiceType;
import com.zhongmei.yunfu.util.ValueEnums;



public class InvoiceRevokeReq {

    private Long shopIdenty;


    private Integer source;


    private String uuid;


    private Long orderId;


    private Integer type;


    private String drawerNo;


    private String drawer;


    private String reason;

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public InvoiceSource getSource() {
        return ValueEnums.toEnum(InvoiceSource.class, source);
    }

    public void setSource(InvoiceSource source) {
        this.source = ValueEnums.toValue(source);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public InvoiceType getType() {
        return ValueEnums.toEnum(InvoiceType.class, type);
    }

    public void setType(InvoiceType type) {
        this.type = ValueEnums.toValue(type);
    }

    public String getDrawerNo() {
        return drawerNo;
    }

    public void setDrawerNo(String drawerNo) {
        this.drawerNo = drawerNo;
    }

    public String getDrawer() {
        return drawer;
    }

    public void setDrawer(String drawer) {
        this.drawer = drawer;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
