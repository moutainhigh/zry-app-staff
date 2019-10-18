package com.zhongmei.bty.cashier.ordercenter.view;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.Intent;

import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.cashier.ordercenter.bean.DispatchFailOrder;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;
import com.zhongmei.yunfu.db.enums.DeliveryPlatform;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.math.BigDecimal;
import java.util.List;



public interface IOrderCenterDetailView {
    void showEmptyView(boolean b);

    void goToDishWindow(TradeVo tradeVo);

        void showPrivilegeInfo(TradePaymentVo tradePaymentVo, boolean isRefund);

        void showAdditionaltPrivilegeInfo(TradeVo tradeVo);

        void showServicePrivilegeInfo(TradeVo tradeVo);

        void showTaxInfo(TradeVo tradeVo);

        void showRejectGoodsInfo(TradeVo tradeVo);

        void showRePayedTrades(List<TradePaymentVo> rePayedList);

        void showSpliteTrades(List<TradePaymentVo> reSpliteList);

        void showOperateReasonInfo(TradeVo tradeVo);

        void showBillInfo(TradeVo tradeVo, TradeVo oriTradeVo);

        void showGoodsInfo(TradePaymentVo tradePaymentVo, List<TradeItemVo> tradeItemVos, TradePaymentVo oriTradePaymentVo);

        void showHandlerButton();

        void showDetailTitle(TradePaymentVo tradePaymentVo);

        void showDeliveryInfo(TradePaymentVo tradePaymentVo);

    void showDeliveryInfoView(boolean show);

    void refreshDeliveryUserInfoView(TradePaymentVo tradePaymentVo);

    void refreshDeliveryPlatformInfoView(TradePaymentVo tradePaymentVo);

    void refreshTipInfoView(TradePaymentVo tradePaymentVo);

        void showPayInfo(TradePaymentVo tradePaymentVo, TradePaymentVo oriTradePaymentVo, boolean isRefund);

        void setTradeVo(TradeVo tradeVo);


    FragmentManager getViewFragmentManager();


    FragmentActivity getViewActivity();



    void showToast(String message);

    void showToast(int strId);

    void startActivity(Intent intent);

    void showDialog(Trade trade);

    boolean isAdd();

    boolean isFromSnack();

    void hideEmptyViewAndContent();

    void showLoadingView();

    void dismissLoadingView();


    void showInvoiceRevokeAlert(String invoiceUuid);


    void showInvoiceQrcodeDialog(TradeVo tradeVo, ElectronicInvoiceVo electronicInvoiceVo, BigDecimal actualAmount);


    void showRefundDialog(Long tradeId, Long paymentId, BigDecimal usefulAmount);


    void showPassiveAddTipDialog(Long deliveryOrderId, DeliveryPlatform deliveryPlatform);


    void showSelectSenderDialog(List<User> authUsers, String selectedAuthUserId, SelectSenderDialogFragment.OnSelectAuthUserListener listener);


    void showDeliveryFeeAlert(TradePaymentVo tradePaymentVo, PartnerShopBiz partnerShopBiz, BigDecimal fee);


    void showDeliveryCancelTip(String text);


    void hideDeliveryCancelTip();

    void showDispatchFailOrderListAlert(List<DispatchFailOrder> dispatchFailOrders);
}
