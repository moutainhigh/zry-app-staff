package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.yunfu.db.entity.dish.DishBrandType;

import java.util.List;

/**
 * 所有的菜品分类列表。
 */
public class DishBrandTypes {

    public final List<DishBrandType> dishTypeList;

    public DishBrandTypes(List<DishBrandType> dishTypeList) {
        this.dishTypeList = dishTypeList;
    }
}
