package com.zhongmei.bty.basemodule.trade.operates;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.zhongmei.bty.basemodule.discount.entity.DiscountShop;
import com.zhongmei.bty.basemodule.discount.enums.DiscountType;
import com.zhongmei.bty.basemodule.notifycenter.bean.NotifyOtherClientPayedVo;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.DeliveryOrderVo;
import com.zhongmei.bty.basemodule.trade.bean.SenderOrderItem;
import com.zhongmei.bty.basemodule.trade.bean.TakeOutVo;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradePendingVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.Invoice;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.bty.basemodule.trade.entity.TradeExtraSecrecyPhone;
import com.zhongmei.bty.basemodule.trade.entity.TradeInvoice;
import com.zhongmei.bty.basemodule.trade.entity.TradeInvoiceNo;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRelExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeMainSubRelation;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;
import com.zhongmei.bty.basemodule.trade.enums.TradeDeliveryStatus;
import com.zhongmei.bty.basemodule.trade.message.TradeBatchUnbindCouponReq;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.commonmodule.database.entity.DeliveryOrder;
import com.zhongmei.bty.commonmodule.database.entity.TradeItemLog;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.orm.DatabaseHelper;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface TradeDal extends IOperates {


    TradeVo findTrade(String tradeUuid) throws Exception;


    Trade findOnlyTrade(String tradeUuid) throws Exception;


    TradeVo findTrade(String tradeUuid, boolean onlyValid) throws Exception;


    TradeVo findTrade(Long tradeId) throws Exception;


    TradeVo findTrade(Long tradeId, boolean onlyValid) throws Exception;

    TradeVo findTrade(Trade trade) throws Exception;


    TradePaymentVo findTradPayment(String tradeUuid) throws Exception;

    TradePaymentVo findTradPayment(String tradeUuid, boolean onlyValid) throws Exception;

    TradePaymentVo findTradPayment(Trade trade) throws Exception;

    TradePaymentVo findTradPaymentByRelateTradeUuid(String relateTradeUuid) throws Exception;


    List<PaymentVo> listPayment(String tradeUuid) throws Exception;

    List<PaymentVo> listPayment(String tradeUuid, PaymentType paymentType) throws Exception;


    List<PaymentVo> listAdjustPayment() throws Exception;


    Pair<Integer, List<TradeVo>> listPending() throws Exception;


    List<TradeVo> loadMore(TradePendingVo lastVo) throws Exception;


    long countPending() throws Exception;


    List<Trade> listRefundFailed() throws Exception;


    void insert(TradeVo tradeVo) throws Exception;


    void delete(String tradeUuid) throws Exception;


    List<TradeVo> findTradeVoList(TradeDeliveryStatus status) throws Exception;


    List<TradeVo> findTradeVoTakeOut() throws Exception;


    TakeOutVo findTradeVoTakeOutNew() throws Exception;


    List<TradeVo> findAutoRejectRecord(long startTime, long endTime, long maxRows) throws Exception;


    TradeVo findTradeVoByPhone(String phone) throws Exception;


    List<TradeVo> findTradeVoBySenderId(String senderId) throws Exception;


    List<SenderOrderItem> findSenderOrderByUserId(String userId) throws Exception;


    TradeStatusLog findTradeStatusLog(String uuid) throws Exception;


    List<TradeStatusLog> findTradeStatusLog(String tradeUuid, TradeStatus tradeStatus) throws Exception;


    long countUnProcessed(BusinessType businessType) throws Exception;


    long countUnProcessedAndPizzaHut(BusinessType businessType) throws Exception;


    long countUnProcessed() throws Exception;

    List<TradeVo> findTradeVoList(TradeStatus status) throws SQLException;


    List<TradeVo> findTradeVoList(TradeStatus status, SourceId sourceId) throws SQLException;


    List<Trade> findTradeList(TradeStatus status) throws SQLException;


    List<Trade> findTradeList(TradeStatus status, SourceId sourceId) throws SQLException;


    List<TradeVo> findPizzaHutTradeVoList() throws SQLException;


    List<Trade> findPizzaHutTradeList() throws SQLException;


    Trade findRepeatTrade(String tradeUuid) throws Exception;


    TradeItem findRejectTradeItem(String relateTradeItemUuid) throws Exception;


    TradeItem findTradeItem(Long id) throws SQLException;


    public TradeVo findTradeById(long id) throws Exception;


    public List<Trade> listSplitTrades(String sourceTradeUuid) throws Exception;


    public List<DiscountShop> findDiscountByType(DiscountType type) throws Exception;

    String findTradeItemBrandTypeByTradeUUID(String tradeItemSkuUUID) throws Exception;




    List<TradeReturnInfo> queryTradeReturnInfo() throws Exception;


    List<TradeReturnInfo> queryTradeAgreeReturnInfo() throws Exception;


    TradeReturnInfo findTradeReturnInfo(Long tradeId) throws Exception;


    Invoice findInvoice(Long tradeId) throws Exception;


    Invoice findInvoice(String uuid) throws Exception;


    Trade getTrade(Long tradeId) throws SQLException;

    Trade getTradeByTableId(Long tableId) throws  SQLException;


    List<Trade> getTrade(List<Long> tradeIds, List<TradeStatus> tradeStatuses) throws SQLException;


    Trade getTrade(String tradeUuid) throws SQLException;


    TradeExtra getTradeExtra(String tradeUuid) throws Exception;


    TradeExtra getTradeExtra(Long tradeId) throws Exception;


    List<TradeExtra> getTradeExtra(List<Long> tradeIds) throws SQLException;


    List<TradeItem> listTradeItemByTradeUuid(String tradeUuid) throws SQLException;


    List<TradeItem> listTradeItemByTradeId(Long tradeId) throws SQLException;


    List<TradeItem> listTradeItem(Iterable<Long> tradeItemIds) throws SQLException;


    TradeDeposit getTradeDepositByUuid(String uuid) throws SQLException;



    long countUntreated() throws Exception;


    long countUntreated(SourceId sourceId) throws Exception;


    long countOtherUntreated(List<SourceId> ids) throws Exception;


    Map<Integer, Long> countUntreatedGroupBySource() throws Exception;


    List<NotifyOtherClientPayedVo> findOtherClientPayedVos() throws SQLException, ParseException;


    List<Long> searchTableWithDish(List<String> dishUuids, BusinessType businessType) throws Exception;


    List<DeliveryOrder> listDeliveryCancelOrders() throws SQLException;


    List<Trade> listWaitingDeliveryOrders() throws SQLException;


    TradeInvoice getTradeInvoiceById(Long tradeId) throws SQLException;


    List<DeliveryOrderVo> listDeliveryOrderVo(String tradeUuid) throws SQLException;


    TradePrivilege getTradePrivilege(String tradePrivilegeUuid) throws Exception;


    TradeItem getTradeItem(String tradeItemUuid) throws Exception;


    List<TradePrivilege> findTradePrivilege(String tradeUuid, boolean onlyValid) throws Exception;


    List<TradeItem> findTradeItem(String tradeUuid) throws Exception;


    void batchUnbindCoupon(TradeBatchUnbindCouponReq tradeBatchUnbindCouponReq) throws Exception;


    List<TradeCustomer> findTradeCustomer(String tradeUuid) throws Exception;


    List<TradeItemProperty> findTradeItemProperty(String tradeUuid) throws Exception;


    List<TradePlanActivity> findTradePlanActivity(String tradeUuid) throws Exception;


    List<TradeItemPlanActivity> findTradeItemPlanActivity(String tradeUuid) throws Exception;


    List<TradeReasonRel> findTradeReasonRel(String tradeUuid) throws Exception;


    List<TradeItemExtra> findTradeItemExtra(String tradeUuid) throws Exception;


    List<TradeItemLog> findTradeItemLog(String tradeUuid) throws Exception;


    Payment getPayment(String paymentUuid) throws Exception;


    List<Trade> getDinnerNotFinishTrade() throws Exception;


    TradeExtraSecrecyPhone findTradeExtraSecrecyPhone(Long tradeExtraId) throws Exception;

    List<TradeMainSubRelation> tradeMainSubRelationList() throws Exception;


    List<TradeItemMainBatchRel> getTradeItemMainBatchRelListBySubTradeId(Long subTradeId) throws SQLException;


    List<TradeItemMainBatchRelExtra> getTradeItemMainBatchRelExtraListBySubTradeId(Long subTradeId) throws SQLException;


    List<TradeVo> getTradeVosByTrades(List<Trade> trades) throws Exception;



    List<Trade> listTradeByUUID(List<String> uuids) throws Exception;


    List<Trade> getUnionTradesByTrade(Trade trade) throws Exception;


    TradeMainSubRelation getTradeMainSubRelationBySubTrade(Trade trade) throws Exception;


    List<TradeMainSubRelation> getTradeSubRelationByMainTrade(Long mainTradeId) throws Exception;


    Map<Long, BigDecimal> getTradeItemQuantityMap(Long subTradeId) throws Exception;


    @NonNull
    List<TradeTable> getTradeTablesByMainTrade(DatabaseHelper helper, Long tradeId) throws SQLException;


    TradeInvoiceNo findTradeInvoiceNoByTradeId(Long tradeId);
}
