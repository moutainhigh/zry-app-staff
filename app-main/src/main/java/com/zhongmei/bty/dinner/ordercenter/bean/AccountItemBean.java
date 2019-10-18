package com.zhongmei.bty.dinner.ordercenter.bean;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayModelGroup;


public class AccountItemBean {
    private byte status = 0;    private String money = "";    private long payModeId = PayModeId.CASH.value();
    private PayModelGroup payModeGroup = PayModelGroup.CASH;
    private String payModeName = MainApplication.getInstance().getString(R.string.returnguest_privilege_title);

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getMoney() {
        if (money.equals("")) {
            return "";
        }
        if (status == 0) {
            return money;
        } else {
            return "-" + money;
        }

    }

    public void setMoney(String money) {
        this.money = money;
    }

    public long getPayModeId() {
        return payModeId;
    }

    public void setPayModeId(long payModeId) {
        this.payModeId = payModeId;
    }

    public PayModelGroup getPayModeGroup() {
        return payModeGroup;
    }

    public void setPayModeGroup(PayModelGroup payModeGroup) {
        this.payModeGroup = payModeGroup;
    }

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }

}

