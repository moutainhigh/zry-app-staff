package com.zhongmei.bty.basemodule.trade.settings;

import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings;
import com.zhongmei.bty.commonmodule.core.annotions.GET;
import com.zhongmei.bty.commonmodule.core.annotions.PUT;
import com.zhongmei.bty.commonmodule.core.annotions.SETTINGS;


@SETTINGS(name = "type4")
public interface IPanelItemSettings_Type4 extends IPanelItemSettings {

    @GET(key = "order_scan", defBoolean = true)
    boolean isOrderByScanChecked();


    @PUT(key = "order_scan")
    void setOrderByScanChecked(boolean value);


    @GET(key = "order_search", defBoolean = true)
    boolean isOrderBySearchChecked();


    @PUT(key = "order_search")
    void setOrderBySearchChecked(boolean value);

    @Override
    @GET(key = "order_home_page", defInt = 6)
    int getOrderHomePage();

    @GET(key = "inner_used", defBoolean = false)
    boolean isSupportInnerUsed();
}
