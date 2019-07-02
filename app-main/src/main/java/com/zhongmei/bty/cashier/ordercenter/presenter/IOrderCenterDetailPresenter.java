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

/**
 * 订单中心订单详情Presenter接口
 */

public interface IOrderCenterDetailPresenter {
    /**
     * presenter销毁，主要用于反注册掉一些东西
     */
    void destroy();

    /**
     * 派送订单到指定配送平台
     *
     * @param partnerShopBiz 开通的配送平台配置信息
     * @param deliveryFee    配送费,可为空
     */
    void sendOrder(PartnerShopBiz partnerShopBiz, BigDecimal deliveryFee);

    /**
     * 将订单下发到配送平台
     *
     * @param tradePaymentVo   订单信息
     * @param deliveryFee      配送费
     * @param deliveryPlatform 目标配送平台
     */
    void deliveryOrderDispatch(TradePaymentVo tradePaymentVo, BigDecimal deliveryFee, Integer deliveryPlatform);

    /**
     * 查询配送费
     *
     * @param tradeId        订单Id
     * @param tradeNo        订单号
     * @param thirdTranNo    第三方订单号
     * @param partnerShopBiz 开通的配送平台配置信息
     */
    void queryDeliveryFee(Long tradeId, String tradeNo, String thirdTranNo, PartnerShopBiz partnerShopBiz);

    /**
     * 配送订单查询
     *
     * @param tradeVo 订单数据
     */
    void deliveryOrderList(TradeVo tradeVo);

    /**
     * 取消配送订单
     */
    void cancelDeliveryOrder();

    /**
     * 二次退款
     */
    void retryRefund();

    /**
     * 根据TradeUUid查询相关数据
     *
     * @param tradeUuid tradeUuid
     */
    void loadData(String tradeUuid);

    /**
     * 设置当前栏位
     *
     * @param currentTab
     */
    void setCurrentTab(int currentTab);

    /**
     * 设置是否清账模式
     *
     * @param isSquareAccountMode true清账模式 false为非清账
     */
    void setIsSquareAccountMode(boolean isSquareAccountMode);

    /**
     * 设置是否绑定配送员模式
     *
     * @param isBindDeliveryUserMode true为绑定配送员模式 false为非绑定配送员模式
     */
    void setIsBindDeliveryUserMode(boolean isBindDeliveryUserMode);

    /**
     * 呼叫用户
     */
    void doCall();

    void doRefuse();

    /**
     * 验证权限并执行接受订单
     */
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

    /**
     * 电子发票获取开票二维码
     */
    void invoiceQrcode();

    /**
     * 执行电子发票获取开票二维码
     */
    void performInvoiceQrcode(InvoiceQrcodeReq req, TradeVo tradeVo);

    /**
     * 电子发票冲红
     */
    void invoiceRevoke();

    /**
     * 执行电子发票冲红
     *
     * @param invoiceUuid 电子发票invoiceUuid
     */
    void performInvoiceRevoke(String invoiceUuid);

    /**
     * 重新绑定配送员
     */
    void doRebindDeliveryUser();

    /**
     * 执行重新绑定配送员
     *
     * @param authUser 指定账号
     */
    void performRebindDeliveryUser(User authUser);

    /**
     * 判断是否展示优惠信息
     *
     * @param tradeVo
     * @return true为展示，false为不展示
     */
    boolean showPrivilegeInfo(TradeVo tradeVo);

    boolean showPrivilegeInfo(TradePaymentVo tradePaymentVo);

    /**
     * 创建优惠条目
     *
     * @param context context
     * @param tradeVo tradeVo
     * @return 优惠条目
     * @see #createPayInfoItem(Context, TradePaymentVo, TradePaymentVo, boolean)
     */
    View createPrivilegeInfoItem(Context context, TradeVo tradeVo, boolean isRefund);

    /**
     * 创建优惠条目(new)
     *
     * @param context
     * @param tradePaymentVo
     * @param isRefund
     * @return
     */
    View createPrivilegeInfoItem(Context context, TradePaymentVo tradePaymentVo, boolean isRefund);

    /**
     * 判断是否展示支付信息
     *
     * @param tradeVo    tradeVo
     * @param paymentVos paymentVos
     * @return
     */
    boolean showPayInfo(TradeVo tradeVo, List<PaymentVo> paymentVos);

    /**
     * 创建支付条目
     *
     * @param context
     * @param tradePaymentVo
     * @param oriTradePaymentVo
     * @return 支付条目
     */
    View createPayInfoItem(Context context, TradePaymentVo tradePaymentVo, TradePaymentVo oriTradePaymentVo, boolean isRefund);

    /**
     * 是否展示接受订单按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showAccept();

    /**
     * 是否展示拒绝订单按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showRefuse();

    /**
     * 是否展示接受订单按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showCall();

    /**
     * 是否展示收银按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showPay();

    /**
     * 是否展示作废订单按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showRecision();

    /**
     * 是否展示退货订单按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showRefund();

    /**
     * 是否展示打印订单按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showPrint();

    /**
     * 是否展示重新退款按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showRetryRefund();

    /**
     * 是否展示反结账订单按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showRepay();

    /**
     * 是否展示继续反结账订单按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showContinueRepay();

    /**
     * 是否展示继续支付按钮
     *
     * @return
     */
    boolean showContinuePay();

    /**
     * 是否展示派送达达按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showSendOrder();

    /**
     * 是否展示取消派送按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showCancelOrder();

    /**
     * 是否展示确认取消订单按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showAcceptReturn();

    /**
     * 是否展示拒绝退单按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showRefuseReturn();

    /**
     * 是否展示退押金按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showDepositRefund();

    /**
     * 是否展示开发票按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showInvoice();

    /**
     * 是否展示冲红按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showInvoiceRevoke();

    /**
     * 是否展示修改派单按钮
     *
     * @return true为展示，false为不展示
     */
    boolean showRebindDeliveryUser();

    /**
     * 比较后返回请求使用的TradeVo
     */
    TradeVo compareTradeVo(String uuid, TradeVo tradeVo);

    /**
     * 获取配送平台名称
     *
     * @param deliveryPlatform 配送平台
     */
    String getDeliveryPlatformName(DeliveryPlatform deliveryPlatform);

    /**
     * 获取商户开通的配送平台
     */
    Map<Integer, PartnerShopBiz> getDeliveryPlatformPartnerShopBizMap();

    /**
     * 显示退款弹窗
     *
     * @param tradeId
     * @param paymentId
     * @param usefulAmount
     */
    void showRefundDialog(Long tradeId, Long paymentId, BigDecimal usefulAmount);

    /**
     * 获取订单内的优惠项列表
     *
     * @param tradeVo
     * @param privilegeType
     * @return
     */
    List<TradePrivilege> getOrderPrivilegeList(TradeVo tradeVo, PrivilegeType privilegeType);

    /**
     * @param trade
     * @return
     * @Date 2016年1月12日
     * @Description: 获取订单来源信息
     * @Return String
     */
    String getTradeSource(Context context, Trade trade);

    /**
     * @param tradeReceiveLog
     * @return
     * @Date 2017年5月12日
     * @Description: 获取订单接受方信息
     * @Return String
     */
    String getTradeAcceptSource(TradeReceiveLog tradeReceiveLog);

    /**
     * @Date 2016年1月12日
     * @Description: 获取拆单的原单编号
     * @Return void
     */
    String getSplideOrigonTradeNumber(Trade trade);

    /**
     * 构建订单中心支付项视图
     *
     * @param layoutInflater
     * @param context
     * @param tradeVo
     * @param paymentVos
     * @param isRefund
     * @return
     */
    View createOrderCenterPayInfoItem(LayoutInflater layoutInflater, Context context, TradeVo tradeVo,
                                      List<PaymentVo> paymentVos, boolean isRefund);

    /**
     * 筛选无效的菜品条目
     *
     * @param tradeItemList
     * @param invalidType
     * @return
     */
    List<TradeItemVo> getInvalidTradeItemList(List<TradeItemVo> tradeItemList, InvalidType invalidType);

    /**
     * @Title: getShiShouAmountString
     * @Description: 获取实收金额
     * @Param @param context
     * @Param @param paymentVos
     * @Return BigDecimal 返回类型
     */
    BigDecimal getShiShouAmountString(TradeVo tradeVo, List<PaymentVo> paymentVos, Boolean isRefund);

    boolean showTakeDish();

    void doTakeDish();

    /**
     * 手动添加小费
     */
    void passiveAddTip();

    /**
     * 刷新数据到数据库
     *
     * @param orderAction 权限关联的单据类型
     * @param tradeId     如果没有tradeId则传null
     * @param tradeUuid   如果没有tradeUuid则传null
     * @param updateTime  订单更新时间
     */
    void flushAuthLog(OrderActionEnum orderAction, Long tradeId, String tradeUuid, Long updateTime);

    /**
     * 清除数据
     */
    void clearAuthLog();

    //根据订单id查询消费税号
    void getTaxNoByTradeId(Long tradeId);

    //获取用于展示的期望时间
    String getDisplayExpectTime();

    //获取用于展示的取餐号
    String getDisplayTakeNumber();

    /**
     * 是否显示创建档案按钮
     * @return
     */
    boolean showCreateDoc();

    /**
     * 是否显示创建任务按钮
     * @return
     */
    boolean showCreateTask();

    TradeCustomer getTradeCustomer();
}
