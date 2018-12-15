package com.zhongmei.bty.mobilepay;

/**
 * Created by demo on 2018/12/15
 */

public interface IPaymentMenuType {
    public static final int PAY_MENU_TYPE_CASH = 1;// 现金

    public static final int PAY_MENU_TYPE_WEIXIN = 2;//微信支付

    public static final int PAY_MENU_TYPE_ALIPAY = 4;// 支付宝

    public static final int PAY_MENU_TYPE_UNION = 5;// 银联

    public static final int PAY_MENU_TYPE_MEMBER = 6;//储值

    public static final int PAY_MENU_TYPE_OTHERS = 10;//其它

    public static final int PAY_MENU_TYPE_MOBILE = 3;// 移动支付菜单

    public static final int PAY_MENU_TYPE_MEITUANCOUPON = 7;// 美团券

    public static final int PAY_MENU_TYPE_MEITUANCASHPAY = 8;//美团闪惠

    public static final int PAY_MENU_TYPE_BAINUOCOUPON = 9;// 百度糯米券


    public static final int PAY_MENU_TYPE_BAIDU = 11;// 百度

    public static final int PAY_MENU_TYPE_LAGPAY = 12;//挂账

    public static final int PAY_MENU_TYPE_JIN_CHENG = 13;//金诚APP

    public static final int PAY_MENU_TYPE_JIN_CHENG_VALUE_CARD = 14;//金诚充值卡

    public static final int PAY_MENU_TYPE_FHXM = 15;//烽火科技

    public static final int PAY_MENU_TYPE_KOUBEI = 16;//口碑

    public static final int PAY_MENU_TYPE_UNIONPAY_CLOUD = 17;//云闪付

    public static final int PAY_MENU_TYPE_ICBC_EPAY = 18;//工商E支付

    public static final int PAY_MENU_TYPE_DX_YIPAY = 19;//电信翼支付

    public static final int PAY_MENU_TYPE_EMPTY = -1;//空占位按钮

    public static final int PAY_MENU_MAX_SIZE = 19;//添加一个菜单加1
}
