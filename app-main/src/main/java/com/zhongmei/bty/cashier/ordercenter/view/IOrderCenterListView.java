package com.zhongmei.bty.cashier.ordercenter.view;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.BatchDeliveryFee;
import com.zhongmei.bty.cashier.ordercenter.bean.DispatchFailOrder;
import com.zhongmei.yunfu.context.session.core.user.User;

import java.util.List;



public interface IOrderCenterListView {

    FragmentManager getViewFragmentManager();


    FragmentActivity getViewActivity();


    void refreshNotifyCenterTip();






    void showToast(String message);

    void refreshList(List<TradePaymentVo> tradePaymentVos, int type);

    void squareAccountFinish();

    void batchBindDeliveryUserFinish();


    void refreshOrderCount(int tab, long orderCount);


    String getOriginTip();


    boolean isInSquareAccountMode();

    void hideEmptyAndListView();

    void showLoadingView();

    void dismissLoadingView();



    void showDeliveryUserChooseDialog(List<User> authUsers);


    void showDeliveryPlatformChooseView(List<PartnerShopBiz> partnerShopBizs);

    void showDeliveryFeeView(List<TradeVo> tradeVos, List<BatchDeliveryFee> deliveryFees, PartnerShopBiz partnerShopBiz);


    void showDispatchFailOrderListAlert(List<DispatchFailOrder> dispatchFailOrders);
}
