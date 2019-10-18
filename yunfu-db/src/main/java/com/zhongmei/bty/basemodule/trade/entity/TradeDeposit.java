package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.UuidEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.math.BigDecimal;


@DatabaseTable(tableName = "trade_deposit")
public class TradeDeposit extends UuidEntityBase implements Cloneable {
    private static final long serialVersionUID = 1L;


    public interface $ extends UuidEntityBase.$ {

        public static final String shopIdenty = "shop_identy";


        public static final String tradeId = "trade_id";


        public static final String tradeUuid = "trade_uuid";


        public static final String id = "id";


        public static final String depositPay = "deposit_pay";


        public static final String depositRefund = "deposit_refund";


        public static final String statusFlag = "status_flag";


        public static final String serverCreateTime = "server_create_time";


        public static final String serverUpdateTime = "server_update_time";


        public static final String brandIdenty = "brand_identy";


        public static final String type = "type";

        public static final String unitPrice = "unit_price";
    }

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "trade_id")
    private Long tradeId;

    @DatabaseField(columnName = "trade_uuid", canBeNull = false)
    private String tradeUuid;

    @DatabaseField(columnName = "id")
    private Long id;

    @DatabaseField(columnName = "deposit_pay", canBeNull = false)
    private BigDecimal depositPay;

    @DatabaseField(columnName = "deposit_refund")
    private BigDecimal depositRefund;


    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;


    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;


    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;


    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;


    @DatabaseField(columnName = "type")
    private Integer type;


    @DatabaseField(columnName = "unit_price")
    private BigDecimal unitPrice;

    public BigDecimal getDepositRefund() {
        return depositRefund;
    }

    public void setDepositRefund(BigDecimal depositRefund) {
        this.depositRefund = depositRefund;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
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

    public BigDecimal getDepositPay() {
        return depositPay;
    }

    public void setDepositPay(BigDecimal depositPay) {
        this.depositPay = depositPay;
    }

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(shopIdenty, tradeUuid, depositPay, statusFlag, brandIdenty);
    }

    public boolean isNeedToPay() {
                if (this.statusFlag == 1 && this.depositPay != null && this.depositPay.compareTo(BigDecimal.ZERO) == 1) {
            return true;
        }
        return false;
    }

    public TradeDeposit clone() {
        TradeDeposit tradeDeposit = null;
        try {
            tradeDeposit = (TradeDeposit) super.clone();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return tradeDeposit;
    }

    public void setInvalid() {
        this.setStatusFlag(StatusFlag.INVALID);
        this.setChanged(true);
    }

    public void validateCreate() {
        validateCreate(null);
    }

    public void validateCreate(IAuthUser waiter) {
        setStatusFlag(StatusFlag.VALID);
        setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        setShopIdenty(BaseApplication.sInstance.getShopIdenty());
    }

}
