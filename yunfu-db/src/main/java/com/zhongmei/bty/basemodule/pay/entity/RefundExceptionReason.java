package com.zhongmei.bty.basemodule.pay.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.CommonEntityBase;
import com.zhongmei.yunfu.db.enums.PaymentType;


@DatabaseTable(tableName = "refund_exception_reason")
public class RefundExceptionReason extends CommonEntityBase {

    public interface $ extends CommonEntityBase.$ {

        String brandIdenty = "brand_identy";

        String shopIdenty = "shop_identy";

        String paymentItemId = "payment_item_id";

        String exceptionCode = "exception_code";

        String paymentType = "payment_type";

    }


    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;


    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "payment_item_id", canBeNull = false)
    private Long paymentItemId;

    @DatabaseField(columnName = "exception_code", canBeNull = false)
    private Long exceptionCode;

    @DatabaseField(columnName = "payment_type", canBeNull = false)
    private Integer paymentType;

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public Long getPaymentItemId() {
        return paymentItemId;
    }

    public void setPaymentItemId(Long paymentItemId) {
        this.paymentItemId = paymentItemId;
    }

    public Long getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(Long exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public PaymentType getPaymentType() {
        return ValueEnums.toEnum(PaymentType.class, paymentType);
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = ValueEnums.toValue(paymentType);
    }

}