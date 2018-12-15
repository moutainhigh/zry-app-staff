package com.zhongmei.bty.basemodule.devices.mispos.enums;

import com.zhongmei.yunfu.data.R;
import com.zhongmei.yunfu.util.ValueEnum;

/**
 * accountStatus，1已到账、0未到账。
 */
public enum AccountStatus implements ValueEnum<Integer> {

    /**
     * 未到账
     */
    NotAccount(0, R.string.card_order_status_no_account),

    /**
     * 发行中
     */
    HaveAccount(1, R.string.card_order_status_have_account),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;
    //类型描述
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
