package com.zhongmei.beauty.operates.message;

import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeApplet;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeLimitNumCard;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;

import java.io.Serializable;
import java.util.List;



public class BeautyTradeResp implements Serializable {
    private Trade trade;
    private List<TradeItem> tradeItems;
    private List<TradeItemExtra> tradeItemExtras;
    private List<TradeCustomer> tradeCustomers;
    private List<TradePrivilege> tradePrivileges;
    private List<TradeItemProperty> tradeItemProperties;
    private List<TradeItemPlanActivity> tradeItemPlanActivities;
    private List<TradePlanActivity> tradePlanActivities;
    private List<TradeUser> tradeUsers;
    private List<TradeUser> tradeItemUsers;
    private List<TradeReasonRel> tradeReasonRels;
    private List<TradeTax> tradeTaxs;
    private List<TradeTable> tradeTables;

        private List<TradePrivilegeLimitNumCard> tradePrivilegeLimitNumCards;

    private List<TradePrivilegeApplet> tradePrivilegeApplets;

    private List<Tables> tables;

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

    public List<TradeItemExtra> getTradeItemExtras() {
        return tradeItemExtras;
    }

    public void setTradeItemExtras(List<TradeItemExtra> tradeItemExtras) {
        this.tradeItemExtras = tradeItemExtras;
    }


    public List<TradeCustomer> getTradeCustomers() {
        return tradeCustomers;
    }

    public void setTradeCustomers(List<TradeCustomer> tradeCustomers) {
        this.tradeCustomers = tradeCustomers;
    }

    public List<TradePrivilege> getTradePrivileges() {
        return tradePrivileges;
    }

    public void setTradePrivileges(List<TradePrivilege> tradePrivileges) {
        this.tradePrivileges = tradePrivileges;
    }

    public List<TradeItemProperty> getTradeItemProperties() {
        return tradeItemProperties;
    }

    public void setTradeItemProperties(List<TradeItemProperty> tradeItemProperties) {
        this.tradeItemProperties = tradeItemProperties;
    }

    public List<TradeItemPlanActivity> getTradeItemPlanActivities() {
        return tradeItemPlanActivities;
    }

    public void setTradeItemPlanActivities(List<TradeItemPlanActivity> tradeItemPlanActivities) {
        this.tradeItemPlanActivities = tradeItemPlanActivities;
    }

    public List<TradePlanActivity> getTradePlanActivities() {
        return tradePlanActivities;
    }

    public void setTradePlanActivities(List<TradePlanActivity> tradePlanActivities) {
        this.tradePlanActivities = tradePlanActivities;
    }

    public List<TradeUser> getTradeUsers() {
        return tradeUsers;
    }

    public void setTradeUsers(List<TradeUser> tradeUsers) {
        this.tradeUsers = tradeUsers;
    }

    public List<TradeReasonRel> getTradeReasonRels() {
        return tradeReasonRels;
    }

    public void setTradeReasonRels(List<TradeReasonRel> tradeReasonRels) {
        this.tradeReasonRels = tradeReasonRels;
    }

    public List<TradeTax> getTradeTaxs() {
        return tradeTaxs;
    }

    public void setTradeTaxs(List<TradeTax> tradeTaxs) {
        this.tradeTaxs = tradeTaxs;
    }

    public List<TradeUser> getTradeItemUsers() {
        return tradeItemUsers;
    }

    public void setTradeItemUsers(List<TradeUser> tradeItemUsers) {
        this.tradeItemUsers = tradeItemUsers;
    }

    public List<TradeTable> getTradeTables() {
        return tradeTables;
    }

    public void setTradeTables(List<TradeTable> tradeTables) {
        this.tradeTables = tradeTables;
    }

    public List<TradePrivilegeLimitNumCard> getTradePrivilegeLimitNumCards() {
        return tradePrivilegeLimitNumCards;
    }

    public void setTradePrivilegeLimitNumCards(List<TradePrivilegeLimitNumCard> tradePrivilegeLimitNumCards) {
        this.tradePrivilegeLimitNumCards = tradePrivilegeLimitNumCards;
    }

    public List<Tables> getTables() {
        return tables;
    }

    public void setTables(List<Tables> tables) {
        this.tables = tables;
    }

    public List<TradePrivilegeApplet> getTradePrivilegeApplets() {
        return tradePrivilegeApplets;
    }

    public void setTradePrivilegeApplets(List<TradePrivilegeApplet> tradePrivilegeApplets) {
        this.tradePrivilegeApplets = tradePrivilegeApplets;
    }
}
