package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 优惠类型
 */
public enum PrivilegeType implements ValueEnum<Integer> {

    /**
     * 折扣
     */
    DISCOUNT(1),

    /**
     * 折让
     */
    REBATE(2),

    /**
     * 免单
     */
    FREE(3),

    /**
     * 优惠券
     */
    COUPON(4),

    /**
     * 积分抵现
     */
    INTEGRALCASH(5),

    /**
     * 会员自动折扣
     */
    AUTO_DISCOUNT(6),

    /**
     * 平台优惠
     */
    PLATFORM(7),

    /**
     * 商户优惠
     */
    BUSINESS(8),

    /**
     * 物流优惠
     */
    LOGISTICS(9),

    /**
     * 代理商优惠
     */
    AGENTS(10),

    /**
     * 会员价
     */
    MEMBER_PRICE(11),

    /**
     * 附加费
     */
    ADDITIONAL(12),

    /**
     * 商品营销活动
     */
    MARKTING(13),

    /**
     * 熟客优惠
     */
    VIP(14),

    /**
     * 微信卡券
     */
    WECHAT_CARD_COUPONS(15),

    /**
     * 宴请
     */
    BANQUET(16),

    /**
     * 问题菜品
     */
    PROBLEM(17),

    /**
     * 赠送
     */
    GIVE(18),

    /**
     * 服务费
     */
    SERVICE(21),

    /**
     * 次卡服务
     */
    CARD_SERVICE(22),

    /**
     * 拼团
     */
    COLLAGE(23),

    /**
     * 秒杀
     */
    SECKILL(24),

    /**
     * 砍价
     */
    BARGAIN(25),

    /**
     * 储值折扣
     */
    CHARGE(26),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PrivilegeType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PrivilegeType() {
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

    public PrivilegeType createPrivilegeType(int value) {
        for (PrivilegeType privilegeType : PrivilegeType.values()) {
            if (privilegeType.value() == value) {
                return privilegeType;
            }
        }
        return __UNKNOWN__;
    }

}
