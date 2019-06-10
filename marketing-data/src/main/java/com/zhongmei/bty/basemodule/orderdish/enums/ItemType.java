package com.zhongmei.bty.basemodule.orderdish.enums;

/**
 * Created by demo on 2018/12/15
 */

import com.zhongmei.yunfu.util.ValueEnum;

public enum ItemType implements ValueEnum<Integer> {

    GRAY_SEPERATOR(-1), // 灰色间隔线

    SINGLE(0), // 单菜

    SINGLE_DISCOUNT(1), // 单菜折扣

    SINGLE_MEMO(2), // 单菜备注

    COMBO(3), // 套餐

    COMBO_DISCOUNT(4), // 套餐折扣

    COMBO_MEMO(5), // 套餐外壳备注

    CHILD(6), // 套餐子菜

    CHILD_MEMO(7), // 子菜备注

    ALL_DISCOUNT(8), // 整单打折

    COUPONS(9), // 优惠劵

    INTERGRAL(10), // 积分抵现

    ALL_MEMO(11), // 整单备注

    LABEL_UNSAVE(12), // 未生效

    LABEL_SAVE_UNPRINTED(13), // 打印失败

    LABEL_SAVE_PRINTED(14), // 打印成功

    LABEL_PAYMENT(15), // 支付栏

    ADDITIONAL(16), // 附加费

    MARKET_ACTIVITY(17), // 营销活动

    WECHAT_CARD_COUPONS(18), // 微信卡券

    BANQUET_PRIVILIGE(19),//宴请
    GIFT_COUPON(20),//礼品劵
    PROPERTIES(21),//口味做法
    EXTRA_ITEM(22),//加料
    TITLE_ITEM(23),//类别和团餐单点分类

    BUFFET_TRADE_PEOPLE(24),//自助餐人数显示

    BUFFET_EXTRA_DEPOSIT(25),//自助餐额外的费用，例如：押金

    TITLE_CATEGORY(26),//等叫、起菜的中类样式

    //    WEST_SINGLE(27), // 西餐单菜
//    WEST_COMBO(28), // 西餐套餐
    WEST_CHILD(27), // 西餐套餐子菜

    EXCISE_TAX(28), //消费税

    /**
     * 服务费
     */
    SERVICE(29),
    //技师、服务员类型
    ITEM_USER(30),
    //整单技师、服务
    TRADE_USER(31),
    //次卡服务提示
    CARD_SERVICE_LABEL(32),
    //小程序优惠显示
    APPLET_LABEL(33),
    //服务次卡中的子服务
    SERVER_CHILD_ITEM(34),
    //会员储值打折
    CHARGE_PRIVILEGE(35),
    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private ItemType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ItemType() {
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
