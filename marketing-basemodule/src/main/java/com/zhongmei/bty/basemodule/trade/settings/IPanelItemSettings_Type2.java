package com.zhongmei.bty.basemodule.trade.settings;

import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings;
import com.zhongmei.bty.commonmodule.core.annotions.GET;
import com.zhongmei.bty.commonmodule.core.annotions.SETTINGS;


@SETTINGS(name = "type2")
public interface IPanelItemSettings_Type2 extends IPanelItemSettings {

    @GET(key = "extra_charge", defBoolean = true)
    boolean isExtraChargeChecked();

    @Override
    @GET(key = "order_scan", defBoolean = false)
    boolean isOrderByScanChecked();

    @Override
    @GET(key = "order_search", defBoolean = false)
    boolean isOrderBySearchChecked();

    @Override
    @GET(key = "order_page_columns", defInt = 3)
    int getOrderPageColumns();

    @Override
    @GET(key = "combo_page_columns", defInt = 2)
    int getComboPageColumns();
}
