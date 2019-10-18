package com.zhongmei.bty.basemodule.customer.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.commonmodule.database.entity.base.UserEntityBase;


@DatabaseTable(tableName = "member_price_templet")
public class MemberPriceTemplet extends UserEntityBase {


    private static final long serialVersionUID = 1L;

    public interface $ extends UserEntityBase.$ {
        public static final String name = "name";
        public static final String brandId = "brand_id";
        public static final String priceType = "price_type";
        public static final String periodStart = "period_start";
        public static final String periodEnd = "period_end";
    }


    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;


    @DatabaseField(columnName = "brand_id")
    private Long brandId;


    @DatabaseField(columnName = "price_type")
    private Integer priceType;


    @DatabaseField(columnName = "period_start")
    private String periodStart;

    @DatabaseField(columnName = "period_end")
    private String periodEnd;


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return this.name;
    }


    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }


    public Long getBrandId() {
        return this.brandId;
    }


    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }


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
