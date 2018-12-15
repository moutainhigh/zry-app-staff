package com.zhongmei.bty.basemodule.orderdish.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;

import java.math.BigDecimal;

/*CREATE TABLE `dish_carte` (
        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
        `brand_identy` bigint(20) NOT NULL COMMENT '品牌id',
        `shop_identy` bigint(20) NOT NULL COMMENT '店铺id',
        `uuid` varchar(100) NOT NULL COMMENT '菜单uuid',
        `carte_code` varchar(100) NOT NULL COMMENT '菜单简码',
        `name` varchar(100) NOT NULL COMMENT '菜单名称',
        `price` decimal(10,2) NOT NULL COMMENT '菜单总价（用于团餐）',
        `carte_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '菜单类型 : 1 自助餐 2 团餐',
        `is_public` tinyint(4) NOT NULL DEFAULT '1' COMMENT '菜单类型 : 0 非通用模板 1 通用模板 2 自定义模板',
        `soure` tinyint(4) NOT NULL DEFAULT '1' COMMENT '来源 : 1 后台 2 POS',
        `is_disable` tinyint(4) DEFAULT '0' COMMENT '停用标识（0|1），0启用；1停用',
        `statusFlag` tinyint(4) DEFAULT '0' COMMENT '删除标志,1正常 2无效',
        `create_id` int(11) DEFAULT NULL COMMENT '创建人',
        `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_id` int(11) DEFAULT NULL COMMENT '更新人',
        `server_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后编辑时间',
        PRIMARY KEY (`id`),
        UNIQUE KEY `unique_dish_carte` (`brand_identy`,`shop_identy`,`code`)
        ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='菜单信息';*/

/**
 * 菜单模板表
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "dish_carte")
public class DishCarte extends BasicEntityBase implements Cloneable {

    public interface $ extends BasicEntityBase.$ {
        String shopIdenty = "shop_identy";
        String uuid = "uuid";
        String carteCode = "carte_code";
        String name = "name";
        String price = "price";
        String carteType = "carte_type";
        String isPublic = "is_public";
        String source = "source";
        String isDisable = "is_disable";
        String createId = "create_id";
        String updateId = "update_id";

    }

    @DatabaseField(columnName = "shop_identy")
    Long shopIdenty;

    @DatabaseField(columnName = "uuid")
    String uuid;
    @DatabaseField(columnName = "create_id")
    Long createId;
    @DatabaseField(columnName = "update_id")
    Long updateId;

    @DatabaseField(columnName = "carte_code")
    String carteCode;

    @DatabaseField(columnName = "name")
    String name;


    @DatabaseField(columnName = "price")
    BigDecimal price;


    @DatabaseField(columnName = "carte_type")
    Integer carteType;

    @DatabaseField(columnName = "is_public")
    Integer isPublic;

    @DatabaseField(columnName = "source")
    Integer source;

    @DatabaseField(columnName = "is_disable")
    Integer isDisable;

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public String getCarteCode() {
        return carteCode;
    }

    public void setCarteCode(String carteCode) {
        this.carteCode = carteCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCarteType() {
        return carteType;
    }

    public void setCarteType(Integer carteType) {
        this.carteType = carteType;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(Integer isDisable) {
        this.isDisable = isDisable;
    }

    public DishCarte clone() {
        DishCarte dishCarte = null;
        try {
            dishCarte = (DishCarte) super.clone();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dishCarte;
    }

}
