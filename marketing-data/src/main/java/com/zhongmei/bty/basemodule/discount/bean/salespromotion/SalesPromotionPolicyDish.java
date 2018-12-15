package com.zhongmei.bty.basemodule.discount.bean.salespromotion;

import java.io.Serializable;

public class SalesPromotionPolicyDish extends SalesPromotionDish implements Serializable {
    //策略类型，1-赠送商品，2-加价购商品
    private Integer policyType;

    public Integer getPolicyType() {
        return policyType;
    }

    public void setPolicyType(Integer policyType) {
        this.policyType = policyType;
    }
}
