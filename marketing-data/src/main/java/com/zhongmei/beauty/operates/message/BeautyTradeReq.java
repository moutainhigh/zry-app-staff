package com.zhongmei.beauty.operates.message;

import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeApplet;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeLimitNumCard;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.bty.basemodule.trade.message.TradeItemReq;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;

import java.util.List;



public class BeautyTradeReq extends Trade {

    private static final long serialVersionUID = 1L;

    private TradeExtra tradeExtra;
    private List<TradeCustomer> tradeCustomers;
    private List<TradeItemReq> tradeItems;
    private List<TradePrivilege> tradePrivileges;
    private List<TradeItemProperty> tradeItemProperties;
    private List<TradeReasonRel> tradeReasonRels;
    private List<TradeItemOperation> tradeItemOperations;
    private List<TradePlanActivity> tradePlanActivities;
    private List<TradeItemPlanActivity> tradeItemPlanActivities;
    private List<TradeItemExtra> tradeItemExtras;
    private List<TradeExtra> tradeExtras;
    private List<TradeUser> tradeUsers;      private List<TradeTax> tradeTaxs;
    private List<TradeTable> tradeTables;

        private List<TradePrivilegeLimitNumCard> tradePrivilegeLimitNumCards;

    private List<TradePrivilegeApplet> tradePrivilegeApplets;

    public List<TradePrivilegeApplet> getTradePrivilegeApplets() {
        return tradePrivilegeApplets;
    }

    public void setTradePrivilegeApplets(List<TradePrivilegeApplet> tradePrivilegeApplets) {
        this.tradePrivilegeApplets = tradePrivilegeApplets;
    }

    public TradeExtra getTradeExtra() {
        return tradeExtra;
    }

    public void setTradeExtra(TradeExtra tradeExtra) {
        this.tradeExtra = tradeExtra;
    }

    public List<TradeCustomer> getTradeCustomers() {
        return tradeCustomers;
    }

    public void setTradeCustomers(List<TradeCustomer> tradeCustomers) {
        this.tradeCustomers = tradeCustomers;
    }

    public List<TradeItemReq> getTradeItems() {
        return tradeItems;
    }

    public void setTradeItems(List<TradeItemReq> tradeItems) {
        this.tradeItems = tradeItems;
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

    public List<TradeReasonRel> getTradeReasonRels() {
        return tradeReasonRels;
    }

    public void setTradeReasonRels(List<TradeReasonRel> tradeReasonRels) {
        this.tradeReasonRels = tradeReasonRels;
    }

    public List<TradeItemOperation> getTradeItemOperations() {
        return tradeItemOperations;
    }

    public void setTradeItemOperations(List<TradeItemOperation> tradeItemOperations) {
        this.tradeItemOperations = tradeItemOperations;
    }

    public List<TradePlanActivity> getTradePlanActivities() {
        return tradePlanActivities;
    }

    public void setTradePlanActivities(List<TradePlanActivity> tradePlanActivities) {
        this.tradePlanActivities = tradePlanActivities;
    }

    public List<TradeItemPlanActivity> getTradeItemPlanActivities() {
        return tradeItemPlanActivities;
    }

    public void setTradeItemPlanActivities(List<TradeItemPlanActivity> tradeItemPlanActivities) {
        this.tradeItemPlanActivities = tradeItemPlanActivities;
    }

    public List<TradeItemExtra> getTradeItemExtras() {
        return tradeItemExtras;
    }

    public void setTradeItemExtras(List<TradeItemExtra> tradeItemExtras) {
        this.tradeItemExtras = tradeItemExtras;
    }

    public List<TradeExtra> getTradeExtras() {
        return tradeExtras;
    }

    public void setTradeExtras(List<TradeExtra> tradeExtras) {
        this.tradeExtras = tradeExtras;
    }

    public List<TradeUser> getTradeUsers() {
        return tradeUsers;
    }

    public void setTradeUsers(List<TradeUser> tradeUsers) {
        this.tradeUsers = tradeUsers;
    }

    public List<TradeTax> getTradeTaxs() {
        return tradeTaxs;
    }

    public void setTradeTaxs(List<TradeTax> tradeTaxs) {
        this.tradeTaxs = tradeTaxs;
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
}
