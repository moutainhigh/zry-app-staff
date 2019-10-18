package com.zhongmei.bty.commonmodule.database.entity.local;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.bty.commonmodule.database.enums.PosOpType;
import com.zhongmei.yunfu.util.ValueEnums;


@DatabaseTable(tableName = "pos_trans_log")
public class PosTransLog extends LocalEntityBase {

    private static final long serialVersionUID = 1L;


    public interface $ extends LocalEntityBase.$ {


        public static final String acqNumber = "acq_number";


        public static final String amount = "amount";


        public static final String appName = "app_name";


        public static final String batchNumber = "batch_number";


        public static final String cardName = "card_name";


        public static final String cardNumber = "card_number";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String expireDate = "expire_date";


        public static final String hostSerialNumber = "host_serial_number";


        public static final String issName = "iss_name";


        public static final String issNumber = "iss_number";


        public static final String merchantName = "merchant_name";


        public static final String merchantNumber = "merchant_number";


        public static final String posOpType = "pos_op_type";


        public static final String posTraceNumber = "pos_trace_number";


        public static final String terminalNumber = "terminal_number";


        public static final String transDate = "trans_date";


        public static final String transTime = "trans_time";


        public static final String transType = "trans_type";


        public static final String memo = "memo";


        public static final String tradeNo = "trade_no";

    }

        @DatabaseField(columnName = "host_serial_number", canBeNull = false)
    private String hostSerialNumber;

        @DatabaseField(columnName = "acq_number")
    private String acqNumber;

        @DatabaseField(columnName = "pos_trace_number", canBeNull = false)
    private String posTraceNumber;

    @DatabaseField(columnName = "amount", canBeNull = false)
    private Integer amount;

    @DatabaseField(columnName = "app_name")
    private String appName;

    @DatabaseField(columnName = "batch_number", canBeNull = false)
    private String batchNumber;

    @DatabaseField(columnName = "card_name")
    private String cardName;

    @DatabaseField(columnName = "card_number", canBeNull = false)
    private String cardNumber;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "expire_date")
    private String expireDate;

    @DatabaseField(columnName = "iss_name")
    private String issName;

    @DatabaseField(columnName = "iss_number")
    private String issNumber;

    @DatabaseField(columnName = "merchant_name")
    private String merchantName;

    @DatabaseField(columnName = "merchant_number")
    private String merchantNumber;

    @DatabaseField(columnName = "pos_op_type")
    private Integer posOpType;

    @DatabaseField(columnName = "terminal_number", canBeNull = false)
    private String terminalNumber;

    @DatabaseField(columnName = "trans_date", canBeNull = false)
    private String transDate;

    @DatabaseField(columnName = "trans_time", canBeNull = false)
    private String transTime;

    @DatabaseField(columnName = "trans_type", canBeNull = false)
    private String transType;

    @DatabaseField(columnName = "memo")
    private String memo;

    @DatabaseField(columnName = "trade_no")
    private String tradeNo;

    private String keyValue;

    public String getHostSerialNumber() {
        return hostSerialNumber;
    }

    public void setHostSerialNumber(String hostSerialNumber) {
        this.hostSerialNumber = hostSerialNumber;
    }

    public String getAcqNumber() {
        return acqNumber;
    }

    public void setAcqNumber(String acqNumber) {
        this.acqNumber = acqNumber;
    }

    public String getPosTraceNumber() {
        return posTraceNumber;
    }

    public void setPosTraceNumber(String posTraceNumber) {
        this.posTraceNumber = posTraceNumber;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
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

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getIssName() {
        return issName;
    }

    public void setIssName(String issName) {
        this.issName = issName;
    }

    public String getIssNumber() {
        return issNumber;
    }

    public void setIssNumber(String issNumber) {
        this.issNumber = issNumber;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantNumber() {
        return merchantNumber;
    }

    public void setMerchantNumber(String merchantNumber) {
        this.merchantNumber = merchantNumber;
    }

    public PosOpType getPosOpType() {
        return ValueEnums.toEnum(PosOpType.class, posOpType);
    }

    public void setPosOpType(PosOpType posOpType) {
        this.posOpType = ValueEnums.toValue(posOpType);
    }

    public String getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && EntityBase.checkNonNull(hostSerialNumber,
                posTraceNumber,
                amount,
                batchNumber,
                cardNumber,
                terminalNumber,
                transDate, transTime, transType);
    }
}
