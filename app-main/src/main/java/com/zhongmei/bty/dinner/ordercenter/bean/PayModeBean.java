package com.zhongmei.bty.dinner.ordercenter.bean;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * @Date：2015年10月20日
 * @Description: 支付方式bean
 * @Version: 1.0
 */
public class PayModeBean {
    private long payModeId = PayModelGroup.CASH.value();
    private Integer payModeGroup = PayModelGroup.CASH.value();
    private String payModeName = MainApplication.getInstance().getString(R.string.returnguest_privilege_title);
    private Status status = Status.ENABLE;//默认可点击
    private byte value;//排序值

    public long getPayModeId() {
        return payModeId;

    }

    public void setPayModeId(long payModeId) {
        this.payModeId = payModeId;
    }

    public PayModelGroup getPayModeGroup() {
        return ValueEnums.toEnum(PayModelGroup.class, payModeGroup);
    }

    public void setPayModeGroup(PayModelGroup payModeGroup) {
        this.payModeGroup = ValueEnums.toValue(payModeGroup);
    }

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }

    public enum Status {
        ENABLE, DISABLE
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }


}

