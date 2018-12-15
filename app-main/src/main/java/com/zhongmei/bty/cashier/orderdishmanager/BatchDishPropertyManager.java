package com.zhongmei.bty.cashier.orderdishmanager;

import android.util.Log;

import com.zhongmei.bty.cashier.orderdishmanager.entity.BatchDishPropertyVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertyVo;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderExtra;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.bty.basemodule.orderdish.entity.DishBrandProperty;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishPropertyType;
import com.zhongmei.yunfu.db.entity.dish.DishSetmeal;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.enums.PropertyKind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public class BatchDishPropertyManager {

    private final static String TAG = BatchDishPropertyManager.class.getSimpleName();

    /**
     * 获取批量菜品的公共做法、加料库（目前只支持正餐）
     *
     * @param shopcartItems
     */
    public BatchDishPropertyVo loadData(List<ShopcartItem> shopcartItems) {
        //查询所有口味做法、加料作为最初结果
        List<DishProperty> propertyResultList = DishCache.getPropertyHolder().filter(new TastePropertyFilter());
        List<DishShop> extraResultList = DishCache.getExtraHolder().filter(null);
        for (ShopcartItem shopcartItem : shopcartItems) {
            Long brandDishId = shopcartItem.getDishShop().getBrandDishId();
            DishShop dishShop = DishCache.getDishHolder().get(brandDishId);
            if (dishShop == null) {
                Log.w(TAG, "菜品已经被停用。brandDishId=" + brandDishId);
                continue;
            }

            //查询当前菜品对应的口味做法属性
            DishTastePropertyFilter filter = new DishTastePropertyFilter(dishShop);
            List<DishBrandProperty> dishBrandPropertyList = DishCache.getDishPropertyHolder().filter(filter);
            List<DishProperty> dishPropertyList = new ArrayList<>();
            if (Utils.isNotEmpty(dishBrandPropertyList)) {
                for (DishBrandProperty dishBrandProperty : dishBrandPropertyList) {
                    DishProperty dishProperty = DishCache.getPropertyHolder().get(dishBrandProperty.getPropertyId());
                    if (dishProperty.getPropertyKind() == PropertyKind.PROPERTY) {
                        dishPropertyList.add(dishProperty);
                    }
                }
            }

            //过滤结果集（与当前菜品属性求交）
            if (Utils.isNotEmpty(dishPropertyList)) {
                propertyResultList.retainAll(dishPropertyList);
            }

            //查询当前菜品对应的加料
            DishExtraFilter extraFilter = new DishExtraFilter(dishShop);
            List<DishSetmeal> dishSetmealList = DishCache.getDishExtraHolder().filter(extraFilter);//加料关联表
            Map<Long, DishShop> dishExtraMap = new HashMap<>();
            if (Utils.isNotEmpty(dishSetmealList)) {
                for (DishSetmeal dishSetmeal : dishSetmealList) {
                    if (dishExtraMap.containsKey(dishSetmeal.getChildDishId())) {
                        continue;
                    }

                    DishShop extra = DishCache.getExtraHolder().get(dishSetmeal.getChildDishId());
                    if (extra != null) {
                        Log.i(TAG, "EXTRA: " + extra.getName());
                        dishExtraMap.put(extra.getBrandDishId(), extra);
                    } else {
                        Log.w(TAG, "无此加料！ childDishId=" + dishSetmeal.getChildDishId());
                    }
                }
            }

            //过滤结果集（与当前菜品加料求交）
            if (!dishExtraMap.isEmpty()) {
                extraResultList.retainAll(dishExtraMap.values());
            }
        }

        Collections.sort(propertyResultList, newPropertyComparator());//对口味做法进行排序
        return makeBatchDishPropertyVo(propertyResultList, extraResultList);
    }

    /**
     * 构建批量操作菜品共有的做法和加料列表
     *
     * @param propertyResultList
     * @param extraResultList
     */
    private BatchDishPropertyVo makeBatchDishPropertyVo(List<DishProperty> propertyResultList, List<DishShop> extraResultList) {
        BatchDishPropertyVo batchDishPropertyVo = new BatchDishPropertyVo();
        //构建做法数据，按做法种类进行分类
        if (Utils.isNotEmpty(propertyResultList)) {
            Map<Long, PropertyGroupVo<DishPropertyVo>> propertyGroupVoMap = new HashMap<>();
            for (DishProperty property : propertyResultList) {
                Long typeId = property.getPropertyTypeId();
                PropertyGroupVo<DishPropertyVo> propertyGroupVo = propertyGroupVoMap.get(typeId);
                if (propertyGroupVo == null) {
                    DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(typeId);
                    propertyGroupVo = new PropertyGroupVo<>(propertyType, new ArrayList<DishPropertyVo>());
                    propertyGroupVoMap.put(typeId, propertyGroupVo);
                }
                propertyGroupVo.getPropertyList().add(new DishPropertyVo(property, false));
            }
            batchDishPropertyVo.propertyGroupVoList = new ArrayList<>();
            batchDishPropertyVo.propertyGroupVoList.addAll(propertyGroupVoMap.values());
            Collections.sort(batchDishPropertyVo.propertyGroupVoList, newGroupVoComparator());//对口味做法分组进行排序
        }

        //构建加料数据
        if (Utils.isNotEmpty(extraResultList)) {
            List<OrderExtra> orderExtraList = new ArrayList<>();
            for (DishShop extra : extraResultList) {
                orderExtraList.add(new OrderExtra(extra, null));
            }
            batchDishPropertyVo.extraList = orderExtraList;
            Collections.sort(batchDishPropertyVo.extraList, newExtraComparator());//对加料进行排序
        }

        return batchDishPropertyVo;
    }

    /**
     * 口味做法属性过滤器
     *
     * @version: 1.0
     * @date 2015年7月20日
     */
    private static class TastePropertyFilter implements DishCache.DataFilter<DishProperty> {

        @Override
        public boolean accept(DishProperty entity) {
            return entity.getPropertyKind() == PropertyKind.PROPERTY;
        }

    }

    /**
     * 菜品对应口味做法属性过滤器
     *
     * @version: 1.0
     * @date 2015年7月20日
     */
    private static class DishTastePropertyFilter implements DishCache.DataFilter<DishBrandProperty> {

        private DishShop dishShop;

        public DishTastePropertyFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishBrandProperty entity) {
            return entity.getPropertyKind() == PropertyKind.PROPERTY
                    && dishShop.getBrandDishId().equals(entity.getDishId());
        }

    }

    /**
     * 菜品对应配料过滤器
     *
     * @version: 1.0
     * @date 2015年7月20日
     */
    private static class DishExtraFilter implements DishCache.DataFilter<DishSetmeal> {

        private DishShop dishShop;

        public DishExtraFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishSetmeal entity) {
            return dishShop.getBrandDishId().equals(entity.getDishId());
        }

    }

    private static Comparator<DishProperty> newPropertyComparator() {
        return new Comparator<DishProperty>() {

            @Override
            public int compare(DishProperty lhs, DishProperty rhs) {
                if (lhs.getSort() == null) {
                    if (rhs.getSort() != null) {
                        return 1;
                    }
                } else if (rhs.getSort() == null) {
                    return -1;
                }
                int v = lhs.getSort().compareTo(rhs.getSort());
                if (v == 0) {
                    v = lhs.getId().compareTo(rhs.getId());
                }
                return v;
            }

        };
    }

    static Comparator<PropertyGroupVo<?>> newGroupVoComparator() {
        return new Comparator<PropertyGroupVo<?>>() {

            @Override
            public int compare(PropertyGroupVo<?> lhs, PropertyGroupVo<?> rhs) {
                if (lhs.getPropertyType() == null) {
                    if (rhs.getPropertyType() != null) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else if (rhs.getPropertyType() == null) {
                    return -1;
                }
                Integer sort1 = lhs.getPropertyType().getSort();
                Integer sort2 = rhs.getPropertyType().getSort();
                if (sort1 == null) {
                    if (sort2 != null) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else if (sort2 == null) {
                    return -1;
                }
                int v = sort1.compareTo(sort2);
                if (v == 0) {
                    v = lhs.getPropertyType().getId().compareTo(rhs.getPropertyType().getId());
                }
                return v;
            }

        };
    }

    private static Comparator<OrderExtra> newExtraComparator() {
        return new Comparator<OrderExtra>() {

            @Override
            public int compare(OrderExtra lhs, OrderExtra rhs) {
                if (lhs.getDishShop().getSort() == null) {
                    if (rhs.getDishShop().getSort() != null) {
                        return 1;
                    }
                } else if (rhs.getDishShop().getSort() == null) {
                    return -1;
                }
                int v = lhs.getDishShop().getSort().compareTo(rhs.getDishShop().getSort());
                if (v == 0) {
                    v = lhs.getSetmealId().compareTo(rhs.getSetmealId());
                }
                return v;
            }

        };
    }

}
