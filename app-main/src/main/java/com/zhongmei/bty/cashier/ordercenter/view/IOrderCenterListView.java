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

/**
 * 订单中心订单列表View接口
 */

public interface IOrderCenterListView {
    /**
     * 获取FragmentManager
     *
     * @return
     */
    FragmentManager getViewFragmentManager();

    /**
     * 获取上下文
     *
     * @return
     */
    FragmentActivity getViewActivity();

    /**
     * 刷新通知中心提示
     */
    void refreshNotifyCenterTip();

    /**
     * 从其他模块的通知中心进入此模块时会发送此消息，通知返回图标显示
     */
    //void refreshBackFromPage(EventBackFromPage event);

    /**
     * 通过通知中心进入新模块时会发送此消息
     */
    //void refreshUpperPage(EventUpperPage event);

    /**
     * 展示Toast
     *
     * @param message 文本消息
     */
    void showToast(String message);

    void refreshList(List<TradePaymentVo> tradePaymentVos, int type);

    void squareAccountFinish();

    void batchBindDeliveryUserFinish();

    /**
     * 刷新订单数量
     *
     * @param tab        标签
     * @param orderCount 订单数量
     */
    void refreshOrderCount(int tab, long orderCount);

    /**
     * 获取清账模式下项数和金额的初始提示
     *
     * @return
     */
    String getOriginTip();

    /**
     * 是否在清账模式下
     *
     * @return
     */
    boolean isInSquareAccountMode();

    void hideEmptyAndListView();

    void showLoadingView();

    void dismissLoadingView();

    //void showNotifyCenterTips(EventNotifyBigTypeCounts event);

    /**
     * 显示批量选择配送员对话框
     *
     * @param authUsers 可选配送员
     */
    void showDeliveryUserChooseDialog(List<User> authUsers);

    /**
     * 选择配送平台
     *
     * @param partnerShopBizs
     */
    void showDeliveryPlatformChooseView(List<PartnerShopBiz> partnerShopBizs);

    void showDeliveryFeeView(List<TradeVo> tradeVos, List<BatchDeliveryFee> deliveryFees, PartnerShopBiz partnerShopBiz);

    /**
     * 展示配送异常提示框
     *
     * @param dispatchFailOrders 下发失败的数据
     */
    void showDispatchFailOrderListAlert(List<DispatchFailOrder> dispatchFailOrders);
}
