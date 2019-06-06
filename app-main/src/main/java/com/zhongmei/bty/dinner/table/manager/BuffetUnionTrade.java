package com.zhongmei.bty.dinner.table.manager;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.discount.bean.IntegralCashPrivilegeVo;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.message.uniontable.BuffetUnionTradeCancelReq;
import com.zhongmei.bty.basemodule.trade.message.uniontable.BuffetUnionTradeCancelResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.buffet.table.view.BuffetUnionCancelVo;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.vo.DinnerConnectTablesVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BuffetUnionTrade extends IUnionTrade {

    @Override
    public void create(List<DinnerConnectTablesVo> tablesVoList, UnionListener listener) {

    }

    @Override
    public void cancel(final List<DinnerConnectTablesVo> tablesVoList, final BuffetUnionCancelVo unionCancelVo, final UnionListener listener) {
        final BuffetSplitUnionManager splitTool = new BuffetSplitUnionManager();
        new AsyncTask<Void, Void, BuffetUnionTradeCancelReq>() {
            @Override
            protected BuffetUnionTradeCancelReq doInBackground(Void... params) {
                return splitTool.createUnionTradeSplitReq(tablesVoList);
            }

            protected void onPostExecute(BuffetUnionTradeCancelReq req) {
                if (req == null) {
                    String errorMsg = tablesVoList.get(0).areaName
                            + BaseApplication.getInstance().getString(R.string.dinner_cancel_union_trade_tables)
                            + tablesVoList.get(0).tables.getTableName()
                            + BaseApplication.getInstance().getString(R.string.dinner_cancel_union_trade_limit_three);
                    setResponse(listener, new Exception(errorMsg), null);
                    return;
                }

                /*TradeVo mainTradeVo = splitTool.getMainTradeVo();
                //判断押金是否支持
                if (businessType == BusinessType.BUFFET && mainTradeVo.isNeedToPayDeposit() && mainTradeVo.isPaidTradeposit()) {
                    setResponse(listener, new Exception("联台单已经交押金，不能取消联台"), null);
                    return;
                }*/

                createDepositPeople(splitTool.getMainTradeVo(), splitTool.getSubTradeVo(), unionCancelVo);
                //checkBuffetUnionTradeMainVail(splitTool);
                BuffetUnionTradeCancelReq buffetUnionTradeCancelReq = splitTool.createBuffetUnionTradeCancelReq(splitTool.getMainTradeVo(), splitTool.getSubTradeVo(), false);
                setResult(buffetUnionTradeCancelReq, listener);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void checkBuffetUnionTradeMainVail(BuffetSplitUnionManager splitTool) {
        //如果是最后一个子单那么设置主单为无效
        if (splitTool.getTradeMainSubRelationList().size() == 1) {
            TradeVo mainTradeVo = splitTool.getMainTradeVo();
            mainTradeVo.getTrade().setStatusFlag(StatusFlag.INVALID);
            mainTradeVo.getTrade().validateUpdate();
        }
    }

    private void createDepositPeople(TradeVo mainTradeVo, TradeVo subTradeVo, BuffetUnionCancelVo unionCancelVo) {
        if (unionCancelVo == null) {
            return;
        }

        createTradeDeposit(subTradeVo, unionCancelVo.buffetDeposit);

        //设置人数为无效
        List<TradeBuffetPeople> tradeBuffetPeopleList = subTradeVo.getTradeBuffetPeoples();
        if (tradeBuffetPeopleList == null) {
            tradeBuffetPeopleList = new ArrayList<>();
            subTradeVo.setTradeBuffetPeoples(tradeBuffetPeopleList);
        } else {
            for (TradeBuffetPeople people : tradeBuffetPeopleList) {
                if (people.getStatusFlag() == StatusFlag.VALID) {
                    people.setStatusFlag(StatusFlag.INVALID);
                    people.validateUpdate();
                }
            }
        }

        List<TradeBuffetPeople> tradeBuffetPeoples = createTradeBuffetPeople(subTradeVo, unionCancelVo.buffetPeople);
        tradeBuffetPeopleList.addAll(tradeBuffetPeoples);

        //修改子单桌台人数
        List<TradeTable> tradeTableList = subTradeVo.getTradeTableList();
        if (tradeTableList != null) {
            for (TradeTable tradeTable : tradeTableList) {
                if (tradeTable.isValid()) {
                    tradeTable.setTablePeopleCount(unionCancelVo.getPeopleCount().intValue());
                    tradeTable.validateUpdate();
                }
            }
        }

        //复制主单消费税到子单
        copyMainTradeTaxToSubTradeTax(mainTradeVo, subTradeVo);
        copyMainTradeInitConfigToSubTradeInitConfig(mainTradeVo, subTradeVo);

        deductionMainTrade(mainTradeVo, subTradeVo, unionCancelVo);
    }

    /**
     * 复制主单消费税到子单
     *
     * @param mainTradeVo
     * @param subTradeVo
     */
    public static void copyMainTradeTaxToSubTradeTax(TradeVo mainTradeVo, TradeVo subTradeVo) {
        List<TradeTax> mainTradeTaxs = mainTradeVo.getTradeTaxs();
        if (Utils.isEmpty(mainTradeTaxs)) {
            return;
        }

        //复制当前主单税费信息
        List<TradeTax> tradeTaxListCopy = new ArrayList<>();
        for (TradeTax mainTradeTax : mainTradeTaxs) {
            TradeTax tradeTaxCopy = new TradeTax();
            tradeTaxCopy.validateCreate();
            tradeTaxCopy.setTradeId(subTradeVo.getTrade().getId());
            tradeTaxCopy.setUuid(SystemUtils.genOnlyIdentifier());
            tradeTaxCopy.setTaxType(mainTradeTax.getTaxType());
            tradeTaxCopy.setTaxPlan(mainTradeTax.getTaxPlan());
            tradeTaxCopy.setTaxTypeName(mainTradeTax.getTaxTypeName());
            tradeTaxCopy.setTaxAmount(BigDecimal.ZERO);
            tradeTaxCopy.setTaxableIncome(BigDecimal.ZERO);
            tradeTaxCopy.setEffectType(mainTradeTax.getEffectType());
            tradeTaxCopy.setDiscountType(mainTradeTax.getDiscountType());
            tradeTaxCopy.setTaxKind(mainTradeTax.getTaxKind());
            tradeTaxCopy.setTaxMethod(mainTradeTax.getTaxMethod());
            tradeTaxListCopy.add(tradeTaxCopy);
        }

        List<TradeTax> subTradeTaxs = subTradeVo.getTradeTaxs();
        if (Utils.isEmpty(subTradeTaxs)) {
            subTradeVo.setTradeTaxs(tradeTaxListCopy);
        } else {
            //设置当前税费为无效
            for (TradeTax tradeTax : subTradeTaxs) {
                if (tradeTax.isValid()) {
                    tradeTax.setStatusFlag(StatusFlag.INVALID);
                    tradeTax.validateUpdate();
                }
            }
            subTradeTaxs.addAll(tradeTaxListCopy);
        }
    }

    /**
     * 复制主单服务费信息到子单
     *
     * @param mainTradeVo
     * @param subTradeVo
     */
    public static void copyMainTradeInitConfigToSubTradeInitConfig(TradeVo mainTradeVo, TradeVo subTradeVo) {
        List<TradeInitConfig> mainTradeInitCfgs = mainTradeVo.getTradeInitConfigs();
        if (Utils.isEmpty(mainTradeInitCfgs)) {
            return;
        }

        //复制当前主单税费信息
        List<TradeInitConfig> tradeTaxListCopy = new ArrayList<>();
        for (TradeInitConfig mainTradeInitCfg : mainTradeInitCfgs) {
            TradeInitConfig tradeInitCfgCopy = new TradeInitConfig();
            tradeInitCfgCopy.validateCreate();
            tradeInitCfgCopy.setTradeId(subTradeVo.getTrade().getId());
            tradeInitCfgCopy.setKeyId(mainTradeInitCfg.getKeyId());
            tradeInitCfgCopy.setValue(mainTradeInitCfg.getValue());
            tradeInitCfgCopy.setShopIdenty(tradeInitCfgCopy.getShopIdenty());
            tradeTaxListCopy.add(tradeInitCfgCopy);
        }

        List<TradeInitConfig> subTradeTaxs = subTradeVo.getTradeInitConfigs();
        if (Utils.isEmpty(subTradeTaxs)) {
            subTradeVo.setTradeInitConfigs(tradeTaxListCopy);
        } else {
            //设置当前税费为无效
            for (TradeInitConfig tradeInitCfg : subTradeTaxs) {
                if (tradeInitCfg.isValid()) {
                    tradeInitCfg.setStatusFlag(StatusFlag.INVALID);
                    tradeInitCfg.validateUpdate();
                }
            }
            subTradeTaxs.addAll(tradeTaxListCopy);
        }
    }

    /**
     * 主单扣减
     *
     * @param mainTradeVo
     * @param subTradeVo
     * @param unionCancelVo
     */
    private void deductionMainTrade(TradeVo mainTradeVo, TradeVo subTradeVo, BuffetUnionCancelVo unionCancelVo) {
        //去除子单会员价
        IntegralCashPrivilegeVo integralCashPrivilegeVo = subTradeVo.getIntegralCashPrivilegeVo();
        if (integralCashPrivilegeVo != null) {
            TradePrivilege tradePrivilege = integralCashPrivilegeVo.getTradePrivilege();
            if (tradePrivilege != null
                    && (tradePrivilege.getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT || tradePrivilege.getPrivilegeType() == PrivilegeType.MEMBER_PRICE || tradePrivilege.getPrivilegeType() == PrivilegeType.MEMBER_REBATE)) {
                if (tradePrivilege.getStatusFlag() == StatusFlag.VALID) {
                    tradePrivilege.setStatusFlag(StatusFlag.INVALID);
                    tradePrivilege.validateUpdate();
                }
            }
        }

        //主单押金减去子单押金
        TradeDeposit mainTradeDeposit = mainTradeVo.getTradeDeposit();
        if (mainTradeDeposit != null && subTradeVo.getTradeDeposit() != null) {
            //mainTradeDeposit.setType(tradeDeposit.getType());
            mainTradeDeposit.setDepositPay(mainTradeDeposit.getDepositPay().subtract(subTradeVo.getTradeDeposit().getDepositPay()));
            mainTradeDeposit.setChanged(true);
        }

        //主单餐标人数减去拆出去的订单
        List<TradeBuffetPeople> mainTradeBuffetPeopleList = mainTradeVo.getTradeBuffetPeoples();
        if (mainTradeBuffetPeopleList != null) {
            for (TradeBuffetPeople it : mainTradeBuffetPeopleList) {
                if (unionCancelVo.buffetPeople != null && it.getStatusFlag() == StatusFlag.VALID) {
                    BuffetUnionCancelVo.BuffetPeople bean = getBuffetPeople(unionCancelVo, it.getCarteNormsId());
                    if (bean != null) {
                        it.setPeopleCount(it.getPeopleCount().subtract(bean.peopleCount));
                        it.validateUpdate();
                    }
                }
            }
        }
    }

    public static BuffetUnionCancelVo.BuffetPeople getBuffetPeople(BuffetUnionCancelVo unionCancelVo, Long carteNormsId) {
        for (BuffetUnionCancelVo.BuffetPeople people : unionCancelVo.buffetPeople) {
            if (Utils.equal(people.carteNormsId, carteNormsId)) {
                return people;
            }
        }
        return null;
    }

    private List<TradeBuffetPeople> createTradeBuffetPeople(TradeVo tradeVo, List<BuffetUnionCancelVo.BuffetPeople> buffetPeople) {
        List<TradeBuffetPeople> tradeBuffetPeoples = new ArrayList<>();
        if (buffetPeople != null) {
            for (BuffetUnionCancelVo.BuffetPeople people : buffetPeople) {
                TradeBuffetPeople tradeBuffetPeople = new TradeBuffetPeople();
                tradeBuffetPeople.validateCreate();
                tradeBuffetPeople.setTradeId(tradeVo.getTrade().getId());
                tradeBuffetPeople.setTradeUuid(tradeVo.getTrade().getUuid());
                tradeBuffetPeople.setPeopleCount(people.peopleCount);
                tradeBuffetPeople.setCarteNormsId(people.carteNormsId);
                tradeBuffetPeople.setCarteNormsName(people.carteNormsName);
                tradeBuffetPeople.setCartePrice(people.cartePrice);
                tradeBuffetPeoples.add(tradeBuffetPeople);
            }
        }
        return tradeBuffetPeoples;
    }

    private void createTradeDeposit(TradeVo tradeVo, BuffetUnionCancelVo.BuffetDeposit deposit) {
        if (deposit != null) {
            TradeDeposit tradeDeposit = new TradeDeposit();
            tradeDeposit.validateCreate();
            tradeDeposit.setUuid(SystemUtils.genOnlyIdentifier());
            tradeDeposit.setTradeId(tradeVo.getTrade().getId());
            tradeDeposit.setTradeUuid(tradeVo.getTrade().getUuid());
            tradeDeposit.setType(deposit.depositType);
            tradeDeposit.setUnitPrice(deposit.unitPrice);
            tradeDeposit.setDepositPay(deposit.depositPay);
            tradeVo.setTradeDeposit(tradeDeposit);
        }
    }

    private void setResult(BuffetUnionTradeCancelReq req, final UnionListener listener) {
        Fragment fragment = mWeakReference.get();
        if (fragment != null) {
            TradeOperates operates = OperatesFactory.create(TradeOperates.class);
            operates.buffetSplitTrade(req, LoadingResponseListener.ensure(newResponseListener(listener), fragment.getChildFragmentManager()));
        }
    }

    private ResponseListener<BuffetUnionTradeCancelResp> newResponseListener(final UnionListener listener) {
        return new ResponseListener<BuffetUnionTradeCancelResp>() {
            @Override
            public void onResponse(ResponseObject<BuffetUnionTradeCancelResp> response) {
                setResponse(listener, null, response);
            }

            @Override
            public void onError(VolleyError error) {
                setResponse(listener, error, null);
            }
        };
    }
}
