package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;
import com.zhongmei.yunfu.db.entity.dish.DishTimeChargingRule;
import com.zhongmei.yunfu.util.Decimal;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.IssueStatus;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.util.ValueEnums;

import java.math.BigDecimal;
import java.util.Set;


public class OrderDish {

    public final DishAndStandards dish;

    private BigDecimal singleQty = BigDecimal.ZERO;

    private BigDecimal totalQty = BigDecimal.ZERO;


    private BigDecimal definePrice = null;


    private String defineName = null;

    public OrderDish(DishAndStandards dish) {
        this.dish = dish;
    }

    public OrderDish(DishShop dishShop, Set<DishProperty> standards, DishUnitDictionary unit) {
        this(new DishAndStandards(dishShop, standards, unit));
    }

    public DishShop getDishShop() {
        return dish.getDishShop();
    }

    public DishType getDishType() {
        return dish.getDishType();
    }


    public boolean isCombo() {
        return dish.isCombo();
    }


    public Bool getIsChangePrice() {
        return dish.getIsChangePrice();
    }


    public boolean isClear() {
        return dish.isClear();
    }

    public Set<DishProperty> getStandards() {
        return dish.getStandards();
    }

    public Long getBrandDishId() {
        return dish.getBrandDishId();
    }

    public Long getSkuId() {
        return dish.getSkuId();
    }

    public String getSkuUuid() {
        return dish.getSkuUuid();
    }

    public String getSkuName() {
        if (getDefineName() != null) {
            return getDefineName();
        }
        return dish.getName();
    }

    public SaleType getSaleType() {
        return dish.getSaleType();
    }

    public String getUnitName() {
        return dish.getDishShop().getUnitName();
    }

    public BigDecimal getPrice() {
        if (getDefinePrice() != null) {
            return getDefinePrice();
        }
        return dish.getPrice();
    }

    protected BigDecimal getDefinePrice() {
        return definePrice;
    }


    public void changePrice(BigDecimal price) {
        if (getIsChangePrice() == Bool.YES) {
            definePrice = price;
        }
    }

    public String getDefineName() {
        return defineName;
    }

    public void setDefineName(String defineName) {
        this.defineName = defineName;
    }

    public BigDecimal getSingleQty() {
        return singleQty;
    }

    public BigDecimal getTotalQty() {
        return totalQty;
    }

    public void setQty(BigDecimal singleQty, BigDecimal totalQty) {
        if (getSaleType() == SaleType.WEIGHING) {            this.singleQty = new BigDecimal(MathDecimal.toTrimThreeZeroString(singleQty));
            this.totalQty = new BigDecimal(MathDecimal.toTrimThreeZeroString(totalQty));
        } else {
            this.singleQty = Decimal.valueOf(singleQty);
            this.totalQty = Decimal.valueOf(totalQty);
        }

    }

    public BigDecimal getActualAmount() {
                if(ValueEnums.equalsValue(getDishShop().getSaleType(),SaleType.TIMECHARGING.value())){
            return caculTimeChargingAmount();
        }
        return mathActualAmount();
    }

    public BigDecimal mathActualAmount() {
        return totalQty == null ? BigDecimal.ZERO : MathDecimal.round(totalQty.multiply(getPrice()), 2);
    }


    private BigDecimal caculTimeChargingAmount() {
        DishTimeChargingRule rule = DishCache.getDishTimeChargingRuleHolder().getRuleByDishId(getDishShop().getId());
        if (rule == null) {
            return mathActualAmount();
        }
                return MathDecimal.round(rule.getStartChargingPrice().multiply(totalQty),2);
    }

    public String getBatchNo() {
        return null;
    }

    public IssueStatus getIssueStatus() {
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dish == null) ? 0 : dish.hashCode());
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
        OrderDish other = (OrderDish) obj;
        if (dish == null) {
            if (other.dish != null)
                return false;
        } else if (!dish.equals(other.dish))
            return false;
        return true;
    }
}
