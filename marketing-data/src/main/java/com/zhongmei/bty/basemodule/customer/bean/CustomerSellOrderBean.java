package com.zhongmei.bty.basemodule.customer.bean;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.BusinessType;

import java.math.BigDecimal;

/**
 * @Date：2016年3月10日
 * @Description:售卡列表、储值列表bean
 * @Version: 1.0
 */
public class CustomerSellOrderBean extends CustomerOrderBean {
    private Long tradeId;
    /*private String mobile;
    private String sellTime;//售卡时间
    private String cardNo;//卡号
    private String sellMoney;//售价
    private String operater;//经手人
    private String deviceNo;//设备号
    private EntityCardType cardType;//实体卡类型 1:会员实体卡 2:匿名实体卡*/
    //服务器更新时间
    private Long serverUpdateTime;

    //实体卡和会员储值需获取到
    private String paymentUuid;
    private long customerId;

    //实体卡储值，需获取到卡信息
    private BigDecimal sendValue;//赠送金额
    private BigDecimal beforeValue;//充值前金额
    private BigDecimal addValue;//充值金额
    private BigDecimal endValue;//当前金额

    private BigDecimal beforeRealValue; // 操作前余额实储金额
    private BigDecimal beforeSendValue;    // 操作前余额赠送金额
    private BigDecimal currentRealValue; // 本次操作实储金额
    private BigDecimal currentSendValue; // 本次操作赠送金额
    private BigDecimal endRealValue; // 操作后实储金额
    private BigDecimal endSendValue; // 操作后赠送金额

    private BigDecimal integral;
    private String cardKindKame;
    private Long cardKindId;
    private Integer cardStatus;
    private BigDecimal remainValue;
    private Integer type;//记录类型 0：储值，1：退款，2：调账,3：消费，4：换卡储值调账

    private Integer addValueType;//储值类型 1：现金，2:银行卡

    private Long tradeClientCreateTime;
    private Long tradeServerUpdateTime;

    private Long historyId;
    private Long storeId;
    private Long bizDate;

    private Integer businessType;

	/*private Integer accountStatus; //1已到账、0未到账。
	private Integer tradeStatus;
	private Integer tradePayStatus;*/

	/*public AccountStatus getAccountStatus() {
		return ValueEnums.toEnum(AccountStatus.class, accountStatus);
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = ValueEnums.toValue(accountStatus);
	}

	public TradeStatus getTradeStatus() {
		return ValueEnums.toEnum(TradeStatus.class, tradeStatus);
	}

	public void setTradeStatus(TradeStatus tradeStatus) {
		this.tradeStatus = ValueEnums.toValue(tradeStatus);
	}

	public TradePayStatus getTradePayStatus() {
		return ValueEnums.toEnum(TradePayStatus.class, tradePayStatus);
	}

	public void setTradePayStatus(TradePayStatus tradePayStatus) {
		this.tradePayStatus = ValueEnums.toValue(tradePayStatus);;
	}*/

	/*public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getSellTime() {
		return sellTime;
	}
	public void setSellTime(String sellTime) {
		this.sellTime = sellTime;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getSellMoney() {
		return sellMoney;
	}
	public void setSellMoney(String sellMoney) {
		this.sellMoney = sellMoney;
	}
	public String getOperater() {
		return operater;
	}
	public void setOperater(String operater) {
		this.operater = operater;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public EntityCardType getCardType() {
		return cardType;
	}

	public void setCardType(EntityCardType cardType) {
		this.cardType = cardType;
	}*/

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getPaymentUuid() {
        return paymentUuid;
    }

    public void setPaymentUuid(String paymentUuid) {
        this.paymentUuid = paymentUuid;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getEndValue() {
        return endValue;
    }

    public void setAddValueType(int addValueType) {
        this.addValueType = addValueType;
    }

    public void setEndValue(BigDecimal endValue) {
        this.endValue = endValue;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public String getCardKindKame() {
        return cardKindKame;
    }

    public void setCardKindKame(String cardKindKame) {
        this.cardKindKame = cardKindKame;
    }
	/*public CardStatus getCardStatus() {
		return ValueEnums.toEnum(CardStatus.class, cardStatus);
	}
	public void setCardStatus(CardStatus cardStatus) {
		this.cardStatus = ValueEnums.toValue(cardStatus);
	}
*/


    public Integer getAddValueType() {
        return addValueType;
    }

    public Integer getCardStatus() {
        return cardStatus;
    }

    public void setAddValueType(Integer addValueType) {
        this.addValueType = addValueType;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }

    public Long getCardKindId() {
        return cardKindId;
    }

    public void setCardKindId(Long cardKindId) {
        this.cardKindId = cardKindId;
    }

    public Long getTradeClientCreateTime() {
        return tradeClientCreateTime;
    }

    public void setTradeClientCreateTime(Long tradeClientCreateTime) {
        this.tradeClientCreateTime = tradeClientCreateTime;
    }

    public Long getTradeServerUpdateTime() {
        return tradeServerUpdateTime;
    }

    public void setTradeServerUpdateTime(Long tradeServerUpdateTime) {
        this.tradeServerUpdateTime = tradeServerUpdateTime;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;

    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getBizDate() {
        return bizDate;
    }

    public void setBizDate(Long bizDate) {
        this.bizDate = bizDate;
    }

    public BigDecimal getBeforeValue() {
        return beforeValue;
    }

    public void setBeforeValue(BigDecimal beforeValue) {
        this.beforeValue = beforeValue;
    }

    public BigDecimal getAddValue() {
        return addValue;
    }

    public void setAddValue(BigDecimal addValue) {
        this.addValue = addValue;
    }

    public BigDecimal getSendValue() {
        return sendValue;
    }

    public void setSendValue(BigDecimal sendValue) {
        this.sendValue = sendValue;
    }

    public BigDecimal getRemainValue() {
        return remainValue;
    }

    public void setRemainValue(BigDecimal remainValue) {
        this.remainValue = remainValue;
    }

    public BusinessType getBusinessType() {
        return ValueEnums.toEnum(BusinessType.class, businessType);
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = ValueEnums.toValue(businessType);
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getBeforeRealValue() {
        return beforeRealValue;
    }

    public void setBeforeRealValue(BigDecimal beforeRealValue) {
        this.beforeRealValue = beforeRealValue;
    }

    public BigDecimal getBeforeSendValue() {
        return beforeSendValue;
    }

    public void setBeforeSendValue(BigDecimal beforeSendValue) {
        this.beforeSendValue = beforeSendValue;
    }

    public BigDecimal getCurrentRealValue() {
        return currentRealValue;
    }

    public void setCurrentRealValue(BigDecimal currentRealValue) {
        this.currentRealValue = currentRealValue;
    }

    public BigDecimal getCurrentSendValue() {
        return currentSendValue;
    }

    public void setCurrentSendValue(BigDecimal currentSendValue) {
        this.currentSendValue = currentSendValue;
    }

    public BigDecimal getEndRealValue() {
        return endRealValue;
    }

    public void setEndRealValue(BigDecimal endRealValue) {
        this.endRealValue = endRealValue;
    }

    public BigDecimal getEndSendValue() {
        return endSendValue;
    }

    public void setEndSendValue(BigDecimal endSendValue) {
        this.endSendValue = endSendValue;
    }
}
