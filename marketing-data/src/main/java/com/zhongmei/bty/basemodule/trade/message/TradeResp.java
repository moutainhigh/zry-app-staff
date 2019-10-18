package com.zhongmei.bty.basemodule.trade.message;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.bty.basemodule.trade.bean.TradeGroupInfo;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRelExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeMainSubRelation;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeApplet;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeLimitNumCard;
import com.zhongmei.bty.basemodule.trade.entity.TradeReceiveLog;
import com.zhongmei.bty.commonmodule.database.entity.TradeEarnestMoney;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.bty.commonmodule.database.entity.TradeItemLog;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.discount.TradePromotion;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;

import java.io.Serializable;
import java.util.List;


public class TradeResp implements Serializable {

    public Trade trade;    private List<Trade> trades;
    private TradeExtra tradeExtra;
    private List<TradeExtra> tradeExtras;
    private List<TradePrivilege> tradePrivileges;
    private List<TradeCustomer> tradeCustomers;
    private List<TradeTable> tradeTables;
    private List<TradeItem> tradeItems;
    @SerializedName("tradeItemProperties")
    private List<TradeItemProperty> tradeItemPropertys;
    private List<Tables> tables;
    private List<TradeUser> tradeUsers;
    private List<TradeStatusLog> tradeStatusLogs;
    private List<TradeItemLog> tradeItemLogs;
    private List<TradeReasonRel> tradeReasonRels;
    private List<DishShop> dishShops;
    private List<TradeItemOperation> tradeItemOperations;
    private List<PrintOperation> printOperations;
    private List<TradeDeposit> tradeDeposit;
    private List<JsonObject> customers;
    private List<JsonObject> memberAddresses;
        @SerializedName(value = "tradePlanActivitys", alternate = "tradePlanActivities")
    private List<TradePlanActivity> tradePlanActivitys;
    @SerializedName(value = "tradeItemPlanActivitys", alternate = "tradeItemPlanActivities")
    private List<TradeItemPlanActivity> tradeItemPlanActivitys;

    private List<Long> promoIds;    private List<TradeItemExtra> tradeItemExtras;
    private List<TradeBuffetPeople> buffetPeoples;
    private List<TradeBuffetPeople> tradeBuffetPeoples;
    private List<TradeItemExtraDinner> tradeItemExtraDinners;

    private List<TradeGroupInfo> tradeGroup;      private List<TradeUser> tradeUser;    private List<TradeMainSubRelation> tradeMainSubRelations;    private List<TradeItemMainBatchRel> tradeItemMainBatchRels;    private List<TradeItemMainBatchRelExtra> tradeItemMainBatchRelExtras;

    public List<TradeReceiveLog> getTradeReceiveLogs() {
        return tradeReceiveLogs;
    }

        private List<TradeTax> tradeTaxs;     private List<TradeInitConfig> tradeInitConfigs;
    private List<TradeDeposit> tradeDeposits;    public List<TradeEarnestMoney> tradeEarnestMoneys;

        private List<TradePrivilegeLimitNumCard> tradePrivilegeLimitNumCards;
    private List<TradeUser> tradeItemUsers;
    private List<TradePrivilegeApplet> tradePrivilegeApplets;

    public void setTradeReceiveLogs(List<TradeReceiveLog> tradeReceiveLogs) {
        this.tradeReceiveLogs = tradeReceiveLogs;
    }

    public TradeExtra getTradeExtra() {
        return tradeExtra;
    }

    public void setTradeExtra(TradeExtra tradeExtra) {
        this.tradeExtra = tradeExtra;
    }

    private List<TradePromotion> tradePromotions;    private List<TradeReceiveLog> tradeReceiveLogs;
    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public List<TradeExtra> getTradeExtras() {
        return tradeExtras;
    }

    public void setTradeExtras(List<TradeExtra> tradeExtras) {
        this.tradeExtras = tradeExtras;
    }

    public List<TradePrivilege> getTradePrivileges() {
        return tradePrivileges;
    }

    public void setTradePrivileges(List<TradePrivilege> tradePrivileges) {
        this.tradePrivileges = tradePrivileges;
    }

    public List<TradeCustomer> getTradeCustomers() {
        return tradeCustomers;
    }

    public void setTradeCustomers(List<TradeCustomer> tradeCustomers) {
        this.tradeCustomers = tradeCustomers;
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

    public List<TradeItemProperty> getTradeItemPropertys() {
        return tradeItemPropertys;
    }

    public void setTradeItemPropertys(List<TradeItemProperty> tradeItemProperties) {
        this.tradeItemPropertys = tradeItemProperties;
    }

    public List<TradeStatusLog> getTradeStatusLogs() {
        return tradeStatusLogs;
    }

    public void setTradeStatusLogs(List<TradeStatusLog> tradeStatusLogs) {
        this.tradeStatusLogs = tradeStatusLogs;
    }

    public List<TradeItemLog> getTradeItemLogs() {
        return tradeItemLogs;
    }

    public void setTradeItemLogs(List<TradeItemLog> tradeItemLogs) {
        this.tradeItemLogs = tradeItemLogs;
    }

    public List<Tables> getTables() {
        return tables;
    }

    public void setTables(List<Tables> tables) {
        this.tables = tables;
    }

    public List<TradeReasonRel> getTradeReasonRels() {
        return tradeReasonRels;
    }

    public void setTradeReasonRels(List<TradeReasonRel> tradeReasonRels) {
        this.tradeReasonRels = tradeReasonRels;
    }

    public List<DishShop> getDishShops() {
        return dishShops;
    }

    public void setDishShops(List<DishShop> dishShops) {
        this.dishShops = dishShops;
    }

    public List<JsonObject> getCustomers() {
        return customers;
    }

    public void setCustomers(List<JsonObject> customers) {
        this.customers = customers;
    }

    public List<JsonObject> getMemberAddresses() {
        return memberAddresses;
    }

    public void setMemberAddresses(List<JsonObject> memberAddresses) {
        this.memberAddresses = memberAddresses;
    }

    public List<TradeItemOperation> getTradeItemOperations() {
        return tradeItemOperations;
    }

    public void setTradeItemOperations(List<TradeItemOperation> tradeItemOperations) {
        this.tradeItemOperations = tradeItemOperations;
    }

    public List<PrintOperation> getPrintOperations() {
        return printOperations;
    }

    public void setPrintOperations(List<PrintOperation> printOperations) {
        this.printOperations = printOperations;
    }

    public List<TradeDeposit> getTradeDeposit() {
        return tradeDeposit;
    }

    public void setTradeDeposit(List<TradeDeposit> tradeDeposit) {
        this.tradeDeposit = tradeDeposit;
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

    public List<Long> getPromoIds() {
        return promoIds;
    }

    public void setPromoIds(List<Long> promoIds) {
        this.promoIds = promoIds;
    }

    public List<TradeItemExtra> getTradeItemExtras() {
        return tradeItemExtras;
    }

    public void setTradeItemExtras(List<TradeItemExtra> tradeItemExtras) {
        this.tradeItemExtras = tradeItemExtras;
    }

    public List<TradePromotion> getTradePromotions() {
        return tradePromotions;
    }

    public void setTradePromotions(List<TradePromotion> tradePromotions) {
        this.tradePromotions = tradePromotions;
    }

    public List<TradeBuffetPeople> getTradeBuffetPeoples() {
        return tradeBuffetPeoples;
    }

    public void setTradeBuffetPeoples(List<TradeBuffetPeople> tradeBuffetPeoples) {
        this.tradeBuffetPeoples = tradeBuffetPeoples;
    }

    public List<TradeGroupInfo> getTradeGroup() {
        return tradeGroup;
    }

    public void setTradeGroup(List<TradeGroupInfo> tradeGroup) {
        this.tradeGroup = tradeGroup;
    }

    public List<TradeItemExtraDinner> getTradeItemExtraDinners() {
        return tradeItemExtraDinners;
    }

    public void setTradeItemExtraDinners(List<TradeItemExtraDinner> tradeItemExtraDinners) {
        this.tradeItemExtraDinners = tradeItemExtraDinners;
    }

    public List<TradeUser> getTradeUser() {
        return tradeUser;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public List<TradeMainSubRelation> getTradeMainSubRelations() {
        return tradeMainSubRelations;
    }

    public List<TradeItemMainBatchRel> getTradeItemMainBatchRels() {
        return tradeItemMainBatchRels;
    }

    public void setTradeItemMainBatchRels(List<TradeItemMainBatchRel> tradeItemMainBatchRels) {
        this.tradeItemMainBatchRels = tradeItemMainBatchRels;
    }

    public void setTradeMainSubRelations(List<TradeMainSubRelation> tradeMainSubRelations) {
        this.tradeMainSubRelations = tradeMainSubRelations;
    }

    public List<TradeItemMainBatchRelExtra> getTradeItemMainBatchRelExtras() {
        return tradeItemMainBatchRelExtras;
    }

    public void setTradeItemMainBatchRelExtras(List<TradeItemMainBatchRelExtra> tradeItemMainBatchRelExtras) {
        this.tradeItemMainBatchRelExtras = tradeItemMainBatchRelExtras;
    }



    public List<TradeTax> getTradeTaxs() {
        return tradeTaxs;
    }

    public List<TradeInitConfig> getTradeInitConfigs() {
        return tradeInitConfigs;
    }

    public void setTradeTaxs(List<TradeTax> tradeTaxs) {
        this.tradeTaxs = tradeTaxs;
    }

    public List<TradeUser> getTradeUsers() {
        return tradeUsers;
    }

    public void setTradeUsers(List<TradeUser> tradeUsers) {
        this.tradeUsers = tradeUsers;
    }

    public List<TradeDeposit> getTradeDeposits() {
        return tradeDeposits;
    }

    public List<TradeBuffetPeople> getBuffetPeoples() {
        return buffetPeoples;
    }

    public void setBuffetPeoples(List<TradeBuffetPeople> buffetPeoples) {
        this.buffetPeoples = buffetPeoples;
    }

    public void setTradeUser(List<TradeUser> tradeUser) {
        this.tradeUser = tradeUser;
    }

    public List<TradePrivilegeLimitNumCard> getTradePrivilegeLimitNumCards() {
        return tradePrivilegeLimitNumCards;
    }

    public void setTradePrivilegeLimitNumCards(List<TradePrivilegeLimitNumCard> tradePrivilegeLimitNumCards) {
        this.tradePrivilegeLimitNumCards = tradePrivilegeLimitNumCards;
    }

    public List<TradeUser> getTradeItemUsers() {
        return tradeItemUsers;
    }

    public void setTradeItemUsers(List<TradeUser> tradeItemUsers) {
        this.tradeItemUsers = tradeItemUsers;
    }

    public List<TradePrivilegeApplet> getTradePrivilegeApplets() {
        return tradePrivilegeApplets;
    }

    public void setTradePrivilegeApplets(List<TradePrivilegeApplet> tradePrivilegeApplets) {
        this.tradePrivilegeApplets = tradePrivilegeApplets;
    }
}
