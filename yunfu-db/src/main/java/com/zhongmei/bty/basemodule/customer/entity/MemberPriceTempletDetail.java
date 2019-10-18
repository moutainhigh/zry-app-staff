package com.zhongmei.bty.basemodule.customer.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.commonmodule.database.entity.base.UserEntityBase;


@DatabaseTable(tableName = "member_price_templet_detail")
public class MemberPriceTempletDetail extends UserEntityBase {


    private static final long serialVersionUID = 1L;

    public interface $ extends UserEntityBase.$ {
        public static final String brandId = "brand_id";
        public static final String brandDishId = "brand_dish_id";
        public static final String memberPrice = "member_price";
        public static final String priceTempletId = "price_templet_id";
        public static final String discount = "discount";
    }


    @DatabaseField(columnName = "price_templet_id", canBeNull = false)
    private Long priceTempletId;


    @DatabaseField(columnName = "brand_id", canBeNull = false)
    private Long brandId;


    @DatabaseField(columnName = "brand_dish_id", canBeNull = false)
    private Long brandDishId;



    @DatabaseField(columnName = "member_price")
    private Double memberPrice;


    @DatabaseField(columnName = "discount")
    private Double discount;


    public MemberPriceTempletDetail() {
    }

    public Long getPriceTempletId() {
        return priceTempletId;
    }


    public void setPriceTempletId(Long priceTempletId) {
        this.priceTempletId = priceTempletId;
    }


    public Long getBrandId() {
        return brandId;
    }


    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }


    public Long getBrandDishId() {
        return brandDishId;
    }


    public void setBrandDishId(Long brandDishId) {
        this.brandDishId = brandDishId;
    }

    public Double getMemberPrice() {
        return memberPrice;
    }


    public void setMemberPrice(Double memberPrice) {
        this.memberPrice = memberPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

}
