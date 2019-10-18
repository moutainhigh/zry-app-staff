package com.zhongmei.bty.cashier.shoppingcart.vo;

import java.util.List;

import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderExtra;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;


public class DishPropertiesVo {


    private OrderDish orderDish;

    private List<PropertyGroupVo<DishStandardVo>> standardGroupList;

    private List<PropertyGroupVo<DishPropertyVo>> propertyGroupList;

    private List<OrderExtra> extraList;

    public OrderDish getOrderDish() {
        return orderDish;
    }

    public void setOrderDish(OrderDish orderDish) {
        this.orderDish = orderDish;
    }

    public List<PropertyGroupVo<DishStandardVo>> getStandardGroupList() {
        return standardGroupList;
    }

    public void setStandardGroupList(List<PropertyGroupVo<DishStandardVo>> standardGroupList) {
        this.standardGroupList = standardGroupList;
    }

    public List<PropertyGroupVo<DishPropertyVo>> getPropertyGroupList() {
        return propertyGroupList;
    }

    public void setPropertyGroupList(List<PropertyGroupVo<DishPropertyVo>> propertyGroupList) {
        this.propertyGroupList = propertyGroupList;
    }

    public List<OrderExtra> getExtraList() {
        return extraList;
    }

    public void setExtraList(List<OrderExtra> extraList) {
        this.extraList = extraList;
    }


    public DishProperty getStandard(int groupIndex, int index) {
        return standardGroupList.get(groupIndex).getProperty(index);
    }


    public DishProperty getProperty(int groupIndex, int index) {
        return propertyGroupList.get(groupIndex).getProperty(index);
    }
}
