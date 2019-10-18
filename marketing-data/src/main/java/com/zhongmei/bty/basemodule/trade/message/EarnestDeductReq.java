package com.zhongmei.bty.basemodule.trade.message;

import java.math.BigDecimal;



public class EarnestDeductReq {
    public Long tradeId;
    public Long operateId;
    public String operateName;
    public BigDecimal exemptAmount;
}
