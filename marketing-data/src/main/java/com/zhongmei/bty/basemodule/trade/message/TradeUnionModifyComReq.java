package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;

import java.util.List;



public class TradeUnionModifyComReq {

    TradeUnionRequest mainTrade;
    private List<TradeCustomer> tradeCustomers;
    private List<TradeItemExtra> tradeItemExtras;
    private List<TradeItemProperty> tradeItemProperties;
    private List<TradeItem> tradeItems;
    private List<TradeReasonRel> tradeReasonRels;
    private List<TradePrivilege> tradePrivileges;
    private List<TradeItemMainBatchRel> tradeItemMainBatchRels;
    private List<TradeItemOperation> tradeItemOperations;
    private TradeUser tradeUser;
    private List<TradeTable> tradeTables;

    public TradeUnionRequest getMainTrade() {
        return mainTrade;
    }

    public void setMainTrade(TradeUnionRequest mainTrade) {
        this.mainTrade = mainTrade;
    }

    public List<TradeCustomer> getTradeCustomers() {
        return tradeCustomers;
    }

    public void setTradeCustomers(List<TradeCustomer> tradeCustomers) {
        this.tradeCustomers = tradeCustomers;
    }

    public List<TradeItemExtra> getTradeItemExtras() {
        return tradeItemExtras;
    }

    public void setTradeItemExtras(List<TradeItemExtra> tradeItemExtras) {
        this.tradeItemExtras = tradeItemExtras;
    }

    public List<TradeItemProperty> getTradeItemProperties() {
        return tradeItemProperties;
    }

    public void setTradeItemProperties(List<TradeItemProperty> tradeItemProperties) {
        this.tradeItemProperties = tradeItemProperties;
    }

    public List<TradeItem> getTradeItems() {
        return tradeItems;
    }

    public void setTradeItems(List<TradeItem> tradeItems) {
        this.tradeItems = tradeItems;
    }

    public List<TradeReasonRel> getTradeReasonRels() {
        return tradeReasonRels;
    }

    public void setTradeReasonRels(List<TradeReasonRel> tradeReasonRels) {
        this.tradeReasonRels = tradeReasonRels;
    }

    public List<TradePrivilege> getTradePrivileges() {
        return tradePrivileges;
    }

    public void setTradePrivileges(List<TradePrivilege> tradePrivileges) {
        this.tradePrivileges = tradePrivileges;
    }

    public List<TradeItemMainBatchRel> getTradeItemMainBatchRels() {
        return tradeItemMainBatchRels;
    }

    public void setTradeItemMainBatchRels(List<TradeItemMainBatchRel> tradeItemMainBatchRels) {
        this.tradeItemMainBatchRels = tradeItemMainBatchRels;
    }

    public List<TradeItemOperation> getTradeItemOperations() {
        return tradeItemOperations;
    }

    public void setTradeItemOperations(List<TradeItemOperation> tradeItemOperations) {
        this.tradeItemOperations = tradeItemOperations;
    }

    public TradeUser getTradeUser() {
        return tradeUser;
    }

    public void setTradeUser(TradeUser tradeUser) {
        this.tradeUser = tradeUser;
    }

    public List<TradeTable> getTradeTables() {
        return tradeTables;
    }

    public void setTradeTables(List<TradeTable> tradeTables) {
        this.tradeTables = tradeTables;
    }
}
