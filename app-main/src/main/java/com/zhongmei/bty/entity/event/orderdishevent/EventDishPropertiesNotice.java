package com.zhongmei.bty.entity.event.orderdishevent;

import com.zhongmei.bty.cashier.orderdishmanager.entity.BatchDishPropertyVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertiesVo;



public class EventDishPropertiesNotice {


    public final String uuid;

    public final DishPropertiesVo dishPropertiesVo;


    public EventDishPropertiesNotice(String uuid, DishPropertiesVo dishPropertiesVo) {
        this.uuid = uuid;
        this.dishPropertiesVo = dishPropertiesVo;
    }

}
