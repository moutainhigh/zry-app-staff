package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @Date：2015年3月31日
 * @Description:
 * @Version: 5.0
 */
public enum CustomerType implements ValueEnum<Integer> {

    /**
     * 预订的顾客
     */
    BOOKING(1),

    /**
     * 支付人
     */
    PAY(2),

    /**
     * 登录的会员
     */
    MEMBER(3),

    /**等删除*/

    /**
     * 顾客
     */
    CUSTOMER(-1),

    /**
     * 登录的实体卡
     */
    CARD(13),

    /**
     * 订单归属人
     */
    OWNER(4),

    /**
     * 预约人
     */
    BOOKING_PEOPLE(-6),

    /**
     * v8.11.0
     * 支付人
     */
    PAY_PEOPLE(-4),

    /**
     * 未知的值，为了避免转为enum出错设置的，不应该直接使用
     *
     * @deprecated
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private CustomerType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CustomerType() {
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
