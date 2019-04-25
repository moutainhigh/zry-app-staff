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

/**
 * @Date： 16/11/29
 * @Description:
 * @Version: 1.0
 */
public class NewTradeOperatesImpl extends TradeOperatesImpl {

    public NewTradeOperatesImpl(ImplContext context) {
        super(context);
    }

    public void newpay(Trade trade, NPaymentReq paymentReq, ResponseListener<PayResp> listener) {
        /*String url = ServerAddressUtil.getInstance().newPayUrl();
        executorPay(url, new NPayReq(), trade, paymentReq).execute(listener, "newPay");*/
    }

    /**
     * 美业收银
     *
     * @param trade
     * @param paymentReq
     * @param listener
     */
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

        /*PayStatusReq req = new PayStatusReq();
        req.setPaymentItemId(paymentItemId);
        req.setPaymentItemUuid(paymentItemUuid);
        req.setReturnPayment(true);
        boolean enable = Snack.isOfflineEnable()
                && Snack.isOfflineTrade(tradeVo);*/

        String url = ServerAddressUtil.getInstance().getPayStatus();
        OpsRequest.Executor<TradePayStateReq, PayResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(payStateReq)
                .responseProcessor(new PayRespProcessor())
                .responseClass(PayResp.class)
                //.interceptEnable(enable)
                .execute(listener, "getOnlinePayStatus");
    }

    /**
     * 获取第三方法支付状态
     *
     * @param tradeVo
     * @param paymentItemId
     * @param paymentItemUuid
     * @param listener
     */
    public void getOnlinePayStatusOfThird(TradeVo tradeVo, long paymentItemId, String paymentItemUuid, ResponseListener<PayResp> listener) {
        TradePayStateReq payStateReq = new TradePayStateReq();
        payStateReq.setTradeId(tradeVo.getTrade().getId());
        payStateReq.setPaymentItemId(paymentItemId);

        /*PayStatusReq req = new PayStatusReq();
        req.setPaymentItemId(paymentItemId);
        req.setPaymentItemUuid(paymentItemUuid);
        req.setReturnPayment(true);
        boolean enable = Snack.isOfflineEnable()
                && Snack.isOfflineTrade(tradeVo);*/

        String url = ServerAddressUtil.getInstance().getPayStatusOfThird();
        OpsRequest.Executor<TradePayStateReq, PayResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(payStateReq)
                .responseProcessor(new PayRespProcessor())
                .responseClass(PayResp.class)
                //.interceptEnable(enable)
                .execute(listener, "getOnlinePayStatusOfThird");
    }

    /*private static class PayProcessor extends SaveDatabaseResponseProcessor<PayResp> {

        protected boolean isSuccessful(ResponseObject<PayResp> response) {
            return ResponseObject.isOk(response) || ResponseObject.isExisted(response) || response.getStatusCode() == ResponseObject.BAINUOCOUPONSPARTOK || (response.getContent() != null && !Utils.isEmpty(response.getContent().getPayments()));
        }

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final PayResp resp) {
            return new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, Trade.class, resp.getTrades());
                    DBHelperManager.saveEntities(helper, TradeTable.class, resp.getTradeTables());
                    DBHelperManager.saveEntities(helper, Tables.class, resp.getTables());
                    DBHelperManager.saveEntities(helper, Payment.class, resp.getPayments());
                    DBHelperManager.saveEntities(helper, PaymentItem.class, resp.getPaymentItems());
                    DBHelperManager.saveEntities(helper, TradePromotion.class, resp.getTradePromotions());
                    DBHelperManager.saveEntities(helper, PaymentItemExtra.class, resp.getPaymentItemExtras());
                    DBHelperManager.saveEntities(helper, TradeStatusLog.class, resp.getTradeStatusLogs());
                    DBHelperManager.saveEntities(helper, PrintOperation.class, resp.getPrintOperations());
                    DBHelperManager.saveEntities(helper, PaymentItemUnionpay.class, resp.getPaymentItemUnionpays());//add 20170313
                    DBHelperManager.saveEntities(helper, TradeDepositPayRelation.class, resp.getTradeDepositPayRelations());//add 20170707
                    DBHelperManager.saveEntities(helper, PaymentItemGrouponDish.class, resp.getPaymentItemGrouponDishes());//add 20171122
                    savePriveleResult(helper, resp.getTradePrivilegeResults());
                    return null;
                }
            };
        }

        private void savePriveleResult(DatabaseHelper helper, List<PayResp.PrivilegeRes> privilegeResList) throws Exception {
            if (Utils.isEmpty(privilegeResList)) {
                return;
            }
            for (PayResp.PrivilegeRes privilegeRes : privilegeResList) {
                if (privilegeRes.getTradePrivilegeExtra() == null) {
                    continue;
                }
                DBHelperManager.saveEntities(helper, TradePrivilegeExtra.class, privilegeRes.getTradePrivilegeExtra());
            }
        }
    }*/


    /**
     * 同步第三方支付保存到数据库
     *
     * @Return void 返回类型
     */
    public static void saveVerifyPayResp(PayResp resp) {
        try {
            new PayRespProcessor().saveToDatabase(resp);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //判断是否是储值支付
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
