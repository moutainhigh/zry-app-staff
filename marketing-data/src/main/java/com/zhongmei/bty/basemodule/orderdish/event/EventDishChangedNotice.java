package com.zhongmei.bty.basemodule.orderdish.event;

import java.util.Map;

import com.zhongmei.yunfu.db.entity.dish.DishShop;

/**
 * @version: 1.0
 * @date 2016年3月14日
 */
public class EventDishChangedNotice {

    /**
     * 剩余可售数量有变化的DishShop。key为DishShop.uuid
     */
    public final Map<String, DishShop> residueTotalChangedMap;

    public EventDishChangedNotice(Map<String, DishShop> residueTotalMap) {
        this.residueTotalChangedMap = residueTotalMap;
    }

}
