package com.zhongmei.bty.basemodule.orderdish.event;

import java.util.List;

import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealGroupVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealVo;

/**
 * 通知某个套餐分组下的套餐明细列表。在切换套餐分组将发送此Event
 *
 * @version: 1.0
 * @date 2015年7月10日
 */
public class EventSetmealsNotice {

    /**
     * 操作时传入的UUID
     */
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
