package com.zhongmei.bty.basemodule.discount.bean.salespromotion;

import java.io.Serializable;

public class SalesPromotionDish implements Serializable {
        private Integer type;
        private Long relateId;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getRelateId() {
        return relateId;
    }

    public void setRelateId(Long relateId) {
        this.relateId = relateId;
    }
}
