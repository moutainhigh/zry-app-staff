package com.zhongmei.bty.dinner.shopcart.adapter;

import com.zhongmei.yunfu.db.entity.dish.DishBrandType;

import java.util.Comparator;

/**
 * Created by demo on 2018/12/15
 */

public class DishBrandTypeComparator implements Comparator<DishBrandType> {

    @Override
    public int compare(DishBrandType lhs, DishBrandType rhs) {
        if (lhs == null || rhs == null)
            return 0;
        if (lhs.getSort() > rhs.getSort()) {
            return 1;
        } else if (lhs.getSort() != null && rhs.getSort() != null && lhs.getSort().intValue() == rhs.getSort().intValue()) {
            return 0;
        }
        return -1;
    }
}
