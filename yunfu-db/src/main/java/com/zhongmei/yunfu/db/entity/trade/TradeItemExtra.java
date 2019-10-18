package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.Option;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.DishMakeStatus;


@DatabaseTable(tableName = "trade_item_extra")
public class TradeItemExtra extends DataEntityBase implements ICreator, IUpdator, Option {


    @Override
    public void onOption() {
        if (getUuid() == null) {
            setUuid(String.valueOf(getId()));
        }
    }


    public interface $ extends DataEntityBase.$ {


        public static final String tradeItemId = "trade_item_id";


        public static final String tradeItemUuid = "trade_item_uuid";


        public static final String isPack = "is_pack";



        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String dishMakeStatus = "dish_make_status";

    }

    @DatabaseField(columnName = "trade_item_id")
    private Long tradeItemId;

    @DatabaseField(columnName = "trade_item_uuid", canBeNull = false)
    private String tradeItemUuid;

    @DatabaseField(columnName = "is_pack")
    private Integer isPack;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "dish_make_status")
    private Integer dishMakeStatus;


    private Long batchId;

    public Long getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(Long tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public String getTradeItemUuid() {
        return tradeItemUuid;
    }

    public void setTradeItemUuid(String tradeItemUuid) {
        this.tradeItemUuid = tradeItemUuid;
    }

    public Bool getIsPack() {
        return ValueEnums.toEnum(Bool.class, isPack);
    }

    public void setIsPack(Bool isPack) {
        this.isPack = ValueEnums.toValue(isPack);
    }

    @Override
    public Long getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String getCreatorName() {
        return creatorName;
    }

    @Override
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public Long getUpdatorId() {
        return updatorId;
    }

    @Override
    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    @Override
    public String getUpdatorName() {
        return updatorName;
    }

    @Override
    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public DishMakeStatus getDishMakeStatus() {
        return ValueEnums.toEnum(DishMakeStatus.class, dishMakeStatus);
    }

    public void setDishMakeStatus(DishMakeStatus dishMakeStatus) {
        this.dishMakeStatus = ValueEnums.toValue(dishMakeStatus);
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public TradeItemExtra clone() {
        TradeItemExtra tradeItemExtra = new TradeItemExtra();
        tradeItemExtra.setIsPack(getIsPack());
        tradeItemExtra.setStatusFlag(getStatusFlag());
        tradeItemExtra.setTradeItemId(tradeItemId);
        tradeItemExtra.setCreatorId(creatorId);
        tradeItemExtra.setCreatorName(creatorName);
        tradeItemExtra.setUpdatorId(updatorId);
        tradeItemExtra.setUpdatorName(updatorName);
        tradeItemExtra.setTradeItemUuid(tradeItemUuid);
        tradeItemExtra.setDishMakeStatus(ValueEnums.toEnum(DishMakeStatus.class, dishMakeStatus));
        tradeItemExtra.setBrandIdenty(getBrandIdenty());
        tradeItemExtra.setServerCreateTime(getServerCreateTime());
        tradeItemExtra.setServerUpdateTime(getServerUpdateTime());
        tradeItemExtra.setChanged(isChanged());
        tradeItemExtra.setDeviceIdenty(getDeviceIdenty());
        tradeItemExtra.setUuid(getUuid());
        tradeItemExtra.setId(getId());
        tradeItemExtra.setShopIdenty(getShopIdenty());
        tradeItemExtra.setBatchId(getBatchId());
        return tradeItemExtra;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(tradeItemUuid, getShopIdenty(), getDeviceIdenty());
    }
}
