package com.zhongmei.yunfu.bean.req;

import com.zhongmei.bty.basemodule.devices.mispos.enums.AccountStatus;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;

import java.math.BigDecimal;


public class CustomerCardTimeStoreResp {

    private Integer padNo;     private String cardNum;     private Long customerId;     private Integer cardType;     private Integer accountStatus;     private Long tradeId;
    private String tradeNo;     private BigDecimal tradeAmount;     private String creatorName;     private Integer tradeStatus;     private Integer tradePayStatus;     private Long brandIdenty;     private Long shopIdenty;     private String deviceIdenty;     private Long tradeServerUpdateTime;     private Integer storeType;     private BigDecimal beforeRealValue;     private BigDecimal beforeSendValue;     private BigDecimal beforePrepareValue;     private BigDecimal currentRealValue;     private BigDecimal currentSendValue;     private BigDecimal currentPrepareValue;     private BigDecimal endRealValue;     private BigDecimal endSendValue;     private BigDecimal endPrepareValue;     private Long historyId;     private String paymentUuid;
    private Long serverCreateTime;     private Integer addValueType;    private Long bizDate;

    public Integer getPadNo() {
        return padNo;
    }

    public String getCardNum() {
        return cardNum;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public EntityCardType getCardType() {
        return ValueEnums.toEnum(EntityCardType.class, cardType);
    }

    public AccountStatus getAccountStatus() {
        return ValueEnums.toEnum(AccountStatus.class, accountStatus);
    }

    public Long getTradeId() {
        return tradeId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public TradeStatus getTradeStatus() {
        return ValueEnums.toEnum(TradeStatus.class, tradeStatus);
    }

    public TradePayStatus getTradePayStatus() {
        return ValueEnums.toEnum(TradePayStatus.class, tradePayStatus);
    }

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public String getDeviceIdenty() {
        return deviceIdenty;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public Long getTradeServerUpdateTime() {
        return tradeServerUpdateTime;
    }

    public Integer getStoreType() {
        return storeType;
    }

    public BigDecimal getBeforeRealValue() {
        return beforeRealValue;
    }

    public BigDecimal getBeforeSendValue() {
        return beforeSendValue;
    }

    public BigDecimal getBeforePrepareValue() {
        return beforePrepareValue;
    }

    public BigDecimal getCurrentRealValue() {
        return currentRealValue;
    }

    public BigDecimal getCurrentSendValue() {
        return currentSendValue;
    }

    public BigDecimal getCurrentPrepareValue() {
        return currentPrepareValue;
    }

    public BigDecimal getEndRealValue() {
        return endRealValue;
    }

    public BigDecimal getEndSendValue() {
        return endSendValue;
    }

    public BigDecimal getEndPrepareValue() {
        return endPrepareValue;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public String getPaymentUuid() {
        return paymentUuid;
    }

    public Integer getAddValueType() {
        return addValueType;
    }

    public Long getBizDate() {
        return bizDate;
    }
}
