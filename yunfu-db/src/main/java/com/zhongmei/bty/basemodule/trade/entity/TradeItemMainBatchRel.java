package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;

import java.math.BigDecimal;



@DatabaseTable(tableName = "trade_item_main_batch_rel")
public class TradeItemMainBatchRel extends BasicEntityBase {
    private static final long serialVersionUID = 1L;

    public interface $ extends BasicEntityBase.$ {

        public static final String mainItemId = "main_item_id";
        public static final String mainItemUuid = "main_item_uuid";
        public static final String subItemId = "sub_item_Id";
        public static final String subItemUuid = "sub_item_uuid";
        public static final String mainTradeId = "main_trade_id";
        public static final String subTradeId = "sub_trade_id";
        public static final String tradeTableId = "trade_table_id";
        public static final String tradeTableUuid = "trade_table_uuid";
        public static final String tradeItemNum = "trade_item_num";
        public static final String parentItemId = "parent_item_id";
        public static final String parentItemUuid = "parent_item_uuid";
        public static final String relateItemId = "relate_item_id";
        public static final String relateItemUuid = "relate_item_uuid";
        public static final String shopIdenty = "shop_identy";

    }


    @DatabaseField(columnName = "main_item_id")
    private Long mainItemId;

    @DatabaseField(columnName = "main_item_uuid")
    private String mainItemUuid;

    @DatabaseField(columnName = "sub_item_id")
    private Long subItemId;

    @DatabaseField(columnName = "sub_item_uuid")
    private String subItemUuid;

    @DatabaseField(columnName = "main_trade_id")
    private Long mainTradeId;

    @DatabaseField(columnName = "sub_trade_id")
    private Long subTradeId;

    @DatabaseField(columnName = "trade_table_id")
    private Long tradeTableId;

    @DatabaseField(columnName = "trade_table_uuid")
    private String tradeTableUuid;

    @DatabaseField(columnName = "trade_item_num")
    private BigDecimal tradeItemNum;

    @DatabaseField(columnName = "parent_item_id")
    private Long parentItemId;
    @DatabaseField(columnName = "parent_item_uuid")
    private String parentItemUuid;
    @DatabaseField(columnName = "relate_item_id")
    private Long relateItemId;
    @DatabaseField(columnName = "relate_item_uuid")
    private String relateItemUuid;

    public Long getMainItemId() {
        return mainItemId;
    }

    public void setMainItemId(Long mainItemId) {
        this.mainItemId = mainItemId;
    }

    public String getMainItemUuid() {
        return mainItemUuid;
    }

    public void setMainItemUuid(String mainItemUuid) {
        this.mainItemUuid = mainItemUuid;
    }

    public Long getSubItemId() {
        return subItemId;
    }

    public void setSubItemId(Long subItemId) {
        this.subItemId = subItemId;
    }

    public String getSubItemUuid() {
        return subItemUuid;
    }

    public void setSubItemUuid(String subItemUuid) {
        this.subItemUuid = subItemUuid;
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

    public Long getTradeTableId() {
        return tradeTableId;
    }

    public void setTradeTableId(Long tradeTableId) {
        this.tradeTableId = tradeTableId;
    }

    public String getTradeTableUuid() {
        return tradeTableUuid;
    }

    public void setTradeTableUuid(String tradeTableUuid) {
        this.tradeTableUuid = tradeTableUuid;
    }

    public BigDecimal getTradeItemNum() {
        return tradeItemNum;
    }

    public void setTradeItemNum(BigDecimal tradeItemNum) {
        this.tradeItemNum = tradeItemNum;
    }

    public Long getParentItemId() {
        return parentItemId;
    }

    public void setParentItemId(Long parentItemId) {
        this.parentItemId = parentItemId;
    }

    public Long getRelateItemId() {
        return relateItemId;
    }

    public void setRelateItemId(Long relateItemId) {
        this.relateItemId = relateItemId;
    }

    public String getParentItemUuid() {
        return parentItemUuid;
    }

    public void setParentItemUuid(String parentItemUuid) {
        this.parentItemUuid = parentItemUuid;
    }

    public String getRelateItemUuid() {
        return relateItemUuid;
    }

    public void setRelateItemUuid(String relateItemUuid) {
        this.relateItemUuid = relateItemUuid;
    }


    public void initSplitState() {
        setId(null);
        setSubItemId(null);
        setServerCreateTime(null);
        setServerUpdateTime(null);
        setChanged(true);
    }

}