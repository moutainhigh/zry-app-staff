package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.yunfu.db.entity.dish.DishBrandType;

import java.util.List;

/**
 * 菜品分类下的所有菜品列表
 */
public class DishInfo {

    /**
     * 为null时表示是搜索结果
     */
    public final DishBrandType dishType;
    public final List<DishVo> dishList;
    public final boolean scanResult;

    public DishInfo(DishBrandType dishType, List<DishVo> dishList, boolean scanResult) {
        this.dishType = dishType;
        this.dishList = dishList;
        this.scanResult = scanResult;
    }
}
