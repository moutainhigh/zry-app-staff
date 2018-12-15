package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 * CREATE TABLE `trade_item_main_batch_rel` (
 * `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '服务端自增ID',
 * `main_item_id` bigint(20) NOT NULL COMMENT '主单关联的菜品ID',
 * `main_item_uuid` varchar(32) NOT NULL COMMENT '主单菜品的UUID',
 * `sub_item_Id` bigint(20) DEFAULT NULL COMMENT '子单菜品ID',
 * `sub_item_uuid` varchar(32) DEFAULT NULL COMMENT '子单菜品UUID',
 * `main_trade_id` bigint(20) NOT NULL COMMENT '主单ID',
 * `sub_trade_id` bigint(20) NOT NULL COMMENT '子单ID',
 * `parent_item_id` bigint(20) DEFAULT NULL COMMENT '商品是组合明细时，指向该明细的组合商品(即指向套餐主餐)',
 * `parent_item_uuid` varchar(32) DEFAULT NULL COMMENT '指向父记录的uuid，如果是子菜才有值，单菜此字段为空',
 * `relate_item_id` bigint(20) DEFAULT NULL COMMENT '当此记录是修改其他品项而来时记录被修改的品项ID',
 * `relate_item_uuid` varchar(32) DEFAULT NULL COMMENT '当此记录是修改其他品项而来时记录被修改的品项UUID',
 * `trade_table_id` bigint(20) DEFAULT NULL COMMENT '桌台ID',
 * `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1:VALID:有效的 2: INVALID: 无效的',
 * `server_create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '服务端创建时间',
 * `server_update_time` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '服务端最后修改时间',
 * `brand_identy` bigint(20) NOT NULL COMMENT '品牌标识',
 * `shop_identy` bigint(20) NOT NULL COMMENT '门店标识',
 * `trade_item_num` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '菜品数量',
 * PRIMARY KEY (`id`),
 * KEY `idx_brand_identy_shop_identy` (`brand_identy`,`shop_identy`) USING BTREE,
 * KEY `idx_shop_identy_server_update_time` (`shop_identy`,`server_update_time`) USING BTREE,
 * KEY `idx_main_trade_id` (`main_trade_id`) USING BTREE
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='联台菜品批次关系表'
 */

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

    /**
     * 子单中主单批量菜拆出时，信息改变
     */
    public void initSplitState() {
        setId(null);
        setSubItemId(null);
        setServerCreateTime(null);
        setServerUpdateTime(null);
        setChanged(true);
    }

}