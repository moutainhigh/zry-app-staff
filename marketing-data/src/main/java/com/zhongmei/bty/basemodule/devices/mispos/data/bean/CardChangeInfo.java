package com.zhongmei.bty.basemodule.devices.mispos.data.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 换卡信息
 */

public class CardChangeInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private long brandId;//品牌ID
    private long commercialId;//门店ID
    private String clientType;//客户端请求来源
    private long userId;//操作用户ID
    private String oldCardNum;//原卡号
    private String newCardNum;//新卡号
    private String tradeUuid;//订单Uuid
    private BigDecimal payAmount;//换卡实际支付费用
    private String remark;//换卡备注

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(long commercialId) {
        this.commercialId = commercialId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getOldCardNum() {
        return oldCardNum;
    }

    public void setOldCardNum(String oldCardNum) {
        this.oldCardNum = oldCardNum;
    }

    public String getNewCardNum() {
        return newCardNum;
    }

    public void setNewCardNum(String newCardNum) {
        this.newCardNum = newCardNum;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
