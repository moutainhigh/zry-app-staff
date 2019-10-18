package com.zhongmei.bty.basemodule.orderdish.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;

import java.math.BigDecimal;


@DatabaseTable(tableName = "dish_carte_norms")
public class DishCarteNorms extends BasicEntityBase implements Cloneable {

    public interface $ extends BasicEntityBase.$ {
        String carteId = "carte_id";
        String name = "name";
        String price = "price";
        String createId = "create_id";
        String updateId = "update_id";
    }

    @DatabaseField(columnName = "shop_identy")
    Long shopIdenty;

    @DatabaseField(columnName = "carte_id")
    Long carteId;

    @DatabaseField(columnName = "name")
    String name;

    @DatabaseField(columnName = "price")
    BigDecimal price;

    @DatabaseField(columnName = "create_id")
    Long createId;
    @DatabaseField(columnName = "update_id")
    Long updateId;

    public Long getCarteId() {
        return carteId;
    }

    public void setCarteId(Long carteId) {
        this.carteId = carteId;
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

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    @Override
    public Long verValue() {
        return id;
    }

    public DishCarteNorms clone() {
        DishCarteNorms dishCarteNorms = null;
        try {
            dishCarteNorms = (DishCarteNorms) super.clone();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dishCarteNorms;
    }
}
