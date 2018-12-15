package com.zhongmei.bty.basemodule.discount.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 附加费计算方式
 *
 * @version: 1.0
 * @date 2016年3月9日
 */
public enum ExtraChargeCalcWay implements ValueEnum<Integer> {

    /**
     * 按例比
     */
    RATE(1),

    /**
     * 按人数
     */
    NUMBER_OF_PEOPLE(2),

    /**
     * 固定金额
     */
    FIXED_AMOUNT(3),

    /**
     * 最低消费
     */
    MINIMUM_CHARGE(4),

    /**
     * 根据每单位每人来计算费用
     */
    PER_UNIT_OF_PEOPLE(5),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private ExtraChargeCalcWay(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ExtraChargeCalcWay() {
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
