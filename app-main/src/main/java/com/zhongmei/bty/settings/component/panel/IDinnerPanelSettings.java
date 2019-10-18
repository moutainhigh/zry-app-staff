package com.zhongmei.bty.settings.component.panel;

import com.zhongmei.bty.commonmodule.component.ISettings;
import com.zhongmei.bty.commonmodule.core.annotions.GET;
import com.zhongmei.bty.commonmodule.core.annotions.PUT;
import com.zhongmei.bty.commonmodule.core.annotions.SETTINGS;


@SETTINGS(name = "dinner_panel")
public interface IDinnerPanelSettings extends ISettings {


    int PANEL_TYPE_1 = 0xf11;

    int PANEL_TYPE_2 = 0xf12;


    @GET(key = "dinner_layout", defInt = PANEL_TYPE_1)
    int getPanel();


    @PUT(key = "dinner_layout")
    void setPanel(int value);
}
