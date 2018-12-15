package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 闪惠的source枚举类型
 */
public enum QuickDiscountType implements ValueEnum<Integer> {

    /**
     * 美团团购
     */
    MEI_TUAN_TUAN_GOU(1),

    /**
     * 美团闪惠
     */
    MEI_TUAN_QUICK_DISCOUNT(2),

    /**
     * 百度糯米团购
     */
    BAI_DU_NUO_MI_TUAN_GOU(3),

    /**
     * 自定义支付
     */
    CUSTOM_PAY(4),

    /**
     * 微信支付
     */
    WEIXIN_PAY(5),

    /**
     * 支付宝支付
     */
    ALI_PAY(6),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private QuickDiscountType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private QuickDiscountType() {
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
