package com.zhongmei.bty.basemodule.orderdish.event;

import java.util.List;

import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealGroupVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealVo;


public class EventSetmealGroupsNotice {


    public final String uuid;
    public final List<DishSetmealGroupVo> groupVoList;

    public final List<DishSetmealVo> selectedList;

    public EventSetmealGroupsNotice(String uuid) {
        this(uuid, null, null);
    }

    public EventSetmealGroupsNotice(String uuid, List<DishSetmealGroupVo> groupVoList,
                                    List<DishSetmealVo> selectedList) {
        this.uuid = uuid;
        this.groupVoList = groupVoList;
        this.selectedList = selectedList;
    }


    public boolean isSuccessful() {
        return groupVoList != null;
    }
}
