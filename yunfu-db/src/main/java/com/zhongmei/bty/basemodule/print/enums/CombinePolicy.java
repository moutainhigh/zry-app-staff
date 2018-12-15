package com.zhongmei.bty.basemodule.print.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @version: 1.0
 * @date 2015年6月24日
 */
public enum CombinePolicy implements ValueEnum<Integer> {

    /**
     * 同品项数量合并
     */
    COMBINE(1),

    /**
     * 同品项数量拆分。比如某商品数量为3，将拆分为3张单品项打印单
     */
    SPLIT(2),

    /**
     * 未知的值，为了避免转为enum出错设置的，不应该直接使用
     *
     * @deprecated
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private CombinePolicy(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CombinePolicy() {
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
