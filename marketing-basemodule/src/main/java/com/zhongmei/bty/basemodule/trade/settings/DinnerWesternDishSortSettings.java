package com.zhongmei.bty.basemodule.trade.settings;

import com.zhongmei.bty.commonmodule.component.ISettings;
import com.zhongmei.bty.commonmodule.core.annotions.GET;
import com.zhongmei.bty.commonmodule.core.annotions.PUT;
import com.zhongmei.bty.commonmodule.core.annotions.SETTINGS;

/**
 * 西餐点单顺序设置
 * Created by demo on 2018/12/15
 */

@SETTINGS(name = "dinner_western_sort")
public interface DinnerWesternDishSortSettings extends ISettings {

    /**
     * 中类顺序
     */
    int MED_TYPE_1 = 0xf11;
    /**
     * 上菜顺序
     */
    int SERVING_TYPE_2 = 0xf12;

    /**
     * 获取当前的设置
     *
     * @return ：{@link DinnerWesternDishSortSettings#MED_TYPE_1},
     * {@link DinnerWesternIPanelSettings#PANEL_TYPE_2}
     */
    @GET(key = "type", defInt = MED_TYPE_1)
    int getType();

    /**
     * 设置当前的面板
     *
     * @param value 整型面板标示
     * @see DinnerWesternIPanelSettings#getPanel()
     */
    @PUT(key = "type")
    void setType(int value);
}
