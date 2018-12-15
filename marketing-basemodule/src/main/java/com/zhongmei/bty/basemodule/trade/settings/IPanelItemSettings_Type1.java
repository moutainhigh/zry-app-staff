package com.zhongmei.bty.basemodule.trade.settings;

import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings;
import com.zhongmei.bty.commonmodule.core.annotions.GET;
import com.zhongmei.bty.commonmodule.core.annotions.PUT;
import com.zhongmei.bty.commonmodule.core.annotions.SETTINGS;

/**

 */
@SETTINGS(name = "type1")
public interface IPanelItemSettings_Type1 extends IPanelItemSettings {

    @Override
    @GET(key = "client_number", defBoolean = true)
    boolean isClientNumberChecked();

    @Override
    @PUT(key = "client_number")
    void setClientNumberChecked(boolean value);

    @Override
    @GET(key = "order_scan", defBoolean = true)
    boolean isOrderByScanChecked();

    @Override
    @PUT(key = "order_scan")
    void setOrderByScanChecked(boolean value);

    @Override
    @GET(key = "order_search", defBoolean = true)
    boolean isOrderBySearchChecked();

    @Override
    @PUT(key = "order_search")
    void setOrderBySearchChecked(boolean value);
}
