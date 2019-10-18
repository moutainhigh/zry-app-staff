package com.zhongmei.bty.basemodule.trade.settings;

import com.zhongmei.bty.commonmodule.component.ISettings;
import com.zhongmei.bty.commonmodule.core.annotions.GET;
import com.zhongmei.bty.commonmodule.core.annotions.PUT;


public interface IPanelItemSettings extends ISettings {


    int SERIAL_FLAPPER_MODE = 0xF01;


    int SERIAL_DESK_MODE = 0XF02;


    int SERIAL_NONE = 0XF00;


    int SERIAL_FLAPPER_MODE_AUTO_FIX = 0XF001;

    int SERIAL_FLAPPER_MODE_INPUT = 0XF002;


    @GET(key = "sale", defBoolean = true)
    boolean isSaleChecked();


    @PUT(key = "sale")
    void setSaleChecked(boolean value);


    @GET(key = "coupon", defBoolean = true)
    boolean isCouponChecked();


    @PUT(key = "coupon")
    void setCouponChecked(boolean value);


    @GET(key = "extra_charge", defBoolean = true)
    boolean isExtraChargeChecked();


    @PUT(key = "extra_charge")
    void setExtraChargeChecked(boolean value);


    @GET(key = "motto", defBoolean = true)
    boolean isMottoChecked();


    @PUT(key = "motto")
    void setMottoChecked(boolean value);


    @GET(key = "guadan", defBoolean = true)
    boolean isGuadanChecked();


    @PUT(key = "guadan")
    void setGuadanChecked(boolean value);


    @GET(key = "sales_promotion", defBoolean = true)
    boolean isSalesPromotionChecked();


    @PUT(key = "sales_promotion")
    void setSalesPromotionChecked(boolean value);


    @GET(key = "client_number", defBoolean = false)
    boolean isClientNumberChecked();


    @PUT(key = "client_number")
    void setClientNumberChecked(boolean value);


    @GET(key = "order_scan", defBoolean = false)
    boolean isOrderByScanChecked();


    @PUT(key = "order_scan")
    void setOrderByScanChecked(boolean value);


    @GET(key = "order_search", defBoolean = false)
    boolean isOrderBySearchChecked();


    @PUT(key = "order_search")
    void setOrderBySearchChecked(boolean value);


    @GET(key = "serial", defInt = SERIAL_NONE)
    int getSerialMode();


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

        @PUT(key = "serial_current_no_time")
    void setSerialCurrentNoTime(long currentNoTime);

        @GET(key = "serial_current_no_time", defInt = -1)
    long getSerialCurrentNoTime();

    @GET(key = "inner_used", defBoolean = true)
    boolean isSupportInnerUsed();

    @PUT(key = "inner_used")
    void setSupportInnerUsed(boolean value);

        @GET(key = "snack_remark_type")
    int getSnackRemarkType();

        @PUT(key = "snack_remark_type")
    void setSnackRemarkType(int type);

        @PUT(key = "snack_quick_pay")
    void setSnackQuickPay(String quickPay);

        @GET(key = "snack_quick_pay")
    String getSnackQuickPay();

        @GET(key = "need_edit_backup_money", defBoolean = true)
    boolean getNeedEditBackupMoney();

    @PUT(key = "need_edit_backup_money")
    void setNeedEditBackupMoney(boolean needEditBackupMoney);

    @GET(key = "backup_money", defFloat = Float.MAX_VALUE)
    float getBackupMoney();

    @PUT(key = "backup_money")
    void setBackupMoney(float backupMoney);


}
