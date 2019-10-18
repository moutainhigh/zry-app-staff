package com.zhongmei.bty.basemodule.trade.settings;

import com.zhongmei.bty.commonmodule.component.ISettings;
import com.zhongmei.bty.commonmodule.core.annotions.GET;
import com.zhongmei.bty.commonmodule.core.annotions.PUT;
import com.zhongmei.bty.commonmodule.core.annotions.SETTINGS;



@SETTINGS(name = "dinner_western_sort")
public interface DinnerWesternDishSortSettings extends ISettings {


    int MED_TYPE_1 = 0xf11;

    int SERVING_TYPE_2 = 0xf12;


    @GET(key = "type", defInt = MED_TYPE_1)
    int getType();


    @PUT(key = "type")
    void setType(int value);
}
