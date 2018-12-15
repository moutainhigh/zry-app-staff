package com.zhongmei.bty.basemodule.trade.message;

import java.util.List;

/**
 * 订单下发配送平台返回体
 */

public class DeliveryOrderDispatchResp {
    private List<FailOrder> failOrders;

    public List<FailOrder> getFailOrders() {
        return failOrders;
    }

    public void setFailOrders(List<FailOrder> failOrders) {
        this.failOrders = failOrders;
    }

    public class FailOrder {
        //订单ID
        private Long orderId;
        //失败原因
        private String reason;

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
