package com.zhongmei.bty.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.yunfu.db.BasicEntityBase;

public class ShopEntityBase extends BasicEntityBase {

    public interface $ extends BasicEntityBase.$ {
        String shopIdenty = "shop_identy";
    }

    @DatabaseField(columnName = $.shopIdenty, canBeNull = false)
    private Long shopIdenty;
    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }
}
