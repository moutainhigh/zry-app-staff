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


@SuppressLint("SimpleDateFormat")
public class BookingOperatesImpl extends AbstractOpeartesImpl implements BookingOperates {

    private static final String TAG = BookingOperatesImpl.class.getSimpleName();

    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public BookingOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void insertOrModify(BookingVo bookingVo, ResponseListener<BookingResp> listener) {


    }

    public void submitBooking(BookingVo bookingVo, CalmResponseListener<ResponseObject<BookingResp>> listener) {

    }

    public void updateBooking(BookingVo bookingVo, CalmResponseListener<ResponseObject<BookingResp>> listener) {

    }

    @Override
    public void bookingDetail(Long bookingId, CalmResponseListener<ResponseObject<BookingDetailResp>> listener) {

    }

    @Override
    public void bookingArrivalShop(Fragment fragment, BookingVo bookingVo, CalmResponseListener<ResponseObject<BookingResp>> listener) {

    }

    @Override
    public void confirmArrivalShop(Long bookingId, CalmResponseListener<ResponseObject<BookingConfirmArrivalShopResp>> listener) {

    }

    @Override
    public void sendMessage(String orderUuid, ResponseListener<Boolean> listener) {


    }

    @Override
    public void bookingRecorde(String customerUuid, ResponseListener<BookingStatisticsResp> listener) {

    }

    @Override
    public void queryBookingTablesByPeriod(Long orderTime, Long periodId, CalmResponseListener<ResponseObject<BookingGroupTableResq>> listener) {

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

    }

    @Override
    public void bookingQueryPost(String queryParam, CalmResponseListener<ResponseObject<BookingListResp>> listener) {

    }

    @Override
    public void bookingToDinnerSubmitPost(Fragment fragment, BookingVo bookingVo, Trade trade, CalmResponseListener<ResponseObject<OpenTableResp>> listener) {

    }

    @Override
    public void groupOpenTable(BookingVo bookingVo, TradeVo tradeVo, CalmResponseListener<ResponseObject<BookingGroupTradeResp>> listener) {

    }


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
            tradeUser.setStatusFlag(StatusFlag.VALID);            tradeUser.setType(TradeScenceType.SALEDISH);            tradeUser.setTradeId(tradeVo.getTrade().getId());
            tradeUser.setTradeUuid(tradeVo.getTrade().getUuid());
            req.tradeUser = tradeUser;
        }
        return req;
    }


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


    private BookingReq toBookingReq(BookingVo bookingVo) {
        BookingDalImpl dal = new BookingDalImpl(getImplContext());
        BookingReq bookingReq = new BookingReq();
        Booking booking = bookingVo.getBooking();
        bookingReq.setCancelOrderUser(booking.getCancelOrderUser());
        bookingReq.setCommercialId(booking.getCommercialId());
        bookingReq.setConsumeStandard(booking.getConsumeStandard());
        bookingReq.setCreatorID(booking.getCreatorId() + "");
        bookingReq.setCommercialGroup(booking.getCommercialGroup());
                bookingReq.setCustomerID(booking.getCustomerSynflag());
                bookingReq.setCustomerLocalID(booking.getCommercialId());
        bookingReq.setCommercialName(booking.getCommercialName());
        bookingReq.setCommercialPhone(booking.getCommercialPhone());
        bookingReq.setCustomerSynflag(booking.getCustomerSynflag());
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
                bookingTableGroupReqs.add(reqTable);
            }
            req.setBookingTables(bookingTableGroupReqs);
        }
                req.setBookingId(booking.getId());
        req.setCancelOrderUser(booking.getCancelOrderUser());
        req.setRealConsume(booking.getRealConsume());
        req.setRealConsumeTime(booking.getRealConsumeTime());
        req.setRealConsumeUser(booking.getRealConsumeUser());
        req.setRr(booking.getRefusalReason());
        req.setModifyDateTime(booking.getServerUpdateTime());
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

    }

    private BookingToUnionSubmitReq toBookingUnionReq(BookingVo bookingVo, List<Tables> selectTables) {
        BookingToUnionSubmitReq req = new BookingToUnionSubmitReq();
        req.bookingId = bookingVo.getBooking().getId();
        List<BookingTable> bookingTables = BookingTablesManager.getUpdateTableListByTables(bookingVo.getBooking(), bookingVo.getBookingTableList(), selectTables);
        req.bookingTableList = BookingOpenTableUtils.createBookingUnionTableList(bookingTables);
        req.modifyDateTime = bookingVo.getBooking().getServerUpdateTime();
        req.subList = BookingOpenTableUtils.createBookingUnionSubTradeList(bookingVo, selectTables);
        req.mainTrade = BookingOpenTableUtils.createBookingUnionMainTrade(bookingVo, selectTables);
                IAuthUser user = IAuthUser.Holder.get();
        if (user != null) {
            req.shopArriveUserId = user.getId();
        }

        return req;
    }


    private static class BookingProcessor extends SaveDatabaseResponseProcessor<BookingResp> {
        private BookingVo bookingVo;

        BookingProcessor(BookingVo bookingVo) {
            this.bookingVo = bookingVo;
        }

        @Override
        public void saveToDatabase(BookingResp resp) throws Exception {
            super.saveToDatabase(resp);
                    }

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final BookingResp resp) {
            return new Callable<Void>() {

                @Override
                public Void call() throws Exception {
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

    }

    @Override
    public void refuse(Booking booking, String reason, CalmResponseListener<ResponseObject<BookingObjectResp>> listener) {

    }

    @Override
    public void cancelOrder(Long bookingId, String reason, CalmResponseListener<ResponseObject<BookingObjectResp>> listener) {

    }

    @Override
    public void queryBookingNum(long beginTime, long endTime, ResponseListener<BookingQueryNumResp> listener) {

    }
}
