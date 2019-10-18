package com.zhongmei.bty.basemodule.orderdish.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;

import java.math.BigDecimal;


@DatabaseTable(tableName = "dish_carte_detail")
public class DishCarteDetail extends BasicEntityBase {

    public interface $ extends BasicEntityBase.$ {
        String shopIdenty = "shop_identy";
        String carteId = "carte_id";
        String dishId = "dish_id";
        String memo = "memo";
        String num = "num";
        String mealId = "meal_id";
        String createId = "create_id";
        String updateId = "update_id";
        String dishUUid = "dish_uuid";
        String setmealGroupId = "setmeal_group_id";
    }

    @DatabaseField(columnName = "shop_identy")
    Long shopIdenty;

    @DatabaseField(columnName = "carte_id")
    Long carteId;

    @DatabaseField(columnName = "dish_id")
    Long dishId;

    @DatabaseField(columnName = "meal_id")
    Long mealId;

    @DatabaseField(columnName = "setmeal_group_id")
    Long setmealGroupId;

    @DatabaseField(columnName = "num")
    BigDecimal num;

    @DatabaseField(columnName = "create_id")
    Long createId;
    @DatabaseField(columnName = "update_id")
    Long updateId;

    @DatabaseField(columnName = "memo")
    String memo;

    @DatabaseField(columnName = "dish_uuid")
    String dishUuid;

    public Long getCarteId() {
        return carteId;
    }

    public void setCarteId(Long carteId) {
        this.carteId = carteId;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public String getDishUuid() {
        return dishUuid;
    }

    public void setDishUuid(String dishUuid) {
        this.dishUuid = dishUuid;
    }

    public Long getSetmealGroupId() {
        return setmealGroupId;
    }

    public void setSetmealGroupId(Long setmealGroupId) {
        this.setmealGroupId = setmealGroupId;
    }

    @Override
    public Long verValue() {
        return id;
    }


}
