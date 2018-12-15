package com.zhongmei.bty.data.operates.impl;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.zhongmei.bty.basemodule.booking.bean.BookingTradeItemVo;
import com.zhongmei.bty.basemodule.booking.bean.BookingVo;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.bty.basemodule.booking.manager.BookingTablesManager;
import com.zhongmei.bty.basemodule.booking.message.BookingAndTableReq;
import com.zhongmei.bty.basemodule.booking.message.BookingAndTableResp;
import com.zhongmei.bty.basemodule.booking.message.BookingConfirmArrivalShopResp;
import com.zhongmei.bty.basemodule.booking.message.BookingGroupTableReq;
import com.zhongmei.bty.basemodule.booking.message.BookingGroupTableResq;
import com.zhongmei.bty.basemodule.booking.message.BookingGroupTradeResp;
import com.zhongmei.bty.basemodule.booking.message.BookingListReq;
import com.zhongmei.bty.basemodule.booking.message.BookingListResp;
import com.zhongmei.bty.basemodule.booking.message.BookingObjectResp;
import com.zhongmei.bty.basemodule.booking.message.BookingQueryNumReq;
import com.zhongmei.bty.basemodule.booking.message.BookingQueryNumResp;
import com.zhongmei.bty.basemodule.booking.message.BookingReq;
import com.zhongmei.bty.basemodule.booking.message.BookingResp;
import com.zhongmei.bty.basemodule.booking.message.BookingStatisticsResp;
import com.zhongmei.bty.basemodule.booking.message.BookingTableReq;
import com.zhongmei.bty.basemodule.booking.operates.BookingOperates;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.entity.TaxRateInfo;
import com.zhongmei.yunfu.http.CalmNetWorkRequest;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.yunfu.http.OpsRequest.SaveDatabaseResponseProcessor;
import com.zhongmei.yunfu.http.processor.CalmDatabaseProcessor;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.bty.basemodule.trade.bean.TradeGroupInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.enums.TradeScenceType;
import com.zhongmei.bty.basemodule.trade.enums.TradeUserType;
import com.zhongmei.bty.basemodule.trade.message.OpenTableResp;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.booking.utils.BookingOpenTableUtils;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.entity.Period;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.TradeEarnestMoney;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.bty.commonmodule.database.entity.TradeItemLog;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.db.enums.BookingType;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.data.operates.message.content.BookingDetailReq;
import com.zhongmei.bty.data.operates.message.content.BookingDetailResp;
import com.zhongmei.bty.data.operates.message.content.BookingGroupOpenInfoReq;
import com.zhongmei.bty.data.operates.message.content.BookingGroupOpenReq;
import com.zhongmei.bty.data.operates.message.content.BookingNewReq;
import com.zhongmei.bty.data.operates.message.content.BookingPeriodReq;
import com.zhongmei.bty.data.operates.message.content.BookingQueryReq;
import com.zhongmei.bty.data.operates.message.content.BookingTableGroupReq;
import com.zhongmei.bty.data.operates.message.content.BookingToDinnerSubmitReq;
import com.zhongmei.bty.data.operates.message.content.BookingToUnionSubmitReq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

/**
 * 预订相关接口
 */
@SuppressLint("SimpleDateFormat")
public class BookingOperatesImpl extends AbstractOpeartesImpl implements BookingOperates {

    private static final String TAG = BookingOperatesImpl.class.getSimpleName();

    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public BookingOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void insertOrModify(BookingVo bookingVo, ResponseListener<BookingResp> listener) {
        String url = ServerAddressUtil.getInstance().creatOrUpdateBooking();

        BookingReq bookingReq = toBookingReq(bookingVo);
        OpsRequest.Executor<BookingReq, BookingResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(bookingReq)
                .responseClass(BookingResp.class)
                .responseProcessor(new BookingProcessor(bookingVo))
                .execute(listener, TAG);

    }

    public void submitBooking(BookingVo bookingVo, CalmResponseListener<ResponseObject<BookingResp>> listener) {
        String url = ServerAddressUtil.getInstance().createBooking();
        BookingNewReq req = toBookingNewReq(bookingVo);
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(url)
                .requestContent(req)
                .responseClass(BookingResp.class)
                .responseProcessor(new CalmDatabaseProcessor<BookingResp>() {
                    @Override
                    protected boolean isSuccessful(ResponseObject response) {
                        return super.isSuccessful(response);
                    }

                    @Override
                    protected void transactionCallable(DatabaseHelper helper, BookingResp resp) throws Exception {
                    }
                })
                .successListener(listener)
                .errorListener(listener)
                .tag("submitBooking")
//                .showLoading()
                .create();
    }

    public void updateBooking(BookingVo bookingVo, CalmResponseListener<ResponseObject<BookingResp>> listener) {
        String url = ServerAddressUtil.getInstance().updateBooking();
        BookingNewReq bookingReq = toBookingNewReq(bookingVo);
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(url)
                .requestContent(bookingReq)
                .responseClass(BookingResp.class)
                .responseProcessor(new CalmDatabaseProcessor<BookingResp>() {
                    @Override
                    protected boolean isSuccessful(ResponseObject response) {
                        return super.isSuccessful(response);
                    }

                    @Override
                    protected void transactionCallable(DatabaseHelper helper, BookingResp resp) throws Exception {
                    }
                })
                .successListener(listener)
                .errorListener(listener)
                .tag("submitBooking")
//                .showLoading()
                .create();
    }

    @Override
    public void bookingDetail(Long bookingId, CalmResponseListener<ResponseObject<BookingDetailResp>> listener) {
        final BookingDetailReq detailReq = new BookingDetailReq();
        detailReq.setBookingId(bookingId);
        String url = ServerAddressUtil.getInstance().bookingDetail();
        new CalmNetWorkRequest.Builder<BookingDetailReq, BookingDetailResp>()
                .with(BaseApplication.getInstance())
                .url(url)
                .requestContent(detailReq)
                .responseClass(BookingDetailResp.class)
                .successListener(listener)
                .errorListener(listener)
                .showLoading()
                .tag("bookingDetail")
                .create();
    }

    @Override
    public void bookingArrivalShop(Fragment fragment, BookingVo bookingVo, CalmResponseListener<ResponseObject<BookingResp>> listener) {
        String url = ServerAddressUtil.getInstance().bookingArrivalShop();
        BookingNewReq bookingReq = toBookingNewReq(bookingVo);
        //老版本数据没有BookingPeriod，设置为null，避免传空对象导致出错
        if (bookingReq.getBookingType() == BookingType.NORMAL) {
            bookingReq.setBookingPeriod(null);
        }
        //到店添加到店操作员（同步组调用loyalty逻辑走不通，周群力临上线前让加的）
        IAuthUser user = IAuthUser.Holder.get();
        if (user != null && user.getId() != null) {
            bookingReq.setShopArriveUser(user.getId().toString());
        }
        CalmNetWorkRequest.with(fragment)
                .url(url)
                .requestContent(bookingReq)
                .responseClass(BookingResp.class)
                .responseProcessor(new CalmDatabaseProcessor<BookingResp>() {
                    @Override
                    protected boolean isSuccessful(ResponseObject response) {
                        return super.isSuccessful(response);
                    }

                    @Override
                    protected void transactionCallable(DatabaseHelper helper, BookingResp resp) throws Exception {
                    }
                })
                .successListener(listener)
                .errorListener(listener)
                .tag("submitBooking")
                .showLoading()
                .create();
    }

    @Override
    public void confirmArrivalShop(Long bookingId, CalmResponseListener<ResponseObject<BookingConfirmArrivalShopResp>> listener) {
        String url = ServerAddressUtil.getInstance().bookingConfirm();
        HashMap<String, Object> bookingReq = new HashMap<>();
        bookingReq.put("bookingId", bookingId);
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(url)
                .requestContent(bookingReq)
                .responseClass(BookingConfirmArrivalShopResp.class)
                .successListener(listener)
                .errorListener(listener)
                .tag("confirmArrivalShop")
                .showLoading()
                .create();
    }

    @Override
    public void sendMessage(String orderUuid, ResponseListener<Boolean> listener) {
        String url = ServerAddressUtil.getInstance().sendBookingMessage();
        OpsRequest.Executor<Object, Boolean> executor = OpsRequest.Executor.create(url);
        executor.requestValue(new SendMessageReq(orderUuid)).responseClass(Boolean.class).execute(listener, TAG);

    }

    @Override
    public void bookingRecorde(String customerUuid, ResponseListener<BookingStatisticsResp> listener) {
        String url = ServerAddressUtil.getInstance().bookingRecorde();
        OpsRequest.Executor<BookingRecordeReq, BookingStatisticsResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(new BookingRecordeReq(customerUuid))
                .responseClass(BookingStatisticsResp.class)
                .execute(listener, TAG);
    }

    @Override
    public void queryBookingTablesByPeriod(Long orderTime, Long periodId, CalmResponseListener<ResponseObject<BookingGroupTableResq>> listener) {
        BookingGroupTableReq req = new BookingGroupTableReq();
        String time = new SimpleDateFormat("yyyy-MM-dd").format(orderTime);
        req.startTime = DateTimeUtils.formatDate(time + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        req.endTime = DateTimeUtils.formatDate(time + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        req.periodId = periodId;
        String url = ServerAddressUtil.getInstance().bookingTableListByTime();
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(url)
                .requestContent(req)
                .responseClass(BookingGroupTableResq.class)
                .responseProcessor(new CalmDatabaseProcessor<BookingGroupTableResq>() {
                    @Override
                    protected boolean isSuccessful(ResponseObject response) {
                        return super.isSuccessful(response);
                    }

                    @Override
                    protected void transactionCallable(DatabaseHelper helper, BookingGroupTableResq resp) throws Exception {
                    }
                })
                .successListener(listener)
                .errorListener(listener)
                .tag("submitBooking")
//                .showLoading()
                .create();
    }

    @Override
    public void bookingListPost(Date _calendar, CalmResponseListener<ResponseObject<BookingListResp>> listener) {
        String yyyyMMdd = DateTimeUtils.formatDate(_calendar.getTime(), "yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(DateTimeUtils.formatDate(yyyyMMdd));
        long startTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        long endTime = calendar.getTimeInMillis();
        bookingListPost(startTime, endTime, listener);
    }

    @Override
    public void bookingListPost(long startTime, long endTime, CalmResponseListener<ResponseObject<BookingListResp>> listener) {
        BookingListReq req = new BookingListReq(startTime, endTime);
        String url = ServerAddressUtil.getInstance().bookingList();
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(url)
                .requestContent(req)
                .responseClass(BookingListResp.class)
                .successListener(listener)
                .errorListener(listener)
                .tag("bookingListPost")
                .create();
    }

    @Override
    public void bookingQueryPost(String queryParam, CalmResponseListener<ResponseObject<BookingListResp>> listener) {
        BookingQueryReq req = new BookingQueryReq(queryParam);
        String url = ServerAddressUtil.getInstance().bookingQueryV1();
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(url)
                .requestContent(req)
                .responseClass(BookingListResp.class)
                .successListener(listener)
                .errorListener(listener)
                .tag("bookingQueryPost")
                .create();
    }

    @Override
    public void bookingToDinnerSubmitPost(Fragment fragment, BookingVo bookingVo, Trade trade, CalmResponseListener<ResponseObject<OpenTableResp>> listener) {
        BookingToDinnerSubmitReq req = new BookingToDinnerSubmitReq();
        copyProperties(trade, req);
        Booking booking = bookingVo.getBooking();
        BookingToDinnerSubmitReq.BookingInfoBean bookingInfoBean = new BookingToDinnerSubmitReq.BookingInfoBean();
        bookingInfoBean.bookingId = booking.getId();
        bookingInfoBean.bookingUuid = booking.getUuid();
        bookingInfoBean.bookingServerUpdateTime = booking.getServerUpdateTime();
        bookingInfoBean.shopArriveUser = Session.getAuthUser().getName();
        bookingInfoBean.shopArriveTime = new Date().getTime();
        req.bookingInfo = bookingInfoBean;
        TradeExtra tradeExtra = new TradeExtra();
        tradeExtra.validateCreate();
        tradeExtra.setTradeId(trade.getId());
        tradeExtra.setTradeUuid(trade.getUuid());
        tradeExtra.setUuid(SystemUtils.genOnlyIdentifier());
        req.tradeExtra = tradeExtra;

        //增加默认税率
        TaxRateInfo taxRateInfo = ServerSettingCache.getInstance().getmTaxRateInfo();
        if (taxRateInfo != null && taxRateInfo.isTaxSupplyOpen()) {
            TradeTax tradeTax = taxRateInfo.toTradeTax(null);
            req.tradeTaxs = Arrays.asList(tradeTax);
        }

        //加入服务费
        ExtraCharge serviceExtraCharge = ServerSettingCache.getInstance().getmServiceExtraCharge();
        if (serviceExtraCharge != null && serviceExtraCharge.isAutoJoinTrade()) {
            req.tradeInitConfigs = Arrays.asList(serviceExtraCharge.toTradeInitConfig());
        }

        String url = ServerAddressUtil.getInstance().toDinnerSubmit();
        CalmNetWorkRequest.with(fragment)
                .url(url)
                .requestContent(req)
                .responseClass(OpenTableResp.class)
                .responseProcessor(new CalmDatabaseProcessor<OpenTableResp>() {
                    @Override
                    protected boolean isSuccessful(ResponseObject response) {
                        return super.isSuccessful(response);
                    }

                    @Override
                    protected void transactionCallable(DatabaseHelper helper, OpenTableResp resp) throws Exception {
                        // 数据库操作
                        DBHelperManager.saveEntities(helper, Trade.class, resp.trades);
                        DBHelperManager.saveEntities(helper, TradeExtra.class, resp.tradeExtras);
                        DBHelperManager.saveEntities(helper, TradeCustomer.class, resp.tradeCustomers);
                        DBHelperManager.saveEntities(helper, TradePrivilege.class, resp.tradePrivileges);
                        DBHelperManager.saveEntities(helper, TradeItem.class, resp.tradeItems);
                        DBHelperManager.saveEntities(helper, TradeItemProperty.class, resp.tradeItemProperties);
                        DBHelperManager.saveEntities(helper, TradeItemLog.class, resp.tradeItemLogs);
                        DBHelperManager.saveEntities(helper, TradeStatusLog.class, resp.tradeStatusLogs);
                        DBHelperManager.saveEntities(helper, TradeTable.class, resp.tradeTables);
                        DBHelperManager.saveEntities(helper, TradeTax.class, resp.tradeTaxs);
                        DBHelperManager.saveEntities(helper, TradeInitConfig.class, resp.tradeInitConfigs);
                        DBHelperManager.saveEntities(helper, TradeEarnestMoney.class, resp.tradeEarnestMoneys);
                    }
                })
                .successListener(listener)
                .errorListener(listener)
                .showLoading()
                .tag("bookingToDinnerSubmitPost")
                .create();
    }

    @Override
    public void groupOpenTable(BookingVo bookingVo, TradeVo tradeVo, CalmResponseListener<ResponseObject<BookingGroupTradeResp>> listener) {
        BookingGroupOpenReq req = toBookingGroupOpenReq(bookingVo, tradeVo);
        //增加默认税率
        TaxRateInfo taxRateInfo = ServerSettingCache.getInstance().getmTaxRateInfo();
        if (taxRateInfo != null && taxRateInfo.isTaxSupplyOpen()) {
            TradeTax tradeTax = taxRateInfo.toTradeTax(null);
            req.tradeTaxs = Arrays.asList(tradeTax);
        }
        //加入服务费
        ExtraCharge serviceExtraCharge = ServerSettingCache.getInstance().getmServiceExtraCharge();
        if (serviceExtraCharge != null && serviceExtraCharge.isAutoJoinTrade()) {
            req.tradeInitConfigs = Arrays.asList(serviceExtraCharge.toTradeInitConfig());
        }
        String url = ServerAddressUtil.getInstance().bookingToGroupDinner();
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(url)
                .requestContent(req)
                .responseClass(BookingGroupTradeResp.class)
                .responseProcessor(new CalmDatabaseProcessor<BookingGroupTradeResp>() {
                    @Override
                    protected boolean isSuccessful(ResponseObject response) {
                        return super.isSuccessful(response);
                    }

                    @Override
                    protected void transactionCallable(DatabaseHelper helper, BookingGroupTradeResp resp) throws Exception {
                        DBHelperManager.saveEntities(helper, Trade.class, resp.trade);
                        DBHelperManager.saveEntities(helper, TradeExtra.class, resp.tradeExtra);
                        DBHelperManager.saveEntities(helper, TradeCustomer.class, resp.tradeCustomers);
                        DBHelperManager.saveEntities(helper, TradeItem.class, resp.tradeItems);
                        DBHelperManager.saveEntities(helper, TradeTable.class, resp.tradeTables);
                        DBHelperManager.saveEntities(helper, TradeGroupInfo.class, resp.tradeGroup);
                        DBHelperManager.saveEntities(helper, TradeUser.class, resp.tradeUser);
                        DBHelperManager.saveEntities(helper, TradeTax.class, resp.tradeTaxs);
                        DBHelperManager.saveEntities(helper, TradeInitConfig.class, resp.tradeInitConfigs);
                        DBHelperManager.saveEntities(helper, TradeEarnestMoney.class, resp.tradeEarnestMoneys);
                    }
                })
                .successListener(listener)
                .errorListener(listener)
                .tag("bookingToGroupDinner")
//                .showLoading()
                .create();
    }

    /**
     * 构建开台数据
     *
     * @param bookingVo
     * @param tradeVo
     * @return
     */
    private BookingGroupOpenReq toBookingGroupOpenReq(BookingVo bookingVo, TradeVo tradeVo) {
        BookingGroupOpenReq req = new BookingGroupOpenReq();
        copyProperties(tradeVo.getTrade(), req);
        req.tradeExtra = tradeVo.getTradeExtra();
        BookingGroupOpenInfoReq infoReq = new BookingGroupOpenInfoReq();
        infoReq.bookingId = bookingVo.getBooking().getId();
        infoReq.bookingUuid = bookingVo.getBooking().getUuid();
        infoReq.bookingServerUpdateTime = bookingVo.getBooking().getServerUpdateTime();
        infoReq.shopArriveUser = Session.getAuthUser().getName();
        infoReq.shopArriveTime = new Date().getTime();
        req.bookingInfo = infoReq;
        AuthUser saleAuthUser = bookingVo.getSaleAuthUser();
        if (saleAuthUser != null) {
            TradeUser tradeUser = new TradeUser();
            tradeUser.setChanged(true);
            tradeUser.setUserId(saleAuthUser.getId());
            tradeUser.setUserName(saleAuthUser.getName());
            tradeUser.setStatusFlag(StatusFlag.VALID);//默认有效
            tradeUser.setType(TradeScenceType.SALEDISH);//卖菜
            tradeUser.setTradeId(tradeVo.getTrade().getId());
            tradeUser.setTradeUuid(tradeVo.getTrade().getUuid());
            req.tradeUser = tradeUser;
        }
        return req;
    }

    /**
     * 发送短信请求
     */
    class SendMessageReq {
        private String serverId;

        public SendMessageReq(String orderUuid) {
            this.serverId = orderUuid;
        }

        public String getServerId() {
            return serverId;
        }

        public void setServerId(String serverId) {
            this.serverId = serverId;
        }

    }

    /**
     * 预订统计
     */
    class BookingRecordeReq {
        private String customerSynFlag;

        public BookingRecordeReq(String customerSynFlag) {
            this.setCustomerSynFlag(customerSynFlag);
        }

        public String getCustomerSynFlag() {
            return customerSynFlag;
        }

        public void setCustomerSynFlag(String customerSynFlag) {
            this.customerSynFlag = customerSynFlag;
        }

    }

    /**
     * 预订单转换成请求对象
     *
     * @param bookingVo
     * @return
     */
    private BookingReq toBookingReq(BookingVo bookingVo) {
        BookingDalImpl dal = new BookingDalImpl(getImplContext());
        BookingReq bookingReq = new BookingReq();
        Booking booking = bookingVo.getBooking();
//        if (booking.getCancelOrderTime() != null) {
//            bookingReq.setCancelOrderTime(dateTimeFormat.format(booking.getCancelOrderTime()));
//        }
        bookingReq.setCancelOrderUser(booking.getCancelOrderUser());
        bookingReq.setCommercialId(booking.getCommercialId());
        bookingReq.setConsumeStandard(booking.getConsumeStandard());
        bookingReq.setCreatorID(booking.getCreatorId() + "");
        bookingReq.setCommercialGroup(booking.getCommercialGroup());
        // 请求的cunstomerId是客户的uuid
        bookingReq.setCustomerID(booking.getCustomerSynflag());
        // 请求的本地用户标识是同步下来的 id
        bookingReq.setCustomerLocalID(booking.getCommercialId());
        bookingReq.setCommercialName(booking.getCommercialName());
        bookingReq.setCommercialPhone(booking.getCommercialPhone());
        bookingReq.setCustomerSynflag(booking.getCustomerSynflag());
        // bookingReq.setDeleted(booking.);
        bookingReq.setEnvFavorite(booking.getEnvFavorite());
        bookingReq.setInnerOrderPerson(booking.getInnerOrderPerson());
        bookingReq.setIsImportant(booking.getIsImportant());
        bookingReq.setLocalCreateDateTime(booking.getClientCreateTime());
        bookingReq.setLocalModifyDateTime(booking.getClientUpdateTime());
        bookingReq.setMemo(booking.getMemo());
        bookingReq.setOrderID(booking.getId());
        bookingReq.setOrderNumber(booking.getCustomerNum());
        bookingReq.setBookingSource(booking.getBookingSource());
        bookingReq.setOrderStatus(booking.getOrderStatus());
        bookingReq.setOrderTime(booking.getOrderTime());
        // 通过 periodId获取periodUUid
        try {
            Period period = dal.findPeriodById(booking.getPeriodID());
            bookingReq.setPeriodServerId(period.getUuid());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        bookingReq.setRealConsume(booking.getRealConsume());
        if (booking.getRealConsumeTime() != null) {
            bookingReq.setRealConsumeTime(dateTimeFormat.format(booking.getRealConsumeTime()));
        }
        bookingReq.setRealConsumeUser(booking.getRealConsumeUser());
        bookingReq.setRr(booking.getRefusalReason());
        bookingReq.setServerId(booking.getUuid());
        if (booking.getCommercialGender() != null) {
            bookingReq.setCommercialGender(booking.getCommercialGender());
        } else {
            bookingReq.setCommercialGender(Sex.MALE);
        }

//        if (booking.getShopArriveTime() != null) {
//            bookingReq.setShopArriveTime(dateTimeFormat.format(booking.getShopArriveTime()));
//        }
//        bookingReq.setShopArriveUser(booking.getShopArriveUser());
//        if (booking.getShopLeaveTime() != null) {
//            bookingReq.setShopLeaveTime(dateTimeFormat.format(booking.getShopLeaveTime()));
//        }
//        bookingReq.setShopLeaveUser(booking.getShopLeaveUser());

        List<BookingTable> bookingTableList = bookingVo.getBookingTableList();
        List<BookingTableReq> bookingTableReqList = new ArrayList<BookingTableReq>();
        bookingReq.setBookingTableList(bookingTableReqList);
        for (BookingTable bookingTable : bookingTableList) {
            if (bookingTable.getStatus() == 0) {
                BookingTableReq bookingTableReq = new BookingTableReq();
                bookingTableReq.setBookingTableId(bookingTable.getBtid());
                bookingTableReq.setStatus(bookingTable.getStatus());
                bookingTableReq.setTableLocalId(bookingTable.getBtid());
                try {
                    Tables table = dal.findTableById(bookingTable.getTableID());
                    bookingTableReq.setTableName(table.getTableName());
                    bookingTableReq.setTableServerId(table.getUuid());
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                bookingTableReqList.add(bookingTableReq);
            }
        }
        return bookingReq;
    }

    /**
     * 预订单传唤成新的请求
     */
    public BookingNewReq toBookingNewReq(BookingVo vo) {
        BookingNewReq req = new BookingNewReq();
        BookingDalImpl dal = new BookingDalImpl(getImplContext());
        Booking booking = vo.getBooking();
        req.setBookingType(booking.getBookingType());
        req.setConsumeStandard(booking.getConsumeStandard());
        req.setCreatorID((booking.getCreatorId()));
        req.setCommercialGroup(booking.getCommercialGroup());
        req.setCommercialName(booking.getCommercialName());
        req.setCommercialMobile(booking.getCommercialPhone());
        req.setEnvFavorite(booking.getEnvFavorite());
        req.setInnerOrderPerson(booking.getInnerOrderPerson());
        req.setIsImportant(booking.getIsImportant() == null ? 0 : booking.getIsImportant());
        req.setClientCreateTime(booking.getClientCreateTime());
        req.setClientUpdateTime(booking.getClientUpdateTime());
        req.setMemo(booking.getMemo());
        req.setOrderNumber(booking.getCustomerNum());
        req.setOrderSource(ValueEnums.toValue(booking.getBookingSource()));
        req.setOrderStatus(ValueEnums.toValue(booking.getOrderStatus()));
        req.setOrderDesc("");
        req.setOrderTime(booking.getOrderTime());
        try {
            Period period = dal.findPeriodById(booking.getPeriodID());
            req.setPeriodUuid(period.getUuid());
            req.setPeriodID(booking.getPeriodID());
            /*BookingPeriodReq periodReq = new BookingPeriodReq();
            periodReq.setStartTime(vo.getBookingPeriod().getStartTime());
			periodReq.setEndTime(vo.getBookingPeriod().getEndTime());*/
            req.setBookingPeriod(vo.getBookingPeriod());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        req.setUuid(booking.getUuid());
        req.setCommercialGender(ValueEnums.toValue(booking.getCommercialGender()));
        req.setNotifyCount(0);
        req.setBoxFirst(0);
        if (vo.getBookingTableList() != null) {
            List<BookingTableGroupReq> bookingTableGroupReqs = new ArrayList<>();
            for (BookingTable bookingTable : vo.getBookingTableList()) {
                BookingTableGroupReq reqTable = new BookingTableGroupReq();
                copyProperties(bookingTable, reqTable);
                reqTable.setId(bookingTable.getBtid());
                reqTable.setTableId(bookingTable.getTableID());
//                try {
//                    Tables table = dal.findTableById(bookingTable.getTableID());
//                    reqTable.setTableName(table.getTableName());
//                } catch (Exception e) {
//                    Log.e(TAG, e.getMessage(), e);
//                }
                bookingTableGroupReqs.add(reqTable);
            }
            req.setBookingTables(bookingTableGroupReqs);
        }
        //更新预定
        req.setBookingId(booking.getId());
//        req.setCancelOrderTime(booking.getCancelOrderTime());
        req.setCancelOrderUser(booking.getCancelOrderUser());
        req.setRealConsume(booking.getRealConsume());
        req.setRealConsumeTime(booking.getRealConsumeTime());
        req.setRealConsumeUser(booking.getRealConsumeUser());
        req.setRr(booking.getRefusalReason());
//        req.setShopArriveTime(booking.getShopArriveTime());
//        req.setShopArriveUser(booking.getShopArriveUser());
//        req.setShopLeaveTime(booking.getShopLeaveTime());
//        req.setShopLeaveUser(booking.getShopLeaveUser());
        req.setModifyDateTime(booking.getServerUpdateTime());
//        req.setCountry(booking.getCountry());
//        req.setNation(booking.getNation());
        req.setNationalTelCode(booking.getNationalTelCode());
        List<BookingTradeItemVo> bookingTradeItemVoList = vo.getTradeItemVoList();
        List<BookingTradeItem> bookingTradeItemList = new ArrayList<>();
        if (vo.getMealShellVo() != null && vo.getMealShellVo().getTradeItem() != null) {
            vo.getMealShellVo().getTradeItem().setBookingUuid(booking.getUuid());
            vo.getMealShellVo().getTradeItem().setBookingId(booking.getId());
            bookingTradeItemList.add(vo.getMealShellVo().getTradeItem());
        }
        if (Utils.isNotEmpty(bookingTradeItemVoList)) {
            List<BookingTradeItemProperty> bookingTradeItemPropertyList = new ArrayList<>();
            for (BookingTradeItemVo bookingTradeItemVo : bookingTradeItemVoList) {
                bookingTradeItemList.add(bookingTradeItemVo.getTradeItem());
                if (Utils.isNotEmpty(bookingTradeItemVo.getTradeItemPropertyList()))
                    bookingTradeItemPropertyList.addAll(bookingTradeItemVo.getTradeItemPropertyList());
            }
            req.setBookingTradeItemPropertys(bookingTradeItemPropertyList);
        }
        req.setBookingTradeItems(bookingTradeItemList);
        req.setBookingGroupInfo(vo.getBookingGroupInfo());
        req.setUserId(Session.getAuthUser() == null ? booking.getCreatorId() : Session.getAuthUser().getId());
        return req;
    }

    private BookingPeriodReq getBookingPeriod(Long orderTime, Period period) {
        BookingPeriodReq periodReq = new BookingPeriodReq();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String orderTimeStr = format1.format(new Date(orderTime));
        String orderStartTimeStr = orderTimeStr + " " + period.getPeriodStarttime();
        String orderEndTimeStr = orderTimeStr + " " + period.getPeriodEndtime();
        try {
            Date startDate = format2.parse(orderStartTimeStr);
            Date endDate = format2.parse(orderEndTimeStr);
            periodReq.setStartTime(startDate.getTime());
            periodReq.setEndTime(endDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return periodReq;
    }

    @Override
    public void bookingToUnionTable(Fragment fragment, BookingVo bookingVo, List<Tables> selectTables, CalmResponseListener<ResponseObject<TradeResp>> listener) {
        String url = ServerAddressUtil.getInstance().toUnionTable();
        BookingToUnionSubmitReq bookingToUnionSubmitReq = toBookingUnionReq(bookingVo, selectTables);
        //增加默认税率
        TaxRateInfo taxRateInfo = ServerSettingCache.getInstance().getmTaxRateInfo();
        if (taxRateInfo != null && taxRateInfo.isTaxSupplyOpen()) {
            TradeTax tradeTax = taxRateInfo.toTradeTax(null);
            bookingToUnionSubmitReq.tradeTaxs = Arrays.asList(tradeTax);
        }
        //加入服务费
        ExtraCharge serviceExtraCharge = ServerSettingCache.getInstance().getmServiceExtraCharge();
        if (serviceExtraCharge != null && serviceExtraCharge.isAutoJoinTrade()) {
            bookingToUnionSubmitReq.tradeInitConfigs = Arrays.asList(serviceExtraCharge.toTradeInitConfig());
        }
        CalmNetWorkRequest.with(fragment)
                .url(url)
                .requestContent(bookingToUnionSubmitReq)
                .responseClass(TradeResp.class)
                .responseProcessor(new CalmDatabaseProcessor<TradeResp>() {
                    @Override
                    protected boolean isSuccessful(ResponseObject response) {
                        return super.isSuccessful(response);
                    }

                    @Override
                    protected void transactionCallable(DatabaseHelper helper, TradeResp resp) throws Exception {
                        TradeOperatesImpl.saveTradeResp(helper, resp);
                        DBHelperManager.saveEntities(helper, TradeTax.class, resp.getTradeTaxs());
                        DBHelperManager.saveEntities(helper, TradeInitConfig.class, resp.getTradeInitConfigs());
                        DBHelperManager.saveEntities(helper, TradeEarnestMoney.class, resp.tradeEarnestMoneys);
                    }
                })
                .successListener(listener)
                .errorListener(listener)
                .showLoading()
                .tag("bookingToUnionTable")
                .create();
    }

    private BookingToUnionSubmitReq toBookingUnionReq(BookingVo bookingVo, List<Tables> selectTables) {
        BookingToUnionSubmitReq req = new BookingToUnionSubmitReq();
        req.bookingId = bookingVo.getBooking().getId();
        List<BookingTable> bookingTables = BookingTablesManager.getUpdateTableListByTables(bookingVo.getBooking(), bookingVo.getBookingTableList(), selectTables);
        req.bookingTableList = BookingOpenTableUtils.createBookingUnionTableList(bookingTables);
        req.modifyDateTime = bookingVo.getBooking().getServerUpdateTime();
        req.subList = BookingOpenTableUtils.createBookingUnionSubTradeList(bookingVo, selectTables);
        req.mainTrade = BookingOpenTableUtils.createBookingUnionMainTrade(bookingVo, selectTables);
        //添加到店员（同步组调用loyalty逻辑走不通，周群力临上线前让加的）
        IAuthUser user = IAuthUser.Holder.get();
        if (user != null) {
            req.shopArriveUserId = user.getId();
        }

        return req;
    }

    /**
     * 将创建订单但保存数据保存到数据库的处理器
     *
     * @version: 1.0
     * @date 2015年4月15日
     */
    private static class BookingProcessor extends SaveDatabaseResponseProcessor<BookingResp> {
        private BookingVo bookingVo;

        BookingProcessor(BookingVo bookingVo) {
            this.bookingVo = bookingVo;
        }

        @Override
        public void saveToDatabase(BookingResp resp) throws Exception {
            super.saveToDatabase(resp);
            //saveBaseInfo(Customer.class, resp.getCustomers());
        }

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final BookingResp resp) {
            return new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    // 先删除之前选中的桌台
                    if (bookingVo.getBookingTableList() != null && bookingVo.getBookingTableList().size() > 0) {
                        List<BookingTable> bookingTableList = new ArrayList<BookingTable>();
                        for (BookingTable bookingTable : bookingVo.getBookingTableList()) {
                            if (bookingTable.getBtid() != null && bookingTable.getStatus() == -1) {
                                bookingTableList.add(bookingTable);
                            }
                        }
                        if (bookingTableList != null && bookingTableList.size() > 0) {
                            DBHelperManager.saveEntities(helper, BookingTable.class, bookingTableList);
                        }
                    }
                    DBHelperManager.saveEntities(helper, Booking.class, resp.getBookings());
                    DBHelperManager.saveEntities(helper, BookingTable.class, resp.getBookingTables());
                    return null;
                }
            };
        }

    }

    CalmDatabaseProcessor<BookingObjectResp> bookingObjectRespProcessor = new CalmDatabaseProcessor<BookingObjectResp>() {
        @Override
        protected boolean isSuccessful(ResponseObject response) {
            return super.isSuccessful(response);
        }

        @Override
        protected void transactionCallable(DatabaseHelper helper, BookingObjectResp resp) throws Exception {
            DBHelperManager.saveEntities(helper, Booking.class, resp.booking);
        }
    };

    @Override
    public void accept(Booking booking, List<BookingTable> bookingTableList, CalmResponseListener<ResponseObject<BookingAndTableResp>> listener) {
        String url = ServerAddressUtil.getInstance().bookingAccept();
        BookingAndTableReq req = BookingAndTableReq.toBookingAndTableReq(booking, bookingTableList);
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(url)
                .requestContent(req)
                .responseClass(BookingAndTableResp.class)
                .responseProcessor(new CalmDatabaseProcessor<BookingAndTableResp>() {
                    @Override
                    protected boolean isSuccessful(ResponseObject response) {
                        return super.isSuccessful(response);
                    }

                    @Override
                    protected void transactionCallable(DatabaseHelper helper, BookingAndTableResp resp) throws Exception {
                        DBHelperManager.saveEntities(helper, Booking.class, resp.booking);
                        DBHelperManager.saveEntities(helper, BookingTable.class, resp.bookingTables);
                    }
                })
                .successListener(listener)
                .errorListener(listener)
                .tag("submitBooking")
//                .showLoading()
                .create();
    }

    @Override
    public void refuse(Booking booking, String reason, CalmResponseListener<ResponseObject<BookingObjectResp>> listener) {
        String url = ServerAddressUtil.getInstance().bookingRefuse();
        HashMap<String, Object> req = new HashMap<>();
        req.put("bookingId", booking.getId());
        req.put("refusalReason", reason);
        req.put("modifyDateTime", booking.getServerUpdateTime());
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(url)
                .requestContent(req)
                .responseClass(BookingObjectResp.class)
                .responseProcessor(bookingObjectRespProcessor)
                .successListener(listener)
                .errorListener(listener)
                .tag("submitBooking")
//                .showLoading()
                .create();
    }

    @Override
    public void cancelOrder(Long bookingId, String reason, CalmResponseListener<ResponseObject<BookingObjectResp>> listener) {
        String url = ServerAddressUtil.getInstance().bookingCancel();
        HashMap<String, Object> req = new HashMap<>();
        req.put("orderID", bookingId);
        req.put("reason", reason);
        req.put("cancelOrderUser", Session.getAuthUser().getId());
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(url)
                .requestContent(req)
                .responseClass(BookingObjectResp.class)
                .responseProcessor(bookingObjectRespProcessor)
                .successListener(listener)
                .errorListener(listener)
                .tag("submitBooking")
//                .showLoading()
                .create();
    }

    @Override
    public void queryBookingNum(long beginTime, long endTime, ResponseListener<BookingQueryNumResp> listener) {
        BookingQueryNumReq req = new BookingQueryNumReq();
        req.setBeginTime(beginTime);
        req.setEndTime(endTime);
        String url = ServerAddressUtil.getInstance().bookingQueryNum();
        OpsRequest.Executor<BookingQueryNumReq, BookingQueryNumResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(BookingQueryNumResp.class)
                .execute(listener, TAG);
    }
}
