package com.zhongmei.bty.basemodule.orderdish.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 * 商品扩展表
 * Created by demo on 2018/12/15
 */

@DatabaseTable(tableName = "trade_item_extra_takeaway")
public class TradeItemExtraTakeaway extends IdEntityBase implements ICreator, IUpdator {

    public interface $ extends IdEntityBase.$ {
        public static final String statusFlag = "status_flag";
        public static final String brandIdenty = "brand_identy";
        public static final String serverCreateTime = "server_create_time";
        public static final String serverUpdateTime = "server_update_time";
        public static final String shopIdenty = "shop_identy";
        public static final String updatorName = "updator_name";
        public static final String creatorName = "creator_name";
        public static final String creatorId = "creator_id";
        public static final String updatorId = "updator_id";

        public static final String tradeItemId = "trade_item_id";
        public static final String tradeId = "trade_id";
        public static final String packName = "pack_name";

    }


    @DatabaseField(columnName = "trade_item_id", canBeNull = false)
    private Long tradeItemId;
    @DatabaseField(columnName = "server_create_time", canBeNull = false)
    private Long serverCreateTime;
    @DatabaseField(columnName = "server_update_time", canBeNull = false)
    private Long serverUpdateTime;
    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;
    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;
    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;
    @DatabaseField(columnName = "trade_id", canBeNull = false)
    private Long tradeId;
    @DatabaseField(columnName = "pack_name", canBeNull = false)
    private String packName;


    @DatabaseField(columnName = "creator_id")
    private Long creatorId;
    @DatabaseField(columnName = "creator_name")
    private String creatorName;
    @DatabaseField(columnName = "updator_id")
    private Long updatorId;
    @DatabaseField(columnName = "updator_name")
    private String updatorName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(Long tradeItemId) {
        this.tradeItemId = tradeItemId;
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

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
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

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }


}