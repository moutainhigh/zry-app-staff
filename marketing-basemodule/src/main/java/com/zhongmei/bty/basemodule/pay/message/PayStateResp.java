package com.zhongmei.bty.basemodule.pay.message;

public class PayStateResp {
    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer payState;
    public String msg;
}
