package com.zhongmei.bty.data.operates.impl;


import com.zhongmei.beauty.utils.BeautyServerAddressUtil;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.pay.message.NPayReq;
import com.zhongmei.bty.basemodule.pay.message.NPaymentItemReq;
import com.zhongmei.bty.basemodule.pay.message.NPaymentReq;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.TradePayStateReq;
import com.zhongmei.bty.basemodule.trade.processor.PayRespProcessor;
import com.zhongmei.bty.snack.offline.Snack;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.yunfu.http.OpsRequest.Executor;
import com.zhongmei.yunfu.resp.ResponseListener;

import java.util.List;


public class NewTradeOperatesImpl extends TradeOperatesImpl {

    public NewTradeOperatesImpl(ImplContext context) {
        super(context);
    }

    public void newpay(Trade trade, NPaymentReq paymentReq, ResponseListener<PayResp> listener) {

    }


    public void beautyPay(Trade trade, NPaymentReq paymentReq, ResponseListener<PayResp> listener) {
        String url = BeautyServerAddressUtil.pay();
        executorPay(url, new NPayReq(), trade, paymentReq).execute(listener, "beautyPay");
    }

    private <T extends NPayReq> Executor<T, PayResp> executorPay(String url, T req, Trade trade, NPaymentReq paymentReq) {
        req.setTradeInfo(trade);
        req.setPayment(paymentReq);
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setOperateId(user.getId());
            req.setOperateName(user.getName());
        }
        boolean enable = Snack.isSnackBusiness(trade)
                && Snack.isOfflineEnable()
                && Snack.isOfflineTrade(trade);
        OpsRequest.Executor<T, PayResp> executor = OpsRequest.Executor.create(url);
        return executor.requestValue(req)
                .responseProcessor(new PayRespProcessor())
                .responseClass(PayResp.class)
                .timeout(18000)
                .interceptEnable(enable);
    }

    @Deprecated
    public void getOnlinePayStatus(long pItemId, ResponseListener<PayResp> listener) {
        getOnlinePayStatus(null, pItemId, null, listener);
    }

    public void getOnlinePayStatus(TradeVo tradeVo, long paymentItemId, String paymentItemUuid, ResponseListener<PayResp> listener) {
        TradePayStateReq payStateReq = new TradePayStateReq();
        payStateReq.setTradeId(tradeVo.getTrade().getId());
        payStateReq.setPaymentItemId(paymentItemId);



        String url = ServerAddressUtil.getInstance().getPayStatus();
        OpsRequest.Executor<TradePayStateReq, PayResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(payStateReq)
                .responseProcessor(new PayRespProcessor())
                .responseClass(PayResp.class)
                                .execute(listener, "getOnlinePayStatus");
    }


    public void getOnlinePayStatusOfThird(TradeVo tradeVo, long paymentItemId, String paymentItemUuid, ResponseListener<PayResp> listener) {
        TradePayStateReq payStateReq = new TradePayStateReq();
        payStateReq.setTradeId(tradeVo.getTrade().getId());
        payStateReq.setPaymentItemId(paymentItemId);



        String url = ServerAddressUtil.getInstance().getPayStatusOfThird();
        OpsRequest.Executor<TradePayStateReq, PayResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(payStateReq)
                .responseProcessor(new PayRespProcessor())
                .responseClass(PayResp.class)
                                .execute(listener, "getOnlinePayStatusOfThird");
    }





    public static void saveVerifyPayResp(PayResp resp) {
        try {
            new PayRespProcessor().saveToDatabase(resp);
        } catch (Exception e) {
                        e.printStackTrace();
        }
    }

        public static boolean isMemberPay(List<NPaymentItemReq> paidItems) {
        if (!Utils.isEmpty(paidItems)) {
            for (PaymentItem item : paidItems) {
                if (PayModeId.MEMBER_CARD.value().equals(item.getPayModeId()) ||
                        PayModeId.ENTITY_CARD.value().equals(item.getPayModeId())
                        || PayModeId.ANONYMOUS_ENTITY_CARD.value().equals(item.getPayModeId())
                        ) {
                    return true;
                }
            }
        }
        return false;
    }
}
