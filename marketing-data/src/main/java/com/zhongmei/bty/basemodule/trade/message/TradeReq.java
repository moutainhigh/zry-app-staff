package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.http.NationInfo;
import com.zhongmei.yunfu.http.NationInfoInterface;
import com.zhongmei.bty.basemodule.discount.entity.TradeItemActivityGift;
import com.zhongmei.bty.basemodule.discount.manager.PrivilegeApportionManager;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableState;
import com.zhongmei.bty.basemodule.trade.bean.TradeGroupInfo;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.bty.commonmodule.database.entity.TradeItemLog;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装Trade相关的实时请求的Request数据
 *
 * @Date：2015-4-14 下午6:30:02
 * @Description:
 * @Version: 1.0
 */
public class TradeReq extends Trade implements NationInfoInterface {

    /**
     * @date：2015-4-14 下午6:30:21
     * @Description:
     */
    private static final long serialVersionUID = 1L;

    private TradeExtra tradeExtra;
    private TradeDeposit tradeDeposit;
    private List<TradeCustomer> tradeCustomers;
    private List<TradeTable> tradeTables;
    private List<TradeItemReq> tradeItems;
    private List<TradePrivilege> tradePrivileges;
    private List<TradeItemProperty> tradeItemProperties;
    private List<TradeItemLog> tradeItemLogs;
    private List<DinnertableState> tables;
    private List<TradeReasonRel> tradeReasonRels;
    private List<TradeItemOperation> tradeItemOperations;
    private List<TradePlanActivity> tradePlanActivitys;
    private List<TradeItemPlanActivity> tradeItemPlanActivitys;
    private List<TradeItemActivityGift> tradeItemActivityGifts;
    private List<TradeItemExtra> tradeItemExtras;
    private List<TradeExtra> tradeExtras;
    private Integer relatedType;
    private Long relatedId;
    private List<TradeBuffetPeople> tradeBuffetPeoples;
    private TradeGroupInfo tradeGroup;
    private TradeUser tradeUser;//add 20170916 订单推销员
    private List<TradeUser> tradeUsers;  //add 20180309 增加多销售员支持
    private List<TradeItemExtraDinner> tradeItemExtraDinners;
    private List<TradeTax> tradeTaxs;
    private List<TradeInitConfig> tradeInitConfigs;
    /**
     * 商品分摊优惠结合
     */
    public List<PrivilegeApportionManager.ItemApportion> skuPrivileges;

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

    public List<TradeTable> getTradeTables() {
        return tradeTables;
    }

    public void setTradeTables(List<TradeTable> tradeTables) {
        this.tradeTables = tradeTables;
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

    public List<TradeItemLog> getTradeItemLogs() {
        return tradeItemLogs;
    }

    public void setTradeItemLogs(List<TradeItemLog> tradeItemLogs) {
        this.tradeItemLogs = tradeItemLogs;
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

    public List<TradeItemOperation> getTradeItemOperations() {
        return tradeItemOperations;
    }

    public void setTradeItemOperations(List<TradeItemOperation> tradeItemOperations) {
        this.tradeItemOperations = tradeItemOperations;
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

    public List<TradeItemActivityGift> getTradeItemActivityGifts() {
        return tradeItemActivityGifts;
    }

    public void setTradeItemActivityGifts(List<TradeItemActivityGift> tradeItemActivityGifts) {
        this.tradeItemActivityGifts = tradeItemActivityGifts;
    }

    public TradeDeposit getTradeDeposit() {
        return tradeDeposit;
    }

    public void setTradeDeposit(TradeDeposit tradeDeposit) {
        this.tradeDeposit = tradeDeposit;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedType(Integer relatedType) {
        this.relatedType = relatedType;
    }

    public int getRelatedType() {
        return relatedType;
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

    public List<TradeBuffetPeople> getTradeBuffetPeoples() {
        return tradeBuffetPeoples;
    }

    public void setTradeBuffetPeoples(List<TradeBuffetPeople> tradeBuffetPeoples) {
        this.tradeBuffetPeoples = tradeBuffetPeoples;
    }

    public TradeGroupInfo getTradeGroup() {
        return tradeGroup;
    }

    public void setTradeGroup(TradeGroupInfo tradeGroup) {
        this.tradeGroup = tradeGroup;
    }

    public void setTradeUser(TradeUser tradeUser) {
        this.tradeUser = tradeUser;
    }

    public TradeUser getTradeUser() {
        return tradeUser;
    }

    public List<TradeItemExtraDinner> getTradeItemExtraDinners() {
        return tradeItemExtraDinners;
    }

    public void setTradeItemExtraDinners(List<TradeItemExtraDinner> tradeItemExtraDinners) {
        this.tradeItemExtraDinners = tradeItemExtraDinners;
    }

    public List<TradeTax> getTradeTaxs() {
        return tradeTaxs;
    }

    public void setTradeTaxs(List<TradeTax> tradeTaxs) {
        this.tradeTaxs = tradeTaxs;
    }

    public void setTradeInitConfigs(List<TradeInitConfig> tradeInitConfigs) {
        this.tradeInitConfigs = tradeInitConfigs;
    }

    public List<TradeUser> getTradeUsers() {
        return tradeUsers;
    }

    public void setTradeUsers(List<TradeUser> tradeUsers) {
        this.tradeUsers = tradeUsers;
    }

    @Override
    public List<NationInfo> getNationInfos() {
        List<NationInfo> nationInfos = new ArrayList<>();
        TradeExtra extra = getTradeExtra();
        if (extra != null) {
            NationInfo nationInfo = new NationInfo();
            nationInfo.country = extra.getCountry();
            nationInfo.nation = extra.getNation();
            nationInfo.nationalTelCode = extra.getNationalTelCode();
            nationInfo.mobile = extra.getReceiverPhone();
            nationInfos.add(nationInfo);
        }
        return nationInfos;
    }
}
