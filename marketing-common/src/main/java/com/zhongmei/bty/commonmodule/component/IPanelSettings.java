package com.zhongmei.bty.commonmodule.component;

import com.zhongmei.bty.commonmodule.core.annotions.GET;
import com.zhongmei.bty.commonmodule.core.annotions.PUT;
import com.zhongmei.bty.commonmodule.core.annotions.SETTINGS;


@SETTINGS(name = "panel")
public interface IPanelSettings extends ISettings {


    int PANEL_TYPE_1 = 0xf11;

    int PANEL_TYPE_2 = 0xf12;

    int PANEL_TYPE_3 = 0xf13;

    int PANEL_TYPE_4 = 0xf14;


    @GET(key = "layout", defInt = PANEL_TYPE_1)
    int getPanel();


    @PUT(key = "layout")
    void setPanel(int value);
}
