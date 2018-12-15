package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 外送平台
 */
public enum DeliveryPlatform implements ValueEnum<Integer> {
    /**
     * 商家自送
     */
    MERCHANT(1),

    /**
     * 百度外卖
     */
    BAIDU_TAKEOUT(2),

    /**
     * 蜂鸟（饿了么）
     */
    ELEME(3),

    /**
     * 美团配送
     */
    MEITUAN_TAKEOUT(4),

    /**
     * 达达配送
     */
    DA_DA(5),

    /**
     * 顺丰同城配送
     */
    SHUN_FENG(6),

    /**
     * 美团众包
     */
    MEITUAN_ZHONGBAO(7),

    /**
     * 饿了么众包
     */
    ELEME_ZHONGBAO(8),

    /**
     * 京东到家
     */
    JD_HOME(11),

    /**
     * 开放平台
     */
    OPEN_PLATFORM(20),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private DeliveryPlatform(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private DeliveryPlatform() {
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
