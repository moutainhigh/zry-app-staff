package com.zhongmei.bty.basemodule.discount.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @date:2016年4月27日下午3:12:52
 */
public enum PromotionType implements ValueEnum<Integer> {
    //	(1-立减，2-折扣，3-赠送，4-特价)
    MINUS(1), //立减
    DISCOUNT(2), //折扣
    GIFT(3),//赠送
    SPECAILPRICE(4), //特价

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated __UNKNOWN__;

    private final Helper<Integer> helper;

    private PromotionType(Integer value) {
        // TODO Auto-generated constructor stub
        helper = Helper.valueHelper(value);
    }

    private PromotionType() {
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
