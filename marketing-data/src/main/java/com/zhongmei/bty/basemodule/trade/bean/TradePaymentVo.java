package com.zhongmei.bty.basemodule.trade.bean;

import android.util.Log;

import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.db.enums.TradeType;

import java.util.ArrayList;
import java.util.List;

/**
 * @version: 1.0
 * @date 2015年5月13日
 */
public class TradePaymentVo implements java.io.Serializable {
    private static final String TAG = TradePaymentVo.class.getSimpleName();

    private static final long serialVersionUID = 1L;

    private TradeVo tradeVo;

    private TradeUnionType tradeUnionType = TradeUnionType.NONE;

    private List<PaymentVo> paymentVoList;

    private List<DeliveryOrderVo> mDeliveryOrderVoList;

    public TradeVo getTradeVo() {
        return tradeVo;
    }

    public void setTradeVo(TradeVo tradeVo) {
        this.tradeVo = tradeVo;
    }

    public TradeUnionType getTradeUnionType() {
        return tradeUnionType;
    }

    public void setTradeUnionType(TradeUnionType tradeUnionType) {
        this.tradeUnionType = tradeUnionType;
    }

    public List<PaymentVo> getPaymentVoList() {
        return paymentVoList;
    }

    public void setPaymentVoList(List<PaymentVo> paymentVoList) {
        this.paymentVoList = paymentVoList;
    }

    public List<DeliveryOrderVo> getDeliveryOrderVoList() {
        return mDeliveryOrderVoList;
    }

    public void setDeliveryOrderVoList(List<DeliveryOrderVo> deliveryOrderVoList) {
        mDeliveryOrderVoList = deliveryOrderVoList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tradeVo == null) ? 0 : tradeVo.hashCode());
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
        TradePaymentVo other = (TradePaymentVo) obj;
        if (tradeVo == null) {
            if (other.tradeVo != null)
                return false;
        } else if (!tradeVo.equals(other.tradeVo))
            return false;
        return true;
    }

    @Override
    public TradePaymentVo clone() {
        TradePaymentVo tradePaymentVo = new TradePaymentVo();
        try {
            //拷贝tradeVo
            tradePaymentVo.setTradeVo(tradeVo.clone());
            //拷贝paymentVoList
            if (paymentVoList != null) {
                List<PaymentVo> paymentVos = new ArrayList<PaymentVo>();
                for (PaymentVo paymentVo : paymentVoList) {
                    paymentVos.add(paymentVo.clone());
                }
                tradePaymentVo.setPaymentVoList(paymentVos);
            }
            //拷贝mDeliveryOrderVoList
            if (mDeliveryOrderVoList != null) {
                List<DeliveryOrderVo> deliveryOrderVos = new ArrayList<DeliveryOrderVo>();
                for (DeliveryOrderVo deliveryOrderVo : mDeliveryOrderVoList) {
                    deliveryOrderVos.add(deliveryOrderVo.clone());
                }
                tradePaymentVo.setDeliveryOrderVoList(deliveryOrderVos);
            }
            return tradePaymentVo;
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error!", e);
        }

        return null;
    }
}
