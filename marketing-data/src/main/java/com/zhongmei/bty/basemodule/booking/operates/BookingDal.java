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


public interface BookingDal extends IOperates {


    List<CommercialOrderSource> listOrderSource() throws Exception;


    Tables findTableById(String tableUuid) throws Exception;


    Period findPeriodById(Long periodId) throws Exception;


    List<BookingVo> findAllBookingVoList() throws Exception;


    List<BookingVo> findBookingVoByDate(Date date) throws Exception;


    long findBookingByDate(Date date) throws Exception;


    List<Booking> findBookingByDate(Date beginDate, Date endDate) throws Exception;


    List<Period> listPeriod() throws Exception;


    Period getPeriod(Long orderTime) throws Exception;


    List<BookingTable> listBookingTables(Date date, Long periodId) throws Exception;


    boolean addBooking(Booking booking) throws Exception;


    boolean addBookingTable(BookingTable table) throws Exception;


    Booking findBookingByUUID(String uuid) throws Exception;


    Period findPeriodByUuid(String periodServerId) throws Exception;


    BookingVo findBookingVoByid(Long bookingId) throws Exception;


    List<BookingTable> listBookTablesById(Long bookingId) throws Exception;


    List<BookingPeriodPopupVo> findAllPeriodList() throws Exception;


    List<BookingVo> findBookingVoByStr(String s) throws Exception;


    List<Booking> listUnProcess() throws Exception;


    long getCountUnProcess() throws Exception;


    BookingSetting getBookingSetting() throws Exception;


    List<BookingTable> listCurrentPeriodBookingTables(Long stratTime, Long endTime) throws Exception;


    List<BookingTable> listBookingTablesByTime(Long stratTime, Long endTime) throws Exception;

    List<Tables> listOrderTables(List<BookingTable> bookingTableList) throws Exception;


    void setCommercialAreaVoList(List<BookingVo> bookingVoList);


    List<CommercialArea> getCommercialAreaList(List<BookingVo> bookingVoList);


    List<BookingVo> getBookingListByArea(List<BookingVo> bookingVoList, List<CommercialArea> areaList);
}
