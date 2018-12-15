package com.zhongmei.bty.basemodule.orderdish.event;

import java.util.List;

import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealGroupVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealVo;

/**
 * 通知所有的套餐分组列表
 *
 * @version: 1.0
 * @date 2015年7月10日
 */
public class EventSetmealGroupsNotice {

    /**
     * 操作时传入的UUID
     */
    public final String uuid;
    public final List<DishSetmealGroupVo> groupVoList;
    /**
     * 默认选中的明细(含必选项)，仅在初次创建套餐后才有
     */
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

    /**
     * 如果操作成功返回true
     *
     * @return
     */
    public boolean isSuccessful() {
        return groupVoList != null;
    }
}
