package com.zhongmei.bty.basemodule.inventory.bean;

import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class InventoryItem implements Serializable {
    private static final long serialVersionUID = 1L;
    //初始化库存数的tag
    public static final int TAG_INVENTORY_NUM = 1;
    //初始化菜品增量的tag
    public static final int TAG_STEP_NUM = 2;

    private TradeItem tradeItem;

    private BigDecimal returnInventoryNum;//实际扣库存数，默认为maxInventoryNum

    private BigDecimal maxInventoryNum;//非称重商品的作废必传，默认为菜品数量

    private BigDecimal tempReturnInventoryNum;//中间变量，用于存储未确认最终最作废量的值

    private BigDecimal stepNum;//菜品增量，默认为1

    private BigDecimal dishQuantity;//操作菜品的原数量，只有套餐才会使用，用于计算子菜的退库存数

    private boolean isGroupDish;

    private List<TradeItem> childTradeItem;//套餐子菜

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
            //在内存中拿去
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
