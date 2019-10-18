package com.zhongmei.bty.basemodule.orderdish.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;

import java.math.BigDecimal;




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
