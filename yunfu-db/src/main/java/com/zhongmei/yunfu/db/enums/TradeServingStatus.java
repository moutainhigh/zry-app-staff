package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 支付状态
 *
 * @Date：2015年3月31日
 * @Description:
 * @Version: 5.0
 */
public enum TradeServingStatus implements ValueEnum<Integer> {

    /**
     * 未上餐
     */
    UNSERVING(1),

    /**
     * 已上餐
     */
    SERVED(2),

    /**
     * 部分上餐
     */
    PART_SERVEDED(3),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TradeServingStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TradeServingStatus() {
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