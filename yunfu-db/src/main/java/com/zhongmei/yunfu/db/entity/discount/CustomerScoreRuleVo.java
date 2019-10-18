package com.zhongmei.yunfu.db.entity.discount;

import java.math.BigDecimal;



public class CustomerScoreRuleVo extends CustomerScoreRule {
    private BigDecimal maxUserInteger;
    public BigDecimal getMaxUserInteger() {
        return maxUserInteger;
    }

    public void setMaxUserInteger(BigDecimal maxUserInteger) {
        this.maxUserInteger = maxUserInteger;
    }
}
