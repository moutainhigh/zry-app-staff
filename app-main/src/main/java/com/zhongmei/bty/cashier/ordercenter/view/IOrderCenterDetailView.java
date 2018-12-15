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

/**
 * 订单中心订单详情View接口
 */

public interface IOrderCenterDetailView {
    void showEmptyView(boolean b);

    void goToDishWindow(TradeVo tradeVo);

    //显示优惠
    void showPrivilegeInfo(TradePaymentVo tradePaymentVo, boolean isRefund);

    //附加费信息
    void showAdditionaltPrivilegeInfo(TradeVo tradeVo);

    //显示服务费
    void showServicePrivilegeInfo(TradeVo tradeVo);

    //显示税费信息
    void showTaxInfo(TradeVo tradeVo);

    //显示拒绝商品信息
    void showRejectGoodsInfo(TradeVo tradeVo);

    //显示反结账信息
    void showRePayedTrades(List<TradePaymentVo> rePayedList);

    //显示拆单信息
    void showSpliteTrades(List<TradePaymentVo> reSpliteList);

    //显示各种操作理由
    void showOperateReasonInfo(TradeVo tradeVo);

    //显示订单信息
    void showBillInfo(TradeVo tradeVo, TradeVo oriTradeVo);

    //显示商品信息
    void showGoodsInfo(TradePaymentVo tradePaymentVo, List<TradeItemVo> tradeItemVos, TradePaymentVo oriTradePaymentVo);

    //处理订单操作button
    void showHandlerButton();

    //显示内用，外带
    void showDetailTitle(TradePaymentVo tradePaymentVo);

    //收货人信息,正餐不调用此方法
    void showDeliveryInfo(TradePaymentVo tradePaymentVo);

    void showDeliveryInfoView(boolean show);

    void refreshDeliveryUserInfoView(TradePaymentVo tradePaymentVo);

    void refreshDeliveryPlatformInfoView(TradePaymentVo tradePaymentVo);

    void refreshTipInfoView(TradePaymentVo tradePaymentVo);

    //支付信息
    void showPayInfo(TradePaymentVo tradePaymentVo, TradePaymentVo oriTradePaymentVo, boolean isRefund);

    //设置tradeVo
    void setTradeVo(TradeVo tradeVo);

    /**
     * 获取当前View的FragmentManager
     *
     * @return FragmentManager
     */
    FragmentManager getViewFragmentManager();

    /**
     * 获取当前View的Activity
     *
     * @return Activity
     */
    FragmentActivity getViewActivity();


    /**
     * 展示Toast提示
     *
     * @param message 需要展示的文本
     */
    void showToast(String message);

    void showToast(int strId);

    void startActivity(Intent intent);

    void showDialog(Trade trade);

    boolean isAdd();

    boolean isFromSnack();

    void hideEmptyViewAndContent();

    void showLoadingView();

    void dismissLoadingView();

    /**
     * 展示发票冲红提示框
     *
     * @param invoiceUuid 电子发票开票记录Uuid
     */
    void showInvoiceRevokeAlert(String invoiceUuid);

    /**
     * 展示电子发票获取开票二维码
     *
     * @param tradeVo             tradeVo
     * @param electronicInvoiceVo electronicInvoiceVo
     * @param actualAmount        actualAmount
     */
    void showInvoiceQrcodeDialog(TradeVo tradeVo, ElectronicInvoiceVo electronicInvoiceVo, BigDecimal actualAmount);

    /**
     * 显示退款弹窗
     *
     * @param tradeId
     * @param paymentId
     * @param usefulAmount
     */
    void showRefundDialog(Long tradeId, Long paymentId, BigDecimal usefulAmount);

    /**
     * 显示手动加小费对话框
     *
     * @param deliveryOrderId  配送单Id
     * @param deliveryPlatform 配送平台
     */
    void showPassiveAddTipDialog(Long deliveryOrderId, DeliveryPlatform deliveryPlatform);

    /**
     * 显示配送员选择列表
     *
     * @param authUsers          配送员列表
     * @param selectedAuthUserId 当前选中的配送员
     * @param listener           回调接口
     */
    void showSelectSenderDialog(List<User> authUsers, String selectedAuthUserId, SelectSenderDialogFragment.OnSelectAuthUserListener listener);

    /**
     * 展示配送费对话框
     *
     * @param tradePaymentVo 订单相关数据
     * @param partnerShopBiz 开通的配送平台配置信息
     * @param fee            配送费金额
     */
    void showDeliveryFeeAlert(TradePaymentVo tradePaymentVo, PartnerShopBiz partnerShopBiz, BigDecimal fee);

    /**
     * 展示配送取消提示
     */
    void showDeliveryCancelTip(String text);

    /**
     * 隐藏配送取消提示
     */
    void hideDeliveryCancelTip();

    void showDispatchFailOrderListAlert(List<DispatchFailOrder> dispatchFailOrders);
}
