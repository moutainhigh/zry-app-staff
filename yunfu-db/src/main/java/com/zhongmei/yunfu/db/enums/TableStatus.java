package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 桌台状态
 */
public enum TableStatus implements ValueEnum<Integer> {

    /**
     * 空闲
     */
    EMPTY(1),

    /**
     * 就餐
     */
    OCCUPIED(2),

    /**
     * 待清台
     */
    DONE(3),

    /**
     * 锁定
     */
    LOCKING(4),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TableStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TableStatus() {
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
