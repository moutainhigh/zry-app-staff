package com.zhongmei.bty.entity.event.orderdishevent;

import com.zhongmei.bty.cashier.orderdishmanager.entity.BatchDishPropertyVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertiesVo;


/**
 * @version: 1.0
 * @date 2015年7月15日
 */
public class EventDishPropertiesNotice {

    /**
     * 操作时传入的UUID
     */
    public final String uuid;
    /**
     * 商品属性信息
     */
    public final DishPropertiesVo dishPropertiesVo;


    public EventDishPropertiesNotice(String uuid, DishPropertiesVo dishPropertiesVo) {
        this.uuid = uuid;
        this.dishPropertiesVo = dishPropertiesVo;
    }

}
