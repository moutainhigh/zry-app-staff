package com.zhongmei.bty.basemodule.pay.enums;

import com.zhongmei.yunfu.util.ValueEnum;

import java.io.Serializable;

/**
 * 支付场景值枚举
 * Created by demo on 2018/12/15
 */


public enum PayScene implements ValueEnum<Integer>, Serializable {

    /**
     * 充值
     */
    SCENE_CODE_CHARGE(1),

    /**
     * 购物消费
     */
    SCENE_CODE_SHOP(2),
    /**
     * 会员销账
     */
    SCENE_CODE_WRITEOFF(3),

    /**
     * 烘焙预定
     */
    SCENE_CODE_BAKERY_BOOKING(4),

    /**
     * 正餐自助交押金
     */
    SCENE_CODE_BUFFET_DEPOSIT(5),
    /**
     * 预定交定金
     */
    SCENE_CODE_BOOKING_DEPOSIT(6),
    /**
     * 烘焙押金
     */
    SCENE_CODE_BAKERY_BOOKING_DEPOSIT(7),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PayScene(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PayScene() {
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