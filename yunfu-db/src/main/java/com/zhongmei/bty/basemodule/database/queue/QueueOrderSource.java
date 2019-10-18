package com.zhongmei.bty.basemodule.database.queue;

import com.zhongmei.yunfu.util.ValueEnum;


public enum QueueOrderSource implements ValueEnum<String> {

    NONE("NONE"),
        DaiDingYuDing("DaiDingYuDing"),
        DaoDian("DaoDian"),
        DaZhongDianPing("DaZhongDianPing"),
        MeiTuan("MeiTuan"),
        Nuomi("Nuomi"),
        WeiXin("weixin"),
        XiaoMiShu("XiaoMiShu"),
        ZhaoWei("ZhaoWei"),
        ShouJiYuDing("ShouJiYuDing"),
        DianHuaYuDing("DianHuaYuDing"),
        TaoDianDian("TaoDianDian"),
        BaiduDitu("BaiduDitu"),
        ZhiDaHao("ZhiDaHao"),
        Baidu("Baidu"),
        Enjoy("Enjoy"),
        SelfHelp("SelfHelp"),
        Alipay("Alipay"),
        Shop("Shop"),
        BaiduWaiMai("BaiduWaiMai"),
        DianHua("DianHua"),
        Daodian("Daodian"),
        App("App"),
        BaiduMap("BaiduMap"),
        MeiDaQueue("XINMEIDA"),


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
