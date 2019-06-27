package com.zhongmei.bty.customer.util;

/**
 * 顾客
 * <p>
 * Created by demo on 2018/12/15
 */
public class CustomerContants {

    public static final int CARD_ACTIVITY_CREATE_TYPE = 0x001;// 传递给给创建会员的方法

    public static final String BUNDLE_KEY_CREATE_MEMBER = "create_member";

    public static final String BUNDLE_KEY_ACTIVE_LIST = "key_active_list";

    public static final String BUNDLE_KEY_SALE_CARD_LIST = "sale_card_list";

    public static final String BUNDLE_KEY_IS_FROM_SALE = "is_from_sale";

    public static final String KEY_CUSTOMER_EDIT = "key_customer_edit";

    public static final String KEY_CUSTOMER_CREDIT = "key_customer_credit";

    /**
     * 标记会员卡售卡
     */
    public static final int FLAG_CUSTOMER_CARD_SALE = 0;
    /**
     * 标记进入页面为会员卡绑定
     */
    public static final int FLAG_CUSTOMER_BAND = 1;

    /**
     * TYPE - 余额
     */
    public static final int TYPE_BALANCE = 0;

    /**
     * TYPE - 积分
     */
    public static final int TYPE_INTEGRAL = 1;

    /**
     * TYPE - 优惠券
     */
    public static final int TYPE_COUPON = 2;

    //次卡服务
    public static final int TYPE_CARD_TIME = 3;

    //小程序服务
    public static final int TYPE_WX_APP = 4;

    //消费记录
    public static final int TYPE_EXPENSE = 5;

    //会员档案
    public static final int TYPE_DOCMENT = 6;

}
