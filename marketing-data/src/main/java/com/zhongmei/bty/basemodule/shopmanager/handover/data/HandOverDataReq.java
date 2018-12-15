package com.zhongmei.bty.basemodule.shopmanager.handover.data;

/**
 * Created by demo on 2018/12/15
 */

public class HandOverDataReq {

    private Integer bussinessType;//业务类型

    private boolean checkClearAccount;//是否检查为清账订单

    private boolean checkUnpaid;//是否检查为结账订单

    public Integer getBussinessType() {
        return bussinessType;
    }

    public void setBussinessType(Integer bussinessType) {
        this.bussinessType = bussinessType;
    }

    public boolean isCheckClearAccount() {
        return checkClearAccount;
    }

    public void setCheckClearAccount(boolean checkClearAccount) {
        this.checkClearAccount = checkClearAccount;
    }

    public boolean isCheckUnpaid() {
        return checkUnpaid;
    }

    public void setCheckUnpaid(boolean checkUnpaid) {
        this.checkUnpaid = checkUnpaid;
    }
}
