package com.zhongmei.bty.basemodule.database.queue;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 预订单来源
 */
public enum QueueOrderSource implements ValueEnum<String> {

    NONE("NONE"),
    // 代订
    DaiDingYuDing("DaiDingYuDing"),
    // 到店
    DaoDian("DaoDian"),
    // 大众点评
    DaZhongDianPing("DaZhongDianPing"),
    // 美团
    MeiTuan("MeiTuan"),
    // 百度糯米
    Nuomi("Nuomi"),
    // 微信
    WeiXin("weixin"),
    // 订餐小秘书
    XiaoMiShu("XiaoMiShu"),
    // 找位
    ZhaoWei("ZhaoWei"),
    // 手机
    ShouJiYuDing("ShouJiYuDing"),
    // 电话
    DianHuaYuDing("DianHuaYuDing"),
    // 淘点点
    TaoDianDian("TaoDianDian"),
    // 百度地图
    BaiduDitu("BaiduDitu"),
    // 直达号
    ZhiDaHao("ZhiDaHao"),
    // 百度直达号
    Baidu("Baidu"),
    // Enjoy
    Enjoy("Enjoy"),
    // 自助
    SelfHelp("SelfHelp"),
    // 支付宝
    Alipay("Alipay"),
    // 商户官网
    Shop("Shop"),
    // 百度外卖
    BaiduWaiMai("BaiduWaiMai"),
    //
    DianHua("DianHua"),
    // 到店取号
    Daodian("Daodian"),
    // APP取号
    App("App"),
    // 百度取号
    BaiduMap("BaiduMap"),
    //美大排队
    MeiDaQueue("XINMEIDA"),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<String> helper;

    private QueueOrderSource(String value) {
        helper = Helper.valueHelper(value);
    }

    private QueueOrderSource() {
        helper = Helper.unknownHelper();
    }

    @Override
    public String value() {
        return helper.value();
    }

    @Override
    public boolean equalsValue(String value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public boolean isUnknownEnum() {
        return helper.isUnknownEnum();
    }

    @Override
    public void setUnknownValue(String value) {
        helper.setUnknownValue(value);
    }

    @Override
    public String toString() {
        return value();
    }

}
