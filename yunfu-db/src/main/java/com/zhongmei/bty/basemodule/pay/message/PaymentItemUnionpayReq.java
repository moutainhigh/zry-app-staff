package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.bty.commonmodule.database.enums.PosBusinessType;
import com.zhongmei.yunfu.util.ValueEnums;

import java.io.Serializable;

/**
 * @Date：2016-2-18 下午12:52:29
 * @Description: TODO
 * @Version: 1.0
 */
public class PaymentItemUnionpayReq implements Serializable {

    private String uuid;

    /**
     * 支付明细uuid.
     */
    private String paymentItemUuid;

    /**
     * 交易日期.
     */
    private Long transDate;

    /**
     * 交易类型.
     */
    private Integer transType;

    /**
     * 金额.
     */
    private Integer amount;

    /**
     * 费率.
     */
    private Double rates;

    /**
     * 手续费.
     */
    private Double fee;

    /**
     * 系统参考号.
     */
    private String hostSerialNumber;

    /**
     * 流水号.
     */
    private String posTraceNumber;

    /**
     * 批次号.
     */
    private String batchNumber;

    /**
     * 终端号.
     */
    private String terminalNumber;

    /**
     * pos渠道id.
     */
    private Long posChannelId;

    /**
     * 刷卡pos设备id.
     */
    private Long paymentDeviceId;

    /**
     * 刷卡pos设备id.
     */
    private String appname;

    private Long creatorId;

    private String creatorName;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPaymentItemUuid() {
        return paymentItemUuid;
    }

    public void setPaymentItemUuid(String paymentItemUuid) {
        this.paymentItemUuid = paymentItemUuid;
    }

    public Long getTransDate() {
        return transDate;
    }

    public void setTransDate(Long transDate) {
        this.transDate = transDate;
    }

    public PosBusinessType getTransType() {
        return ValueEnums.toEnum(PosBusinessType.class, transType);
    }

    public void setTransType(PosBusinessType transType) {
        this.transType = ValueEnums.toValue(transType);
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getRates() {
        return rates;
    }

    public void setRates(Double rates) {
        this.rates = rates;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getHostSerialNumber() {
        return hostSerialNumber;
    }

    public void setHostSerialNumber(String hostSerialNumber) {
        this.hostSerialNumber = hostSerialNumber;
    }

    public String getPosTraceNumber() {
        return posTraceNumber;
    }

    public void setPosTraceNumber(String posTraceNumber) {
        this.posTraceNumber = posTraceNumber;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public Long getPosChannelId() {
        return posChannelId;
    }

    public void setPosChannelId(Long posChannelId) {
        this.posChannelId = posChannelId;
    }

    public Long getPaymentDeviceId() {
        return paymentDeviceId;
    }

    public void setPaymentDeviceId(Long paymentDeviceId) {
        this.paymentDeviceId = paymentDeviceId;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
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
}
