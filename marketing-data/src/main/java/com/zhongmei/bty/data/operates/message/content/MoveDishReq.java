package com.zhongmei.bty.data.operates.message.content;

import android.util.Log;

import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.util.Beans;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;

import java.util.List;


public class MoveDishReq {

    private final static String TAG = MoveDishReq.class.getName();
        private Integer actionType;    private Integer moveAdd;
    private Source source;
    private Target target;
    private Long updatorId;    private String updatorName;
    public MoveDishReq(Source source, Target target, Long updatorId, String updatorName, Integer actionType, Integer moveAdd) {
        this.source = source;
        this.target = target;
        this.updatorId = updatorId;
        this.updatorName = updatorName;
        this.actionType = actionType;
        this.moveAdd = moveAdd;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public Integer getMoveAdd() {
        return moveAdd;
    }

    public void setMoveAdd(Integer moveAdd) {
        this.moveAdd = moveAdd;
    }

    public final static class Source {
        private Trade trade;
        private List<TradeItem> tradeItems;
        private List<TradePrivilege> tradePrivileges;
        private List<TradePlanActivity> tradePlanActivitys;
        private List<TradeItemPlanActivity> tradeItemPlanActivitys;

        public Source(Trade trade, List<TradeItem> tradeItems, List<TradePrivilege> tradePrivileges, List<TradePlanActivity> tradePlanActivitys, List<TradeItemPlanActivity> tradeItemPlanActivitys) {
            this.trade = trade;
            this.tradeItems = tradeItems;
            this.tradePrivileges = tradePrivileges;
            this.tradePlanActivitys = tradePlanActivitys;
            this.tradeItemPlanActivitys = tradeItemPlanActivitys;
        }

        public Trade getTrade() {
            return trade;
        }

        public void setTrade(Trade trade) {
            this.trade = trade;
        }

        public List<TradeItem> getTradeItems() {
            return tradeItems;
        }

        public void setTradeItems(List<TradeItem> tradeItems) {
            this.tradeItems = tradeItems;
        }

        public List<TradePlanActivity> getTradePlanActivitys() {
            return tradePlanActivitys;
        }

        public void setTradePlanActivitys(List<TradePlanActivity> tradePlanActivitys) {
            this.tradePlanActivitys = tradePlanActivitys;
        }

        public List<TradeItemPlanActivity> getTradeItemPlanActivitys() {
            return tradeItemPlanActivitys;
        }

        public void setTradeItemPlanActivitys(List<TradeItemPlanActivity> tradeItemPlanActivitys) {
            this.tradeItemPlanActivitys = tradeItemPlanActivitys;
        }
    }

    public final static class Target extends Trade {
        private List<TradePrivilege> tradePrivileges;
        private List<TradeTable> tradeTables;
        private TradeExtra tradeExtra;
        private List<TradeItemExtraDinner> tradeItemExtraDinners;
        private List<TradeTax> tradeTaxs;
        private List<TradeInitConfig> tradeInitConfigs;

        public Target(Trade trade, List<TradePrivilege> tradePrivileges,
                      List<TradeTable> tradeTables, TradeExtra tradeExtra, List<TradeItemExtraDinner> tradeItemExtraDinners) {
            try {
                Beans.copyProperties(trade, this);
            } catch (Exception e) {
                Log.i(TAG, "Copy properties error!", e);
            }
            this.tradePrivileges = tradePrivileges;
            this.tradeTables = tradeTables;
            this.tradeExtra = tradeExtra;
            this.tradeItemExtraDinners = tradeItemExtraDinners;
        }

        public void setTradeTaxs(List<TradeTax> tradeTaxs) {
            this.tradeTaxs = tradeTaxs;
        }

        public void setTradeInitConfigs(List<TradeInitConfig> tradeInitConfigs) {
            this.tradeInitConfigs = tradeInitConfigs;
        }
    }
}
