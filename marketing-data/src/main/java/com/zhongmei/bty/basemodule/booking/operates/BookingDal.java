package com.zhongmei.bty.basemodule.booking.operates;

import com.zhongmei.bty.basemodule.booking.bean.BookingPeriodPopupVo;
import com.zhongmei.yunfu.db.entity.booking.BookingSetting;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;
import com.zhongmei.bty.basemodule.booking.bean.BookingVo;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialOrderSource;
import com.zhongmei.bty.commonmodule.database.entity.Period;
import com.zhongmei.yunfu.db.entity.trade.Tables;

import java.util.Date;
import java.util.List;

/**
 * 预订相关查询
 */
public interface BookingDal extends IOperates {

    /**
     * 查找所有来源
     *
     * @return
     * @throws Exception
     */
    List<CommercialOrderSource> listOrderSource() throws Exception;

    /**
     * 通过id查找桌台
     *
     * @param tableUuid
     * @return
     * @throws Exception
     */
    Tables findTableById(String tableUuid) throws Exception;

    /**
     * 通过id获取时段
     *
     * @param periodId
     * @return
     * @throws Exception
     */
    Period findPeriodById(Long periodId) throws Exception;

    /**
     * 查询所有订单
     *
     * @return
     * @throws Exception
     */
    List<BookingVo> findAllBookingVoList() throws Exception;

    /**
     * 查询某一天的订单
     *
     * @return
     * @throws Exception
     */
    List<BookingVo> findBookingVoByDate(Date date) throws Exception;

    /**
     * 通过预订日期查找数据
     *
     * @param date
     * @return
     * @throws Exception
     */
    long findBookingByDate(Date date) throws Exception;

    /**
     * 通过预订日期范围查找数据
     *
     * @param beginDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List<Booking> findBookingByDate(Date beginDate, Date endDate) throws Exception;

    /**
     * 获取时段列表
     *
     * @return
     * @throws Exception
     */
    List<Period> listPeriod() throws Exception;

    /**
     * 根据时间获取时间段
     *
     * @param orderTime
     * @return
     * @throws Exception
     */
    Period getPeriod(Long orderTime) throws Exception;

    /**
     * 获取指定时段已经预订的桌台
     *
     * @param date
     * @param periodId
     * @return
     * @throws Exception
     */
    List<BookingTable> listBookingTables(Date date, Long periodId) throws Exception;

    /**
     * 创建预订单
     *
     * @param booking
     * @return
     * @throws Exception
     */
    boolean addBooking(Booking booking) throws Exception;

    /**
     * 添加预订桌台信息到预订桌台表
     *
     * @param table
     * @return
     * @throws Exception
     */
    boolean addBookingTable(BookingTable table) throws Exception;

    /**
     * 通过uuid查询预订信息
     *
     * @param uuid
     * @return
     * @throws Exception
     */
    Booking findBookingByUUID(String uuid) throws Exception;

    /**
     * 查找时段名称
     *
     * @param periodServerId
     * @return
     */
    Period findPeriodByUuid(String periodServerId) throws Exception;

    /**
     * 获取新的vo
     *
     * @param bookingId
     * @return
     * @throws Exception
     */
    BookingVo findBookingVoByid(Long bookingId) throws Exception;

    /**
     * 通过预订id查询预订桌台表
     *
     * @param bookingId
     * @return
     * @throws Exception
     */
    List<BookingTable> listBookTablesById(Long bookingId) throws Exception;

    /**
     * 查找所有可用时段
     *
     * @return
     */
    List<BookingPeriodPopupVo> findAllPeriodList() throws Exception;


    List<BookingVo> findBookingVoByStr(String s) throws Exception;

    /**
     * 查询未处理订单
     *
     * @return
     * @throws Exception
     */
    List<Booking> listUnProcess() throws Exception;

    /**
     * 查询未处理订单数量
     *
     * @return
     * @throws Exception
     */
    long getCountUnProcess() throws Exception;

    /**
     * 预定设置
     *
     * @return
     * @throws Exception
     */
    BookingSetting getBookingSetting() throws Exception;

    /**
     * 获取开始和结束时间之间预订了的桌台
     *
     * @param stratTime
     * @param endTime
     * @return
     * @throws Exception
     */
    List<BookingTable> listCurrentPeriodBookingTables(Long stratTime, Long endTime) throws Exception;

    /**
     * 获取指定时段已经预订的桌台
     *
     * @param stratTime 开始时间
     * @param endTime   结束时间
     * @return
     * @throws Exception
     */
    List<BookingTable> listBookingTablesByTime(Long stratTime, Long endTime) throws Exception;

    List<Tables> listOrderTables(List<BookingTable> bookingTableList) throws Exception;

    /**
     * 设置BookingVo里的List<CommercialAreaVo>值
     *
     * @param bookingVoList
     * @return
     */
    void setCommercialAreaVoList(List<BookingVo> bookingVoList);


    List<CommercialArea> getCommercialAreaList(List<BookingVo> bookingVoList);


    List<BookingVo> getBookingListByArea(List<BookingVo> bookingVoList, List<CommercialArea> areaList);
}
