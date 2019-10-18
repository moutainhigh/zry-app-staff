package com.zhongmei.bty.basemodule.trade.message.uniontable;

import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.inventory.message.InventoryChangeReq;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.bty.basemodule.trade.message.TradeUnionModifyMainWarpReq;
import com.zhongmei.bty.basemodule.trade.message.TradeUnionRequest;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.bty.commonmodule.database.entity.TradeItemLog;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;

import java.util.List;


public class UnionAndModifyUnionTradeReq {
    public TradeUnionModifyMainWarpReq.TradeUnionModifyMainReq modifyRequest;    public UnionTradeModifyReq unionTradeModifyRequest;    public UnionReq unionRequest;    public InventoryChangeReq inventoryRequest;

    public final static class UnionTradeModifyReq {
        public TradeUnionRequest mainTrade;
        public TradeUser tradeUser;
                        public List<TradeTable> tradeTables;
        public List<TradeItem> tradeItems;
        public List<TradePrivilege> tradePrivileges;
        public List<TradeItemProperty> tradeItemProperties;
        public List<TradeItemLog> tradeItemLogs;
                public List<TradeCustomer> tradeCustomers;
        public List<TradeItemOperation> tradeItemOperations;
        public List<TradePlanActivity> tradePlanActivitys;
        public List<TradeItemPlanActivity> tradeItemPlanActivitys;
        public List<TradeItemExtraDinner> tradeItemExtraDinners;
        public List<TradeItemExtra> tradeItemExtras;
    }


    public final static class UnionReq {
        public List<SubTrade> tradeList;
    }


    public final static class SubTrade {
        public Long tradeId;
        public Long serverUpdateTime;
    }

}
