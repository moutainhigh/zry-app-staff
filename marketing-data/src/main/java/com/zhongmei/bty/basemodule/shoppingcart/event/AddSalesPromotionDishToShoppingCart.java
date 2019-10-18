package com.zhongmei.bty.basemodule.shoppingcart.event;

import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionPolicyDish;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRuleVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;

import java.math.BigDecimal;
import java.util.List;



public class AddSalesPromotionDishToShoppingCart {

    public static final int DISH_POLICY_ADD_PRICE_PURCHASE = 101;    public static final int DISH_POLICY_FREE = 102;
    private BigDecimal qty;
    private int policyType;    private SalesPromotionRuleVo salesPromotionRuleVo;

    public int getPolicyType() {
        return policyType;
    }

    public void setPolicyType(int policyType) {
        this.policyType = policyType;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public DishVo getDishVo() {
        return dishVo;
    }

    public void setDishVo(DishVo dishVo) {
        this.dishVo = dishVo;
    }

    public SalesPromotionRuleVo getSalesPromotionRuleVo() {
        return salesPromotionRuleVo;
    }

    public void setSalesPromotionRuleVo(SalesPromotionRuleVo salesPromotionRuleVo) {
        this.salesPromotionRuleVo = salesPromotionRuleVo;
    }

    private DishVo dishVo;

    public AddSalesPromotionDishToShoppingCart(SalesPromotionRuleVo salesPromotionRuleVo, DishVo dishVo, BigDecimal count, int policyType) {
        this.dishVo = dishVo;
        this.qty = count;
        this.policyType = policyType;
        this.salesPromotionRuleVo = salesPromotionRuleVo;
    }


}
