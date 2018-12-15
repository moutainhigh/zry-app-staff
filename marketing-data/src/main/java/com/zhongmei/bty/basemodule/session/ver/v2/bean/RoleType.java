package com.zhongmei.bty.basemodule.session.ver.v2.bean;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * Created by demo on 2018/12/15
 */

public enum RoleType implements ValueEnum<Integer> {

    /**
     * 管理员角色
     */
    Manager(1),

    /**
     * 业务员角色
     */
    Salesman(2),

    /**
     * 模板角色
     */
    Template(3);

    private final Helper<Integer> helper;

    RoleType(Integer value) {
        this.helper = Helper.valueHelper(value);
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
