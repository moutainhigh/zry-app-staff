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

/**
 * 订单中心订单列表Model，业务处理类主要负责数据库操作、网络操作
 */

public interface IOrderCenterListManager {
    /**
     * 语音叫号
     *
     * @param type      1=微信叫号,2=IVR叫号
     * @param tradeUuid 订单UUID
     * @param mobile    客户手机号
     * @param serialNo  订单流水号
     * @param tradeNo   订单号
     * @param listener  响应回调
     */
    void notifyVoice(int type, String tradeUuid, String mobile, String serialNo, String tradeNo, ResponseListener<OrderNotify> listener);

    /**
     * 批量清账
     *
     * @param tradeVos 需要清账的单据列表
     * @param listener 响应回调
     */
    void deliveryPayment(List<TradeVo> tradeVos, ResponseListener<TradePaymentResp> listener);

    /**
     * 正餐批量清账
     *
     * @param tradeVos 需要清账的单据列表
     * @param listener 响应回调
     */
    void clearAccounts(List<TradeVo> tradeVos, ResponseListener<TradePaymentResp> listener);

    /**
     * 批量绑定配送员
     *
     * @param tradeVos 订单列表
     * @param authUser 配送员
     * @param listener listener
     */
    void bindDeliveryUser(List<TradeVo> tradeVos, User authUser, ResponseListener<BindOrderResp> listener);

    /**
     * 根据关键字搜索符合条件的订单数据(部分数据，会阻塞主线程)
     *
     * @param tab       一级分栏
     * @param position  选中的item的位置
     * @param keyword   搜索关键字
     * @param condition 筛选条件
     * @param lastData  最后一条数据
     * @return 符合条件的订单数据
     */
    List<TradePaymentVo> search(int tab, int position, String keyword, FilterCondition condition, Trade lastData);

    /**
     * 查询订单数据(部分数据，会阻塞主线程)
     *
     * @param childTab  二级分栏
     * @param condition 条件
     * @param lastData  最后一条数据
     * @return 符合条件的订单数据
     */
    List<TradePaymentVo> loadData(int childTab, FilterCondition condition, Trade lastData);

    /**
     * 返回tab下订单数量
     *
     * @param tab
     * @return
     */
    long countOrder(int tab);

    /**
     * 创建业务类型的过滤条件
     *
     * @param queryBuilder queryBuilder
     * @return Where
     */
    Where<Trade, String> generateTradeBusinessTypeWhere(QueryBuilder<Trade, String> queryBuilder) throws Exception;

    /**
     * 获取桌台设置
     */
    List<TableNumberSetting> getTableNumberSetting();

    void batchQueryDeliveryFee(BatchQueryDeliveryFeeReq req, ResponseListener<GatewayTransferResp<JsonArray>> listener);

    void deliveryOrderDispatch(DeliveryOrderDispatchReq req, ResponseListener<GatewayTransferResp<DeliveryOrderDispatchResp>> listener);

    /**
     * 通过订单list 查询订单的配送信息，在下发配送后调用
     *
     * @param req
     * @param listener
     */
    void deliveryOrderList(DeliveryOrderListReq req, ResponseListener<GatewayTransferResp<DeliveryOrderListResp>> listener);

    /**
     * v8.12.0 是否开通口碑业务
     */
    boolean isOpenKoubeiBiz();

}

