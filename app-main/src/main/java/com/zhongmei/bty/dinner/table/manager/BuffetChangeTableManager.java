package com.zhongmei.bty.dinner.table.manager;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.basemodule.trade.bean.CustomerTypeBean;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.dinner.table.TableInfoFragment;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.pay.utils.PayUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date 2016/7/26
 * @Description:开台操作封装
 */
public class BuffetChangeTableManager {
    protected DinnertableTradeVo dinnertableTradeVo;
    protected DinnerShoppingCart mShoppingCart;
    protected int customerNum;
    protected Context context;
    private Long userId;
    private String userName;

    //保存改动前的人数和服务员
    protected int customerNumOld;
    private Long userIdOld;
    private String userNameOld;
    protected TradeDeposit oldDeposit;
    protected List<TradeBuffetPeople> oldTradeBuffetPeople;

    protected BusinessType mBusinessType;

    List<CustomerTypeBean> mListCustomer;

    protected TradeDeposit mTradeDeposit;

    public AuthUser salesman; //add  v8.1 销售员

    public BuffetChangeTableManager(DinnertableTradeVo dinnertableTradeVo, List<CustomerTypeBean> listCustomer, int customerNum, Context context, BusinessType businessType) {
        this.dinnertableTradeVo = dinnertableTradeVo;
        mShoppingCart = DinnerShoppingCart.getInstance();
        this.customerNum = customerNum;
        this.context = context;
        this.mBusinessType = businessType;
        this.mListCustomer = listCustomer;
        this.oldTradeBuffetPeople = new ArrayList<>();
    }

    public void setmTradeDeposit(TradeDeposit mTradeDeposit) {
        this.mTradeDeposit = mTradeDeposit;
    }

    public void setUser(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public void finishChangeTable() {
        if (TableInfoFragment.getDataManager() == null) {
            Log.e("zhubo", "dataManage为null");
            return;
        }

        modifyTradeVo();
        changeTable();


    }


    public void modifyTradeVo() {
        TradeTable tradeTable = TableInfoFragment.getDataManager().getOpenTableInfo(dinnertableTradeVo);
        if (tradeTable == null) {
            return;
        }
        //保存改动前的人数和服务员
        customerNumOld = tradeTable.getTablePeopleCount();
        userIdOld = tradeTable.getWaiterId();
        userNameOld = tradeTable.getWaiterName();

        TradeVo tradeVo = dinnertableTradeVo.getTradeVo();

        if (tradeVo.getTradeDeposit() != null) {
            oldDeposit = tradeVo.getTradeDeposit().clone();
        }

        tradeTable.setTablePeopleCount(customerNum);
//        //需要获取waiterId
//        Long userId = 0L;
//        if(waiterNameTv.getTag() != null){
//            userId = Long.valueOf(waiterNameTv.getTag().toString());
//        }
        tradeTable.setWaiterId(userId);
//        tradeTable.setWaiterName(waiterNameTv.getText().toString());
        tradeTable.setWaiterName(userName);
        tradeVo.getTrade().setTradePeopleCount(customerNum);
        if (depositChanged(mTradeDeposit, tradeVo)) {
            mTradeDeposit.setChanged(true);
            tradeVo.setTradeDeposit(mTradeDeposit);
        }
        //modify by zhubo 2015-10-30 resetOrderFromTable方法参数调整
        DinnertableTradeInfo tradeInfo = DinnertableTradeInfo.create(dinnertableTradeVo.getDinnertableTrade(), tradeVo);
        mShoppingCart.resetOrderFromTable(tradeInfo, true);

        //将改动信息存入购物车
        mShoppingCart.updateTable(tradeTable);
        mShoppingCart.setOrderBusinessType(mShoppingCart.getShoppingCartVo(), mBusinessType);
        mShoppingCart.setOrderType(mShoppingCart.getShoppingCartVo(), tradeVo.getTrade().getDeliveryType());
        //还需要写入tradeVo到购物车


        //将修改的人数放入tradeBuffetPeople中。
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
        //add start  v8.1 销售员
        if (salesman != null) {
            if (mShoppingCart.getTradeUser() != null) {
                //如果发生修改
                if (!salesman.getId().equals(mShoppingCart.getTradeUser().getUserId())) {
                    mShoppingCart.getTradeUser().setUserId(salesman.getId());
                    mShoppingCart.getTradeUser().setUserName(salesman.getName());
                    mShoppingCart.getTradeUser().setChanged(true);

                }
            } else {
                mShoppingCart.setTradeUser(PayUtils.creatTradeUser(tradeVo.getTrade(), salesman));
            }
        }
        //add  end v8.1 销售员
        MathShoppingCartTool.mathTotalPrice(mShoppingCart.getShoppingCartDish(), dinnertableTradeVo.getTradeVo());
    }

    protected boolean depositChanged(TradeDeposit tradeposit, TradeVo tradeVo) {
        TradeDeposit orderDeposit = tradeVo.getTradeDeposit();
        if (tradeposit == null || (orderDeposit != null && orderDeposit.getType() == tradeposit.getType() && orderDeposit.getDepositPay().compareTo(tradeposit.getDepositPay()) == 0)) {
            return false;
        }
        return true;
    }

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
        mTradeOperates.modifyBuffet(mTradeVo, LoadingResponseListener.ensure(listener, activity.getSupportFragmentManager()));
    }

    /**
     * @Date 2016/10/27
     * @Description:网络请求失败重置tradetable
     * @Param
     * @Return
     */
    public void resetCusomerNumberAndWaiter() {
        TradeTable tradeTable = TableInfoFragment.getDataManager().getOpenTableInfo(dinnertableTradeVo);
        tradeTable.setTablePeopleCount(customerNumOld);
        tradeTable.setWaiterId(userIdOld);
        tradeTable.setWaiterName(userNameOld);
        dinnertableTradeVo.getTradeVo().getTrade().setTradePeopleCount(customerNumOld);
        dinnertableTradeVo.getTradeVo().setTradeDeposit(oldDeposit);
        dinnertableTradeVo.getTradeVo().setTradeBuffetPeoples(oldTradeBuffetPeople);
    }

    public void setSalesman(AuthUser salesman) {
        this.salesman = salesman;
    }
}
