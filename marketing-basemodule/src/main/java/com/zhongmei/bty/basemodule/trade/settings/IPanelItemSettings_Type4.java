package com.zhongmei.bty.basemodule.trade.settings;

import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings;
import com.zhongmei.bty.commonmodule.core.annotions.GET;
import com.zhongmei.bty.commonmodule.core.annotions.PUT;
import com.zhongmei.bty.commonmodule.core.annotions.SETTINGS;

/**

 */
@SETTINGS(name = "type4")
public interface IPanelItemSettings_Type4 extends IPanelItemSettings {
    /**
     * 是否支持扫码点单
     *
     * @return true：支持扫码点单；反之，不支持
     */
    @GET(key = "order_scan", defBoolean = true)
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
    @GET(key = "order_search", defBoolean = true)
    boolean isOrderBySearchChecked();

    /**
     * 设置是否支持搜索点单
     *
     * @param value true：支持扫码点单；反之，不支持
     */
    @PUT(key = "order_search")
    void setOrderBySearchChecked(boolean value);

    @Override
    @GET(key = "order_home_page", defInt = 6)
    int getOrderHomePage();

    @GET(key = "inner_used", defBoolean = false)
    boolean isSupportInnerUsed();
}
