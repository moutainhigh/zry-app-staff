package com.zhongmei.bty.basemodule.pay.bean;

import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.context.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 已经支付的信息
 */

public class PaidPaymentRecord implements Serializable {
    private List<PaymentVo> paymentVoList;
    private double mPaidValue = 0;//已支付金额
    private double mPaidExemptAmount = 0;//已抹零金额
    private List<PaymentItem> items = new ArrayList<PaymentItem>(2);
    private Payment mPayment = null;//已付payment

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
                //数据库是根据时间升序，遍历后找到最新的支付记录
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

    public void addPaymentRecord(PaymentVo paymentVo) {//add v8.4 分布支付成功后调用
        if (paymentVo == null || Utils.isEmpty(paymentVo.getPaymentItemList())) return;

        if (Utils.isEmpty(paymentVoList)) {//支付为空，全新记录
            paymentVoList = new ArrayList<PaymentVo>(2);
            paymentVoList.add(paymentVo);
        } else {
            if (paymentVoList.contains(paymentVo)) {//已经有支付记录
                for (PaymentVo pv : paymentVoList) {
                    if (pv.equals(paymentVo)) {//查找相同的记录
                        if (Utils.isEmpty(pv.getPaymentItemList())) {
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
            } else {//新记录
                paymentVoList.add(paymentVo);
            }
        }
        this.sumPaiAmount();//计算已支付金额
    }


    public List<PaymentVo> getPaidPayments() {
        return this.paymentVoList;
    }

    public List<PaymentItem> getPaidPaymentItems() {

        return items;
    }

    public Payment getPaidPayment() {
        //modify  20170801 begin
        if (Utils.isEmpty(this.paymentVoList) || this.paymentVoList.size() == 1) {
            return mPayment;
        } else {
            //如果有多个payment，获取最新的，如果其中有已经支付的，优先取已经支付的。
            for (PaymentVo vo : paymentVoList) {
                mPayment = vo.getPayment();
                if (vo.getPaidAmount() > 0)
                    break;
            }
            return mPayment;
        }
        //modify  20170801 end
    }


    public double getPaidAmount() {//已支付金额（已经到账）
        return mPaidValue;
    }

    /**
     * 包括抹零的金额
     */
    public double getActualPaidAmount() {
        return mPaidValue + mPaidExemptAmount;
    }

    public double getPaiExemptAmount() {//已经抹零金额
        return mPaidExemptAmount;
    }
}
