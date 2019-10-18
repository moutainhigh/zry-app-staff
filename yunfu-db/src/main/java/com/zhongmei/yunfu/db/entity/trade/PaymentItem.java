package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGroupon;
import com.zhongmei.bty.basemodule.pay.message.PaymentItemUnionCardReq;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.yunfu.db.enums.PaySource;
import com.zhongmei.yunfu.db.enums.RefundWay;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.util.ValueEnums;

import java.math.BigDecimal;


@DatabaseTable(tableName = "payment_item")
public class PaymentItem extends DataEntityBase implements ICreator {

    private static final long serialVersionUID = 1L;


    public interface $ extends DataEntityBase.$ {


        public static final String changeAmount = "change_amount";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String faceAmount = "face_amount";


        public static final String paymentId = "payment_id";


        public static final String paymentUuid = "payment_uuid";


        public static final String payMemo = "pay_memo";


        public static final String payModeId = "pay_mode_id";


        public static final String payModelGroup = "pay_model_group";


        public static final String payModeName = "pay_mode_name";


        public static final String payStatus = "pay_status";


        public static final String relateId = "relate_id";


        public static final String usefulAmount = "useful_amount";


        public static final String paySource = "pay_source";

        public static final String quantity = "quantity";

    }

    @DatabaseField(columnName = "change_amount", canBeNull = false)
    private java.math.BigDecimal changeAmount;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "face_amount", canBeNull = false)
    private java.math.BigDecimal faceAmount;

    @DatabaseField(columnName = "payment_id", index = true)
    private Long paymentId;

    @DatabaseField(columnName = "payment_uuid")
    private String paymentUuid;

    @DatabaseField(columnName = "pay_memo")
    private String payMemo;

    @DatabaseField(columnName = "pay_mode_id", canBeNull = false)
    private Long payModeId;

    @DatabaseField(columnName = "pay_model_group")
    private Integer payModelGroup;

    @DatabaseField(columnName = "pay_mode_name")
    public String payModeName;

    @DatabaseField(columnName = "pay_status")
    private Integer payStatus;

    @DatabaseField(columnName = "relate_id")
    private String relateId;

    @DatabaseField(columnName = "useful_amount", canBeNull = false)
    private java.math.BigDecimal usefulAmount;

    @DatabaseField(columnName = "refund_way")
    private Integer refundWay;

    @DatabaseField(columnName = "pay_source", canBeNull = false, index = true)
    private Integer paySource = PaySource.CASHIER.value();

    @DatabaseField(columnName = "quantity")
    private Integer quantity;

    private PaymentItemExtra paymentItemExtra;

    private PaymentItemUnionCardReq paymentItemUnionPay;
    private PaymentItemGroupon paymentItemGroupon;
    private String authCode;
    private String consumePassword;
    private Integer type;
    private Integer isDeposit;
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsDeposit() {
        return isDeposit;
    }

    public void setIsDeposit(Integer isDeposit) {
        this.isDeposit = isDeposit;
    }

    public BigDecimal getNoDiscountAmount() {
        return noDiscountAmount;
    }

    public void setNoDiscountAmount(BigDecimal noDiscountAmount) {
        this.noDiscountAmount = noDiscountAmount;
    }

    private BigDecimal noDiscountAmount;
    public java.math.BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(java.math.BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
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

    public java.math.BigDecimal getFaceAmount() {
        return faceAmount;
    }

    public void setFaceAmount(java.math.BigDecimal faceAmount) {
        this.faceAmount = faceAmount;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentUuid() {
        return paymentUuid;
    }

    public void setPaymentUuid(String paymentUuid) {
        this.paymentUuid = paymentUuid;
    }

    public String getPayMemo() {
        return payMemo;
    }

    public void setPayMemo(String payMemo) {
        this.payMemo = payMemo;
    }

    public Long getPayModeId() {
        return payModeId;
    }

    public void setPayModeId(Long payModeId) {
        this.payModeId = payModeId;
    }

    public PayModelGroup getPayModelGroup() {
        return ValueEnums.toEnum(PayModelGroup.class, payModelGroup);
    }

    public void setPayModelGroup(PayModelGroup payModelGroup) {
        this.payModelGroup = ValueEnums.toValue(payModelGroup);
    }

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }

    public TradePayStatus getPayStatus() {
        return ValueEnums.toEnum(TradePayStatus.class, payStatus);
    }

    public void setPayStatus(TradePayStatus payStatus) {
        this.payStatus = ValueEnums.toValue(payStatus);
    }

    public String getRelateId() {
        return relateId;
    }

    public void setRelateId(String relateId) {
        this.relateId = relateId;
    }

    public java.math.BigDecimal getUsefulAmount() {
        return usefulAmount;
    }

    public void setUsefulAmount(java.math.BigDecimal usefulAmount) {
        this.usefulAmount = usefulAmount;
    }

    public RefundWay getRefundWay() {
        return ValueEnums.toEnum(RefundWay.class, refundWay);
    }

    public void setRefundWay(RefundWay refundWay) {
        this.refundWay = ValueEnums.toValue(refundWay);
    }

    public PaySource getPaySource() {
        return ValueEnums.toEnum(PaySource.class, paySource);
    }

    public void setPaySource(PaySource paySource) {
        this.paySource = ValueEnums.toValue(paySource);
    }


    public PaymentItemExtra getPaymentItemExtra() {
        return paymentItemExtra;
    }

    public void setPaymentItemExtra(PaymentItemExtra paymentItemExtra) {
        this.paymentItemExtra = paymentItemExtra;
    }

    public void setPaymentItemUnionPay(PaymentItemUnionCardReq paymentItemUnionPay) {
        this.paymentItemUnionPay = paymentItemUnionPay;
    }

    public PaymentItemUnionCardReq getPaymentItemUnionPay() {
        return paymentItemUnionPay;
    }

    public PaymentItemGroupon getPaymentItemGroupon() {
        return paymentItemGroupon;
    }

    public void setPaymentItemGroupon(PaymentItemGroupon paymentItemGroupon) {
        this.paymentItemGroupon = paymentItemGroupon;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getConsumePassword() {
        return consumePassword;
    }

    public void setConsumePassword(String consumePassword) {
        this.consumePassword = consumePassword;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(changeAmount, faceAmount, paymentId, paymentUuid, payModeId, usefulAmount, paySource);
    }
}
