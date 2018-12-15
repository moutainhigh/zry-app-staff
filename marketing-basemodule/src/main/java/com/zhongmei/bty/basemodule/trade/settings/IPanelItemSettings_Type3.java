package com.zhongmei.bty.basemodule.trade.settings;

import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings;
import com.zhongmei.bty.commonmodule.core.annotions.GET;
import com.zhongmei.bty.commonmodule.core.annotions.SETTINGS;

/**

 */
@SETTINGS(name = "type3")
public interface IPanelItemSettings_Type3 extends IPanelItemSettings {

    /**
     * 是否支持扫码点单
     *
     * @return true：支持扫码点单；反之，不支持
     */
    @Override
    @GET(key = "order_scan", defBoolean = true)
    boolean isOrderByScanChecked();

    @GET(key = "serial", defInt = SERIAL_DESK_MODE)
    int getSerialMode();

    @GET(key = "client_number", defBoolean = true)
    boolean isClientNumberChecked();

    @GET(key = "inner_used", defBoolean = false)
    boolean isSupportInnerUsed();

}
