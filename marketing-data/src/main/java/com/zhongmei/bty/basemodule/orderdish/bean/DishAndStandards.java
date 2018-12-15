package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.SaleType;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @version: 1.0
 * @date 2015年8月11日
 */
public class DishAndStandards {

    private final DishShop dishShop;

    private final Set<DishProperty> standards;

    private final DishUnitDictionary unit;

    public DishAndStandards(DishShop dishShop, DishUnitDictionary unit) {
        this(dishShop, new LinkedHashSet<DishProperty>(), unit);
    }

    public DishAndStandards(DishShop dishShop, Set<DishProperty> standards, DishUnitDictionary unit) {
        this.dishShop = dishShop;
        this.standards = standards;
        this.unit = unit;
    }

    public DishShop getDishShop() {
        return dishShop;
    }

    public DishUnitDictionary getUnit() {
        return unit;
    }

    public Long getSkuId() {
        return getDishShop().getId();
    }

    public String getSkuUuid() {
        return getDishShop().getUuid();
    }

    public Long getBrandDishId() {
        return getDishShop().getBrandDishId();
    }

    public BigDecimal getPrice() {
        return dishShop.getMarketPrice();
    }

    public String getName() {
        return dishShop.getName();
    }

    public DishType getDishType() {
        return dishShop.getType();
    }

    public SaleType getSaleType() {
        return dishShop.getSaleType();
    }

    /**
     * 是否为组合套餐
     *
     * @return
     */
    public boolean isCombo() {
        return dishShop.getType() == DishType.COMBO;
    }

    /**
     * 是否可以修改单价
     *
     * @return
     */
    public Bool getIsChangePrice() {
        return dishShop.getIsChangePrice();
    }

    /**
     * 是否已估清
     *
     * @return
     */
    public boolean isClear() {
        return dishShop.getClearStatus() == ClearStatus.CLEAR;
    }

    public Set<DishProperty> getStandards() {
        return standards;
    }

    public void addStandard(DishProperty standard) {
        if (!standards.contains(standard)) {
            standards.add(standard);
        }
    }

    public boolean hasStandard(DishProperty standard) {
        return standards.contains(standard);
    }

    /**
     * 返回-1：传入规格有些不在本商品规格之列。<br/>
     * 返回0：传入规格与本商品规格完全相同。<br/>
     * 返回1：传入规格是本商品规格的子集。<br/>
     *
     * @param standardSet
     * @return
     */
    public int compareStandards(Set<DishProperty> standardSet) {
        int sizeThis = standards.size();
        int sizeParam = standardSet.size();
        if (sizeThis < sizeParam) {
            return -1;
        }
        if (sizeThis == sizeParam) {
            if (standardSet.containsAll(standards)) {
                return 0;
            }
        } else if (standards.containsAll(standardSet)) {
            return 1;
        }
        return -1;
    }

    public OrderDish toOrderDish() {
        return new OrderDish(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dishShop == null) ? 0 : dishShop.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DishAndStandards other = (DishAndStandards) obj;
        if (dishShop == null) {
            if (other.dishShop != null)
                return false;
        } else if (!dishShop.equals(other.dishShop))
            return false;
        return true;
    }

}
