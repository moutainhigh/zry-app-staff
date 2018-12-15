package com.zhongmei.bty.cashier.orderdishmanager.entity;

import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertyVo;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderExtra;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;

import java.util.List;

/**
 * 批量菜品公共口味做法加料等
 * Created by demo on 2018/12/15
 */

public class BatchDishPropertyVo {

    public List<PropertyGroupVo<DishPropertyVo>> propertyGroupVoList;

    public List<OrderExtra> extraList;

}
