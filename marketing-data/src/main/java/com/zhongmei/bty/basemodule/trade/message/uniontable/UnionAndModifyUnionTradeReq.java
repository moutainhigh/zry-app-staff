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

/**
 * 联台订单合单并改单请求数据
 * Created by demo on 2018/12/15
 */
public class UnionAndModifyUnionTradeReq {
    public TradeUnionModifyMainWarpReq.TradeUnionModifyMainReq modifyRequest;//联台主单批量菜改单请求（与主单改单一致）
    public UnionTradeModifyReq unionTradeModifyRequest;//联台主单合单加优惠改单请求
    public UnionReq unionRequest;//联台主单合单
    public InventoryChangeReq inventoryRequest;//联台主单库存（与主单改单一致）


    public final static class UnionTradeModifyReq {
        public TradeUnionRequest mainTrade;
        public TradeUser tradeUser;
        //public TradeDeposit tradeDeposit;
        //public List<TradeCustomer> tradeCustomers;
        public List<TradeTable> tradeTables;
        public List<TradeItem> tradeItems;
        public List<TradePrivilege> tradePrivileges;
        public List<TradeItemProperty> tradeItemProperties;
        public List<TradeItemLog> tradeItemLogs;
        //public List<DinnertableState> tables;
        public List<TradeCustomer> tradeCustomers;
        public List<TradeItemOperation> tradeItemOperations;
        public List<TradePlanActivity> tradePlanActivitys;
        public List<TradeItemPlanActivity> tradeItemPlanActivitys;
        public List<TradeItemExtraDinner> tradeItemExtraDinners;
        public List<TradeItemExtra> tradeItemExtras;
    }

    /**
     * 合单部分
     */
    public final static class UnionReq {
        public List<SubTrade> tradeList;
    }

    /**
     * 子单id和时间戳
     */
    public final static class SubTrade {
        public Long tradeId;
        public Long serverUpdateTime;
    }

}
