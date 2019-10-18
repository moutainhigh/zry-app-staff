package com.zhongmei.bty.data.operates.message.content;

import java.util.List;



public class ClearBillReq {
    List<Long> tradeIds;

    public List<Long> getTradeIds() {
        return tradeIds;
    }

    public void setTradeIds(List<Long> tradeIds) {
        this.tradeIds = tradeIds;
    }
}
