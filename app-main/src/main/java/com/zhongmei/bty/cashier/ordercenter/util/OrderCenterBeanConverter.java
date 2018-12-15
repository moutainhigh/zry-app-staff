package com.zhongmei.bty.cashier.ordercenter.util;

import android.support.annotation.NonNull;

import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.BatchDeliveryFee;
import com.zhongmei.bty.basemodule.trade.message.BatchQueryDeliveryFeeReq;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchReq;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderListReq;
import com.zhongmei.yunfu.context.util.ArgsUtils;
import com.zhongmei.bty.cashier.ordercenter.bean.DispatchFailOrder;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrderCenterBeanConverter {
    private OrderCenterBeanConverter() {
    }

    @NonNull
    public static BatchQueryDeliveryFeeReq createBatchQueryDeliveryFeeReq(@NotNull List<TradeVo> tradeVos, @NotNull PartnerShopBiz partnerShopBiz) {
        BatchQueryDeliveryFeeReq req = new BatchQueryDeliveryFeeReq();
        req.setBrandId(BaseApplication.sInstance.getBrandIdenty());
        req.setShopId(BaseApplication.getInstance().getShopIdenty());
        req.setDeliveryPlatform(partnerShopBiz.getSource());
        final List<BatchQueryDeliveryFeeReq.DeliverFeeOrder> deliverFeeOrders = new ArrayList<>();
        for (TradeVo tradeVo : tradeVos) {
            BatchQueryDeliveryFeeReq.DeliverFeeOrder order = new BatchQueryDeliveryFeeReq.DeliverFeeOrder();
            order.setTradeId(tradeVo.getTrade().getId());
            order.setTradeNo(tradeVo.getTrade().getTradeNo());
            order.setThirdTranNo(tradeVo.getTradeExtra().getThirdTranNo());
            deliverFeeOrders.add(order);
        }
        req.setOrders(deliverFeeOrders);
        return req;
    }

    @NonNull
    public static DeliveryOrderListReq createDeliveryOrderListReq(List<TradeVo> tradeVos) {
        DeliveryOrderListReq req = new DeliveryOrderListReq();
        req.setBrandId(BaseApplication.sInstance.getBrandIdenty());
        req.setShopId(BaseApplication.getInstance().getShopIdenty());
        List<Long> orderIds = new ArrayList<>();
        for (TradeVo tradeVo : tradeVos) {
            orderIds.add(tradeVo.getTrade().getId());
        }
        req.setOrderIds(orderIds);
        return req;
    }

    @NonNull
    public static DeliveryOrderListReq createDeliveryOrderListReq(List<TradeVo> tradeVos, List<DeliveryOrderDispatchResp.FailOrder> failOrders) {
        List<Long> failOrderIds = new ArrayList<>();
        if (Utils.isNotEmpty(failOrders)) {
            for (DeliveryOrderDispatchResp.FailOrder failOrder : failOrders) {
                failOrderIds.add(failOrder.getOrderId());
            }
        }
        DeliveryOrderListReq req = createDeliveryOrderListReq(tradeVos);
        req.getOrderIds().removeAll(failOrderIds);
        return req;
    }

    public static List<DispatchFailOrder> listDispatchFailOrder(List<TradeVo> tradeVos, List<DeliveryOrderDispatchResp.FailOrder> failOrders) {
        List<DispatchFailOrder> dispatchFailOrders = new ArrayList<DispatchFailOrder>();

        if (Utils.isNotEmpty(tradeVos) && Utils.isNotEmpty(failOrders)) {
            for (DeliveryOrderDispatchResp.FailOrder failOrder : failOrders) {
                DispatchFailOrder dispatchFailOrder = new DispatchFailOrder();
                dispatchFailOrder.setReason(failOrder.getReason());
                for (TradeVo tradeVo : tradeVos) {
                    if (tradeVo.getTrade() != null
                            && failOrder.getOrderId() != null
                            && failOrder.getOrderId().equals(tradeVo.getTrade().getId())) {
                        dispatchFailOrder.setTradeNo(tradeVo.getTrade().getTradeNo());
                    }
                }
                dispatchFailOrders.add(dispatchFailOrder);
            }
        }

        return dispatchFailOrders;
    }

    @NonNull
    public static DeliveryOrderDispatchReq createDeliveryOrderDispatchReq(List<TradeVo> tradeVos, List<BatchDeliveryFee> deliveryFees, Integer deliveryPlatform) {
        DeliveryOrderDispatchReq req = new DeliveryOrderDispatchReq();
        req.setBrandId(BaseApplication.sInstance.getBrandIdenty());
        req.setShopId(BaseApplication.sInstance.getShopIdenty());
        req.setDeliveryPlatform(deliveryPlatform);
        List<DeliveryOrderDispatchReq.DispatchOrder> dispatchOrders = new ArrayList<>();
        for (TradeVo tradeVo : tradeVos) {
            DeliveryOrderDispatchReq.DispatchOrder order = new DeliveryOrderDispatchReq.DispatchOrder();
            order.setIsResend(0);
            order.setOrderType(1);
            order.setOrderNo(tradeVo.getTrade().getTradeNo());
            order.setOrderId(tradeVo.getTrade().getId());
            //list不会很大，不考虑效率问题
            if (!ArgsUtils.isEmpty(deliveryFees)) {
                for (BatchDeliveryFee deliveryFee : deliveryFees) {
                    if (order.getOrderId().equals(deliveryFee.getTradeId())) {
                        order.setDeliveryFee(deliveryFee.getFee());
                        break;
                    }
                }
            }
            dispatchOrders.add(order);
        }
        req.setOrders(dispatchOrders);
        return req;
    }
}