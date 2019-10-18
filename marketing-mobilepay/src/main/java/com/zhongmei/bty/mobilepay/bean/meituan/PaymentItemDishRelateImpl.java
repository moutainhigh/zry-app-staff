package com.zhongmei.bty.mobilepay.bean.meituan;

import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGrouponDish;

import java.math.BigDecimal;



public class PaymentItemDishRelateImpl implements ICouponDishRelate {
    private PaymentItemGrouponDish mPaymentItemGrouponDish;

    private PaymentItemDishRelateImpl() {
    }


    public PaymentItemDishRelateImpl(PaymentItemGrouponDish paymentItemGrouponDish) {
        this.mPaymentItemGrouponDish = paymentItemGrouponDish;
    }

    @Override
    public String getTradeItemUuid() {
        if (mPaymentItemGrouponDish != null) {
            return mPaymentItemGrouponDish.getTradeItemUuid();
        }
        return null;
    }

    @Override
    public Long getDishId() {
        if (mPaymentItemGrouponDish != null) {
            return mPaymentItemGrouponDish.getDishId();
        }
        return null;
    }

    @Override
    public BigDecimal getDishNum() {
        if (mPaymentItemGrouponDish != null) {
            return mPaymentItemGrouponDish.getDishNum();
        }
        return null;
    }
}
