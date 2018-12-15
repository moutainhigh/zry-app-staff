package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;

import java.util.List;

public class SubTradeModifyTradeItemPrintStatusReq extends ModifyTradeItemPrintStatusReq {

    private Long mainTradeId;

    private Long subTradeId;

    public SubTradeModifyTradeItemPrintStatusReq(Long mainTradeId, Long subTradeId, List<TradeItem> tis, List<PrintOperation> pos, List<TradeItemOperation> tios) {
        super(tis, pos, tios);
        this.mainTradeId = mainTradeId;
        this.subTradeId = subTradeId;
    }
}
