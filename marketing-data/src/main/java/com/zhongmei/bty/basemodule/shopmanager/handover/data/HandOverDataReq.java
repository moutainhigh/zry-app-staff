package com.zhongmei.bty.basemodule.shopmanager.handover.data;



public class HandOverDataReq {

    private Integer bussinessType;
    private boolean checkClearAccount;
    private boolean checkUnpaid;
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
