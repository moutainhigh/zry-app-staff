package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.inventory.message.InventoryChangeReq;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;

import java.util.List;

/**
 * 联台主单改单请求
 * Created by demo on 2018/12/15
 */

public class TradeUnionModifyMainWarpReq {

    private TradeUnionModifyMainReq modifyRequest;
    private InventoryChangeReq inventoryRequest;

    public TradeUnionModifyMainReq getModifyRequest() {
        return modifyRequest;
    }

    public void setModifyRequest(TradeUnionModifyMainReq modifyRequest) {
        this.modifyRequest = modifyRequest;
    }

    public InventoryChangeReq getInventoryRequest() {
        return inventoryRequest;
    }

    public void setInventoryRequest(InventoryChangeReq inventoryRequest) {
        this.inventoryRequest = inventoryRequest;
    }

    public static class TradeUnionModifyMainReq {
        TradeUnionRequest mainTrade;
        private List<TradeCustomer> tradeCustomers;
        private TradeExtra tradeExtra;
        private List<TradeUnionTradeItemReq> tradeItems;
        private TradeUnionModifyRelReq tradeRelRequest;
        private List<TradeReasonRel> tradeReasonRels;
        private List<TradePrivilege> tradePrivileges;
        private TradeUser tradeUser;
        private List<TradeTax> tradeTaxs;

        public TradeExtra getTradeExtra() {
            return tradeExtra;
        }

        public void setTradeExtra(TradeExtra tradeExtra) {
            this.tradeExtra = tradeExtra;
        }

        public TradeUnionModifyRelReq getTradeRelRequest() {
            return tradeRelRequest;
        }

        public void setTradeRelRequest(TradeUnionModifyRelReq tradeRelRequest) {
            this.tradeRelRequest = tradeRelRequest;
        }

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

        public void setTradeItems(List<TradeUnionTradeItemReq> tradeItems) {
            this.tradeItems = tradeItems;
        }

        public List<TradeUnionTradeItemReq> getTradeItems() {
            return tradeItems;
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

        public TradeUser getTradeUser() {
            return tradeUser;
        }

        public void setTradeUser(TradeUser tradeUser) {
            this.tradeUser = tradeUser;
        }

        public List<TradeTax> getTradeTaxs() {
            return tradeTaxs;
        }

        public void setTradeTaxs(List<TradeTax> tradeTaxs) {
            this.tradeTaxs = tradeTaxs;
        }
    }

    public static class TradeUnionTradeItemReq {
        private TradeItem tradeItem;
        private TradeItemExtra tradeItemExtra;
        private List<TradeItemProperty> tradeItemProperties;
        private List<TradeItemOperation> tradeItemOperations;
        private List<TradeUnionTradeItemReq> subTradeItems;

        public TradeItem getTradeItem() {
            return tradeItem;
        }

        public void setTradeItem(TradeItem tradeItem) {
            this.tradeItem = tradeItem;
        }

        public TradeItemExtra getTradeItemExtra() {
            return tradeItemExtra;
        }

        public void setTradeItemExtra(TradeItemExtra tradeItemExtra) {
            this.tradeItemExtra = tradeItemExtra;
        }

        public List<TradeItemProperty> getTradeItemProperties() {
            return tradeItemProperties;
        }

        public void setTradeItemProperties(List<TradeItemProperty> tradeItemProperties) {
            this.tradeItemProperties = tradeItemProperties;
        }

        public List<TradeItemOperation> getTradeItemOperations() {
            return tradeItemOperations;
        }

        public void setTradeItemOperations(List<TradeItemOperation> tradeItemOperations) {
            this.tradeItemOperations = tradeItemOperations;
        }

        public List<TradeUnionTradeItemReq> getSubTradeItems() {
            return subTradeItems;
        }

        public void setSubTradeItems(List<TradeUnionTradeItemReq> subTradeItems) {
            this.subTradeItems = subTradeItems;
        }
    }

    public static class TradeUnionModifyRelReq {
        List<Long> addItemSubTradeIds;

        public List<Long> getAddItemSubTradeIds() {
            return addItemSubTradeIds;
        }

        public void setAddItemSubTradeIds(List<Long> addItemSubTradeIds) {
            this.addItemSubTradeIds = addItemSubTradeIds;
        }
    }
}
