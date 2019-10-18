package com.zhongmei.bty.cashier.ordercenter.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeReceiveLog;
import com.zhongmei.bty.basemodule.trade.message.InvoiceQrcodeReq;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.enums.DeliveryPlatform;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.bty.dinner.action.ActionReprintType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;



public interface IOrderCenterDetailPresenter {

    void destroy();


    void sendOrder(PartnerShopBiz partnerShopBiz, BigDecimal deliveryFee);


    void deliveryOrderDispatch(TradePaymentVo tradePaymentVo, BigDecimal deliveryFee, Integer deliveryPlatform);


    void queryDeliveryFee(Long tradeId, String tradeNo, String thirdTranNo, PartnerShopBiz partnerShopBiz);


    void deliveryOrderList(TradeVo tradeVo);


    void cancelDeliveryOrder();


    void retryRefund();


    void loadData(String tradeUuid);


    void setCurrentTab(int currentTab);


    void setIsSquareAccountMode(boolean isSquareAccountMode);


    void setIsBindDeliveryUserMode(boolean isBindDeliveryUserMode);


    void doCall();

    void doRefuse();


    void doAccept();

    void doPay();

    void doRecision();

    void doRefund();

    void doPrint(String tag);

    void doRepay();

    void doContinueRepay();

    void doAcceptReturn();

    void doRefuseReturn();

    void doRefreshPayStatus(PaymentItem paymentItem);

    void doContinuePay();

    void doRePrint(String uuid, int printType, ActionReprintType actionReprintType);


    void invoiceQrcode();


    void performInvoiceQrcode(InvoiceQrcodeReq req, TradeVo tradeVo);


    void invoiceRevoke();


    void performInvoiceRevoke(String invoiceUuid);


    void doRebindDeliveryUser();


    void performRebindDeliveryUser(User authUser);


    boolean showPrivilegeInfo(TradeVo tradeVo);

    boolean showPrivilegeInfo(TradePaymentVo tradePaymentVo);


    View createPrivilegeInfoItem(Context context, TradeVo tradeVo, boolean isRefund);


    View createPrivilegeInfoItem(Context context, TradePaymentVo tradePaymentVo, boolean isRefund);


    boolean showPayInfo(TradeVo tradeVo, List<PaymentVo> paymentVos);


    View createPayInfoItem(Context context, TradePaymentVo tradePaymentVo, TradePaymentVo oriTradePaymentVo, boolean isRefund);


    boolean showAccept();


    boolean showRefuse();


    boolean showCall();


    boolean showPay();


    boolean showRecision();


    boolean showRefund();


    boolean showPrint();


    boolean showRetryRefund();


    boolean showRepay();


    boolean showContinueRepay();


    boolean showContinuePay();


    boolean showSendOrder();


    boolean showCancelOrder();


    boolean showAcceptReturn();


    boolean showRefuseReturn();


    boolean showDepositRefund();


    boolean showInvoice();


    boolean showInvoiceRevoke();


    boolean showRebindDeliveryUser();


    TradeVo compareTradeVo(String uuid, TradeVo tradeVo);


    String getDeliveryPlatformName(DeliveryPlatform deliveryPlatform);


    Map<Integer, PartnerShopBiz> getDeliveryPlatformPartnerShopBizMap();


    void showRefundDialog(Long tradeId, Long paymentId, BigDecimal usefulAmount);


    List<TradePrivilege> getOrderPrivilegeList(TradeVo tradeVo, PrivilegeType privilegeType);


    String getTradeSource(Context context, Trade trade);


    String getTradeAcceptSource(TradeReceiveLog tradeReceiveLog);


    String getSplideOrigonTradeNumber(Trade trade);


    View createOrderCenterPayInfoItem(LayoutInflater layoutInflater, Context context, TradeVo tradeVo,
                                      List<PaymentVo> paymentVos, boolean isRefund);


    List<TradeItemVo> getInvalidTradeItemList(List<TradeItemVo> tradeItemList, InvalidType invalidType);


    BigDecimal getShiShouAmountString(TradeVo tradeVo, List<PaymentVo> paymentVos, Boolean isRefund);

    boolean showTakeDish();

    void doTakeDish();


    void passiveAddTip();


    void flushAuthLog(OrderActionEnum orderAction, Long tradeId, String tradeUuid, Long updateTime);


    void clearAuthLog();

        void getTaxNoByTradeId(Long tradeId);

        String getDisplayExpectTime();

        String getDisplayTakeNumber();


    boolean showCreateDoc();


    boolean showCreateTask();

    TradeCustomer getTradeCustomer();
}
