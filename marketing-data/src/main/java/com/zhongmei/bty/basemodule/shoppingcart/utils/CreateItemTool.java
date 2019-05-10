package com.zhongmei.bty.basemodule.shoppingcart.utils;

import com.zhongmei.bty.basemodule.orderdish.bean.DishAndStandards;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderSetmeal;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishPropertyType;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.context.util.SystemUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CreateItemTool {

    /**
     * 通过规格创建shopcartItem
     *
     * @param dishAndStandards
     * @return
     */
    public static ShopcartItem createShopcartItem(DishAndStandards dishAndStandards) {
        OrderDish orderDish = dishAndStandards.toOrderDish();
        String uuid = SystemUtils.genOnlyIdentifier();
        ShopcartItem shopcartItem = new ShopcartItem(uuid, orderDish);
        // 获取该菜品当前选择数量
//        BigDecimal increaseUnit = MathDecimal.trimZero(dishAndStandards.getDishShop().getDishIncreaseUnit());
        BigDecimal increaseUnit = BigDecimal.ONE;
        shopcartItem.changeQty(increaseUnit);
        // 设置规格属性
        List<OrderProperty> properties = new ArrayList<OrderProperty>();
        Set<DishProperty> standards = orderDish.getStandards();
        if (standards != null) {
            for (DishProperty dishProperty : standards) {
                Long propertyTypeId = dishProperty.getPropertyTypeId();
                DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(propertyTypeId);
                properties.add(new OrderProperty(propertyType, dishProperty));
            }
        }
        shopcartItem.setProperties(properties);
        return shopcartItem;
    }

    /**
     * @Title: createShopcartItem
     * @Description: 创建购物车项
     * @Param @param dishVo
     * @Param @return TODO
     * @Return ShopcartItem 返回类型
     */
    public static ShopcartItem createShopcartItem(DishVo dishVo) {
        OrderDish orderDish = dishVo.toOrderDish();
        String uuid = SystemUtils.genOnlyIdentifier();
        ShopcartItem shopcartItem = new ShopcartItem(uuid, orderDish);
        // 获取该菜品当前选择数量
        BigDecimal increaseUnit = MathDecimal.trimZero(dishVo.getDishShop().getDishIncreaseUnit());
        if(increaseUnit.compareTo(BigDecimal.ZERO)<=0){
            increaseUnit=BigDecimal.ONE;
        }
        shopcartItem.changeQty(increaseUnit);
        // shopcartItem.setTotalQty(increaseUnit);
        // 设置规格属性
        List<OrderProperty> properties = new ArrayList<OrderProperty>();
        Set<DishProperty> standards = orderDish.getStandards();
        if (standards != null) {
            for (DishProperty dishProperty : standards) {
                Long propertyTypeId = dishProperty.getPropertyTypeId();
                DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(propertyTypeId);
                properties.add(new OrderProperty(propertyType, dishProperty));
            }
        }
        shopcartItem.setProperties(properties);
        return shopcartItem;
    }

    public static ShopcartItem createShopcartItem(Long dishId) {
        if(dishId==null || dishId.longValue()==0){
            return null;
        }
        DishShop dishShop = DishCache.getDishHolder().get(dishId);
        if (dishShop == null)
            return null;
        //modify by zhubo 2016 解决oneapm空指针
        DishVo dishVo = null;
        try {
            dishVo = createDishVo(dishShop);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        OrderDish orderDish = dishVo.toOrderDish();
        String uuid = SystemUtils.genOnlyIdentifier();
        ShopcartItem shopcartItem = new ShopcartItem(uuid, orderDish);
        // 获取该菜品当前选择数量
        BigDecimal increaseUnit = MathDecimal.trimZero(dishVo.getDishShop().getDishIncreaseUnit());
        if(increaseUnit.compareTo(BigDecimal.ZERO)<=0){
            increaseUnit=BigDecimal.ONE;
        }
        shopcartItem.changeQty(increaseUnit);
        // 设置规格属性
        List<OrderProperty> properties = new ArrayList<OrderProperty>();
        Set<DishProperty> standards = orderDish.getStandards();
        if (standards != null) {
            for (DishProperty dishProperty : standards) {
                Long propertyTypeId = dishProperty.getPropertyTypeId();
                DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(propertyTypeId);
                properties.add(new OrderProperty(propertyType, dishProperty));
            }
        }
        shopcartItem.setProperties(properties);
        return shopcartItem;
    }

    private static DishVo createDishVo(DishShop dishShop) throws Exception {
        DishUnitDictionary unit = DishCache.getUnitHolder().get(dishShop.getUnitId());
        // 没有规格的与有规格的不合成一个
        if (dishShop.getHasStandard() == Bool.YES) {
            Set<DishProperty> standards = DishManager.filterStandards(dishShop);
            return new DishVo(dishShop, standards, unit);
        } else {
            return new DishVo(dishShop, unit);
        }
    }

    public static SetmealShopcartItem createSetmealShopcartItem(ShopcartItem shopcartItem, DishSetmealVo setmealVo) {
        OrderSetmeal orderSetmeal = setmealVo.toOrderSetmeal();
        String uuid = SystemUtils.genOnlyIdentifier();
        SetmealShopcartItem setmealShopcartItem = new SetmealShopcartItem(uuid, orderSetmeal, shopcartItem);
        setmealShopcartItem.changeQty(BigDecimal.ZERO);
        setmealShopcartItem.setIsGroupDish(shopcartItem.isGroupDish());
//		setmealShopcartItem.setSingleQty(BigDecimal.ZERO);
//		setmealShopcartItem.setTotalQty(BigDecimal.ZERO);
        // 设置规格属性
        List<OrderProperty> properties = new ArrayList<OrderProperty>();
        Set<DishProperty> standards = orderSetmeal.getStandards();
        if (standards != null) {
            for (DishProperty dishProperty : standards) {
                Long propertyTypeId = dishProperty.getPropertyTypeId();
                DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(propertyTypeId);
                properties.add(new OrderProperty(propertyType, dishProperty));
            }
        }
        setmealShopcartItem.setProperties(properties);
        return setmealShopcartItem;
    }

}
