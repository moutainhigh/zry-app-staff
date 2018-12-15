package com.zhongmei.bty.basemodule.pay.bean;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.bty.basemodule.pay.entity.RefundExceptionReason;
import com.zhongmei.bty.basemodule.pay.message.PaymentItemUnionpayVoReq;
import com.zhongmei.yunfu.util.Beans;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**

 */
public class PaymentVo implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 支付主单
     */
    private Payment payment;

    /**
     * 支付明细列表
     */
    private List<PaymentItem> paymentItemList;

    private List<PaymentItemExtra> paymentItemExtraList;

    private List<PaymentItemUnionpayVoReq> paymentCards;// 银联刷卡记录  add 20160218

    private Map<Long, List<RefundExceptionReason>> refundExceptionReasonMap;//退款失败原因，key为PaymentItem的id

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<PaymentItem> getPaymentItemList() {
        return paymentItemList;
    }

    public void setPaymentItemList(List<PaymentItem> paymentItemList) {
        this.paymentItemList = paymentItemList;
    }

    public List<PaymentItemExtra> getPaymentItemExtraList() {
        return paymentItemExtraList;
    }

    public void setPaymentItemExtraList(List<PaymentItemExtra> paymentItemExtraList) {
        this.paymentItemExtraList = paymentItemExtraList;
    }

    public Map<Long, List<RefundExceptionReason>> getRefundExceptionReasonMap() {
        return refundExceptionReasonMap;
    }

    public void setRefundExceptionReasonMap(Map<Long, List<RefundExceptionReason>> refundExceptionReasonMap) {
        this.refundExceptionReasonMap = refundExceptionReasonMap;
    }

    public double getSubmitPayValue() {//获取上行提交的应付金额
        double payValue = 0;
        if (!Utils.isEmpty(paymentItemList)) {
            for (PaymentItem item : paymentItemList) {
                if (item.getUsefulAmount() != null) {
                    payValue += item.getUsefulAmount().doubleValue();
                }
            }
        }
        return payValue;
    }

    public double getPaidAmount() {//add 20170801获取已付金额
        double payValue = 0;
        if (!Utils.isEmpty(paymentItemList)) {
            for (PaymentItem item : paymentItemList) {
                if (item.getPayStatus() == TradePayStatus.PAID && item.getStatusFlag() == StatusFlag.VALID && item.getUsefulAmount() != null) {
                    payValue += item.getUsefulAmount().doubleValue();
                }
            }
        }
        return payValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((payment == null) ? 0 : payment.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PaymentVo other = (PaymentVo) obj;
        if (payment == null) {
            if (other.payment != null)
                return false;
        } else if (!payment.equals(other.payment))
            return false;
        return true;
    }

    public List<PaymentItemUnionpayVoReq> getPaymentCards() {
        return paymentCards;
    }

    public void setPaymentCards(List<PaymentItemUnionpayVoReq> paymentCards) {
        this.paymentCards = paymentCards;
    }

    @Override
    public PaymentVo clone() throws CloneNotSupportedException {
        try {
            PaymentVo paymentVo = new PaymentVo();
            //拷贝payment
            paymentVo.setPayment(copyEntity(payment, new Payment()));
            //拷贝paymentItemList
            if (paymentItemList != null) {
                List<PaymentItem> paymentItems = new ArrayList<PaymentItem>();
                for (PaymentItem paymentItem : paymentItemList) {
                    paymentItems.add(copyEntity(paymentItem, new PaymentItem()));
                }
                paymentVo.setPaymentItemList(paymentItems);
            }
            //拷贝paymentItemExtraList
            if (paymentItemExtraList != null) {
                List<PaymentItemExtra> paymentItemExtras = new ArrayList<PaymentItemExtra>();
                for (PaymentItemExtra paymentItemExtra : paymentItemExtras) {
                    paymentItemExtras.add(copyEntity(paymentItemExtra, new PaymentItemExtra()));
                }
                paymentVo.setPaymentItemExtraList(paymentItemExtras);
            }
            //拷贝paymentCards
            if (paymentCards != null) {
                List<PaymentItemUnionpayVoReq> paymentCardList = new ArrayList<PaymentItemUnionpayVoReq>();
                for (PaymentItemUnionpayVoReq paymentCard : paymentCards) {
                    paymentCardList.add(copyEntity(paymentCard, new PaymentItemUnionpayVoReq()));
                }
                paymentVo.setPaymentCards(paymentCardList);
            }

            return paymentVo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static <T> T copyEntity(T source, T target) throws Exception {
        Beans.copyProperties(source, target);
        return target;
    }
}
