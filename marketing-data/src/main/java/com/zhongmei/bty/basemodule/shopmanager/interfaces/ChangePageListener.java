package com.zhongmei.bty.basemodule.shopmanager.interfaces;

import android.os.Bundle;

public interface ChangePageListener {
    /**
     * 点菜列表
     */
    public int ORDERDISHLIST = 0;
    /**
     * 折扣
     */
    public int DISCOUNT = 1;
    /**
     * 整单备注
     */
    public int MEMO = 3;
    /**
     * 会员登录
     */
    public int REMOBER_LOGIN = 5;

    /**
     * 搜索商品
     */
    public int SEARCH = 6;

    /**
     * 套餐点菜界面
     */
    public int DISHCOMBO = 7;

    /**
     * 菜品属性界面
     */
    public int DISHPROPERTY = 8;

    /**
     * 优惠劵
     */
    public int DISH_CUSTOMER_COUPONS = 9;

    /**
     * 结算界面
     */
    public int SETTLEMENT = 14;

    /**
     * 保存返回
     */
    public int SAVE_BACK = 15;

    /**
     * 正餐点菜主界面
     */
    public int PAGE_ORDER_DISH = 16;

    /**
     * 正餐交接界面
     */
    public int PAGE_HANDOVER = 17;

    /**
     * 正餐票据中心
     */
    public int PAGE_ORDER_CENTER = 18;

    /**
     * 正餐桌台界面
     */
    public int PAGE_TABLE_HOME = 19;

    /**
     * 正餐整单备注
     */
    public int ORDER_COMMENTS = 20;

    /**
     * 关账
     */
    public int CLOSING_FRAGMENT = 21;

    /**
     * 附加费
     */
    public int EXTRA_CHARGE = 22;

    /**
     * 活动列表
     */
    public int ORDER_ACTIVITY = 23;

    /**
     * 券的界面
     */
    public int COUPONS = 24;

    public int REPORT_FORM_FRAGMENT = 25;

    /**
     * 报表中心
     */
    public int FORM_CENTER = 26;

    /**
     * 收支管理
     */
    public int PAYMENT_MANAGER = 27;

    /**
     * 门店管理
     */
    public int SHOP_MANAGEMENT_MAIN_FRAGMENT = 28;

    /**
     * 规格页
     */
    int PAGE_STANDARD = 29;

    /**
     * 做法页
     */
    int PAGE_PROPERTY = 30;

    /**
     * 加料页
     */
    int PAGE_EXTRA = 31;

    /**
     * 变价页
     */
    int PAGE_CHANGE_PRICE = 32;

    //SKU组件化主页
    int PAGE_OPEN_API_SKU = 33;

    //SKU组件化服务
    int SERVICE_OPEN_API_SKU = 34;

    /**
     * 套餐子菜交换页
     */
    int PAGE_SUB_DISH_EXCHANGE = 35;

    /**
     * 取消营销活动
     */
    int PAGE_CANCEL_MARKET = 36;

    /**
     * 促销活动
     */
    int PAGE_SALES_PROMOTION = 37;

    /**
     * 切换设置界面
     *
     * @Title: changePage
     * @Description: TODO
     * @Param @param pageNo TODO
     * @Return void 返回类型
     */
    public void changePage(int pageNo, Bundle bundle);

    /**
     * 清空购车时关闭所有设置界面
     *
     * @Title: clearShoppingCart
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    public void clearShoppingCart();
}
