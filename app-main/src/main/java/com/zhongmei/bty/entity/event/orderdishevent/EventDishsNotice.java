package com.zhongmei.bty.entity.event.orderdishevent;

import java.util.List;

import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;


public class EventDishsNotice {


    public final DishBrandType dishType;
    public final List<DishVo> dishList;
    public final boolean scanResult;

    public EventDishsNotice(DishBrandType dishType, List<DishVo> dishList, boolean scanResult) {
        this.dishType = dishType;
        this.dishList = dishList;
        this.scanResult = scanResult;
    }
}
