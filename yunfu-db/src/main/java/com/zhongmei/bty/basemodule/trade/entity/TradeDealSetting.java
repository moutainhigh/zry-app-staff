package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingBusinessType;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingOperateType;


@DatabaseTable(tableName = "trade_deal_setting")
public class TradeDealSetting extends BasicEntityBase {

    private static final long serialVersionUID = 1L;


    public interface $ extends BasicEntityBase.$ {


        public static final String businessType = "business_type";


        public static final String creatorId = "creator_id";


        public static final String isEnabled = "is_enabled";


        public static final String operateType = "operate_type";


        public static final String shopIdenty = "shop_identy";


        public static final String updatorId = "updator_id";


        public static final String waitTime = "wait_time";

    }

    @DatabaseField(columnName = "business_type")
    private Integer businessType;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "is_enabled", canBeNull = false)
    private Integer isEnabled;

    @DatabaseField(columnName = "operate_type", canBeNull = false)
    private Integer operateType;

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "wait_time", canBeNull = false)
    private Integer waitTime;

    public TradeDealSettingBusinessType getBusinessType() {
        return ValueEnums.toEnum(TradeDealSettingBusinessType.class, businessType);
    }

    public void setBusinessType(TradeDealSettingBusinessType businessType) {
        this.businessType = ValueEnums.toValue(businessType);
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public YesOrNo getIsEnabled() {
        return ValueEnums.toEnum(YesOrNo.class, isEnabled);
    }

    public void setIsEnabled(YesOrNo isEnabled) {
        this.isEnabled = ValueEnums.toValue(isEnabled);
    }

    public TradeDealSettingOperateType getOperateType() {
        return ValueEnums.toEnum(TradeDealSettingOperateType.class, operateType);
    }

    public void setOperateType(TradeDealSettingOperateType operateType) {
        this.operateType = ValueEnums.toValue(operateType);
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public Integer getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(isEnabled, operateType, shopIdenty, waitTime);
    }
}

