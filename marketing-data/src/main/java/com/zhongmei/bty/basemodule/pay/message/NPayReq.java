package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.db.entity.trade.Trade;

/**
 * v3支付请求专用
 * Created by demo on 2018/12/15
 */

public class NPayReq {
    private long operateId;
    private String operateName;
    private TradeInfoReq trade;
    private NPaymentReq payment;

    public TradeInfoReq getTrade() {
        return trade;
    }

    public void setTradeInfo(Trade trade) {
        if (trade != null) {
            this.trade = new TradeInfoReq();
            if (trade.getServerUpdateTime() != null)
                this.trade.serverUpdateTime = trade.getServerUpdateTime();
            if (trade.getId() != null) {
                this.trade.id = trade.getId();
            }
            this.trade.tradeUuid = trade.getUuid();
            this.trade.businessType = trade.getBusinessType().value();
            this.trade.tradeNo = trade.getTradeNo();
        }
    }

    public long getOperateId() {
        return operateId;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateId(long operateId) {
        this.operateId = operateId;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public NPaymentReq getPayment() {
        return payment;
    }

    public void setPayment(NPaymentReq payment) {
        this.payment = payment;
    }

    public static class TradeInfoReq {
        private long id;
        private long serverUpdateTime;
        private String tradeUuid;//可选参数，服务端接口不需要这个参数，这个参数是异步机制需要的
        private Integer businessType;//可选参数，服务端接口不需要这个参数，这个参数是异步机制需要的
        private String tradeNo;//订单号

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getServerUpdateTime() {
            return serverUpdateTime;
        }

        public void setServerUpdateTime(long serverUpdateTime) {
            this.serverUpdateTime = serverUpdateTime;
        }

        public String getTradeUuid() {
            return tradeUuid;
        }

        public void setTradeUuid(String tradeUuid) {
            this.tradeUuid = tradeUuid;
        }

        public Integer getBusinessType() {
            return businessType;
        }

        public void setBusinessType(Integer businessType) {
            this.businessType = businessType;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }
    }
}
