package com.zhongmei.bty.cashier.ordercenter.manager;

import com.google.gson.JsonArray;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.yunfu.resp.data.GatewayTransferResp;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.BatchQueryDeliveryFeeReq;
import com.zhongmei.bty.basemodule.trade.message.BindOrderResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchReq;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderListReq;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderListResp;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.bty.cashier.ordercenter.bean.FilterCondition;
import com.zhongmei.bty.commonmodule.database.entity.TableNumberSetting;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.data.db.common.OrderNotify;

import java.util.List;



public interface IOrderCenterListManager {

    void notifyVoice(int type, String tradeUuid, String mobile, String serialNo, String tradeNo, ResponseListener<OrderNotify> listener);


    void deliveryPayment(List<TradeVo> tradeVos, ResponseListener<TradePaymentResp> listener);


    void clearAccounts(List<TradeVo> tradeVos, ResponseListener<TradePaymentResp> listener);


    void bindDeliveryUser(List<TradeVo> tradeVos, User authUser, ResponseListener<BindOrderResp> listener);


    List<TradePaymentVo> search(int tab, int position, String keyword, FilterCondition condition, Trade lastData);


    List<TradePaymentVo> loadData(int childTab, FilterCondition condition, Trade lastData);


    long countOrder(int tab);


    Where<Trade, String> generateTradeBusinessTypeWhere(QueryBuilder<Trade, String> queryBuilder) throws Exception;


    List<TableNumberSetting> getTableNumberSetting();

    void batchQueryDeliveryFee(BatchQueryDeliveryFeeReq req, ResponseListener<GatewayTransferResp<JsonArray>> listener);

    void deliveryOrderDispatch(DeliveryOrderDispatchReq req, ResponseListener<GatewayTransferResp<DeliveryOrderDispatchResp>> listener);


    void deliveryOrderList(DeliveryOrderListReq req, ResponseListener<GatewayTransferResp<DeliveryOrderListResp>> listener);


    boolean isOpenKoubeiBiz();

}

