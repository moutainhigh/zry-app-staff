package com.zhongmei.bty.basemodule.orderdish.enums;



import com.zhongmei.yunfu.util.ValueEnum;

public enum ItemType implements ValueEnum<Integer> {

    GRAY_SEPERATOR(-1),
    SINGLE(0),
    SINGLE_DISCOUNT(1),
    SINGLE_MEMO(2),
    COMBO(3),
    COMBO_DISCOUNT(4),
    COMBO_MEMO(5),
    CHILD(6),
    CHILD_MEMO(7),
    ALL_DISCOUNT(8),
    COUPONS(9),
    INTERGRAL(10),
    ALL_MEMO(11),
    LABEL_UNSAVE(12),
    LABEL_SAVE_UNPRINTED(13),
    LABEL_SAVE_PRINTED(14),
    LABEL_PAYMENT(15),
    ADDITIONAL(16),
    MARKET_ACTIVITY(17),
    WECHAT_CARD_COUPONS(18),
    BANQUET_PRIVILIGE(19),    GIFT_COUPON(20),    PROPERTIES(21),    EXTRA_ITEM(22),    TITLE_ITEM(23),
    BUFFET_TRADE_PEOPLE(24),
    BUFFET_EXTRA_DEPOSIT(25),
    TITLE_CATEGORY(26),
        WEST_CHILD(27),
    EXCISE_TAX(28),

    SERVICE(29),
        ITEM_USER(30),
        TRADE_USER(31),
        CARD_SERVICE_LABEL(32),
        APPLET_LABEL(33),
        SERVER_CHILD_ITEM(34),
        CHARGE_PRIVILEGE(35),

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
