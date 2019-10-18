package com.zhongmei.bty.basemodule.booking.operates;

import android.support.v4.app.Fragment;

import com.zhongmei.bty.basemodule.booking.bean.BookingVo;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;
import com.zhongmei.bty.basemodule.booking.message.BookingAndTableResp;
import com.zhongmei.bty.basemodule.booking.message.BookingConfirmArrivalShopResp;
import com.zhongmei.bty.basemodule.booking.message.BookingGroupTableResq;
import com.zhongmei.bty.basemodule.booking.message.BookingGroupTradeResp;
import com.zhongmei.bty.basemodule.booking.message.BookingListResp;
import com.zhongmei.bty.basemodule.booking.message.BookingQueryNumResp;
import com.zhongmei.bty.basemodule.booking.message.BookingObjectResp;
import com.zhongmei.bty.basemodule.booking.message.BookingResp;
import com.zhongmei.bty.basemodule.booking.message.BookingStatisticsResp;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.OpenTableResp;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.data.operates.message.content.BookingDetailResp;

import java.util.Date;
import java.util.List;


public interface BookingOperates extends IOperates {


    void insertOrModify(BookingVo bookingVo, ResponseListener<BookingResp> listener);

    void submitBooking(BookingVo bookingVo, CalmResponseListener<ResponseObject<BookingResp>> listener);

    void updateBooking(BookingVo bookingVo, CalmResponseListener<ResponseObject<BookingResp>> listener);

    void bookingDetail(Long bookingId, CalmResponseListener<ResponseObject<BookingDetailResp>> listener);


    void bookingArrivalShop(Fragment fragment, BookingVo bookingVo, CalmResponseListener<ResponseObject<BookingResp>> listener);


    void confirmArrivalShop(Long bookingId, CalmResponseListener<ResponseObject<BookingConfirmArrivalShopResp>> listener);


    void sendMessage(String orderUuid, ResponseListener<Boolean> listener);


    void bookingRecorde(String customerUuid, ResponseListener<BookingStatisticsResp> listener);


    void queryBookingTablesByPeriod(Long orderTime, Long periodId, CalmResponseListener<ResponseObject<BookingGroupTableResq>> listener);


    void groupOpenTable(BookingVo bookingVo, TradeVo tradeVo, CalmResponseListener<ResponseObject<BookingGroupTradeResp>> listener);


    void bookingListPost(Date calendar, CalmResponseListener<ResponseObject<BookingListResp>> listener);


    void bookingListPost(long startTime, long endTime, CalmResponseListener<ResponseObject<BookingListResp>> listener);


    void bookingQueryPost(String queryParam, CalmResponseListener<ResponseObject<BookingListResp>> listener);


    void bookingToDinnerSubmitPost(Fragment fragment, BookingVo bookingVo, Trade trade, CalmResponseListener<ResponseObject<OpenTableResp>> listener);


    void bookingToUnionTable(Fragment fragment, BookingVo bookingVo, List<Tables> selectTables, CalmResponseListener<ResponseObject<TradeResp>> listener);


    void accept(Booking booking, List<BookingTable> bookingTableList, CalmResponseListener<ResponseObject<BookingAndTableResp>> listener);


    void refuse(Booking booking, String reason, CalmResponseListener<ResponseObject<BookingObjectResp>> listener);


    void cancelOrder(Long bookingId, String reason, CalmResponseListener<ResponseObject<BookingObjectResp>> listener);

    void queryBookingNum(long beginTime, long endTime, ResponseListener<BookingQueryNumResp> listener);
}
