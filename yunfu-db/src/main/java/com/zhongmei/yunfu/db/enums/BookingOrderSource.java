package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum BookingOrderSource implements ValueEnum<Integer> {

    NONE(20),
        DaiDingYuDing(21),
        DaoDian(1),
    WECHAT(2),
        DaZhongDianPing(23),
        MeiTuan(24),
        Nuomi(25),
        WeiXin(26),
        XiaoMiShu(27),
        ZhaoWei(28),
        ShouJiYuDing(29),
        DianHuaYuDing(30),
        TaoDianDian(31),
        BaiduDitu(32),
        ZhiDaHao(33),
        Baidu(34),
        Enjoy(35),
        SelfHelp(36),
        Alipay(37),
        Shop(38),
        BaiduWaiMai(39),
        DianHua(40),
        Daodian(41),
        App(42),
        BaiduMap(43),
        ShuKeApp(44),
        XinMeiDa(45),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private BookingOrderSource(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private BookingOrderSource() {
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
