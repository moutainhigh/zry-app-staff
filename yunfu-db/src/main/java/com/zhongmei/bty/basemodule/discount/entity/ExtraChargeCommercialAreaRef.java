package com.zhongmei.bty.basemodule.discount.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;


@DatabaseTable(tableName = "extra_charge_commercial_area_ref")
public class ExtraChargeCommercialAreaRef extends BasicEntityBase  {

    private static final long serialVersionUID = 1L;


    public interface $ extends BasicEntityBase.$ {
        String commercial_area_id = "commercial_area_id";         String extra_charge_id = "extra_charge_id";         String shop_identy = "shop_identy";         String creator_name = "creator_name";         String creator_id = "creator_id";         String updator_name = "updator_name";         String updator_id = "updator_id";     }

    @DatabaseField(columnName = $.commercial_area_id)
    private Long commercialAreaId;     @DatabaseField(columnName = $.extra_charge_id)
    private Long extraChargeId;     @DatabaseField(columnName = $.shop_identy)
    private Long shopIdenty;     @DatabaseField(columnName = $.creator_name)
    private String creatorName;     @DatabaseField(columnName = $.creator_id)
    private Long creatorId;     @DatabaseField(columnName = $.updator_name)
    private String updatorName;     @DatabaseField(columnName = $.updator_id)
    private Long updatorId;
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
