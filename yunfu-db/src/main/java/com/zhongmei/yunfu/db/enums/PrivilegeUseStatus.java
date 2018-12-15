package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 优惠使用状态
 * Created by demo on 2018/12/15
 */

public enum PrivilegeUseStatus implements ValueEnum<Integer> {
    //未使用
    UNUSE(0),
    //    已使用
    USED(1),
    //    使用失败
    USEFAIL(2),

    @Deprecated __UNKNOWN__;

    private final Helper<Integer> helper;

    private PrivilegeUseStatus(Integer value) {
        // TODO Auto-generated constructor stub
        helper = Helper.valueHelper(value);
    }

    private PrivilegeUseStatus() {
        // TODO Auto-generated constructor stub
        helper = Helper.unknownHelper();
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
