package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.bty.commonmodule.database.enums.BatchDishRelType;

import java.math.BigDecimal;


@DatabaseTable(tableName = "trade_item_main_batch_rel_extra")
public class TradeItemMainBatchRelExtra extends BasicEntityBase {
    private static final long serialVersionUID = 1L;

    public interface $ extends BasicEntityBase.$ {


        public static final String mainId = "main_id";
        public static final String mainUuid = "main_uuid";
        public static final String subId = "sub_Id";
        public static final String subUuid = "sub_uuid";
        public static final String mainTradeId = "main_trade_id";
        public static final String subTradeId = "sub_trade_id";
        public static final String subTradeItemUuid = "sub_trade_item_uuid";
        public static final String subTradeItemId = "sub_trade_item_id";
        public static final String relType = "rel_type";
        public static final String quantity = "quantity";
        public static final String shopIdenty = "shop_identy";
    }


    @DatabaseField(columnName = "main_id")
    private Long mainId;

    @DatabaseField(columnName = "main_uuid")
    private String mainUuid;

    @DatabaseField(columnName = "sub_id")
    private Long subId;

    @DatabaseField(columnName = "sub_uuid")
    private String subUuid;

    @DatabaseField(columnName = "main_trade_id")
    private Long mainTradeId;

    @DatabaseField(columnName = "sub_trade_id")
    private Long subTradeId;

    @DatabaseField(columnName = "sub_trade_item_id")
    private Long subTradeItemId;

    @DatabaseField(columnName = "sub_trade_item_uuid")
    private String subTradeItemUuid;

    @DatabaseField(columnName = "rel_type")
    private Integer relType;

    @DatabaseField(columnName = "quantity")
    private BigDecimal quantity;

    public BatchDishRelType getType() {
        return ValueEnums.toEnum(BatchDishRelType.class, relType);
    }

    public void setType(BatchDishRelType type) {
        this.relType = ValueEnums.toValue(type);
    }

    public Long getMainId() {
        return mainId;
    }

    public void setMainId(Long mainId) {
        this.mainId = mainId;
    }

    public String getMainUuid() {
        return mainUuid;
    }

    public void setMainUuid(String mainUuid) {
        this.mainUuid = mainUuid;
    }

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }

    public String getSubUuid() {
        return subUuid;
    }

    public void setSubUuid(String subUuid) {
        this.subUuid = subUuid;
    }

    public Long getMainTradeId() {
        return mainTradeId;
    }

    public void setMainTradeId(Long mainTradeId) {
        this.mainTradeId = mainTradeId;
    }

    public Long getSubTradeId() {
        return subTradeId;
    }

    public void setSubTradeId(Long subTradeId) {
        this.subTradeId = subTradeId;
    }


    public Integer getRelType() {
        return relType;
    }

    public void setRelType(Integer relType) {
        this.relType = relType;
    }

    public Long getSubTradeItemId() {
        return subTradeItemId;
    }

    public void setSubTradeItemId(Long subTradeItemId) {
        this.subTradeItemId = subTradeItemId;
    }

    public String getSubTradeItemUuid() {
        return subTradeItemUuid;
    }

    public void setSubTradeItemUuid(String subTradeItemUuid) {
        this.subTradeItemUuid = subTradeItemUuid;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }


    public void initSplitState() {
        setId(null);
        setSubId(null);
        setServerCreateTime(null);
        setServerUpdateTime(null);
        setChanged(true);
    }
}
