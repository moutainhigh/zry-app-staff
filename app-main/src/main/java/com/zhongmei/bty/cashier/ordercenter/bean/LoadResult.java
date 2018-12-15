package com.zhongmei.bty.cashier.ordercenter.bean;

import android.util.SparseArray;

import com.zhongmei.bty.basemodule.trade.entity.Invoice;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.util.List;
import java.util.Map;

/**
 * 订单详情界面加载数据
 */

public class LoadResult {
    //订单数据
    private TradePaymentVo mTradePaymentVo;

    //退货原单数据
    private TradePaymentVo oriTradePaymentVo;

    //与订单相关的反结账数据
    private List<TradePaymentVo> mRepeatTradePaymentVos;

    //与订单相关的拆单数据
    private List<TradePaymentVo> mSpliteTradePaymentVos;

    //退单申请数据
    private TradeReturnInfo mTradeReturnInfo;

    //订单开发票记录
    private Invoice mInvoice;

    /**
     * 达达配送是否开启
     */
    private boolean hasDaDaSwitchOn = false;

    //电子发票设置数据
    private ElectronicInvoiceVo mElectronicInvoiceVo;

    //商户开通的第三方配送平台数据,仅包含后台打开的
    private Map<Integer, PartnerShopBiz> mDeliverylatformPartnerShopBizMap;

    //商户开通的第三方配送平台数据
    private SparseArray<String> mDeliverylatformPartnerShopBizSparseArray;

    private Long lastClosingTime;

    public Long getLastClosingTime() {
        return lastClosingTime == null ? Long.valueOf(0) : lastClosingTime;
    }

    public void setLastClosingTime(Long lastClosingTime) {
        this.lastClosingTime = lastClosingTime;
    }

    public TradePaymentVo getTradePaymentVo() {
        return mTradePaymentVo;
    }

    public void setTradePaymentVo(TradePaymentVo tradePaymentVo) {
        mTradePaymentVo = tradePaymentVo;
    }

    public List<TradePaymentVo> getRepeatTradePaymentVos() {
        return mRepeatTradePaymentVos;
    }

    public void setRepeatTradePaymentVos(List<TradePaymentVo> repeatTradePaymentVos) {
        mRepeatTradePaymentVos = repeatTradePaymentVos;
    }

    public TradeReturnInfo getTradeReturnInfo() {
        return mTradeReturnInfo;
    }

    public void setTradeReturnInfo(TradeReturnInfo tradeReturnInfo) {
        mTradeReturnInfo = tradeReturnInfo;
    }

    public Invoice getInvoice() {
        return mInvoice;
    }

    public void setInvoice(Invoice invoice) {
        mInvoice = invoice;
    }

    public TradePaymentVo getOriTradePaymentVo() {
        return oriTradePaymentVo;
    }

    public void setOriTradePaymentVo(TradePaymentVo oriTradePaymentVo) {
        this.oriTradePaymentVo = oriTradePaymentVo;
    }

    public TradeVo getTradeVo() {
        if (getTradePaymentVo() != null) {
            return getTradePaymentVo().getTradeVo();
        }

        return null;
    }

    public List<PaymentVo> getPaymentVoList() {
        if (getTradePaymentVo() != null) {
            return getTradePaymentVo().getPaymentVoList();
        }

        return null;
    }

    public boolean isHasDaDaSwitchOn() {
        return hasDaDaSwitchOn;
    }

    public void setHasDaDaSwitchOn(boolean hasDaDaSwitchOn) {
        this.hasDaDaSwitchOn = hasDaDaSwitchOn;
    }

    public List<TradePaymentVo> getmSpliteTradePaymentVos() {
        return mSpliteTradePaymentVos;
    }

    public void setmSpliteTradePaymentVos(List<TradePaymentVo> mSpliteTradePaymentVos) {
        this.mSpliteTradePaymentVos = mSpliteTradePaymentVos;
    }

    public ElectronicInvoiceVo getElectronicInvoiceVo() {
        return mElectronicInvoiceVo;
    }

    public void setElectronicInvoiceVo(ElectronicInvoiceVo electronicInvoiceVo) {
        mElectronicInvoiceVo = electronicInvoiceVo;
    }

    public Map<Integer, PartnerShopBiz> getmDeliverylatformPartnerShopBizMap() {
        return mDeliverylatformPartnerShopBizMap;
    }

    public void setmDeliverylatformPartnerShopBizMap(Map<Integer, PartnerShopBiz> mDeliverylatformPartnerShopBizMap) {
        this.mDeliverylatformPartnerShopBizMap = mDeliverylatformPartnerShopBizMap;
    }

    public SparseArray<String> getDeliverylatformPartnerShopBizSparseArray() {
        return mDeliverylatformPartnerShopBizSparseArray;
    }

    public void setDeliverylatformPartnerShopBizSparseArray(SparseArray<String> deliverylatformPartnerShopBizSparseArray) {
        this.mDeliverylatformPartnerShopBizSparseArray = deliverylatformPartnerShopBizSparseArray;
    }
}
