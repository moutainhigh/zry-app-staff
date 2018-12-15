package com.zhongmei.bty.basemodule.trade.settings;

import com.zhongmei.bty.commonmodule.component.ISettings;
import com.zhongmei.bty.commonmodule.core.annotions.GET;
import com.zhongmei.bty.commonmodule.core.annotions.PUT;
import com.zhongmei.bty.commonmodule.core.annotions.SETTINGS;

/**
 * 正西餐面板设置接口
 */
@SETTINGS(name = "dinner_western_panel")
public interface DinnerWesternIPanelSettings extends ISettings {

    /**
     * 中餐模板1
     */
    int PANEL_TYPE_1 = 0xf11;
    /**
     * 西餐模板2
     */
    int PANEL_TYPE_2 = 0xf12;

    /**
     * 获取当前的面板
     *
     * @return 整型面板标示, 取值为如下之一：{@link DinnerWesternIPanelSettings#PANEL_TYPE_1},
     * {@link DinnerWesternIPanelSettings#PANEL_TYPE_2}
     */
    @GET(key = "layout", defInt = PANEL_TYPE_1)
    int getPanel();

    /**
     * 设置当前的面板
     *
     * @param value 整型面板标示
     * @see DinnerWesternIPanelSettings#getPanel()
     */
    @PUT(key = "layout")
    void setPanel(int value);
}
