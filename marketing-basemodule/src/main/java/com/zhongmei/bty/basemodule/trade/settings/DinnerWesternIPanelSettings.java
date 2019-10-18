package com.zhongmei.bty.basemodule.trade.settings;

import com.zhongmei.bty.commonmodule.component.ISettings;
import com.zhongmei.bty.commonmodule.core.annotions.GET;
import com.zhongmei.bty.commonmodule.core.annotions.PUT;
import com.zhongmei.bty.commonmodule.core.annotions.SETTINGS;


@SETTINGS(name = "dinner_western_panel")
public interface DinnerWesternIPanelSettings extends ISettings {


    int PANEL_TYPE_1 = 0xf11;

    int PANEL_TYPE_2 = 0xf12;


    @GET(key = "layout", defInt = PANEL_TYPE_1)
    int getPanel();


    @PUT(key = "layout")
    void setPanel(int value);
}
