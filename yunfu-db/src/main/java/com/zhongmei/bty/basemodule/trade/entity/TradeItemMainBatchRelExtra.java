package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.bty.commonmodule.database.enums.BatchDishRelType;

import java.math.BigDecimal;

/**
 * CREATE TABLE `trade_item_main_batch_rel_extra` (
 * `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '服务端自增ID',
 * `main_id` bigint(20) NOT NULL COMMENT '主单数据id 根据rel_type区分',
 * `main_uuid` varchar(32) NOT NULL COMMENT '主单数据uuid 根据rel_type区分',
 * `sub_id` bigint(20) NOT NULL COMMENT '子单数据id 根据rel_type区分',
 * `sub_uuid` varchar(32) NOT NULL COMMENT '子单数据uuid 根据rel_type区分',
 * `main_trade_id` bigint(20) NOT NULL COMMENT '主订单ID',
 * `sub_trade_id` bigint(20) NOT NULL COMMENT '子订单ID',
 * `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1:VALID:有效的 2: INVALID: 无效的',
 * `server_create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '服务端创建时间',
 * `server_update_time` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '服务端最后修改时间',
 * `rel_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1: trade_item_extra关联关系  2:trade_item_property关联关系  3:trade_item_operation关联关系',
 * `brand_identy` bigint(20) NOT NULL COMMENT '品牌标识',
 * `shop_identy` bigint(20) NOT NULL COMMENT '门店标识',
 * `quantity` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '菜品数量，当记录为trade_item和trade_item_property 口味做法关系时，才有此值',
 * PRIMARY KEY (`id`),
 * KEY `idx_brand_identy_shop_identy` (`brand_identy`,`shop_identy`) USING BTREE,
 * KEY `idx_shop_identy_server_update_time` (`shop_identy`,`server_update_time`) USING BTREE,
 * KEY `idx_main_id_rel_type` (`main_id`,`rel_type`),
 * KEY `idx_main_trade_id` (`main_trade_id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='联台菜品条目数据批次关系表'
 * Created by demo on 2018/12/15
 */
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

    /**
     * 子单中主单批量菜拆出时，信息改变
     */
    public void initSplitState() {
        setId(null);
        setSubId(null);
        setServerCreateTime(null);
        setServerUpdateTime(null);
        setChanged(true);
    }
}
