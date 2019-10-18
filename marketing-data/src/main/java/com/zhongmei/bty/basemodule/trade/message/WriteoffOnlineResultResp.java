package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;



public class WriteoffOnlineResultResp {
    private String outTradeNo;
    private int payStatus;
    private int tradeStatus;
    private String desc;
    private Long tradeId;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public TradeStatus getTradeStatus() {
        return ValueEnums.toEnum(TradeStatus.class, tradeStatus);
    }

    public TradePayStatus getTradePayStatus() {
        return ValueEnums.toEnum(TradePayStatus.class, payStatus);
    }

    public String getDesc() {
        return desc;
    }

    public Long getTradeId() {
        return tradeId;
    }
}
