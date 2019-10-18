package com.zhongmei.bty.basemodule.shopmanager.interfaces;

import android.os.Bundle;

public interface ChangePageListener {

    public int ORDERDISHLIST = 0;

    public int DISCOUNT = 1;

    public int MEMO = 3;

    public int REMOBER_LOGIN = 5;


    public int SEARCH = 6;


    public int DISHCOMBO = 7;


    public int DISHPROPERTY = 8;


    public int DISH_CUSTOMER_COUPONS = 9;


    public int SETTLEMENT = 14;


    public int SAVE_BACK = 15;


    public int PAGE_ORDER_DISH = 16;


    public int PAGE_HANDOVER = 17;


    public int PAGE_ORDER_CENTER = 18;


    public int PAGE_TABLE_HOME = 19;


    public int ORDER_COMMENTS = 20;


    public int CLOSING_FRAGMENT = 21;


    public int EXTRA_CHARGE = 22;


    public int ORDER_ACTIVITY = 23;


    public int COUPONS = 24;

    public int REPORT_FORM_FRAGMENT = 25;


    public int FORM_CENTER = 26;


    public int PAYMENT_MANAGER = 27;


    public int SHOP_MANAGEMENT_MAIN_FRAGMENT = 28;


    int PAGE_STANDARD = 29;


    int PAGE_PROPERTY = 30;


    int PAGE_EXTRA = 31;


    int PAGE_CHANGE_PRICE = 32;

        int PAGE_OPEN_API_SKU = 33;

        int SERVICE_OPEN_API_SKU = 34;


    int PAGE_SUB_DISH_EXCHANGE = 35;


    int PAGE_CANCEL_MARKET = 36;


    int PAGE_SALES_PROMOTION = 37;


    public void changePage(int pageNo, Bundle bundle);


    public void clearShoppingCart();
}
