package com.zhongmei.bty.cashier.orderdishmanager.entity;

import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertyVo;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderExtra;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;

import java.util.List;



public class BatchDishPropertyVo {

    public List<PropertyGroupVo<DishPropertyVo>> propertyGroupVoList;

    public List<OrderExtra> extraList;

}
