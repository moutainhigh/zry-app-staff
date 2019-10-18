package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.PaymentType;

import java.math.BigDecimal;


@DatabaseTable(tableName = "payment")
public class Payment extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends DataEntityBase.$ {


        public static final String actualAmount = "actual_amount";


        public static final String bizDate = "biz_date";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String exemptAmount = "exempt_amount";


        public static final String handoverUuid = "handover_uuid";


        public static final String memo = "memo";


        public static final String paymentTime = "payment_time";


        public static final String paymentType = "payment_type";


        public static final String receivableAmount = "receivable_amount";


        public static final String relateId = "relate_id";


        public static final String relateUuid = "relate_uuid";


        public static final String isPaid = "is_paid";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";

    }

    @DatabaseField(columnName = "actual_amount")
    private java.math.BigDecimal actualAmount;

    @DatabaseField(columnName = "biz_date", canBeNull = false)
    private Long bizDate;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "exempt_amount", canBeNull = false)
    private java.math.BigDecimal exemptAmount;

    @DatabaseField(columnName = "handover_uuid")
    private String handoverUuid;

    @DatabaseField(columnName = "memo")
    private String memo;

    @DatabaseField(columnName = "payment_time", canBeNull = false)
    private Long paymentTime;

    @DatabaseField(columnName = "payment_type", canBeNull = false)
    private Integer paymentType;

    @DatabaseField(columnName = "receivable_amount", canBeNull = false)
    private java.math.BigDecimal receivableAmount;

    @DatabaseField(columnName = "relate_id", index = true)
    private Long relateId;

    @DatabaseField(columnName = "relate_uuid")
    private String relateUuid;

    @DatabaseField(columnName = "is_paid")
    private Integer isPaid = Bool.YES.value();

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    private BigDecimal beforePrivilegeAmount;

    @DatabaseField(columnName = "shop_actual_amount", canBeNull = false)
    private BigDecimal shopActualAmount;
    public java.math.BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(java.math.BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getBeforePrivilegeAmount() {
        return beforePrivilegeAmount;
    }

    public void setBeforePrivilegeAmount(BigDecimal beforePrivilegeAmount) {
        this.beforePrivilegeAmount = beforePrivilegeAmount;
    }

    public BigDecimal getShopActualAmount() {
        return shopActualAmount;
    }

    public void setShopActualAmount(BigDecimal shopActualAmount) {
        this.shopActualAmount = shopActualAmount;
    }

    public Long getBizDate() {
        return bizDate;
    }

    public void setBizDate(Long bizDate) {
        this.bizDate = bizDate;
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

    public java.math.BigDecimal getExemptAmount() {
        return exemptAmount;
    }

    public void setExemptAmount(java.math.BigDecimal exemptAmount) {
        this.exemptAmount = exemptAmount;
    }

    public String getHandoverUuid() {
        return handoverUuid;
    }

    public void setHandoverUuid(String handoverUuid) {
        this.handoverUuid = handoverUuid;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Long paymentTime) {
        this.paymentTime = paymentTime;
    }

    public PaymentType getPaymentType() {
        return ValueEnums.toEnum(PaymentType.class, paymentType);
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = ValueEnums.toValue(paymentType);
    }

    public java.math.BigDecimal getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(java.math.BigDecimal receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public Long getRelateId() {
        return relateId;
    }

    public void setRelateId(Long relateId) {
        this.relateId = relateId;
    }

    public String getRelateUuid() {
        return relateUuid;
    }

    public void setRelateUuid(String relateUuid) {
        this.relateUuid = relateUuid;
    }

    public Bool getIsPaid() {
        return ValueEnums.toEnum(Bool.class, isPaid);
    }

    public void setIsPaid(Bool isPaid) {
        this.isPaid = ValueEnums.toValue(isPaid);
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

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(bizDate, exemptAmount, paymentTime, paymentType, receivableAmount, shopActualAmount);
    }
}

