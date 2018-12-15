package com.zhongmei.bty.basemodule.trade.processor;

import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.bty.basemodule.trade.bean.TradeGroupInfo;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.bty.basemodule.trade.entity.TradeReceiveLog;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.bty.commonmodule.database.entity.TradeItemLog;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.discount.TradePromotion;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.resp.ResponseObject;

import java.util.concurrent.Callable;

/**
 * Created by demo on 2018/12/15
 */

public class TradeRespProcessor extends OpsRequest.SaveDatabaseResponseProcessor<TradeResp> {

    private final boolean mGenSn;

    public TradeRespProcessor() {
        this(false);
    }

    TradeRespProcessor(boolean genSn) {
        this.mGenSn = genSn;
    }

    @Override
    public ResponseObject<TradeResp> process(final ResponseObject<TradeResp> response) {
        if (isSuccessful(response) && mGenSn) {
            // 生成一个新的流水号
            //PrHelper.getDefault().generateSerialNumber();
        }
        return super.process(response);
    }

    @Override
    public void saveToDatabase(TradeResp resp)
            throws Exception {
        super.saveToDatabase(resp);
        //saveBaseInfo(Customer.class, resp.getCustomers());
    }

    @Override
    protected Callable<Void> getCallable(final DatabaseHelper helper, final TradeResp resp) {
        return new Callable<Void>() {

            @Override
            public Void call()
                    throws Exception {
                saveTradeResp(helper, resp);
                return null;
            }
        };
    }

    public static void saveTradeResp(DatabaseHelper helper, TradeResp resp)
            throws Exception {
        DBHelperManager.saveEntities(helper, Trade.class, resp.getTrade());
        DBHelperManager.saveEntities(helper, TradeExtra.class, resp.getTradeExtras());
        DBHelperManager.saveEntities(helper, TradePrivilege.class, resp.getTradePrivileges());
        DBHelperManager.saveEntities(helper, TradeCustomer.class, resp.getTradeCustomers());
        DBHelperManager.saveEntities(helper, TradeTable.class, resp.getTradeTables());
        DBHelperManager.saveEntities(helper, TradeItem.class, resp.getTradeItems());
        DBHelperManager.saveEntities(helper, TradeItemProperty.class, resp.getTradeItemPropertys());
        DBHelperManager.saveEntities(helper, TradeStatusLog.class, resp.getTradeStatusLogs());
        DBHelperManager.saveEntities(helper, TradeItemLog.class, resp.getTradeItemLogs());
        DBHelperManager.saveEntities(helper, Tables.class, resp.getTables());
        DBHelperManager.saveEntities(helper, TradeReasonRel.class, resp.getTradeReasonRels());
        DBHelperManager.saveEntities(helper, DishShop.class, resp.getDishShops());
        DBHelperManager.saveEntities(helper, TradeItemOperation.class, resp.getTradeItemOperations());
        DBHelperManager.saveEntities(helper, PrintOperation.class, resp.getPrintOperations());
        DBHelperManager.saveEntities(helper, TradeDeposit.class, resp.getTradeDeposit());
        DBHelperManager.saveEntities(helper, TradePlanActivity.class, resp.getTradePlanActivitys());
        DBHelperManager.saveEntities(helper, TradeItemPlanActivity.class, resp.getTradeItemPlanActivitys());
        DBHelperManager.saveEntities(helper, TradeItemExtra.class, resp.getTradeItemExtras());
        DBHelperManager.saveEntities(helper, TradePromotion.class, resp.getTradePromotions());//add 20161117
        DBHelperManager.saveEntities(helper, Trade.class, resp.getTrades());
        DBHelperManager.saveEntities(helper, TradeReceiveLog.class, resp.getTradeReceiveLogs());
        DBHelperManager.saveEntities(helper, TradeBuffetPeople.class, resp.getTradeBuffetPeoples());
        DBHelperManager.saveEntities(helper, TradeGroupInfo.class, resp.getTradeGroup()); // v7.15 添加团餐信息表
        DBHelperManager.saveEntities(helper, TradeUser.class, resp.getTradeUser()); // v8.1 添加销售员
        DBHelperManager.saveEntities(helper, TradeItemExtraDinner.class, resp.getTradeItemExtraDinners());
        DBHelperManager.saveEntities(helper, TradeTax.class, resp.getTradeTaxs());
        DBHelperManager.saveEntities(helper, TradeInitConfig.class, resp.getTradeInitConfigs());
    }

}
