package com.zhongmei.bty.basemodule.database.entity.base;

import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.bty.commonmodule.database.entity.base.UserEntityBase;

/**
 * 针对brandId 使用 brand_id字段的数据库
 *
 * @date:2016年4月21日下午3:21:56
 */
public abstract class ServerBrandIdEntityBase extends UserEntityBase {

    public interface $ extends UserEntityBase.$ {
        /**
         * brand_id
         */
        public static final String brandId = "brand_id";
    }

    /**
     * 品牌id.
     */
    @DatabaseField(columnName = "brand_id")
    private Long brandId;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }


}
