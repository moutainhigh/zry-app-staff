package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.enums.InvoiceSource;
import com.zhongmei.bty.basemodule.trade.enums.InvoiceType;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * 电子发票冲红请求
 */

public class InvoiceRevokeReq {
    /**
     * 商户id
     */
    private Long shopIdenty;

    /**
     * 来源
     */
    private Integer source;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 消费订单id，挂单开票必传，就餐对应的trade.id，储值对应的是储值记录的id
     */
    private Long orderId;

    /**
     * 消费类型
     */
    private Integer type;

    /**
     * POS操作人编号
     */
    private String drawerNo;

    /**
     * 开票人/POS操作人名称
     */
    private String drawer;

    /**
     * 原因
     */
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
