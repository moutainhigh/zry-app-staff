package com.zhongmei.bty.dinner.table.manager;

import android.support.v4.app.Fragment;

import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.entity.TaxRateInfo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.shoppingcart.utils.CreateTradeTool;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.message.uniontable.UnionTradeReq;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.yunfu.util.Beans;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.vo.DinnerConnectTablesVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class DinnerCreateUnionManager {

    public List<DinnerConnectTablesVo> emptyTablesVoList;

    public List<DinnerConnectTablesVo> occupyTablesVoList;

    private UnionTradeReq req;

    public void unionTrade(final Fragment fragment, final ResponseListener<TradeResp> listener,
                           List<DinnerConnectTablesVo> tablesVoList, final User mSaleMan) {
        initTablesVoList(tablesVoList);
        if (needBuildTradeVo()) {
            TaskContext.execute(new SimpleAsyncTask<List<TradeVo>>() {
                @Override
                public List<TradeVo> doInBackground(Void... params) {
                    return buildTradeVo();
                }

                @Override
                public void onPostExecute(List<TradeVo> tradeVoList) {
                    super.onPostExecute(tradeVoList);
                    boolean isOK = buffetOrderJudge();
                    if (isOK) {
                        createUnionTrades(mSaleMan);
                        commit(fragment, listener);
                    }
                }
            });
        } else {
            createUnionTrades(mSaleMan);
            commit(fragment, listener);
        }
    }

    protected boolean needBuildTradeVo() {
        return false;
    }

    protected List<TradeVo> buildTradeVo() {
        return null;
    }

    protected boolean buffetOrderJudge() {
        return true;
    }

    private void initTablesVoList(List<DinnerConnectTablesVo> dinnerConnectTablesVoList) {
        emptyTablesVoList = new ArrayList<>();
        occupyTablesVoList = new ArrayList<>();

        for (DinnerConnectTablesVo tablesVo : dinnerConnectTablesVoList) {
            if (Utils.isEmpty(tablesVo.tradeList) || tablesVo.tables.getTableStatus() == TableStatus.EMPTY) {
                emptyTablesVoList.add(tablesVo);
            } else {
                occupyTablesVoList.add(tablesVo);
            }
        }
    }

    protected void createUnionTrades(User mSaleMan) {
        req = new UnionTradeReq();
        req.setUnionReq(createUnionReq());
        req.setSubmitReq(createSubmitReq());
        req.setTradeUserReq(createTradeUserReq(mSaleMan));
        req.setTradeTaxs(createTradeTaxReq());
        req.setTradeInitConfigs(createTradeInitConfig());
    }

    private List<TradeTax> createTradeTaxReq() {
        //增加默认税率
        TaxRateInfo taxRateInfo = ServerSettingCache.getInstance().getmTaxRateInfo();
        if (taxRateInfo != null && taxRateInfo.isTaxSupplyOpen()) {
            TradeTax tradeTax = taxRateInfo.toTradeTax(null);
            return Arrays.asList(tradeTax);
        }
        return null;
    }

    private List<TradeInitConfig> createTradeInitConfig() {
        //加入服务费
        ExtraCharge serviceExtraCharge = ServerSettingCache.getInstance().getmServiceExtraCharge();
        if (serviceExtraCharge != null && serviceExtraCharge.isAutoJoinTrade()) {
            return Arrays.asList(serviceExtraCharge.toTradeInitConfig());
        }
        return null;
    }

    protected void commit(Fragment fragment,
                          ResponseListener<TradeResp> listener) {
        TradeOperates operates = OperatesFactory.create(TradeOperates.class);
        operates.creatUnionTrade(req, LoadingResponseListener.ensure(listener, fragment.getFragmentManager()));
    }


    private UnionTradeReq.UnionReq createUnionReq() {
        UnionTradeReq.UnionReq unionReq = new UnionTradeReq.UnionReq();
        UnionTradeReq.MainTrade mainTrade = new UnionTradeReq.MainTrade();
        mainTrade.tradeNo = SystemUtils.getBillNumber();
        if (Session.getAuthUser() != null) {
            mainTrade.creatorId = Session.getAuthUser().getId();
            mainTrade.creatorName = Session.getAuthUser().getName();
            mainTrade.updatorId = Session.getAuthUser().getId();
            mainTrade.updatorName = Session.getAuthUser().getName();
        }
        unionReq.mainTrade = mainTrade;
        List<UnionTradeReq.DinnerTrade> dinnerTradeList = new ArrayList<>();
        if (Utils.isNotEmpty(occupyTablesVoList)) {
            UnionTradeReq.DinnerTrade dinnerTrade;
            for (DinnerConnectTablesVo tablesVo : occupyTablesVoList) {
                dinnerTrade = new UnionTradeReq.DinnerTrade();
                dinnerTrade.serverUpdateTime = tablesVo.tradeList.get(0).getServerUpdateTime();
                dinnerTrade.tradeId = tablesVo.tradeList.get(0).getId();
                dinnerTradeList.add(dinnerTrade);
            }
        }
        unionReq.tradeList = dinnerTradeList;
        return unionReq;
    }

    private UnionTradeReq.SubmitReq createSubmitReq() {
        UnionTradeReq.SubmitReq submitReq = new UnionTradeReq.SubmitReq();
        List<UnionTradeReq.UnionTrade> submitList = new ArrayList<>();
        if (Utils.isNotEmpty(emptyTablesVoList)) {
            UnionTradeReq.UnionTrade unionTrade;
            for (DinnerConnectTablesVo tablesVo : emptyTablesVoList) {
                unionTrade = createUnionTrade(tablesVo);
                submitList.add(unionTrade);
            }
        }
        submitReq.submitList = submitList;
        return submitReq;
    }

    private UnionTradeReq.TradeUserReq createTradeUserReq(User user) {
        if (user == null) return null;
        UnionTradeReq.TradeUserReq tradeUserReq = new UnionTradeReq.TradeUserReq();
        tradeUserReq.userId = user.getId();
        tradeUserReq.userName = user.getName();
        tradeUserReq.userType = 9;
        return tradeUserReq;
    }

    private UnionTradeReq.UnionTrade createUnionTrade(DinnerConnectTablesVo tablesVo) {
        UnionTradeReq.UnionTrade mTrade = new UnionTradeReq.UnionTrade();
        Trade trade = CreateTradeTool.createTrade();
        try {
            Beans.copyProperties(trade, mTrade);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTrade.setBusinessType(BusinessType.DINNER);
        mTrade.setTradeType(TradeType.UNOIN_TABLE_SUB); // 交易类型 1:SELL:售货 2:REFUND:退货
        mTrade.setDeliveryType(DeliveryType.HERE);
        mTrade.setTradeNo(SystemUtils.getBillNumber());
        mTrade.setTradeAmountBefore(new BigDecimal(0));
        mTrade.setTradePeopleCount(tablesVo.tables.getTablePersonCount());
        mTrade.validateCreate();
        mTrade.tradeTable = createTradeTable(tablesVo, mTrade.getUuid());
        return mTrade;
    }

    private TradeTable createTradeTable(DinnerConnectTablesVo tablesVo, String tradeUuid) {
        TradeTable tradeTable = new TradeTable();
        tradeTable.setTableId(tablesVo.tables.getId());
        tradeTable.setTableName(tablesVo.tables.getTableName());
        tradeTable.validateCreate();
        String uuid = SystemUtils.genOnlyIdentifier();
        tradeTable.setUuid(uuid);

        tradeTable.setTablePeopleCount(tablesVo.tables.getTablePersonCount());
        tradeTable.setMemo("");

        tradeTable.isValid();

        //设置登录操作员为默认服务员
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            tradeTable.setWaiterId(user.getId());
            tradeTable.setWaiterName(user.getName());
        }
        tradeTable.setTradeUuid(tradeUuid);

        return tradeTable;
    }
}
