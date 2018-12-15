package com.zhongmei.yunfu.db.entity.discount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.enums.QuickDiscountType;
import com.zhongmei.yunfu.util.ValueEnums;

import java.math.BigDecimal;

/**
 * TradePromotion is a ORMLite bean type. Corresponds to the database table "trade_promotion"
 */
@DatabaseTable(tableName = "trade_promotion")
public class TradePromotion extends DataEntityBase {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "trade_promotion"
     */
    public interface $ extends BasicEntityBase.$ {
        String shopIdenty = "shop_identy";
        String desc = "desc";
        String mount = "mount";
        String tradeId = "trade_id";
        String tradeUuid = "trade_uuid";
        String source = "source";
        String paymentItemId = "payment_item_id";
        String paymentItemUuid = "payment_item_uuid";
        String platformAmount = "platform_amount";
        String shopActivityAmount = "shop_activity_amount";
        String serviceCharge = "service_charge";
        String shopActualAmount = "shop_actual_amount";
        String isReconciliation = "is_reconciliation";
    }

    @DatabaseField(columnName = "desc")
    private String desc;

    @DatabaseField(columnName = "mount", canBeNull = false)
    private BigDecimal mount;

    @DatabaseField(columnName = "trade_id")
    private Long tradeId;

    @DatabaseField(columnName = "trade_uuid")
    private String tradeUuid;

    @DatabaseField(columnName = "source", canBeNull = false)
    private Integer source;

    @DatabaseField(columnName = "payment_item_id")
    private Long paymentItemId;//add 20161220

    @DatabaseField(columnName = "payment_item_uuid")
    private String paymentItemUuid;

    @DatabaseField(columnName = "platform_amount")
    private BigDecimal platformAmount;

    @DatabaseField(columnName = "shop_activity_amount")
    private BigDecimal shopActivityAmount;

    @DatabaseField(columnName = "service_charge")
    private BigDecimal serviceCharge;

    @DatabaseField(columnName = "shop_actual_amount")
    private BigDecimal shopActualAmount;

    @DatabaseField(columnName = "is_reconciliation")
    private Integer isReconciliation;

    public Long getPaymentItemId() {
        return paymentItemId;
    }

    public void setPaymentItemId(Long paymentItemId) {
        this.paymentItemId = paymentItemId;
    }

    public QuickDiscountType getSource() {
        return ValueEnums.toEnum(QuickDiscountType.class, source);
    }

    public void setSource(QuickDiscountType source) {
        this.source = ValueEnums.toValue(source);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BigDecimal getMount() {
        return mount;
    }

    public void setMount(BigDecimal mount) {
        this.mount = mount;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public String getPaymentItemUuid() {
        return paymentItemUuid;
    }

    public void setPaymentItemUuid(String paymentItemUuid) {
        this.paymentItemUuid = paymentItemUuid;
    }

    public BigDecimal getPlatformAmount() {
        return platformAmount;
    }

    public void setPlatformAmount(BigDecimal platformAmount) {
        this.platformAmount = platformAmount;
    }

    public BigDecimal getShopActivityAmount() {
        return shopActivityAmount;
    }

    public void setShopActivityAmount(BigDecimal shopActivityAmount) {
        this.shopActivityAmount = shopActivityAmount;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getShopActualAmount() {
        return shopActualAmount;
    }

    public void setShopActualAmount(BigDecimal shopActualAmount) {
        this.shopActualAmount = shopActualAmount;
    }

    public Integer getIsReconciliation() {
        return isReconciliation;
    }

    public void setIsReconciliation(Integer isReconciliation) {
        this.isReconciliation = isReconciliation;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(mount, tradeId, source);
    }
}

