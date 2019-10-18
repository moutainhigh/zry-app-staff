package com.zhongmei.bty.basemodule.orderdish.event;

import java.util.List;

import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealGroupVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealVo;


public class EventSetmealsNotice {


    public final String uuid;
    public final DishSetmealGroupVo setmealGroup;
    public final List<DishSetmealVo> setmealVoList;

    public EventSetmealsNotice(String uuid, DishSetmealGroupVo setmealGroup,
                               List<DishSetmealVo> setmealVoList) {
        this.uuid = uuid;
        this.setmealGroup = setmealGroup;
        this.setmealVoList = setmealVoList;
    }

}
