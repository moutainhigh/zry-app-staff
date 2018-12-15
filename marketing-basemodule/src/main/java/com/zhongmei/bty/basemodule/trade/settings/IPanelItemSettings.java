package com.zhongmei.bty.basemodule.trade.settings;

import com.zhongmei.bty.commonmodule.component.ISettings;
import com.zhongmei.bty.commonmodule.core.annotions.GET;
import com.zhongmei.bty.commonmodule.core.annotions.PUT;

/**
 * 面板的元素设置接口
 */
public interface IPanelItemSettings extends ISettings {

    /**
     * 号牌/桌台：号牌模式
     */
    int SERIAL_FLAPPER_MODE = 0xF01;

    /**
     * 号牌/桌台：桌台模式
     */
    int SERIAL_DESK_MODE = 0XF02;

    /**
     * 号牌/桌台：无
     */
    int SERIAL_NONE = 0XF00;

    /**
     * 在号牌模式下的自动填充模式
     */
    int SERIAL_FLAPPER_MODE_AUTO_FIX = 0XF001;
    /**
     * 在号牌模式下的手动输入模式
     */
    int SERIAL_FLAPPER_MODE_INPUT = 0XF002;

    /**
     * 是否支持手动折扣
     *
     * @return true：支持；false：不支持
     */
    @GET(key = "sale", defBoolean = true)
    boolean isSaleChecked();

    /**
     * 设置是否支持手动折扣
     *
     * @param value true：支持；false：不支持
     */
    @PUT(key = "sale")
    void setSaleChecked(boolean value);

    /**
     * 是否支持优惠券
     *
     * @return true：支持；false：不支持
     */
    @GET(key = "coupon", defBoolean = true)
    boolean isCouponChecked();

    /**
     * 设置是否支持优惠券
     *
     * @param value true：支持；false：不支持
     */
    @PUT(key = "coupon")
    void setCouponChecked(boolean value);

    /**
     * 是否支持附加费
     *
     * @return true：支持；false：不支持
     */
    @GET(key = "extra_charge", defBoolean = true)
    boolean isExtraChargeChecked();

    /**
     * 设置是否支持附加费
     *
     * @param value true：支持；false：不支持
     */
    @PUT(key = "extra_charge")
    void setExtraChargeChecked(boolean value);

    /**
     * 是否支持备注
     *
     * @return true：支持；false：不支持
     */
    @GET(key = "motto", defBoolean = true)
    boolean isMottoChecked();

    /**
     * 设置是否支持备注
     *
     * @param value true：支持；false：不支持
     */
    @PUT(key = "motto")
    void setMottoChecked(boolean value);

    /**
     * 是否支持挂单
     *
     * @return true：支持；false：不支持
     */
    @GET(key = "guadan", defBoolean = true)
    boolean isGuadanChecked();

    /**
     * 设置是否支持挂单
     *
     * @param value true：支持；false：不支持
     */
    @PUT(key = "guadan")
    void setGuadanChecked(boolean value);

    /**
     * 是否支持促销活动
     *
     * @return true：支持；false：不支持
     */
    @GET(key = "sales_promotion", defBoolean = true)
    boolean isSalesPromotionChecked();

    /**
     * 设置是否支持促销活动
     *
     * @param value true：支持；false：不支持
     */
    @PUT(key = "sales_promotion")
    void setSalesPromotionChecked(boolean value);

    /**
     * 是否支持人数编辑
     *
     * @return true：支持；false：不支持
     */
    @GET(key = "client_number", defBoolean = false)
    boolean isClientNumberChecked();

    /**
     * 设置是否支持人数编辑
     *
     * @param value true：支持；false：不支持
     */
    @PUT(key = "client_number")
    void setClientNumberChecked(boolean value);

    /**
     * 是否支持扫码点单
     *
     * @return true：支持扫码点单；反之，不支持
     */
    @GET(key = "order_scan", defBoolean = false)
    boolean isOrderByScanChecked();

    /**
     * 设置是否支持扫码点单
     *
     * @param value true：支持扫码点单；反之，不支持
     */
    @PUT(key = "order_scan")
    void setOrderByScanChecked(boolean value);

    /**
     * 是否支持搜索点单
     *
     * @return true：支持扫码点单；反之，不支持
     */
    @GET(key = "order_search", defBoolean = false)
    boolean isOrderBySearchChecked();

    /**
     * 设置是否支持搜索点单
     *
     * @param value true：支持扫码点单；反之，不支持
     */
    @PUT(key = "order_search")
    void setOrderBySearchChecked(boolean value);

    /**
     * 获取号牌/桌台模式,所有模式{@link IPanelItemSettings#SERIAL_FLAPPER_MODE},
     * {@link IPanelItemSettings#SERIAL_DESK_MODE},
     * {@link IPanelItemSettings#SERIAL_NONE}
     *
     * @return 返回当前模式, 默认无选中
     */
    @GET(key = "serial", defInt = SERIAL_NONE)
    int getSerialMode();

    /**
     * 设置号牌/桌台模式
     *
     * @param serialMode
     * @see IPanelItemSettings#getSerialMode()
     */
    @PUT(key = "serial")
    void setSerialMode(int serialMode);

    @GET(key = "serial_flapper_mode", defInt = SERIAL_FLAPPER_MODE_AUTO_FIX)
    int getFlapperMode();

    @PUT(key = "serial_flapper_mode")
    void setFlappermode(int mode);

    @GET(key = "serial_fix_auto_start", defInt = 1)
    int getSerialAutoFixStart();

    @PUT(key = "serial_fix_auto_start")
    void setSerialAutoFixStart(int value);

    @GET(key = "serial_fix_auto_end", defInt = 100)
    int getSerialAutoFixEnd();

    @PUT(key = "serial_fix_auto_end")
    void setSerialAutoFixEnd(int value);

    @GET(key = "serial_fix_must", defBoolean = false)
    boolean isSerialMustFix();

    @PUT(key = "serial_fix_must")
    void setSerialFixMust(boolean value);

    @GET(key = "order_page_columns", defInt = 5)
    int getOrderPageColumns();

    @PUT(key = "order_page_columns")
    void setOrderPageColumns(int columns);

    @GET(key = "combo_page_columns", defInt = 4)
    int getComboPageColumns();

    @PUT(key = "combo_page_columns")
    void setComboPageColumns(int columns);

    @GET(key = "order_home_page", defInt = 0)
    int getOrderHomePage();

    @PUT(key = "order_home_page")
    void setOrderHomePage(int orderHomePage);

    @GET(key = "serial_current_no", defInt = -1)
    int getSerialCurrentNo();

    @PUT(key = "serial_current_no")
    void setSerialCurrentNo(int no);

    // v8.1 添加生成号牌的时间
    @PUT(key = "serial_current_no_time")
    void setSerialCurrentNoTime(long currentNoTime);

    // v8.1 获取生成号牌的时间
    @GET(key = "serial_current_no_time", defInt = -1)
    long getSerialCurrentNoTime();

    @GET(key = "inner_used", defBoolean = true)
    boolean isSupportInnerUsed();

    @PUT(key = "inner_used")
    void setSupportInnerUsed(boolean value);

    // v8.3 添加生成的备注选项
    @GET(key = "snack_remark_type")
    int getSnackRemarkType();

    // v8.3 获取生成备注选项
    @PUT(key = "snack_remark_type")
    void setSnackRemarkType(int type);

    // v8.4 保存快餐快捷支付方式
    @PUT(key = "snack_quick_pay")
    void setSnackQuickPay(String quickPay);

    // v8.4 读取快餐快捷支付方式
    @GET(key = "snack_quick_pay")
    String getSnackQuickPay();

    // v8.5 保存是否需要在点菜前填写备用金
    @GET(key = "need_edit_backup_money", defBoolean = true)
    boolean getNeedEditBackupMoney();

    @PUT(key = "need_edit_backup_money")
    void setNeedEditBackupMoney(boolean needEditBackupMoney);

    @GET(key = "backup_money", defFloat = Float.MAX_VALUE)
    float getBackupMoney();

    @PUT(key = "backup_money")
    void setBackupMoney(float backupMoney);


}
