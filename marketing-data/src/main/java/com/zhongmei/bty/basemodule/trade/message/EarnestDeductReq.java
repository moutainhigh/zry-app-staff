package com.zhongmei.bty.basemodule.trade.message;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 * 订金抵扣上行请求
 */

public class EarnestDeductReq {
    public Long tradeId;
    public Long operateId;
    public String operateName;
    public BigDecimal exemptAmount;
}
