package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.yunfu.db.enums.BusinessType;

import java.io.Serializable;
import java.math.BigDecimal;



public class JCRechargeCardReq implements Serializable {
    Long userId;    String oldCardNum;    String newCardNum;    String remark = "金诚充值";    Integer source;    Integer sourceChild;
    String tradeNo;    BigDecimal tradeAmount;    String tradeMemo;    String uuid;
    Long clientCreateTime;    Long clientUpdateTime;    Long serverUpdateTime;    Long creatorId;    String creatorName;    Long updatorId;    String updatorName;    Integer statusFlag;    Long bizDate;    Long tradeTime;    Integer cardType;    String mobile;    String payPassword;    BigDecimal totalValueCard;
    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    Integer businessType;


    public BigDecimal getTotalValueCard() {
        return totalValueCard;
    }

    public void setTotalValueCard(BigDecimal totalValueCard) {
        this.totalValueCard = totalValueCard;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    public String getEntitycardNum() {
        return entitycardNum;
    }

    public void setEntitycardNum(String entitycardNum) {
        this.entitycardNum = entitycardNum;
    }

    Integer customerType;    String entitycardNum;    boolean notNeedPwd;
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }


    private static final long serialVersionUID = 1L;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getSourceChild() {
        return sourceChild;
    }

    public void setSourceChild(Integer sourceChild) {
        this.sourceChild = sourceChild;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getTradeMemo() {
        return tradeMemo;
    }

    public void setTradeMemo(String tradeMemo) {
        this.tradeMemo = tradeMemo;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getClientCreateTime() {
        return clientCreateTime;
    }

    public void setClientCreateTime(Long clientCreateTime) {
        this.clientCreateTime = clientCreateTime;
    }

    public Long getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Long getBizDate() {
        return bizDate;
    }

    public void setBizDate(Long bizDate) {
        this.bizDate = bizDate;
    }

    public Long getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Long tradeTime) {
        this.tradeTime = tradeTime;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public boolean isNotNeedPwd() {
        return notNeedPwd;
    }

    public void setNotNeedPwd(boolean notNeedPwd) {
        this.notNeedPwd = notNeedPwd;
    }
}
