package com.zhongmei.bty.cashier.ordercenter.manager;

import android.util.Log;

import com.google.gson.JsonArray;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.yunfu.util.EmptyUtils;
import com.zhongmei.yunfu.resp.data.GatewayTransferResp;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.BatchQueryDeliveryFeeReq;
import com.zhongmei.bty.basemodule.trade.message.BindOrderReq;
import com.zhongmei.bty.basemodule.trade.message.BindOrderResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchReq;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderListReq;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderListResp;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.data.db.common.OrderNotify;
import com.zhongmei.bty.data.operates.CallDishNotifyOperates;
import com.zhongmei.bty.data.operates.impl.CallDishNotifyOperatesImpl;

import java.util.List;



public class SnackOrderCenterListManager extends OrderCenterListManagerNew {
    private static final String TAG = SnackOrderCenterListManager.class.getSimpleName();

    @Override
    public void notifyVoice(int type, String tradeUuid, String mobile, String serialNo, String tradeNo, ResponseListener<OrderNotify> listener) {
        CallDishNotifyOperatesImpl.NotifyReq notifyReq = toNotifyReq(type, tradeUuid, mobile, serialNo, tradeNo);
        CallDishNotifyOperates callDishNotifyOperates = OperatesFactory.create(CallDishNotifyOperates.class);
        callDishNotifyOperates.notifyVoice(notifyReq, listener);
    }

    private CallDishNotifyOperatesImpl.NotifyReq toNotifyReq(int type, String tradeUuid, String mobile, String serialNo, String tradeNo) {
        CallDishNotifyOperatesImpl.NotifyReq notifyReq = new CallDishNotifyOperatesImpl.NotifyReq();
        notifyReq.setType(type);
        notifyReq.setTradeUuid(tradeUuid);
        notifyReq.setMobile(mobile);
        notifyReq.setSerialNo(serialNo);
        notifyReq.setTradeNo(tradeNo);
        return notifyReq;
    }

    @Override
    public void deliveryPayment(List<TradeVo> tradeVos, ResponseListener<TradePaymentResp> listener) {
        mTradeOperates.deliveredPayment(tradeVos, listener);
    }

    @Override
    public void batchQueryDeliveryFee(BatchQueryDeliveryFeeReq req, ResponseListener<GatewayTransferResp<JsonArray>> listener) {
        mTradeOperates.batchQueryDeliveryFee(req, listener);
    }

    @Override
    public void bindDeliveryUser(List<TradeVo> tradeVos, User authUser, ResponseListener<BindOrderResp> listener) {
        mTradeOperates.bindDeliveryUser(toBindOrderReq(tradeVos, authUser), listener);
    }

    @Override
    public void deliveryOrderDispatch(DeliveryOrderDispatchReq req, ResponseListener<GatewayTransferResp<DeliveryOrderDispatchResp>> listener) {
        mTradeOperates.deliveryOrderDispatch(req, listener);
    }

    @Override
    public void deliveryOrderList(DeliveryOrderListReq req, ResponseListener<GatewayTransferResp<DeliveryOrderListResp>> listener) {
        mTradeOperates.deliveryOrderList(req, listener);
    }

    private BindOrderReq toBindOrderReq(List<TradeVo> tradeVos, User authUser) {
        BindOrderReq req = new BindOrderReq();
        if (authUser != null) {
            req.setUserId(authUser.getId());
            req.setUserName(authUser.getName());
        }
        req.setTradeInfos(tradeVos);
        return req;
    }

    @Override
    public Where<Trade, String> generateTradeBusinessTypeWhere(QueryBuilder<Trade, String> queryBuilder) throws Exception {
        return queryBuilder.where().in(Trade.$.businessType, BusinessType.SNACK, BusinessType.TAKEAWAY);
    }

    @Override
    public boolean isOpenKoubeiBiz() {
        DatabaseHelper helper = DBHelperManager.getHelper();
        List<PartnerShopBiz> bizs = null;
        try {
            Dao<PartnerShopBiz, String> dao = helper.getDao(PartnerShopBiz.class);
            bizs = dao.queryBuilder().where()
                    .eq(PartnerShopBiz.$.bizType, 1)
                    .and()
                    .eq(PartnerShopBiz.$.source, SourceId.KOU_BEI.value())
                    .and()
                    .eq(PartnerShopBiz.$.enableFlag, StatusFlag.VALID)
                    .and()
                    .eq(PartnerShopBiz.$.statusFlag, StatusFlag.VALID)
                    .query();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return EmptyUtils.isNotEmpty(bizs);
    }
}
