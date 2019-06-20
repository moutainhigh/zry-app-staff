package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 支付方式ID。暂用枚举，以后要使用关联表
 *
 * @version: 1.0
 * @date 2015年5月18日
 */
public enum PayModeId implements ValueEnum<Long> {

    /**
     * 会员余额
     */
    MEMBER_CARD(1L),

    /**
     * 现金
     */
    CASH(2L),

    /**
     * 银行卡
     */
    BANK_CARD(3L),

    /**
     * 微信支付
     */
    WEIXIN_PAY(4L),

    /**
     * 支付宝
     */
    ALIPAY(5L),

    /**
     * 积分抵现
     */
    INTERGRAL(6L),

    /**
     * 优惠券
     */
    COUPON(7L),

    /**
     * 101自定义微信
     */
    OTHER_WX_PAY(101L),

    /**
     * 102自定义支付宝
     */
    OTHER_ALI_PAY(102L),


    /**以下是无用类型,后期删除*/

    /**
     * 百度钱包
     */
    BAIFUBAO(-7L),

    /**
     * 百度直达号
     */
    BAIDU_ZHIDA(-8L),


    /**
     * 百度地图
     */
    BAIDU_MAP(-10L),

    /**
     * 银联pos刷卡（区别于银行卡）
     */
    POS_CARD(-11L),

    /**
     * 百糯到店付
     */
    BAIDU_RICE(-12L),

    /**
     * 百度外卖
     */
    BAIDU_TAKEOUT(-13L),

    /**
     * 饿了么
     */
    ELM(-14L),

    /**
     * 会员实体卡
     */
    ENTITY_CARD(-15L),
    /**
     * 大众点评
     */
    DAZHONG_DIANPING(-16L),
    /**
     * 美团外卖
     */
    MEITUAN_TAKEOUT(-17L),
    /**
     * 点评团购劵
     */
    DIANPING_COUPON(-18L),
    /**
     * 点评闪惠
     */
    DIANPING_FASTPAY(-19L),

    /**
     * 匿名实体卡余额
     */
    ANONYMOUS_ENTITY_CARD(-20L),
    /**
     * 糯米点菜
     */
    NUOMI_DIANCAI(-21L),
    /**
     * 第3方C端
     */
    THIRD_CLIENT(-22L),
    /**
     * 美团闪付
     */
    MEITUAN_FASTPAY(-23L),
    /**
     * 美团团购
     */
    MEITUAN_TUANGOU(-24L),
    /**
     * 钱包生活
     */
    PURSE_LIFE(-25L),

    /**
     * 百度糯米券
     */
    BAINUO_TUANGOU(-26L),
    /**
     * 金诚支付
     */
    JIN_CHENG(-29L),
    /**
     * 金诚充值卡
     */
    JIN_CHENG_VALUE_CARD(-30L),
    /**
     * 烽火手环支付
     */
    FENGHUO_WRISTBAND(-34L),
    /**
     * 口碑券
     */
    KOUBEI_TUANGOU(-36L),
    /**
     * 银联云闪付
     */
    UNIONPAY_CLOUD_PAY(-37L),

    /**
     * 工商e支付
     */
    ICBC_E_PAY(-38L),

    /**
     * 移动支付（含一码多付和多码一扫）
     */
    MOBILE_PAY(-40L),

    /**
     * 订金抵扣
     */
    EARNEST_DEDUCT(-42L),

    /**
     * 电信翼支付
     */
    DIANXIN_YIPAY(-101L),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Long> helper;

    private PayModeId(Long value) {
        helper = Helper.valueHelper(value);
    }

    private PayModeId() {
        helper = Helper.unknownHelper();
    }

    @Override
    public Long value() {
        return helper.value();
    }

    @Override
    public boolean equalsValue(Long value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public boolean isUnknownEnum() {
        return helper.isUnknownEnum();
    }

    @Override
    public void setUnknownValue(Long value) {
        helper.setUnknownValue(value);
    }

    @Override
    public String toString() {
        return "" + value();
    }

    public static PayModeId toEnum(Long value) {
        if (value != null) {
            for (PayModeId _enum : PayModeId.values()) {
                if (_enum.value() == value) {
                    return _enum;
                }
            }
        }
        return __UNKNOWN__;
    }

}
