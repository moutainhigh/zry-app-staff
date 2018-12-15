package com.zhongmei.bty.data.operates.message.content;

import java.util.List;

import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableState;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;

/**
 * @version: 1.0
 * @date 2015年9月25日
 */
public class MergeDinnerReq {

    private Long updatorId;
    private String updatorName;
    private List<Trade> trades;
    private List<TradeTable> tradeTables;
    private List<TradeItem> tradeItems;
    private List<TradeItemProperty> tradeItemProperties;
    private List<TradePrivilege> tradePrivileges;
    private List<DinnertableState> tables;
    private List<TradeReasonRel> tradeReasonRels;
    private List<OperationRelation> tradeItemOperations;
    private List<TradeItemExtraDinner> tradeItemExtraDinners;

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public List<TradeTable> getTradeTables() {
        return tradeTables;
    }

    public void setTradeTables(List<TradeTable> tradeTables) {
        this.tradeTables = tradeTables;
    }

    public List<TradeItem> getTradeItems() {
        return tradeItems;
    }

    public void setTradeItems(List<TradeItem> tradeItems) {
        this.tradeItems = tradeItems;
    }

    public List<TradeItemProperty> getTradeItemProperties() {
        return tradeItemProperties;
    }

    public void setTradeItemProperties(List<TradeItemProperty> tradeItemProperties) {
        this.tradeItemProperties = tradeItemProperties;
    }

    public List<TradePrivilege> getTradePrivileges() {
        return tradePrivileges;
    }

    public void setTradePrivileges(List<TradePrivilege> tradePrivileges) {
        this.tradePrivileges = tradePrivileges;
    }

    public List<DinnertableState> getTables() {
        return tables;
    }

    public void setTables(List<DinnertableState> tables) {
        this.tables = tables;
    }

    public List<TradeReasonRel> getTradeReasonRels() {
        return tradeReasonRels;
    }

    public void setTradeReasonRels(List<TradeReasonRel> tradeReasonRels) {
        this.tradeReasonRels = tradeReasonRels;
    }

    public List<OperationRelation> getTradeItemOperations() {
        return tradeItemOperations;
    }

    public void setTradeItemOperations(List<OperationRelation> tradeItemOperations) {
        this.tradeItemOperations = tradeItemOperations;
    }

    public static class OperationRelation {
        private Long srcTradeItemOperationId;//原单菜品操作记录
        private String tgtTradeItemUuid;//合单时目标订单对应菜品的uuid

        public OperationRelation(Long srcTradeItemOperationId, String tgtTradeItemUuid) {
            this.srcTradeItemOperationId = srcTradeItemOperationId;
            this.tgtTradeItemUuid = tgtTradeItemUuid;
        }
    }

    public List<TradeItemExtraDinner> getTradeItemExtraDinners() {
        return tradeItemExtraDinners;
    }

    public void setTradeItemExtraDinners(List<TradeItemExtraDinner> tradeItemExtraDinners) {
        this.tradeItemExtraDinners = tradeItemExtraDinners;
    }
}
