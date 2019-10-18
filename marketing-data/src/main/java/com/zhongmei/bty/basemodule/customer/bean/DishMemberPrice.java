package com.zhongmei.bty.basemodule.customer.bean;

import com.zhongmei.yunfu.db.enums.MemberPrivilegeType;
import com.zhongmei.yunfu.util.ValueEnums;

public class DishMemberPrice {
    private Double discount;
    private Double memberPrice;
    private int priceType;
    private String priceName;
    private String periodStart;
    private String periodEnd;

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(Double memberPrice) {
        this.memberPrice = memberPrice;
    }

    public MemberPrivilegeType getPriceType() {
        return ValueEnums.toEnum(MemberPrivilegeType.class,priceType);
    }

    public void setPriceType(MemberPrivilegeType priceType) {
        this.priceType = ValueEnums.toValue(priceType);
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
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
