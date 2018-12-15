package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 预订单来源
 */
public enum BookingOrderSource implements ValueEnum<Integer> {

    NONE(20),
    // 代订
    DaiDingYuDing(21),
    // 到店
    DaoDian(1),
    WECHAT(2),
    // 大众点评
    DaZhongDianPing(23),
    // 美团
    MeiTuan(24),
    // 百度糯米
    Nuomi(25),
    // 微信
    WeiXin(26),
    // 订餐小秘书
    XiaoMiShu(27),
    // 找位
    ZhaoWei(28),
    // 手机
    ShouJiYuDing(29),
    // 电话
    DianHuaYuDing(30),
    // 淘点点
    TaoDianDian(31),
    // 百度地图
    BaiduDitu(32),
    // 直达号
    ZhiDaHao(33),
    // 百度直达号
    Baidu(34),
    // Enjoy
    Enjoy(35),
    // 自助
    SelfHelp(36),
    // 支付宝
    Alipay(37),
    // 商户官网
    Shop(38),
    // 百度外卖
    BaiduWaiMai(39),
    //
    DianHua(40),
    // 到店取号
    Daodian(41),
    // APP取号
    App(42),
    // 百度取号
    BaiduMap(43),
    //熟客App
    ShuKeApp(44),
    //新美大预订
    XinMeiDa(45),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
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
