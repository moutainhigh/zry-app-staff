package com.zhongmei.bty.dinner.table.manager;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.bty.basemodule.trade.bean.CustomerTypeBean;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.pay.utils.PayUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class BuffetUnionChangeTableManager extends BuffetChangeTableManager {
    public BuffetUnionChangeTableManager(DinnertableTradeVo dinnertableTradeVo, List<CustomerTypeBean> listCustomer, int customerNum, Context context, BusinessType businessType) {
        super(dinnertableTradeVo, listCustomer, customerNum, context, businessType);
    }

    @Override
    public void modifyTradeVo() {
        TradeVo tradeVo = dinnertableTradeVo.getTradeVo();
        tradeVo.getTrade().setTradePeopleCount(customerNum);
        if (tradeVo.getTradeDeposit() != null) {
            oldDeposit = tradeVo.getTradeDeposit().clone();
        }
        if (depositChanged(mTradeDeposit, tradeVo)) {
            mTradeDeposit.setChanged(true);
            tradeVo.setTradeDeposit(mTradeDeposit);
        }
                DinnertableTradeInfo tradeInfo = DinnertableTradeInfo.create(dinnertableTradeVo.getDinnertableTrade(), tradeVo);
        mShoppingCart.resetOrderFromTable(tradeInfo, true);

                mShoppingCart.setOrderBusinessType(mShoppingCart.getShoppingCartVo(), mBusinessType);
        mShoppingCart.setOrderType(mShoppingCart.getShoppingCartVo(), tradeVo.getTrade().getDeliveryType());


                Map<Long, CustomerTypeBean> mapCustomerType = null;
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (Utils.isNotEmpty(mListCustomer)) {
            mapCustomerType = new HashMap<>();
            for (CustomerTypeBean customerTypeBean : mListCustomer) {
                mapCustomerType.put(customerTypeBean.getId(), customerTypeBean);
                totalAmount = totalAmount.add(customerTypeBean.getCount().multiply(customerTypeBean.getPrice()));
            }
        }

        tradeVo.getMealShellVo().getTradeItem().setQuantity(BigDecimal.valueOf(customerNum));
        tradeVo.getMealShellVo().getTradeItem().setActualAmount(totalAmount);
        tradeVo.getMealShellVo().getTradeItem().setAmount(totalAmount);
        tradeVo.getMealShellVo().getTradeItem().setChanged(true);


        List<TradeBuffetPeople> listTradeBuffetPeople = dinnertableTradeVo.getTradeVo().getTradeBuffetPeoples();
        if (Utils.isNotEmpty(listTradeBuffetPeople) && mapCustomerType != null) {
            for (TradeBuffetPeople tradeBuffetPeople : listTradeBuffetPeople) {
                oldTradeBuffetPeople.add(tradeBuffetPeople.clone());
                if (!mapCustomerType.containsKey(tradeBuffetPeople.getCarteNormsId())) {
                    continue;
                }

                CustomerTypeBean customerType = mapCustomerType.get(tradeBuffetPeople.getCarteNormsId());

                if (tradeBuffetPeople.getPeopleCount().compareTo(customerType.getCount()) != 0) {
                    tradeBuffetPeople.setPeopleCount(customerType.getCount());
                    tradeBuffetPeople.setChanged(true);
                }
            }
        }
                if (salesman != null) {
            if (mShoppingCart.getTradeUser() != null) {
                                if (!salesman.getId().equals(mShoppingCart.getTradeUser().getUserId())) {
                    mShoppingCart.getTradeUser().setUserId(salesman.getId());
                    mShoppingCart.getTradeUser().setUserName(salesman.getName());
                    mShoppingCart.getTradeUser().setChanged(true);

                }
            } else {
                mShoppingCart.setTradeUser(PayUtils.creatTradeUser(tradeVo.getTrade(), salesman));
            }
        }
                MathShoppingCartTool.mathTotalPrice(mShoppingCart.getShoppingCartDish(), dinnertableTradeVo.getTradeVo());
    }

    @Override
    void changeTable() {
        final TradeOperates mTradeOperates = OperatesFactory.create(TradeOperates.class);
        final TradeVo mTradeVo = mShoppingCart.createOrder(mShoppingCart.getShoppingCartVo(), false);
        ResponseListener<TradeResp> listener = new ResponseListener<TradeResp>() {

            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                if (ResponseObject.isOk(response)) {
                    try {
                        ToastUtil.showShortToast(response.getMessage());
                        if (mTradeVo.getTrade() != null) {
                            AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_CHANGE_ORDER, mTradeVo.getTrade().getId(), mTradeVo.getTrade().getUuid(), mTradeVo.getTrade().getServerUpdateTime());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    AuthLogManager.getInstance().clear();
                    ToastUtil.showShortToast(response.getMessage());
                    resetCusomerNumberAndWaiter();
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
                resetCusomerNumberAndWaiter();

            }
        };
        FragmentActivity activity = (FragmentActivity) context;
        mTradeOperates.buffetCreateMenu(mTradeVo, LoadingResponseListener.ensure(listener, activity.getSupportFragmentManager()));
    }

    @Override
    public void resetCusomerNumberAndWaiter() {
        dinnertableTradeVo.getTradeVo().getTrade().setTradePeopleCount(customerNumOld);
        dinnertableTradeVo.getTradeVo().setTradeDeposit(oldDeposit);
        dinnertableTradeVo.getTradeVo().setTradeBuffetPeoples(oldTradeBuffetPeople);
    }
}
