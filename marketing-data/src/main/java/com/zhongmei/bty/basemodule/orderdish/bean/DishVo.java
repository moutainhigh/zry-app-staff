package com.zhongmei.bty.basemodule.orderdish.bean;

import android.text.TextUtils;

import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.util.Checks;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DishVo {

    protected final DishAndStandards dish;

    public Map<String, DishShop> getOtherDishs() {
        return otherDishs;
    }


    private Map<String, DishShop> otherDishs;


    private BigDecimal minPrice;


    private BigDecimal maxPrice;

    private boolean containProperties;
    private boolean isSelected;
    private BigDecimal inventoryNum;

    public List<DishShop> getSelectedDishs() {
        return selectedDishs;
    }

    public void setSelectedDishs(List<DishShop> selectedDishs) {
        this.selectedDishs = selectedDishs;
    }

    private List<DishShop> selectedDishs;
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public DishVo(DishShop dishShop, DishUnitDictionary unit) {
        this(dishShop, new LinkedHashSet<DishProperty>(), unit);
    }

    public DishVo(DishShop dishShop, Set<DishProperty> standards, DishUnitDictionary unit) {
        Checks.verifyNotNull(dishShop, "dishShop");
        dish = new DishAndStandards(dishShop, standards, unit);
        otherDishs = new HashMap<String, DishShop>();
        minPrice = dishShop.getMarketPrice();
        maxPrice = minPrice;
        containProperties = false;
    }

    public DishShop getDishShop() {
        return dish.getDishShop();
    }

    public Long getBrandDishId() {
        return dish.getBrandDishId();
    }

    public String getSkuUuid() {
        return dish.getSkuUuid();
    }

    public DishUnitDictionary getUnit() {
        return dish.getUnit();
    }

    public boolean isCombo() {
        return dish.isCombo();
    }

    public boolean isServerComBoPart(){
        return dish.isServerComboPart();
    }

    public boolean isChangePrice() {
        return dish.getIsChangePrice() == Bool.YES;
    }

    public BigDecimal getInventoryNum() {
        return inventoryNum;
    }

    public void setInventoryNum(BigDecimal inventoryNum) {
        this.inventoryNum = inventoryNum;
    }


    public DishShop getLeastResidueFromOtherDishs() {
        DishShop lessDishShop = null;
        boolean first = true;

        if (otherDishs != null && otherDishs.size() > 0) {
            for (String key : otherDishs.keySet()) {
                DishShop ds = otherDishs.get(key);

                if (ds.getClearStatus() == ClearStatus.SALE) {

                    if (first) {
                        lessDishShop = ds;
                        first = false;
                    }

                    if (ds.getResidueTotal().compareTo(lessDishShop.getResidueTotal()) < 1) {
                        lessDishShop = ds;
                    }
                }
            }
        }
        return lessDishShop;
    }


    public DishShop getLeastUnweighResidueFromOtherDishs() {
        DishShop lessDishShop = null;
        boolean first = true;

        if (otherDishs != null && otherDishs.size() > 0) {
            for (String key : otherDishs.keySet()) {
                DishShop ds = otherDishs.get(key);

                if (ds.getClearStatus() == ClearStatus.SALE
                        && ds.getSaleType() == SaleType.UNWEIGHING) {

                    if (first) {
                        lessDishShop = ds;
                        first = false;
                    }

                    if (ds.getResidueTotal().compareTo(lessDishShop.getResidueTotal()) < 1) {
                        lessDishShop = ds;
                    }
                }
            }
        }
        return lessDishShop;
    }


    public boolean isClear() {
        if (dish.isClear()) {
                        for (DishShop dishShop : otherDishs.values()) {
                if (dishShop.getClearStatus() != ClearStatus.CLEAR) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public BigDecimal getPrice() {
        return getDishShop().getMarketPrice();
    }

    public String getName() {
        return getDishShop().getName();
    }

    public String getAliasName() {
        return getDishShop().getAliasName();
    }

    public String getShortName() {
        return TextUtils.isEmpty(getDishShop().getShortName()) ? getDishShop().getName() : getDishShop().getShortName();
    }

    public String getAliasShortName() {
        return TextUtils.isEmpty(getDishShop().getAliasShortName()) ? getDishShop().getAliasName() : getDishShop().getAliasShortName();
    }

    public boolean isContainProperties() {
        return containProperties;
    }

    public void setContainProperties(boolean containProperties) {
        this.containProperties = containProperties;
    }

    public Set<DishProperty> getStandards() {
        return dish.getStandards();
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void addSameSeriesDish(DishShop dishShop) {
        otherDishs.put(dishShop.getUuid(), dishShop);
    }


    public boolean isSameSeries(String skuUuid) {
        return getSkuUuid().equals(skuUuid) || otherDishs.get(skuUuid) != null;
    }

    public OrderDish toOrderDish() {
        return dish.toOrderDish();
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
        DishVo other = (DishVo) obj;
        if (dish == null) {
            if (other.dish != null)
                return false;
        } else if (!dish.equals(other.dish))
            return false;
        return true;
    }


    public boolean isHaveClear() {
        if (dish.isClear()) {
            return true;

        } else {
                        for (DishShop dishShop : otherDishs.values()) {
                if (dishShop.getClearStatus() == ClearStatus.CLEAR) {
                    return true;
                }
            }
            return false;
        }

    }


    public String getStandardName() {
        Set<DishProperty> dishProperties = getStandards();

        String standardName = "";
        if (!Utils.isEmpty(dishProperties)) {
            for (DishProperty item : dishProperties) {
                standardName += item.getName();
            }
        }
        return standardName;
    }

    public String getDishShopImgUrl(){
        return dish.getDishPic();
    }

}
