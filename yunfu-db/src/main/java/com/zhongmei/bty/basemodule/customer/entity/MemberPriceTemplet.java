package com.zhongmei.bty.basemodule.customer.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.commonmodule.database.entity.base.UserEntityBase;

/**
 * Model class of 会员价格策略.
 *
 * @version $Id$
 */
@DatabaseTable(tableName = "member_price_templet")
public class MemberPriceTemplet extends UserEntityBase {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    public interface $ extends UserEntityBase.$ {
        public static final String name = "name";
        public static final String brandId = "brand_id";
        public static final String priceType = "price_type";
        public static final String periodStart = "period_start";
        public static final String periodEnd = "period_end";
    }

    /**
     * 会员价格策略名称.
     */
    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    /**
     * 品牌id.
     */
    @DatabaseField(columnName = "brand_id")
    private Long brandId;

    /**
     * 会员价方式.
     * 1、会员折扣 2、会员价
     */
    @DatabaseField(columnName = "price_type")
    private Integer priceType;

    /**
     * 特价时段
     */
    @DatabaseField(columnName = "period_start")
    private String periodStart;

    @DatabaseField(columnName = "period_end")
    private String periodEnd;

    /**
     * Set the 会员价格策略名称.
     *
     * @param name 会员价格策略名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the 会员价格策略名称.
     *
     * @return 会员价格策略名称
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the 品牌id.
     *
     * @param brandId 品牌id
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    /**
     * Get the 品牌id.
     *
     * @return 品牌id
     */
    public Long getBrandId() {
        return this.brandId;
    }

    /**
     * Set the 会员价方式.
     *
     * @param priceType 会员价方式
     */
    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    /**
     * Get the 会员价方式.
     *
     * @return 会员价方式
     */
    public Integer getPriceType() {
        return this.priceType;
    }

    public String getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(String periodStart) {
        this.periodStart = periodStart;
    }

    public String getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(String periodEnd) {
        this.periodEnd = periodEnd;
    }
}
