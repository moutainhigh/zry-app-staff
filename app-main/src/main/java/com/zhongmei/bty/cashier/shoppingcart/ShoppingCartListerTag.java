package com.zhongmei.bty.cashier.shoppingcart;

public interface ShoppingCartListerTag {

    public static int ORDER_DISH_LEFT = 1;

    public static int ORDER_DISH_RIGHT = 2;
    /**
     * 智盘
     */
    int ORDER_DISH_AI_PANEL = 201;

    /**
     * 搜索
     */
    public static int ORDER_DISH_SEARCH = 3;

    /**
     * 套餐
     */
    public static int ORDER_DISH_COMBO = 4;
    /**
     * 属性
     */
    public static int ORDER_DISH_PROPERTY = 5;

    /**
     * 正餐
     */
    public static int DINNER_DISH_HOME_PAGE = 6;

    /**
     * 正餐搜索
     */
    public static int DINNER_DISH_SEARCH = 7;

    /**
     * 正餐套餐
     */
    public static int DINNER_DISH_COMBO = 8;

    /**
     * 正餐属性
     */
    public static int DINNER_DISH_PROPERTY = 9;

    /**
     * 正餐结算菜品展示
     */
    public static int DINNER_DISH_BALANCE_SHOW = 10;

    /**
     * 正餐 点菜界面监听拆单
     */
    public static int DINNER_LEFT_SPLIT = 11;

    /**
     * 正餐 拆单总单
     */
    public static int DINNER_SPLIT_TOTAL_PAGE = 14;

    /**
     * 正餐收银结算监听拆单
     */
    public static int DINNER_BALANCE_SPLIT = 15;

    /**
     * 正餐收银结算监听拆单
     */
    public static int DINNER_DOPAY_SPLIT = 16;

    /**
     * 正餐 附加费
     */
    public static int DINNER_EXTRA = 12;

    int ORDER_DISH_MIDDLE = 13;
}
