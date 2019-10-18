package com.zhongmei.bty.basemodule.inventory.bean;

import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;



public class InventoryItem implements Serializable {
    private static final long serialVersionUID = 1L;
        public static final int TAG_INVENTORY_NUM = 1;
        public static final int TAG_STEP_NUM = 2;

    private TradeItem tradeItem;

    private BigDecimal returnInventoryNum;
    private BigDecimal maxInventoryNum;
    private BigDecimal tempReturnInventoryNum;
    private BigDecimal stepNum;
    private BigDecimal dishQuantity;
    private boolean isGroupDish;

    private List<TradeItem> childTradeItem;
    public InventoryItem(TradeItem tradeItem) {
        this.tradeItem = tradeItem;
        initData();
    }

    public InventoryItem(TradeItem tradeItem, BigDecimal bigDecimal, int tag) {
        this.tradeItem = tradeItem;
        if (tag == TAG_INVENTORY_NUM) {
            this.maxInventoryNum = bigDecimal;
        } else if (tag == TAG_STEP_NUM) {
            this.stepNum = bigDecimal;
        }
        initData();
    }

    public InventoryItem(TradeItem tradeItem, BigDecimal maxInventoryNum, BigDecimal stepNum) {
        this.tradeItem = tradeItem;
        this.maxInventoryNum = maxInventoryNum;
        this.stepNum = stepNum;
        initData();
    }

    public TradeItem getTradeItem() {
        return tradeItem;
    }

    public BigDecimal getReturnInventoryNum() {
        return returnInventoryNum;
    }

    public BigDecimal getStepNum() {
        return stepNum;
    }

    public BigDecimal getMaxInventoryNum() {
        return maxInventoryNum;
    }

    public void setReturnInventoryNum(BigDecimal returnInventoryNum) {
        this.returnInventoryNum = returnInventoryNum;
    }

    public void setMaxInventoryNum(BigDecimal maxInventoryNum) {
        this.maxInventoryNum = maxInventoryNum;
        returnInventoryNum = this.maxInventoryNum;
    }

    public boolean isGroupDish() {
        return isGroupDish;
    }

    public void setGroupDish(boolean groupDish) {
        isGroupDish = groupDish;
    }

    public List<TradeItem> getChildTradeItem() {
        return childTradeItem;
    }

    public void setChildTradeItem(List<TradeItem> childTradeItem) {
        this.childTradeItem = childTradeItem;
    }

    public BigDecimal getTempReturnInventoryNum() {
        return tempReturnInventoryNum;
    }

    public void setTempReturnInventoryNum(BigDecimal tempReturnInventoryNum) {
        this.tempReturnInventoryNum = tempReturnInventoryNum;
    }

    public void setDishQuantity(BigDecimal dishQuantity) {
        this.dishQuantity = dishQuantity;
    }

    private void initData() {
        if (maxInventoryNum == null) {
            maxInventoryNum = tradeItem.getQuantity();
        }
        returnInventoryNum = maxInventoryNum;
        if (stepNum == null) {
                        DishShop dishShop = DishCache.getDishHolder().get(tradeItem.getSkuUuid());
            if (dishShop != null && dishShop.getStepNum() != null) {
                stepNum = dishShop.getStepNum();
            } else {
                stepNum = BigDecimal.ONE;
            }
        }
    }

    public BigDecimal getChildQuantityValue(BigDecimal childQuantity) {
        if (returnInventoryNum != null && dishQuantity != null) {
            return returnInventoryNum.multiply(childQuantity).divide(dishQuantity);
        } else if (returnInventoryNum != null && maxInventoryNum != null) {
            return returnInventoryNum.multiply(childQuantity).divide(maxInventoryNum);
        }
        return returnInventoryNum;
    }
}
