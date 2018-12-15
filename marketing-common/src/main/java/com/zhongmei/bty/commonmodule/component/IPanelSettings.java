package com.zhongmei.bty.commonmodule.component;

import com.zhongmei.bty.commonmodule.core.annotions.GET;
import com.zhongmei.bty.commonmodule.core.annotions.PUT;
import com.zhongmei.bty.commonmodule.core.annotions.SETTINGS;

/**
 * 面板设置接口
 */
@SETTINGS(name = "panel")
public interface IPanelSettings extends ISettings {

    /**
     * 模板1
     */
    int PANEL_TYPE_1 = 0xf11;
    /**
     * 模板2
     */
    int PANEL_TYPE_2 = 0xf12;
    /**
     * 模板3
     */
    int PANEL_TYPE_3 = 0xf13;
    /**
     * 模板4
     */
    int PANEL_TYPE_4 = 0xf14;

    /**
     * 获取当前的面板
     *
     * @return 整型面板标示, 取值为如下之一：{@link IPanelSettings#PANEL_TYPE_1},
     * {@link IPanelSettings#PANEL_TYPE_2}, {@link IPanelSettings#PANEL_TYPE_3},
     * {@link IPanelSettings#PANEL_TYPE_4}
     */
    @GET(key = "layout", defInt = PANEL_TYPE_1)
    int getPanel();

    /**
     * 设置当前的面板
     *
     * @param value 整型面板标示
     * @see IPanelSettings#getPanel()
     */
    @PUT(key = "layout")
    void setPanel(int value);
}
