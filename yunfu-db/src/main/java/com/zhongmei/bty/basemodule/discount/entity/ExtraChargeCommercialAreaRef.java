package com.zhongmei.bty.basemodule.discount.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;

/**
 * CREATE TABLE `extra_charge_commercial_area_ref` (
 * `id` bigint(20) NOT NULL COMMENT '主键',
 * `commercial_area_id` bigint(20) NOT NULL COMMENT '桌台区域id',
 * `extra_charge_id` bigint(20) NOT NULL COMMENT '附加费id',
 * `brand_identy` bigint(20) NOT NULL COMMENT '品牌id',
 * `shop_identy` bigint(20) NOT NULL COMMENT '门店id',
 * `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '逻辑删除标志;1:否;2:是',
 * `creator_name` varchar(32) DEFAULT NULL COMMENT '创建人名称',
 * `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
 * `updator_name` varchar(32) DEFAULT NULL COMMENT '更新人名称',
 * `updator_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',
 * `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '服务器创建时间',
 * `server_upate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '服务器更新时间',
 * PRIMARY KEY (`id`),
 * KEY `idx_brand_shop_updatetime` (`brand_identy`,`shop_identy`,`server_upate_time`) COMMENT '品牌门店时间的联合索引'
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='附加费与桌台区域关联'
 */
@DatabaseTable(tableName = "extra_charge_commercial_area_ref")
public class ExtraChargeCommercialAreaRef extends BasicEntityBase /*implements ICreator, IUpdator*/ {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "extra_charge"
     */
    public interface $ extends BasicEntityBase.$ {
        String commercial_area_id = "commercial_area_id"; //'桌台区域id',
        String extra_charge_id = "extra_charge_id"; //'附加费id',
        String shop_identy = "shop_identy"; //'门店id',
        String creator_name = "creator_name"; //'创建人名称',
        String creator_id = "creator_id"; //'创建人ID',
        String updator_name = "updator_name"; //'更新人名称',
        String updator_id = "updator_id"; //'更新人ID',
    }

    @DatabaseField(columnName = $.commercial_area_id)
    private Long commercialAreaId; //'桌台区域id',
    @DatabaseField(columnName = $.extra_charge_id)
    private Long extraChargeId; //'附加费id',
    @DatabaseField(columnName = $.shop_identy)
    private Long shopIdenty; //'门店id',
    @DatabaseField(columnName = $.creator_name)
    private String creatorName; //'创建人名称',
    @DatabaseField(columnName = $.creator_id)
    private Long creatorId; //'创建人ID',
    @DatabaseField(columnName = $.updator_name)
    private String updatorName; //'更新人名称',
    @DatabaseField(columnName = $.updator_id)
    private Long updatorId; //'更新人ID',

    public Long getCommercialAreaId() {
        return commercialAreaId;
    }

    public void setCommercialAreaId(Long commercialAreaId) {
        this.commercialAreaId = commercialAreaId;
    }

    public Long getExtraChargeId() {
        return extraChargeId;
    }

    public void setExtraChargeId(Long extraChargeId) {
        this.extraChargeId = extraChargeId;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }
}
