package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;

import java.util.List;

public class MainTradeModifyTradeItemPrintStatusReq extends ModifyTradeItemPrintStatusReq {

    private Long mainTradeId;

    public MainTradeModifyTradeItemPrintStatusReq(Long mainTradeId, List<TradeItem> tis, List<PrintOperation> pos, List<TradeItemOperation> tios) {
        super(tis, pos, tios);
        this.mainTradeId = mainTradeId;
    }
}
