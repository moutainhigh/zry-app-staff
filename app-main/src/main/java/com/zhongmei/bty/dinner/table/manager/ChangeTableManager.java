package com.zhongmei.bty.dinner.table.manager;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.dinner.manager.DinnerUnionManager;
import com.zhongmei.bty.dinner.table.TableInfoFragment;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

/**
 * @Date 2016/7/26
 * @Description:开台操作封装
 */
public class ChangeTableManager {
    private DinnertableTradeVo dinnertableTradeVo;
    private DinnerShoppingCart mShoppingCart;
    private int customerNum;
    private Context context;
    private Long userId;
    private String userName;

    //保存改动前的人数和服务员
    private int customerNumOld;
    private Long userIdOld;
    private String userNameOld;

    private BusinessType mBusinessType;

    private TradeUser tradeUser;

    public ChangeTableManager(DinnertableTradeVo dinnertableTradeVo, int customerNum, Context context, BusinessType businessType) {
        this.dinnertableTradeVo = dinnertableTradeVo;
        mShoppingCart = DinnerShoppingCart.getInstance();
        this.customerNum = customerNum;
        this.context = context;
        this.mBusinessType = businessType;
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
        if (tradeTable != null) {
            //保存改动前的人数和服务
            customerNumOld = tradeTable.getTablePeopleCount();
            userIdOld = tradeTable.getWaiterId();
            userNameOld = tradeTable.getWaiterName();

            tradeTable.setWaiterId(userId);
            tradeTable.setWaiterName(userName);
            tradeTable.setTablePeopleCount(customerNum);
            tradeTable.setChanged(true);
        }

        //修改人数，解决修改人数后附加费不变的bug
        dinnertableTradeVo.getTradeVo().getTrade().setTradePeopleCount(customerNum);
        //modify by zhubo 2015-10-30 resetOrderFromTable方法参数调整
        DinnertableTradeInfo tradeInfo = DinnertableTradeInfo.create(dinnertableTradeVo.getDinnertableTrade(), dinnertableTradeVo.getTradeVo());
        mShoppingCart.resetOrderFromTable(dinnertableTradeVo.getUnionMainTradeInfo(), tradeInfo, true);

        //将改动信息存入购物车
        mShoppingCart.updateTable(tradeTable);
        mShoppingCart.setTradeUser(this.tradeUser);
        mShoppingCart.setOrderBusinessType(mShoppingCart.getShoppingCartVo(), mBusinessType);
        mShoppingCart.setOrderType(mShoppingCart.getShoppingCartVo(), DeliveryType.HERE);
        //还需要写入tradeVo到购物车

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
        DinnerUnionManager unionManager = null;

        switch (mTradeVo.getTrade().getTradeType()) {
            case UNOIN_TABLE_MAIN:
                unionManager = new DinnerUnionManager();
                unionManager.modifyUnionMainTrade(mTradeVo, LoadingResponseListener.ensure(listener, activity.getSupportFragmentManager()), true, mTradeOperates);
                break;
            case UNOIN_TABLE_SUB:
                unionManager = new DinnerUnionManager();
                unionManager.modifyUnionSubTrade(mTradeVo, mShoppingCart.getMainTradeInfo(), LoadingResponseListener.ensure(listener, activity.getSupportFragmentManager()), true, mTradeOperates);
                break;
            default:
                mTradeOperates.modifyDinner(mTradeVo, LoadingResponseListener.ensure(listener, activity.getSupportFragmentManager()));
        }

    }

    /**
     * @Date 2016/10/27
     * @Description:网络请求失败重置tradetable
     * @Param
     * @Return
     */
    private void resetCusomerNumberAndWaiter() {
        TradeTable tradeTable = TableInfoFragment.getDataManager().getOpenTableInfo(dinnertableTradeVo);
        tradeTable.setTablePeopleCount(customerNumOld);
        tradeTable.setWaiterId(userIdOld);
        tradeTable.setWaiterName(userNameOld);
        dinnertableTradeVo.getTradeVo().getTrade().setTradePeopleCount(customerNumOld);
    }

    public void setTradeUser(TradeUser tradeUser) {
        this.tradeUser = tradeUser;
    }
}
