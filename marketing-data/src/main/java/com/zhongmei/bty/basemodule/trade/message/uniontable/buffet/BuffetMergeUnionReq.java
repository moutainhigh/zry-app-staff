package com.zhongmei.bty.basemodule.trade.message.uniontable.buffet;

import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;

import java.util.List;



public class BuffetMergeUnionReq {
    public BuffetMainTrade tradeReq;
    public List<BuffetSubTrade> subTradeReqList;
    public List<TradeItem> tradeItemReqList;
    public List<TradeItemProperty> tradeItemPropertyReqList;
    public List<TradeItemExtra> tradeItemExtraReqList;
    public List<TradePrivilege> tradePrivilegeReqList;

        public static class BuffetMainTrade {
        private Long mainTradeId;
        private String mainTradeUuid;
        private Long serverUpdateTime;

        public BuffetMainTrade(Trade trade) {
            mainTradeId = trade.getId();
            mainTradeUuid = trade.getUuid();
            serverUpdateTime = trade.getServerUpdateTime();
        }
    }

        public static class BuffetSubTrade {
        private Long subTradeId;

        public BuffetSubTrade(Trade trade) {
            subTradeId = trade.getId();
        }
    }
}
