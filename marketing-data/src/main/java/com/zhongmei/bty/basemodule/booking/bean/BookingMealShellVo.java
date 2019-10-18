package com.zhongmei.bty.basemodule.booking.bean;

import android.text.TextUtils;

import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.orderdish.bean.DishMenuVo;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;

import java.io.Serializable;
import java.math.BigDecimal;



public class BookingMealShellVo implements Serializable, Cloneable {

    private static final long serialVersionUID = 4930642099209633498L;
    private DishMenuVo mDishMenuVo;
    private BookingTradeItem tradeItem;

    public void setBookingTradeItem(BookingTradeItem tradeItem) {
        this.tradeItem = tradeItem;
        mDishMenuVo = convertDishMeuVo(tradeItem);
    }

    private DishMenuVo convertDishMeuVo(BookingTradeItem tradeItem) {
        DishMenuVo dishMenuVo = new DishMenuVo();
        dishMenuVo.setName(tradeItem.getMemo());
        dishMenuVo.setPrice(tradeItem.getPrice());
        dishMenuVo.setSkuName(tradeItem.getDishName());
        dishMenuVo.setSkuUuid(tradeItem.getDishUuid());
        dishMenuVo.setSkuId(tradeItem.getDishId());
        return dishMenuVo;
    }

    public BookingTradeItem getTradeItem() {
        return tradeItem;
    }

    public DishMenuVo getDishMenuVo() {
        return mDishMenuVo;
    }

    public void setmDishMenuVo(DishMenuVo dishMenuVo) {
        this.mDishMenuVo = dishMenuVo;
    }


    public void setIsDefaultDishMenu(boolean isDefault) {
        if (mDishMenuVo == null) {
            return;
        }
        mDishMenuVo.setIsDefault(isDefault);
    }


    public boolean isDefaultDishMenu() {
        if (mDishMenuVo == null) {
            return false;
        }
        return mDishMenuVo.getIsDefault();
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
            tradeItem.setDishUuid(dishMenuVo.getSkuUuid());
        tradeItem.validateUpdate();
    }

    public BookingTradeItem buildHell(DishMenuVo defaultDishMenuVo, BigDecimal count) {
        return buildHell(defaultDishMenuVo, DishType.MEAL_SHELL, count);
    }

    public BookingTradeItem buildHell(DishType type) {
        return buildHell(mDishMenuVo, type);
    }


    public BookingTradeItem buildHell(DishMenuVo defaultDishMenuVo, DishType type) {
        return buildHell(defaultDishMenuVo, type, BigDecimal.ONE);
    }

    public BookingTradeItem buildHell(DishMenuVo defaultDishMenuVo, DishType type, BigDecimal count) {
        mDishMenuVo = defaultDishMenuVo;
        BookingTradeItem mTradeItem = new BookingTradeItem();
        mTradeItem.validateCreate();
        mTradeItem.validateUpdate();
        mTradeItem.setPrice(mDishMenuVo.getPrice());
        BigDecimal amount = mDishMenuVo.getPrice().multiply(count);
        mTradeItem.setAmount(amount);
        mTradeItem.setActualAmount(amount);
        mTradeItem.setDishId(mDishMenuVo.getSkuId());
        mTradeItem.setDishName(mDishMenuVo.getSkuName());
        mTradeItem.setDishUuid(mDishMenuVo.getSkuUuid());
        mTradeItem.setStatusFlag(StatusFlag.VALID);
        mTradeItem.setType(type);
        mTradeItem.setPrice(mDishMenuVo.getPrice());
        mTradeItem.setPropertyAmount(BigDecimal.ZERO);
        mTradeItem.setQuantity(count);
        mTradeItem.setUuid(SystemUtils.genOnlyIdentifier());
        this.tradeItem = mTradeItem;
        return tradeItem;
    }

    public BookingTradeItem buildHell(DishMenuVo defaultDishMenuVo, DishType type, BigDecimal count, BigDecimal totalAmount) {
        mDishMenuVo = defaultDishMenuVo;
        BookingTradeItem mTradeItem = new BookingTradeItem();
        mTradeItem.validateCreate();
        mTradeItem.validateUpdate();
        mTradeItem.setPrice(BigDecimal.ZERO);
        mTradeItem.setAmount(totalAmount);
        mTradeItem.setActualAmount(totalAmount);
        mTradeItem.setDishId(mDishMenuVo.getSkuId());
        mTradeItem.setDishName(mDishMenuVo.getSkuName());
        mTradeItem.setDishUuid(mDishMenuVo.getSkuUuid());
        mTradeItem.setStatusFlag(StatusFlag.VALID);
        mTradeItem.setType(type);
        mTradeItem.setPropertyAmount(BigDecimal.ZERO);
        mTradeItem.setQuantity(count);
        mTradeItem.setUuid(SystemUtils.genOnlyIdentifier());
        this.tradeItem = mTradeItem;
        return tradeItem;
    }

    public BookingMealShellVo clone() {
        BookingMealShellVo mealShellVo = null;
        try {
            mealShellVo = (BookingMealShellVo) super.clone();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return mealShellVo;
    }

    public boolean isChanged() {
        return mDishMenuVo != null && mDishMenuVo.isChange();
    }
}
