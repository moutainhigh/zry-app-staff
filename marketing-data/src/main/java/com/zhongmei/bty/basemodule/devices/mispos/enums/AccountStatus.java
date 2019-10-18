package com.zhongmei.bty.basemodule.devices.mispos.enums;

import com.zhongmei.yunfu.data.R;
import com.zhongmei.yunfu.util.ValueEnum;


public enum AccountStatus implements ValueEnum<Integer> {


    NotAccount(0, R.string.card_order_status_no_account),


    HaveAccount(1, R.string.card_order_status_have_account),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;
        private int typeDescResId;

    private AccountStatus(Integer value, int typeDescResId) {
        helper = Helper.valueHelper(value);
        this.typeDescResId = typeDescResId;
    }

    private AccountStatus() {
        helper = Helper.unknownHelper();
    }

    public int getTypeDescResId() {
        return typeDescResId;
    }

    @Override
    public Integer value() {
        return helper.value();
    }

    @Override
    public boolean equalsValue(Integer value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public boolean isUnknownEnum() {
        return helper.isUnknownEnum();
    }

    @Override
    public void setUnknownValue(Integer value) {
        helper.setUnknownValue(value);
    }

    @Override
    public String toString() {
        return "" + value();
    }

}
