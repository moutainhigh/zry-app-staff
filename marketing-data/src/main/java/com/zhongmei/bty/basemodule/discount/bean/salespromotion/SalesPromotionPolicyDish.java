package com.zhongmei.bty.basemodule.discount.bean.salespromotion;

import java.io.Serializable;

public class SalesPromotionPolicyDish extends SalesPromotionDish implements Serializable {
        private Integer policyType;

    public Integer getPolicyType() {
        return policyType;
    }

    public void setPolicyType(Integer policyType) {
        this.policyType = policyType;
    }
}
