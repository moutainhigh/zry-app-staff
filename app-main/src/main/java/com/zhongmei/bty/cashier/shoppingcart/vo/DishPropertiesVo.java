package com.zhongmei.bty.cashier.shoppingcart.vo;

import java.util.List;

import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderExtra;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;

/**
 * @version: 1.0
 * @date 2015年7月15日
 */
public class DishPropertiesVo {

    /**
     * 对应用菜品，为null时表示还未选齐规格
     */
    private OrderDish orderDish;
    /**
     * 规格类属性分组列表
     */
    private List<PropertyGroupVo<DishStandardVo>> standardGroupList;
    /**
     * 非规格类属性分组列表
     */
    private List<PropertyGroupVo<DishPropertyVo>> propertyGroupList;
    /**
     * 加料列表，可能为null
     */
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

    /**
     * 获取指定序号的规格类属性
     *
     * @param groupIndex standardGroupList中的序号
     * @param index      PropertyGroupVo.propertyList中的序号
     * @return
     */
    public DishProperty getStandard(int groupIndex, int index) {
        return standardGroupList.get(groupIndex).getProperty(index);
    }

    /**
     * 获取指定序号的非规格类属性
     *
     * @param groupIndex propertyGroupList中的序号
     * @param index      PropertyGroupVo.propertyList中的序号
     * @return
     */
    public DishProperty getProperty(int groupIndex, int index) {
        return propertyGroupList.get(groupIndex).getProperty(index);
    }
}
