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

/**
 * @version: 1.0
 * @date 2015年5月8日
 */
public interface TradeDal extends IOperates {

    /**
     * 返回指定trade.uuid的交易记录。此方法会阻塞调用线程
     *
     * @param tradeUuid
     * @return
     * @throws Exception
     */
    TradeVo findTrade(String tradeUuid) throws Exception;

    /**
     * 返回指定trade.uuid的trade。此方法会阻塞调用线程
     *
     * @param tradeUuid
     * @return
     * @throws Exception
     */
    Trade findOnlyTrade(String tradeUuid) throws Exception;

    /**
     * 返回指定trade.uuid的交易记录。此方法会阻塞调用线程
     *
     * @param tradeUuid
     * @param onlyValid 为true时只包含有效的明细记录
     * @return
     * @throws Exception
     */
    TradeVo findTrade(String tradeUuid, boolean onlyValid) throws Exception;

    /**
     * 通过id查询tradevo
     *
     * @Title: findTrade
     * @Param @param tradeId
     * @Param @return
     * @Return TradeVo 返回类型
     */
    TradeVo findTrade(Long tradeId) throws Exception;

    /**
     * 通过id查询tradevo
     *
     * @Title: findTrade
     * @Param @param tradeId
     * @Param @param onlyValid
     * @Param @return
     * @Return TradeVo 返回类型
     */
    TradeVo findTrade(Long tradeId, boolean onlyValid) throws Exception;

    TradeVo findTrade(Trade trade) throws Exception;

    /**
     * 返回指定trade.uuid的交易及支付记录。此方法会阻塞调用线程
     *
     * @param tradeUuid
     * @return
     * @throws Exception
     */
    TradePaymentVo findTradPayment(String tradeUuid) throws Exception;

    TradePaymentVo findTradPayment(String tradeUuid, boolean onlyValid) throws Exception;

    TradePaymentVo findTradPayment(Trade trade) throws Exception;

    TradePaymentVo findTradPaymentByRelateTradeUuid(String relateTradeUuid) throws Exception;

    /**
     * 返回指定trade.uuid的支付列表。此方法会阻塞调用线程
     *
     * @param tradeUuid
     * @return
     * @throws Exception
     */
    List<PaymentVo> listPayment(String tradeUuid) throws Exception;

    List<PaymentVo> listPayment(String tradeUuid, PaymentType paymentType) throws Exception;

    /**
     * @return
     * @throws Exception
     */
    List<PaymentVo> listAdjustPayment() throws Exception;

    /**
     * 返回挂单列表。此方法会阻塞调用线程
     *
     * @return 总挂单数, 前10条tradeVo
     * @throws Exception
     */
    Pair<Integer, List<TradeVo>> listPending() throws Exception;

    /**
     * 加载更多挂单。
     *
     * @param lastVo 最后一条tradePendingVo
     * @return 5条更多数据
     * @throws Exception
     */
    List<TradeVo> loadMore(TradePendingVo lastVo) throws Exception;

    /**
     * 返回挂单总数。此方法会阻塞调用线程
     *
     * @return
     * @throws Exception
     */
    long countPending() throws Exception;

    /**
     * 列出退款失败的订单。只包含本Pad退货的并使用了微信支付的订单
     *
     * @return
     * @throws Exception
     */
    List<Trade> listRefundFailed() throws Exception;

    /**
     * 保存tradeVo到数据库。此方法会阻塞调用线程
     *
     * @param tradeVo
     * @throws Exception
     */
    void insert(TradeVo tradeVo) throws Exception;

    /**
     * 将指定的tradeVo记录从数据库中删除。此方法会阻塞调用线程
     *
     * @param tradeUuid
     * @throws Exception
     */
    void delete(String tradeUuid) throws Exception;

    /**
     * 根据外卖单状态类型查询交易记录
     *
     * @param status
     * @return
     */
    List<TradeVo> findTradeVoList(TradeDeliveryStatus status) throws Exception;

    /**
     * 查找外卖单 只有trade和tradeExtra信息
     *
     * @param
     * @return
     */
    List<TradeVo> findTradeVoTakeOut() throws Exception;

    /**
     * 查找外卖单 只有trade和tradeExtra信息
     *
     * @param
     * @return
     */
    TakeOutVo findTradeVoTakeOutNew() throws Exception;

    /**
     * 查找一笔自动拒绝的记录
     *
     * @param startTime 开始时间戳
     * @param endTime   结束时间戳
     * @param maxRows   最大行数
     * @return TradeVo
     * @throws Exception
     */
    List<TradeVo> findAutoRejectRecord(long startTime, long endTime, long maxRows) throws Exception;

    /**
     * 通过电话号码查询外卖单据
     *
     * @param phone
     * @return
     * @throws Exception
     */
    TradeVo findTradeVoByPhone(String phone) throws Exception;

    /**
     * 根据送餐员id查询未清账的外卖单据
     *
     * @Title: findTradeVoBySenderId
     * @Description:
     * @Param @param senderId
     * @Param @return
     * @Param @throws Exception
     * @Return List<TradeVo> 返回类型
     */
    List<TradeVo> findTradeVoBySenderId(String senderId) throws Exception;

    /**
     * 通过外卖员id查找单据
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<SenderOrderItem> findSenderOrderByUserId(String userId) throws Exception;

    /**
     * 查找单据接受时间
     *
     * @param uuid
     * @return
     * @throws Exception
     */
    TradeStatusLog findTradeStatusLog(String uuid) throws Exception;

    /**
     * 根据tradeUuid获取TradeStatusLog列表
     *
     * @param tradeUuid   订单唯一标识uuid
     * @param tradeStatus 订单状态
     * @return
     * @throws Exception
     */
    List<TradeStatusLog> findTradeStatusLog(String tradeUuid, TradeStatus tradeStatus) throws Exception;

    /**
     * 返回未处理丁丁总数
     *
     * @param businessType 对应业务类型
     * @return
     */
    long countUnProcessed(BusinessType businessType) throws Exception;

    /**
     * 返回未处理订单和必胜客订单总数
     *
     * @param businessType 对应业务类型
     * @return
     */
    long countUnProcessedAndPizzaHut(BusinessType businessType) throws Exception;

    /**
     * 返回快餐未处理订单总数。此方法会阻塞调用线程
     *
     * @return
     * @throws Exception
     */
    long countUnProcessed() throws Exception;

    List<TradeVo> findTradeVoList(TradeStatus status) throws SQLException;

    /**
     * 查询指定订单状态和来源的订单，来源为null时，表示任何来源
     *
     * @Title: findTradeVoList
     * @Param @param status
     * @Param @param sourceId
     * @Param @return
     * @Return List<TradeVo> 返回类型
     */
    List<TradeVo> findTradeVoList(TradeStatus status, SourceId sourceId) throws SQLException;

    /**
     * 查询指定类型的订单
     *
     * @Title: findTradeList
     * @Param @param status
     * @Param @return
     * @Return List<Trade> 返回类型
     */
    List<Trade> findTradeList(TradeStatus status) throws SQLException;

    /**
     * 查询指定订单状态和来源的订单，来源为null时，表示任何来源
     *
     * @Title: findTradeList
     * @Param @param status
     * @Param @param sourceId
     * @Param @return
     * @Return List<Trade> 返回类型
     */
    List<Trade> findTradeList(TradeStatus status, SourceId sourceId) throws SQLException;

    /**
     * 查询必胜客订单
     *
     * @Title: findTradeVoList
     * @Param @param status
     * @Param @param sourceId 父级来源，为null时，表示任何来源
     * @Param @param sourceChild 子级来源，为null时，表示任何来源
     * @Param @return
     * @Return List<TradeVo> 返回类型
     */
    List<TradeVo> findPizzaHutTradeVoList() throws SQLException;

    /**
     * 查询必胜客订单
     *
     * @Title: findPizzaHutTradeList
     * @Param @return
     * @Return List<Trade> 返回类型
     */
    List<Trade> findPizzaHutTradeList() throws SQLException;

    /**
     * 通过原单uuid找出反结账后的单据
     *
     * @param tradeUuid
     * @return
     * @throws Exception
     */
    Trade findRepeatTrade(String tradeUuid) throws Exception;

    /**
     * @param
     * @return
     * @throws Exception
     * @description 通过关联relateTradeItemUuid找出原单tradeItem
     * <p>
     * href="mailto:zhengx">zhengxl<
     * /a>
     * @data 2015年12月17日TODO
     */
    TradeItem findRejectTradeItem(String relateTradeItemUuid) throws Exception;

    /**
     * 根据id查询tradeitem
     *
     * @Title: findTradeItem
     * @Param @param id
     * @Param @return
     * @Return TradeItem 返回类型
     */
    TradeItem findTradeItem(Long id) throws SQLException;

    /**
     * 通过id获取交易数据
     *
     * @param id
     * @return
     * @throws Exception
     */
    public TradeVo findTradeById(long id) throws Exception;

    /**
     * @Description: 根据原单uuid 查询拆出的子单
     * @Param String sourceTradeUuid
     * @Param @return
     * @Param @throws Exception
     * @Return List<Trade> 返回类型
     */
    public List<Trade> listSplitTrades(String sourceTradeUuid) throws Exception;

    /**
     * 通过折扣标签查询折扣列表
     *
     * @Title: findDiscountByType
     * @Param @param type
     * @Return List<DiscountShop> 返回类型
     */
    public List<DiscountShop> findDiscountByType(DiscountType type) throws Exception;

    String findTradeItemBrandTypeByTradeUUID(String tradeItemSkuUUID) throws Exception;

    //根据SkuUUID，获取对应的菜品id，
    //DishItemTypeAndSort findTradeItemBrandIDByTradeUUID(String tradeItemSkuUUID) throws Exception;

    //根据tradeItemI获取篮子名称
    //DishItemTypeAndSort findTradeItemPkgName(Long tradeItemId) throws Exception;

    /**
     * 获取所有退票申请
     */
    List<TradeReturnInfo> queryTradeReturnInfo() throws Exception;

    /**
     * 获取所有同意退单
     */
    List<TradeReturnInfo> queryTradeAgreeReturnInfo() throws Exception;

    /**
     * 根据TradeId查询退单申请
     *
     * @param tradeId 订单Id
     * @return
     * @throws Exception
     */
    TradeReturnInfo findTradeReturnInfo(Long tradeId) throws Exception;

    /**
     * 根据TradeId查询开发票记录
     *
     * @param tradeId
     * @return
     * @throws Exception
     */
    Invoice findInvoice(Long tradeId) throws Exception;

    /**
     * 根据uuid查询对应的开发票记录
     *
     * @param uuid
     * @return
     * @throws Exception
     */
    Invoice findInvoice(String uuid) throws Exception;

    /**
     * 根据id获取trade对象
     *
     * @Title: getTrade
     * @Param @param tradeId
     * @Param @return
     * @Return Trade 返回类型
     */
    Trade getTrade(Long tradeId) throws SQLException;

    /**
     * 根据id获取trade对象
     *
     * @param tradeIds 订单Id列表
     */
    List<Trade> getTrade(List<Long> tradeIds, List<TradeStatus> tradeStatuses) throws SQLException;

    /**
     * 根据tradeUuid获取trade对象
     */
    Trade getTrade(String tradeUuid) throws SQLException;

    /**
     * 根据tradeUuid获取TradeExtra
     *
     * @param tradeUuid 订单唯一标识tradeUuid
     * @return
     * @throws Exception
     */
    TradeExtra getTradeExtra(String tradeUuid) throws Exception;

    /**
     * 根据tradeUuid获取TradeExtra
     *
     * @param tradeId
     * @return
     * @throws Exception
     */
    TradeExtra getTradeExtra(Long tradeId) throws Exception;

    /**
     * 根据id获取tradeExtra对象
     *
     * @param tradeIds 订单Id列表
     */
    List<TradeExtra> getTradeExtra(List<Long> tradeIds) throws SQLException;

    /**
     * 根据订单id查询对应的tradeitem
     *
     * @Title: listTradeItemByTradeUuid
     * @Param @param tradeUuid
     * @Param @return
     * @Return List<TradeItem> 返回类型
     */
    List<TradeItem> listTradeItemByTradeUuid(String tradeUuid) throws SQLException;

    /**
     * 根据订单id查询对应的tradeitemList
     *
     * @param tradeId
     * @return
     * @throws SQLException
     */
    List<TradeItem> listTradeItemByTradeId(Long tradeId) throws SQLException;

    /**
     * 查询tradeitem列表
     *
     * @Title: listTradeItem
     * @Param @param tradeItemIds
     * @Param @return
     * @Return List<TradeItem> 返回类型
     */
    List<TradeItem> listTradeItem(Iterable<Long> tradeItemIds) throws SQLException;

    /**
     * 根据UUID查询订单的押金信息
     */
    TradeDeposit getTradeDepositByUuid(String uuid) throws SQLException;


    /**
     * 所有未处理订单的数量
     *
     * @return
     * @throws Exception
     */
    long countUntreated() throws Exception;

    /**
     * 某种来源的未处理订单数量
     *
     * @param sourceId
     * @return
     * @throws Exception
     */
    long countUntreated(SourceId sourceId) throws Exception;

    /**
     * 《不包含》来源的未处理订单
     *
     * @param ids 不需要查询的类型
     * @return
     * @throws Exception
     */
    long countOtherUntreated(List<SourceId> ids) throws Exception;

    /**
     * 按来源分组统计未处理订单数目
     *
     * @return
     * @throws Exception
     */
    Map<Integer, Long> countUntreatedGroupBySource() throws Exception;

    /**
     * 查询微信端支付订单的tradetale列表
     *
     * @return
     * @throws SQLException
     */
    List<NotifyOtherClientPayedVo> findOtherClientPayedVos() throws SQLException, ParseException;


    List<Long> searchTableWithDish(List<String> dishUuids, BusinessType businessType) throws Exception;

    /**
     * 查询配送异常配送单数据
     */
    List<DeliveryOrder> listDeliveryCancelOrders() throws SQLException;

    /**
     * 查询未下发 派送 订单
     *
     * @return
     */
    List<Trade> listWaitingDeliveryOrders() throws SQLException;

    /**
     * 根据tradeId查询发票信息
     */
    TradeInvoice getTradeInvoiceById(Long tradeId) throws SQLException;

    /**
     * 根据订单UUID查询相关配送数据
     *
     * @param tradeUuid 订单UUID
     * @return
     * @throws SQLException
     */
    List<DeliveryOrderVo> listDeliveryOrderVo(String tradeUuid) throws SQLException;

    /**
     * 根据tradePrivilegeUuid获取TradePrivilege
     *
     * @param tradePrivilegeUuid TradePrivilege唯一标识uuid
     * @return
     * @throws Exception
     */
    TradePrivilege getTradePrivilege(String tradePrivilegeUuid) throws Exception;

    /**
     * 根据tradeItemUuid获取TradeItem
     *
     * @param tradeItemUuid tradeItem唯一标识uuid
     * @return
     * @throws Exception
     */
    TradeItem getTradeItem(String tradeItemUuid) throws Exception;

    /**
     * 根据tradeUuid获取TradePrivilege列表
     *
     * @param tradeUuid 订单唯一标识uuid
     * @param onlyValid 仅有效数据
     * @return
     * @throws Exception
     */
    List<TradePrivilege> findTradePrivilege(String tradeUuid, boolean onlyValid) throws Exception;

    /**
     * 根据tradeUuid获取TradeItem列表
     *
     * @param tradeUuid 订单唯一标识uuid
     * @return
     * @throws Exception
     */
    List<TradeItem> findTradeItem(String tradeUuid) throws Exception;

    /**
     * 批量解绑优惠券。此方法会阻塞调用线程
     *
     * @param tradeBatchUnbindCouponReq
     * @throws Exception
     */
    void batchUnbindCoupon(TradeBatchUnbindCouponReq tradeBatchUnbindCouponReq) throws Exception;

    /**
     * 根据tradeUuid获取TradeCustomer列表
     *
     * @param tradeUuid 订单唯一标识uuid
     * @return
     * @throws Exception
     */
    List<TradeCustomer> findTradeCustomer(String tradeUuid) throws Exception;

    /**
     * 根据tradeUuid获取TradeItemProperty列表
     *
     * @param tradeUuid 订单唯一标识uuid
     * @return
     * @throws Exception
     */
    List<TradeItemProperty> findTradeItemProperty(String tradeUuid) throws Exception;

    /**
     * 根据tradeUuid获取TradePlanActivity列表
     *
     * @param tradeUuid 订单唯一标识uuid
     * @return
     * @throws Exception
     */
    List<TradePlanActivity> findTradePlanActivity(String tradeUuid) throws Exception;

    /**
     * 根据tradeUuid获取TradeItemPlanActivity列表
     *
     * @param tradeUuid 订单唯一标识uuid
     * @return
     * @throws Exception
     */
    List<TradeItemPlanActivity> findTradeItemPlanActivity(String tradeUuid) throws Exception;

    /**
     * 根据tradeUuid获取TradeReasonRel列表
     *
     * @param tradeUuid 订单唯一标识uuid
     * @return
     * @throws Exception
     */
    List<TradeReasonRel> findTradeReasonRel(String tradeUuid) throws Exception;

    /**
     * 根据tradeUuid获取TradeItemExtra列表
     *
     * @param tradeUuid 订单唯一标识uuid
     * @return
     * @throws Exception
     */
    List<TradeItemExtra> findTradeItemExtra(String tradeUuid) throws Exception;

    /**
     * 根据tradeUuid获取TradeItemLog列表
     *
     * @param tradeUuid 订单唯一标识uuid
     * @return
     * @throws Exception
     */
    List<TradeItemLog> findTradeItemLog(String tradeUuid) throws Exception;

    /**
     * 根据paymentUuid获取Payment对象
     *
     * @param paymentUuid
     * @return
     * @throws Exception
     */
    Payment getPayment(String paymentUuid) throws Exception;

    /**
     * 获取未结账已确认的订单
     *
     * @return
     * @throws Exception
     */
    List<Trade> getDinnerNotFinishTrade() throws Exception;

    /**
     * 根据TradeExtra Id查找第三方隐私手机号
     *
     * @param tradeExtraId TradeExtra Id
     * @return TradeExtraSecrecyPhone
     */
    TradeExtraSecrecyPhone findTradeExtraSecrecyPhone(Long tradeExtraId) throws Exception;

    List<TradeMainSubRelation> tradeMainSubRelationList() throws Exception;

    /**
     * 根据联台子单id，查询联台菜品关联关系记录
     *
     * @param subTradeId
     * @return
     * @throws SQLException
     */
    List<TradeItemMainBatchRel> getTradeItemMainBatchRelListBySubTradeId(Long subTradeId) throws SQLException;

    /**
     * 根据联台子单id，查询联台菜品属性关联关系记录
     *
     * @param subTradeId
     * @return
     * @throws SQLException
     */
    List<TradeItemMainBatchRelExtra> getTradeItemMainBatchRelExtraListBySubTradeId(Long subTradeId) throws SQLException;

    /**
     * 根据订单，查询所有订单详情信息
     *
     * @param trades
     * @return
     */
    List<TradeVo> getTradeVosByTrades(List<Trade> trades) throws Exception;


    /**
     * 根据订单的uuid查询
     *
     * @param uuids
     * @return
     * @throws Exception
     */
    List<Trade> listTradeByUUID(List<String> uuids) throws Exception;

    /**
     * 根据联台主单、子单获取联台所有订单add v8.4
     *
     * @return
     * @throws Exception
     */
    List<Trade> getUnionTradesByTrade(Trade trade) throws Exception;

    /**
     * 根据联台子单获取联台主单
     *
     * @return
     * @throws Exception
     */
    TradeMainSubRelation getTradeMainSubRelationBySubTrade(Trade trade) throws Exception;

    /**
     * 根据主单获取子单列表
     *
     * @param mainTradeId
     * @return
     * @throws Exception
     */
    List<TradeMainSubRelation> getTradeSubRelationByMainTrade(Long mainTradeId) throws Exception;

    /**
     * 获取主单子单菜品关系表中子单上批量菜的数量
     *
     * @param subTradeId
     * @return
     * @throws Exception
     */
    Map<Long, BigDecimal> getTradeItemQuantityMap(Long subTradeId) throws Exception;

    /**
     * 根据主单查询桌台信息
     */
    @NonNull
    List<TradeTable> getTradeTablesByMainTrade(DatabaseHelper helper, Long tradeId) throws SQLException;

    /**
     * 根据订单id查询税号
     */
    TradeInvoiceNo findTradeInvoiceNoByTradeId(Long tradeId);
}
