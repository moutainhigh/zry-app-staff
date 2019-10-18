package com.zhongmei.bty.basemodule.commonbusiness.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.enums.DeliveryPlatform;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import java.math.BigDecimal;


@DatabaseTable(tableName = "partner_delivery_platform_config")
public class PartnerDeliveryPlatformConfig extends BasicEntityBase {

    public interface $ extends BasicEntityBase.$ {
        String uuid = "uuid";

        String shopIdenty = "shop_identy";


        String deliveryPlatform = "delivery_platform";


        String type = "type";


        String isAutoAddTip = "is_auto_add_tip";


        String intervalTime = "interval_time";


        String autoAddMoney = "auto_add_money";


        String maxAutoAddMoney = "max_auto_add_money";

        String memo = "memo";
    }

    @DatabaseField(columnName = "uuid", canBeNull = false)
    private String uuid;

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;


    @DatabaseField(columnName = "delivery_platform", canBeNull = false)
    private Integer deliveryPlatform;


    @DatabaseField(columnName = "type", canBeNull = false)
    private Integer type;


    @DatabaseField(columnName = "is_auto_add_tip", canBeNull = false)
    private Integer isAutoAddTip;


    @DatabaseField(columnName = "interval_time")
    private Integer intervalTime;


    @DatabaseField(columnName = "auto_add_money")
    private BigDecimal autoAddMoney;


    @DatabaseField(columnName = "max_auto_add_money")
    private BigDecimal maxAutoAddMoney;

    @DatabaseField(columnName = "memo")
    private String memo;

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public DeliveryPlatform getDeliveryPlatform() {
        return ValueEnums.toEnum(DeliveryPlatform.class, deliveryPlatform);
    }

    public void setDeliveryPlatform(DeliveryPlatform deliveryPlatform) {
        this.deliveryPlatform = ValueEnums.toValue(deliveryPlatform);
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public YesOrNo getIsAutoAddTip() {
        return ValueEnums.toEnum(YesOrNo.class, isAutoAddTip);
    }

    public void setIsAutoAddTip(YesOrNo isAutoAddTip) {
        this.isAutoAddTip = ValueEnums.toValue(isAutoAddTip);
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    public BigDecimal getAutoAddMoney() {
        return autoAddMoney;
    }

    public void setAutoAddMoney(BigDecimal autoAddMoney) {
        this.autoAddMoney = autoAddMoney;
    }

    public BigDecimal getMaxAutoAddMoney() {
        return maxAutoAddMoney;
    }

    public void setMaxAutoAddMoney(BigDecimal maxAutoAddMoney) {
        this.maxAutoAddMoney = maxAutoAddMoney;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
