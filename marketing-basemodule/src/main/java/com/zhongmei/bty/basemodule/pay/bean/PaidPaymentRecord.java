package com.zhongmei.bty.basemodule.pay.bean;

import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.context.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class PaidPaymentRecord implements Serializable {
    private List<PaymentVo> paymentVoList;
    private double mPaidValue = 0;    private double mPaidExemptAmount = 0;    private List<PaymentItem> items = new ArrayList<PaymentItem>(2);
    private Payment mPayment = null;
    public void setPaidPayment(List<PaymentVo> paidPayments) {
        this.paymentVoList = paidPayments;
        this.sumPaiAmount();
    }

    private void sumPaiAmount() {
        this.mPaidExemptAmount = 0;
        this.mPaidValue = 0;
        this.items.clear();
        this.mPayment = null;
        if (!Utils.isEmpty(paymentVoList)) {
            Payment payment = null;
            for (PaymentVo vo : paymentVoList) {
                payment = vo.getPayment();
                                if (payment != null && payment.getStatusFlag() == StatusFlag.VALID && payment.getActualAmount().doubleValue() >= 0) {
                    mPayment = payment;
                    if (payment.getExemptAmount() != null) {
                        mPaidExemptAmount += payment.getExemptAmount().doubleValue();
                    }
                }
                if (!Utils.isEmpty(vo.getPaymentItemList())) {
                    for (PaymentItem item : vo.getPaymentItemList()) {
                        if (item.getPayStatus() == TradePayStatus.PAID && item.getStatusFlag() == StatusFlag.VALID && item.getUsefulAmount() != null) {
                            mPaidValue += item.getUsefulAmount().doubleValue();
                            items.add(item);
                        }
                    }
                }
            }
        }
    }

    public void addPaymentRecord(PaymentVo paymentVo) {        if (paymentVo == null || Utils.isEmpty(paymentVo.getPaymentItemList())) return;

        if (Utils.isEmpty(paymentVoList)) {            paymentVoList = new ArrayList<PaymentVo>(2);
            paymentVoList.add(paymentVo);
        } else {
            if (paymentVoList.contains(paymentVo)) {                for (PaymentVo pv : paymentVoList) {
                    if (pv.equals(paymentVo)) {                        if (Utils.isEmpty(pv.getPaymentItemList())) {
                            pv.setPaymentItemList(paymentVo.getPaymentItemList());
                        } else {
                            for (PaymentItem paymentItem : paymentVo.getPaymentItemList()) {
                                if (!pv.getPaymentItemList().contains(paymentItem)) {
                                    pv.getPaymentItemList().add(paymentItem);
                                }
                            }
                        }
                        break;
                    }
                }
            } else {                paymentVoList.add(paymentVo);
            }
        }
        this.sumPaiAmount();    }


    public List<PaymentVo> getPaidPayments() {
        return this.paymentVoList;
    }

    public List<PaymentItem> getPaidPaymentItems() {

        return items;
    }

    public Payment getPaidPayment() {
                if (Utils.isEmpty(this.paymentVoList) || this.paymentVoList.size() == 1) {
            return mPayment;
        } else {
                        for (PaymentVo vo : paymentVoList) {
                mPayment = vo.getPayment();
                if (vo.getPaidAmount() > 0)
                    break;
            }
            return mPayment;
        }
            }


    public double getPaidAmount() {        return mPaidValue;
    }


    public double getActualPaidAmount() {
        return mPaidValue + mPaidExemptAmount;
    }

    public double getPaiExemptAmount() {        return mPaidExemptAmount;
    }
}
