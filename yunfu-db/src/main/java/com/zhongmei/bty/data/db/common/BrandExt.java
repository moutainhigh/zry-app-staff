package com.zhongmei.bty.data.db.common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;

/**
 * 品牌设置
 */
@DatabaseTable(tableName = "brand_ext")
public class BrandExt extends EntityBase<Long> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public interface $ {

        String id = "id";

        String weixinConfigUrl = "weixin_configu_url";

        String isNewInterface = "is_new_interface";

        String status = "status";

        String createDateTime = "crate_date_time";

        String modifyDateTime = "modify_date_time";

        String isLeftAmountPwd = "is_left_amount_pwd";

        String isExchangeIntegralPwd = "is_exchange_integral_pwd";

        String isDiscountPwd = "is_discount_pwd";

    }

    /**
     * 品牌id
     */
    @DatabaseField(columnName = "id", id = true, canBeNull = false)
    private Long id;

    /**
     * 微信公众号需要配置的URL
     */
    @DatabaseField(columnName = "weixin_configu_url")
    private String weixinConfigUrl;

    /**
     * 新老用户 0新用户 1 新用户
     */
    @DatabaseField(columnName = "is_new_interface")
    private Integer isNewInterface;

    /**
     * 状态 -1错误 0正常
     */
    @DatabaseField(columnName = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @DatabaseField(columnName = "crate_date_time")
    private Long createDateTime;

    /**
     * 修改时间
     */
    @DatabaseField(columnName = "modify_date_time")
    private Long modifyDateTime;

    /**
     * 账户余额是否需要密码(0：是，1：否)',
     */
    @DatabaseField(columnName = "is_left_amount_pwd")
    private Integer isLeftAmountPwd;

    /**
     * 积分抵现是否需要密码(0：是，1：否)',
     */
    @DatabaseField(columnName = "is_exchange_integral_pwd")
    private Integer isExchangeIntegralPwd;

    /**
     * 折扣优惠是否需要密码(0：是，1：否)',
     */
    @DatabaseField(columnName = "is_discount_pwd")
    private Integer isDiscountPwd;

    @Override
    public boolean isValid() {
        return status == 0;
    }

    @Override
    public Long pkValue() {
        return id;
    }

    @Override
    public Long verValue() {
        return modifyDateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(id);
    }
}
