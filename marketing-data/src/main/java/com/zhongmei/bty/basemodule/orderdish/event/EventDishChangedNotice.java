package com.zhongmei.bty.basemodule.orderdish.event;

import java.util.Map;

import com.zhongmei.yunfu.db.entity.dish.DishShop;


public class EventDishChangedNotice {


    public final Map<String, DishShop> residueTotalChangedMap;

    public EventDishChangedNotice(Map<String, DishShop> residueTotalMap) {
        this.residueTotalChangedMap = residueTotalMap;
    }

}
