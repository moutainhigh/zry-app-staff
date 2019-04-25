package com.zhongmei.yunfu.db.entity.discount;

import java.math.BigDecimal;

/**
 * Created by dingzb on 2019/3/14.
 */

public class CustomerScoreRuleVo extends CustomerScoreRule {
    private BigDecimal maxUserInteger; //最高抵用的积分数量

    public BigDecimal getMaxUserInteger() {
        return maxUserInteger;
    }

    public void setMaxUserInteger(BigDecimal maxUserInteger) {
        this.maxUserInteger = maxUserInteger;
    }
}
