package com.zhongmei.bty.entity.event.orderdishevent;

import java.util.List;

import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;

/**
 * 通知某个菜品分类下的所有菜品列表。
 *
 * @version: 1.0
 * @date 2015年7月10日
 */
public class EventDishsNotice {

    /**
     * 为null时表示是搜索结果
     */
    public final DishBrandType dishType;
    public final List<DishVo> dishList;
    public final boolean scanResult;

    public EventDishsNotice(DishBrandType dishType, List<DishVo> dishList, boolean scanResult) {
        this.dishType = dishType;
        this.dishList = dishList;
        this.scanResult = scanResult;
    }
}
