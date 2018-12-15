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

/**
 * 预订的一些接口
 */
public interface BookingOperates extends IOperates {

    /**
     * 创建预订或修改预订
     *
     * @param bookingVo
     * @param listener
     */
    void insertOrModify(BookingVo bookingVo, ResponseListener<BookingResp> listener);

    void submitBooking(BookingVo bookingVo, CalmResponseListener<ResponseObject<BookingResp>> listener);

    void updateBooking(BookingVo bookingVo, CalmResponseListener<ResponseObject<BookingResp>> listener);

    void bookingDetail(Long bookingId, CalmResponseListener<ResponseObject<BookingDetailResp>> listener);

    /**
     * 预定到店
     *
     * @param bookingVo
     * @param listener
     */
    void bookingArrivalShop(Fragment fragment, BookingVo bookingVo, CalmResponseListener<ResponseObject<BookingResp>> listener);

    /**
     * 预订确认到店接口
     */
    void confirmArrivalShop(Long bookingId, CalmResponseListener<ResponseObject<BookingConfirmArrivalShopResp>> listener);

    /**
     * 发送短信。
     *
     * @param orderUuid
     * @param listener
     */
    void sendMessage(String orderUuid, ResponseListener<Boolean> listener);

    /**
     * 预订统计
     *
     * @param customerUuid
     * @param listener
     */
    void bookingRecorde(String customerUuid, ResponseListener<BookingStatisticsResp> listener);

    /**
     * 根据时段查询桌台信息
     *
     * @param orderTime 预定时间
     * @param periodId  时段id
     * @param listener
     */
    void queryBookingTablesByPeriod(Long orderTime, Long periodId, CalmResponseListener<ResponseObject<BookingGroupTableResq>> listener);

    /**
     * 团餐预定开台
     *
     * @param bookingVo 预定数据
     * @param tradeVo
     */
    void groupOpenTable(BookingVo bookingVo, TradeVo tradeVo, CalmResponseListener<ResponseObject<BookingGroupTradeResp>> listener);

    /**
     * 根据时间段查询预订列表(获取当天)
     *
     * @param listener
     */
    void bookingListPost(Date calendar, CalmResponseListener<ResponseObject<BookingListResp>> listener);

    /**
     * 根据时间段查询预订列表
     *
     * @param startTime long	开始时间	1231231231231
     * @param endTime   long	结束时间	1231231231231
     */
    void bookingListPost(long startTime, long endTime, CalmResponseListener<ResponseObject<BookingListResp>> listener);

    /**
     * 预定列表查询(参数搜索)
     *
     * @param queryParam String	客户名称 or 手机号(二选一)
     * @param queryParam
     */
    void bookingQueryPost(String queryParam, CalmResponseListener<ResponseObject<BookingListResp>> listener);

    /**
     * 正餐预定开台
     *
     * @param bookingVo
     * @param trade
     * @param listener
     */
    void bookingToDinnerSubmitPost(Fragment fragment, BookingVo bookingVo, Trade trade, CalmResponseListener<ResponseObject<OpenTableResp>> listener);

    /**
     * 正餐预订开联台
     *
     * @param fragment
     * @param bookingVo
     * @param selectTables
     * @param listener
     */
    void bookingToUnionTable(Fragment fragment, BookingVo bookingVo, List<Tables> selectTables, CalmResponseListener<ResponseObject<TradeResp>> listener);

    /**
     * 接受预订单
     *
     * @param booking
     * @param bookingTableList
     * @param listener
     */
    void accept(Booking booking, List<BookingTable> bookingTableList, CalmResponseListener<ResponseObject<BookingAndTableResp>> listener);

    /**
     * 拒绝预订单
     *
     * @param booking
     * @param reason
     * @param listener
     */
    void refuse(Booking booking, String reason, CalmResponseListener<ResponseObject<BookingObjectResp>> listener);

    /**
     * 取消预订
     *
     * @param bookingId
     * @param reason
     * @param listener
     */
    void cancelOrder(Long bookingId, String reason, CalmResponseListener<ResponseObject<BookingObjectResp>> listener);

    void queryBookingNum(long beginTime, long endTime, ResponseListener<BookingQueryNumResp> listener);
}
