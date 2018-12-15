package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.enums.SellerAccountType;
import com.zhongmei.yunfu.db.enums.PayType;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * PaymentItemExtra is a ORMLite bean type. Corresponds to the database table "payment_item_extra"
 */
@DatabaseTable(tableName = "payment_item_extra")
public class PaymentItemExtra extends DataEntityBase {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "payment_item_extra"
     */
    public interface $ extends DataEntityBase.$ {

        /**
         * buyer_account
         */
        public static final String buyerAccount = "buyer_account";

        /**
         * pay_callback_time
         */
        public static final String payCallbackTime = "pay_callback_time";

        /**
         * payment_item_id
         */
        public static final String paymentItemId = "payment_item_id";

        public static final String paymentItemUuid = "payment_item_uuid";

        /**
         * pay_type
         */
        public static final String payType = "pay_type";

        /**
         * pay_tran_no
         */
        public static final String payTranNo = "pay_tran_no";

        /**
         * refund_callback_time
         */
        public static final String refundCallbackTime = "refund_callback_time";

        /**
         * refund_trade_no
         */
        public static final String refundTradeNo = "refund_trade_no";

        /**
         * seller_account
         */
        public static final String sellerAccount = "seller_account";

        /**
         * seller_account_type
         */
        public static final String sellerAccountType = "seller_account_type";

    }

    @DatabaseField(columnName = "buyer_account")
    private String buyerAccount;

    @DatabaseField(columnName = "pay_callback_time")
    private Long payCallbackTime;

    @DatabaseField(columnName = "payment_item_id")
    private Long paymentItemId;

    @DatabaseField(columnName = "payment_item_uuid")
    private String paymentItemUuid;

    @DatabaseField(columnName = "pay_type")
    private Integer payType;

    @DatabaseField(columnName = "pay_tran_no")
    private String payTranNo;

    @DatabaseField(columnName = "refund_callback_time")
    private Long refundCallbackTime;

    @DatabaseField(columnName = "refund_trade_no")
    private String refundTradeNo;

    @DatabaseField(columnName = "seller_account")
    private String sellerAccount;

    @DatabaseField(columnName = "seller_account_type")
    private Integer sellerAccountType;

    @DatabaseField(columnName = "customer_id")
    private Long customerId;

    @DatabaseField(columnName = "entity_no")
    private String entityNo;

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public Long getPayCallbackTime() {
        return payCallbackTime;
    }

    public void setPayCallbackTime(Long payCallbackTime) {
        this.payCallbackTime = payCallbackTime;
    }

    public Long getPaymentItemId() {
        return paymentItemId;
    }

    public void setPaymentItemId(Long paymentItemId) {
        this.paymentItemId = paymentItemId;
    }

    public String getPaymentItemUuid() {
        return paymentItemUuid;
    }

    public void setPaymentItemUuid(String paymentItemUuid) {
        this.paymentItemUuid = paymentItemUuid;
    }

    public String getPayTranNo() {
        return payTranNo;
    }

    public void setPayTranNo(String payTranNo) {
        this.payTranNo = payTranNo;
    }

    public Long getRefundCallbackTime() {
        return refundCallbackTime;
    }

    public void setRefundCallbackTime(Long refundCallbackTime) {
        this.refundCallbackTime = refundCallbackTime;
    }

    public String getRefundTradeNo() {
        return refundTradeNo;
    }

    public void setRefundTradeNo(String refundTradeNo) {
        this.refundTradeNo = refundTradeNo;
    }

    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    public PayType getPayType() {
        return ValueEnums.toEnum(PayType.class, payType);
    }

    public void setPayType(PayType payType) {
        this.payType = ValueEnums.toValue(payType);
    }

    public SellerAccountType getSellerAccountType() {
        return ValueEnums.toEnum(SellerAccountType.class, sellerAccountType);
    }

    public void setSellerAccountType(SellerAccountType sellerAccountType) {
        this.sellerAccountType = ValueEnums.toValue(sellerAccountType);
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(paymentItemId);
    }
}

