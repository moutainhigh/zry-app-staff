package com.zhongmei.bty.dinner.action;

public class ActionDinnerBatchDiscount {
        public Boolean isEditModle;

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
