package com.zhongmei.bty.dinner.action;

public class ActionDinnerBatchDiscount {
    //是否item可选
    public Boolean isEditModle;

    //整单打折
    public Boolean isAllDiscount;


    public ActionDinnerBatchDiscount(Boolean isEditModle, Boolean isAllDiscount) {
        this.isEditModle = isEditModle;
        this.isAllDiscount = isAllDiscount;
    }

    public Boolean getIsEditModle() {
        return isEditModle;
    }

    public void setIsEditModle(Boolean isEditModle) {
        this.isEditModle = isEditModle;
    }

    public Boolean getIsAllDiscount() {
        return isAllDiscount;
    }

    public void setIsAllDiscount(Boolean isAllDiscount) {
        this.isAllDiscount = isAllDiscount;
    }

}
