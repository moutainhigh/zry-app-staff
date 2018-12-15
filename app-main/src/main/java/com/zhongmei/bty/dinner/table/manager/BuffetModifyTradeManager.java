package com.zhongmei.bty.dinner.table.manager;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.bty.basemodule.trade.bean.BuffetComboVo;
import com.zhongmei.bty.basemodule.trade.bean.CustomerTypeBean;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.bty.basemodule.trade.bean.MealShellVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date 2016/7/26
 * @Description:改单操作
 */
public class BuffetModifyTradeManager {
    private TradeVo mTradeVo;
    private int customerNum;
    private Context context;

    private AuthUser mWaiter;

    //保存改动前的人数和服务员
    private int customerNumOld;
    private Long userIdOld;
    private String userNameOld;
    private TradeDeposit oldDeposit;
    private List<TradeBuffetPeople> oldTradeBuffetPeople;
    private DeliveryType oldDeliVerType;
    private MealShellVo oldMealShellVo;

    private TradeDeposit mTradeDeposit;

    private BuffetComboVo comboVo;

    public DinnerShoppingCart mShoppingCart;

    private boolean isFirstChange = false;//是否第一次选择套餐

    private TradeUser tradeUser;//add v8.1 销售员

    public boolean isFirstChange() {
        return isFirstChange;
    }

    public void setmTradeVo(TradeVo mTradeVo) {
        this.mTradeVo = mTradeVo;
        this.isFirstChange = mTradeVo.getMealShellVo() == null;
    }

    public BuffetModifyTradeManager(Context context) {
        mShoppingCart = DinnerShoppingCart.getInstance();
        this.mTradeVo = mShoppingCart.getOrder();
        this.context = context;
        this.isFirstChange = mTradeVo.getMealShellVo() == null;
    }

    public void setComboVo(BuffetComboVo comboVo) {
        this.comboVo = comboVo;
        if (comboVo != null) {
            this.customerNum = comboVo.getTotalCount().intValue();
        }
    }


    public void setmTradeDeposit(TradeDeposit mTradeDeposit) {
        this.mTradeDeposit = mTradeDeposit;
    }

    public void setUser(AuthUser waiter) {
        this.mWaiter = waiter;
    }

    public void finishChangeTable(ResponseListener<TradeResp> listener) {
        modifyTradeVo();//更新其他信息
        changeTable(listener); //调用改单操作
    }


    private void modifyTradeVo() {
        TradeTable tradeTable = getOpenTableInfo(mTradeVo);
        if (tradeTable != null) {
            //保存改动前的人数和服务员
            customerNumOld = tradeTable.getTablePeopleCount();
            userIdOld = tradeTable.getWaiterId();
            userNameOld = tradeTable.getWaiterName();

            tradeTable.setTablePeopleCount(customerNum);
            tradeTable.setWaiterId(mWaiter.getId());
            tradeTable.setWaiterName(mWaiter.getName());
        }

        oldDeliVerType = mTradeVo.getTrade().getDeliveryType();


        //如果是套餐，才会设置套餐相关的数据
        mTradeVo.getTrade().setTradePeopleCount(customerNum);

        if (mTradeVo.getTradeDeposit() != null) {
            oldDeposit = mTradeVo.getTradeDeposit().clone();
        }

        if (depositChanged(mTradeDeposit, mTradeVo)) {
            mTradeDeposit.setChanged(true);
            mTradeVo.setTradeDeposit(mTradeDeposit);
        }

        //将修改的人数放入tradeBuffetPeople中。
        if (mTradeVo.getMealShellVo() == null) {//重新设置
            mShoppingCart.setMealShellVo(comboVo, mWaiter);
        } else {//更新
            oldMealShellVo = mTradeVo.getMealShellVo().clone();
            Map<Long, CustomerTypeBean> mapCustomerType = null;
            BigDecimal totalAmount = BigDecimal.ZERO;
            if (comboVo != null && Utils.isNotEmpty(comboVo.getDishCarteNorms())) {
                mapCustomerType = new HashMap<>();
                for (CustomerTypeBean customerTypeBean : comboVo.getDishCarteNorms()) {
                    mapCustomerType.put(customerTypeBean.getId(), customerTypeBean);
                    totalAmount = totalAmount.add(customerTypeBean.getCount().multiply(customerTypeBean.getPrice()));
                }
            }

            mTradeVo.getMealShellVo().getTradeItem().setQuantity(comboVo.getTotalCount());
            mTradeVo.getMealShellVo().getTradeItem().setActualAmount(totalAmount);
            mTradeVo.getMealShellVo().getTradeItem().setAmount(totalAmount);
            mTradeVo.getMealShellVo().getTradeItem().setChanged(true);


            List<TradeBuffetPeople> listTradeBuffetPeople = mTradeVo.getTradeBuffetPeoples();
            if (Utils.isNotEmpty(listTradeBuffetPeople) && mapCustomerType != null) {
                oldTradeBuffetPeople = new ArrayList<>();
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
        }
        //将改动信息存入购物车
        if (this.tradeUser != null) {//add v8.1 销售员
            mShoppingCart.setTradeUser(this.tradeUser);
        }
        mShoppingCart.updateTable(tradeTable);
        mShoppingCart.setOrderBusinessType(mShoppingCart.getShoppingCartVo(), BusinessType.BUFFET);
        mShoppingCart.setOrderType(mShoppingCart.getShoppingCartVo(), DeliveryType.HERE);

        MathShoppingCartTool.mathTotalPrice(mShoppingCart.getShoppingCartDish(), mTradeVo);
    }

    void changeTable(ResponseListener<TradeResp> listener) {
        final TradeOperates mTradeOperates = OperatesFactory.create(TradeOperates.class);
        final TradeVo mTradeVo = mShoppingCart.createOrder(mShoppingCart.getShoppingCartVo(), false);
        FragmentActivity activity = (FragmentActivity) context;
        mTradeOperates.modifyBuffet(mTradeVo, LoadingResponseListener.ensure(listener, activity.getSupportFragmentManager()));
    }

    /**
     * @Date 2016/10/27
     * @Description:网络请求失败重置tradetable
     * @Param
     * @Return
     */
    public void resetCusomerNumberAndWaiter() {
        if (isFirstChange) {
            return;
        }

        TradeTable tradeTable = getOpenTableInfo(mTradeVo);
        tradeTable.setTablePeopleCount(customerNumOld);
        tradeTable.setWaiterId(userIdOld);
        tradeTable.setWaiterName(userNameOld);
        mTradeVo.getTrade().setTradePeopleCount(customerNumOld);
        mTradeVo.setMealHullVo(oldMealShellVo);
        mTradeVo.setTradeDeposit(oldDeposit);
        mTradeVo.setTradeBuffetPeoples(oldTradeBuffetPeople);
        mTradeVo.getTrade().setDeliveryType(oldDeliVerType);
    }

    private TradeTable getOpenTableInfo(TradeVo tradeVo) {
        List<TradeTable> tables = tradeVo.getTradeTableList();
        if (Utils.isNotEmpty(tables)) {
            return tables.get(0);
        }
        return null;
    }


    private boolean depositChanged(TradeDeposit tradeposit, TradeVo tradeVo) {
        TradeDeposit orderDeposit = tradeVo.getTradeDeposit();
        if (tradeposit == null || (orderDeposit != null && orderDeposit.getType() == tradeposit.getType() && orderDeposit.getDepositPay().compareTo(tradeposit.getDepositPay()) == 0)) {
            return false;
        }
        return true;
    }

    public void setTradeUser(TradeUser tradeUser) {
        this.tradeUser = tradeUser;
    }
}
