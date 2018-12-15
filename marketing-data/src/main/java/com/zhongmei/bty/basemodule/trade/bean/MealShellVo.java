package com.zhongmei.bty.basemodule.trade.bean;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.orderdish.bean.DishMenuVo;
import com.zhongmei.yunfu.context.util.NoProGuard;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 团餐或者自助餐餐标or套餐外壳vo
 * Created by demo on 2018/12/15
 */

public class MealShellVo implements Serializable, Cloneable, NoProGuard {

    private DishMenuVo mDishMenuVo;
    private TradeItem tradeItem;

    public void setTradeItem(TradeItem tradeItem) {
        this.tradeItem = tradeItem;
        mDishMenuVo = convertDishMeuVo(tradeItem);
    }

    private DishMenuVo convertDishMeuVo(TradeItem tradeItem) {
        DishMenuVo dishMenuVo = new DishMenuVo();
        dishMenuVo.setName(tradeItem.getTradeMemo());
        dishMenuVo.setPrice(tradeItem.getPrice());
        dishMenuVo.setSkuName(tradeItem.getDishName());
        dishMenuVo.setSkuUuid(tradeItem.getSkuUuid());
        dishMenuVo.setSkuId(tradeItem.getDishId());
        return dishMenuVo;
    }

    public TradeItem getTradeItem() {
        return tradeItem;
    }

    public DishMenuVo getDishMenuVo() {
        return mDishMenuVo;
    }

    public void setmDishMenuVo(DishMenuVo dishMenuVo) {
        this.mDishMenuVo = dishMenuVo;
    }

    /**
     * 标记当前模版是否是通用模版
     *
     * @param isDefault
     */
    public void setIsDefaultDishMenu(boolean isDefault) {
        if (mDishMenuVo == null) {
            return;
        }
        mDishMenuVo.setIsDefault(isDefault);
    }

    /**
     * 当前模版是否是默认模版
     *
     * @return
     */
    public boolean isDefaultDishMenu() {
        if (mDishMenuVo == null) {
            return false;
        }
        return mDishMenuVo.getIsDefault();
    }

    /**
     * 设置和trade关联
     *
     * @param trade
     */
    public void setTradeRelate(Trade trade) {
        if (tradeItem == null) {
            return;
        }
        this.tradeItem.setTradeUuid(trade.getUuid());
        this.tradeItem.setTradeId(trade.getId());
    }

    public String getSkuName() {
        if (tradeItem != null) {
            return tradeItem.getDishName();
        }
        return "";
    }


    public BigDecimal getPrice() {
        if (tradeItem != null) {
            return tradeItem.getPrice();
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getActualAmount() {
        if (tradeItem != null && tradeItem.isValid()) {
            return tradeItem.getActualAmount();
        }
        return BigDecimal.ZERO;
    }

    public String getUuid() {
        if (tradeItem != null) {
            return tradeItem.getUuid();
        }
        return "";
    }

    public Long getId() {
        if (tradeItem != null) {
            return tradeItem.getId();
        }
        return 0L;
    }

    public void modify(DishMenuVo dishMenuVo, BigDecimal count) {
        if (tradeItem == null) {
            return;
        }
        this.mDishMenuVo = dishMenuVo;
        BigDecimal amount = count.multiply(dishMenuVo.getPrice());
        tradeItem.setAmount(amount);
        tradeItem.setActualAmount(amount);
        tradeItem.setQuantity(count);
        if (!TextUtils.isEmpty(dishMenuVo.getSkuName()))
            tradeItem.setDishName(dishMenuVo.getSkuName());
        tradeItem.setPrice(dishMenuVo.getPrice());
        if (dishMenuVo.getSkuId() != null)
            tradeItem.setDishId(dishMenuVo.getSkuId());
        if (dishMenuVo.getSkuUuid() != null)
            tradeItem.setSkuUuid(dishMenuVo.getSkuUuid());
        tradeItem.validateUpdate();
    }

    public TradeItem buildHell(DishMenuVo defaultDishMenuVo, BigDecimal count) {
        return buildHell(defaultDishMenuVo, DishType.MEAL_SHELL, count);
    }

    public TradeItem buildHell(DishType type) {
        return buildHell(mDishMenuVo, type);
    }

    /**
     * 创建餐标外壳
     *
     * @param defaultDishMenuVo
     * @return
     */
    public TradeItem buildHell(DishMenuVo defaultDishMenuVo, DishType type) {
        return buildHell(defaultDishMenuVo, type, BigDecimal.ONE);
    }

    public TradeItem buildHell(DishMenuVo defaultDishMenuVo, DishType type, BigDecimal count) {
        mDishMenuVo = defaultDishMenuVo;
        TradeItem mTradeItem = new TradeItem();
        mTradeItem.validateCreate();
        mTradeItem.validateUpdate();
        mTradeItem.setPrice(mDishMenuVo.getPrice());
        BigDecimal amount = mDishMenuVo.getPrice().multiply(count);
        mTradeItem.setAmount(amount);
        mTradeItem.setActualAmount(amount);
        mTradeItem.setDishId(mDishMenuVo.getSkuId());
        mTradeItem.setDishName(mDishMenuVo.getSkuName());
        mTradeItem.setSkuUuid(mDishMenuVo.getSkuUuid());
//        mTradeItem.setTradeMemo(mDishMenuVo.getName());
        mTradeItem.setStatusFlag(StatusFlag.VALID);
        mTradeItem.setType(type);
        mTradeItem.setPrice(mDishMenuVo.getPrice());
        mTradeItem.setPropertyAmount(BigDecimal.ZERO);
        mTradeItem.setQuantity(count);
        mTradeItem.setUuid(SystemUtils.genOnlyIdentifier());
        this.tradeItem = mTradeItem;
        return tradeItem;
    }

    public TradeItem buildHell(DishMenuVo defaultDishMenuVo, DishType type, BigDecimal count, BigDecimal totalAmount) {
        mDishMenuVo = defaultDishMenuVo;
        TradeItem mTradeItem = new TradeItem();
        mTradeItem.validateCreate();
        mTradeItem.validateUpdate();
        mTradeItem.setPrice(BigDecimal.ZERO);
        mTradeItem.setAmount(totalAmount);
        mTradeItem.setActualAmount(totalAmount);
        mTradeItem.setDishId(mDishMenuVo.getSkuId());
        mTradeItem.setDishName(mDishMenuVo.getSkuName());
        mTradeItem.setSkuUuid(mDishMenuVo.getSkuUuid());
        mTradeItem.setStatusFlag(StatusFlag.VALID);
        mTradeItem.setType(type);
        mTradeItem.setPropertyAmount(BigDecimal.ZERO);
        mTradeItem.setQuantity(count);
        mTradeItem.setUuid(SystemUtils.genOnlyIdentifier());
        this.tradeItem = mTradeItem;
        return tradeItem;
    }

    public MealShellVo clone() {
        MealShellVo mealShellVo = null;
        try {
            mealShellVo = (MealShellVo) super.clone();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return mealShellVo;
    }
}
