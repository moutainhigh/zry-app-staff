package com.zhongmei.bty.basemodule.trade.utils;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PrepareTradeRelation;
import com.zhongmei.bty.basemodule.commonbusiness.operates.PrepareTradeRelationDal;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilegeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public class TradeManageUtils {

    private final static String TAG = TradeManageUtils.class.getSimpleName();

    public static void delete(DatabaseHelper helper, String tradeUuid)
            throws Exception {
        Dao<TradeItem, String> tiDao = helper.getDao(TradeItem.class);
        // 删除TradeItemProperty
        QueryBuilder<TradeItem, String> subQb = tiDao.queryBuilder();
        subQb.selectColumns(TradeItem.$.uuid);
        subQb.where().eq(TradeItem.$.tradeUuid, tradeUuid);
        Dao<TradeItemProperty, String> tipDao = helper.getDao(TradeItemProperty.class);
        DeleteBuilder<TradeItemProperty, String> tipDeleteBuilder = tipDao.deleteBuilder();
        tipDeleteBuilder.where().in(TradeItemProperty.$.tradeItemUuid, subQb);
        tipDeleteBuilder.delete();

        //删除TradeItemExtra

        Dao<TradeItemExtra, String> tieDao = helper.getDao(TradeItemExtra.class);
        DeleteBuilder<TradeItemExtra, String> tieDeleteBuilder = tieDao.deleteBuilder();
        tieDeleteBuilder.where().in(TradeItemExtra.$.tradeItemUuid, subQb);
        tieDeleteBuilder.delete();

        // 删除TradePrivilege
        Dao<TradePrivilege, String> tpDao = helper.getDao(TradePrivilege.class);
        DeleteBuilder<TradePrivilege, String> tpDeleteBuilder = tpDao.deleteBuilder();
        tpDeleteBuilder.where().eq(TradePrivilege.$.tradeUuid, tradeUuid);
        tpDeleteBuilder.delete();

        // 删除TradePrivilegeExtra
        Dao<TradePrivilegeExtra, String> tpExtraDao = helper.getDao(TradePrivilegeExtra.class);
        DeleteBuilder<TradePrivilegeExtra, String> tpExtraDeleteBuilder = tpExtraDao.deleteBuilder();
        tpExtraDeleteBuilder.where().eq(TradePrivilegeExtra.$.tradeUuid, tradeUuid);
        tpExtraDeleteBuilder.delete();

        // 删除TradeCustomer
        Dao<TradeCustomer, String> tcDao = helper.getDao(TradeCustomer.class);
        DeleteBuilder<TradeCustomer, String> tcDeleteBuilder = tcDao.deleteBuilder();
        tcDeleteBuilder.where().eq(TradeCustomer.$.tradeUuid, tradeUuid);
        tcDeleteBuilder.delete();
        // 删除TradeExtra
        Dao<TradeExtra, String> teDao = helper.getDao(TradeExtra.class);
        DeleteBuilder<TradeExtra, String> teDeleteBuilder = teDao.deleteBuilder();
        teDeleteBuilder.where().eq(TradeExtra.$.tradeUuid, tradeUuid);
        teDeleteBuilder.delete();
        // 删除TradeTable
        Dao<TradeTable, String> ttDao = helper.getDao(TradeTable.class);
        DeleteBuilder<TradeTable, String> ttDeleteBuilder = ttDao.deleteBuilder();
        ttDeleteBuilder.where().eq(TradeTable.$.tradeUuid, tradeUuid);
        ttDeleteBuilder.delete();
        // 删除TradeItem
        DeleteBuilder<TradeItem, String> tiDeleteBuilder = tiDao.deleteBuilder();
        tiDeleteBuilder.where().eq(TradeItem.$.tradeUuid, tradeUuid);
        tiDeleteBuilder.delete();

        // 删除TradeReasonRel
        Dao<TradeReasonRel, String> trrDao = helper.getDao(TradeReasonRel.class);
        DeleteBuilder<TradeReasonRel, String> trrDeleteBuilder = trrDao.deleteBuilder();
        trrDeleteBuilder.where().eq(TradeReasonRel.$.relateUuid, tradeUuid);
        trrDeleteBuilder.delete();

        // 删除TradeDeposit
        Dao<TradeDeposit, String> tdDao = helper.getDao(TradeDeposit.class);
        DeleteBuilder<TradeDeposit, String> tdDeleteBuilder = tdDao.deleteBuilder();
        tdDeleteBuilder.where().eq(TradeExtra.$.tradeUuid, tradeUuid);
        tdDeleteBuilder.delete();

        //删除营销活动
        Dao<TradeItemPlanActivity, String> tipaDao = helper.getDao(TradeItemPlanActivity.class);
        /*
        QueryBuilder<TradeItemPlanActivity, String> supbQb = tipaDao.queryBuilder();
        subQb.selectColumns(TradeItemPlanActivity.$.id);
        subQb.where().eq(TradeItemPlanActivity.$.tradeUuid, tradeUuid);
        */
        DeleteBuilder<TradeItemPlanActivity, String> tipaDeleteBuilder = tipaDao.deleteBuilder();
        tipaDeleteBuilder.where().eq(TradeItemPlanActivity.$.tradeUuid, tradeUuid);
        tipaDeleteBuilder.delete();

        Dao<TradePlanActivity, String> tpaDao = helper.getDao(TradePlanActivity.class);
        DeleteBuilder<TradePlanActivity, String> tpaDeleteBuilder = tpaDao.deleteBuilder();
        tpaDeleteBuilder.where().eq(TradePlanActivity.$.tradeUuid, tradeUuid);
        tpaDeleteBuilder.delete();

        // 删除Trade
        Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
        tradeDao.deleteById(tradeUuid);
    }

    /**
     * 去除预点菜订单
     *
     * @param trades 查询出的菜单
     * @return
     */
    public static List<Trade> delExtrades(List<Trade> trades) {
        synchronized (trades) {
            PrepareTradeRelationDal prepareTradeRelationDal = OperatesFactory.create(PrepareTradeRelationDal.class);
            try {
                Map<Long, PrepareTradeRelation> prepareMap = prepareTradeRelationDal.findMapByTradeId(null);
                //过滤预点菜的数据
                if (prepareMap != null && prepareMap.size() > 0 && Utils.isNotEmpty(trades)) {
                    for (int i = trades.size() - 1; i >= 0; i--) {
                        Trade trade = trades.get(i);
                        if (prepareMap.containsKey(trade.getId())) {
                            trades.remove(i);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return trades;
    }

    /**
     * 去除快餐微信未支付完成的订单(仅在线支付)
     *
     * @param trades
     * @return
     */
    public static List<Trade> delWeXinSnackTradesNotPaid(List<Trade> trades) {
        synchronized (trades) {
            Iterator<Trade> iterator = trades.iterator();
            while (iterator.hasNext()) {
                Trade trade = iterator.next();
                if (trade.getBusinessType() == BusinessType.SNACK
                        && (trade.getSourceChild() == SourceChild.MERCHANT_WEIXIN
                        || trade.getSourceChild() == SourceChild.SELF_WEIXIN)
                        && trade.getTradePayForm() == TradePayForm.ONLINE
                        && trade.getTradePayStatus() != TradePayStatus.PAID) {
                    iterator.remove();
                }
            }
        }
        return trades;
    }
}
