package com.zhongmei.bty.basemodule.database.entity.base;

import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.bty.commonmodule.database.entity.base.UserEntityBase;


public abstract class ServerBrandIdEntityBase extends UserEntityBase {

    public interface $ extends UserEntityBase.$ {

        public static final String brandId = "brand_id";
    }


    @DatabaseField(columnName = "brand_id")
    private Long brandId;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }


}
