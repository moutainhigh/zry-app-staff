package com.zhongmei.bty.basemodule.booking.message;

import android.util.Log;

import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;
import com.zhongmei.bty.basemodule.booking.operates.BookingDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.Tables;

import java.util.ArrayList;
import java.util.List;


public class BookingAndTableReq {

    public Long bookingId;
    public Long modifyDateTime;
    public List<BookingTableListBean> bookingTableList;


    public static BookingAndTableReq toBookingAndTableReq(Booking booking, List<BookingTable> bookingTableList) {
        BookingAndTableReq bookingReq = new BookingAndTableReq();
        bookingReq.bookingId = booking.getId();
        bookingReq.modifyDateTime = booking.getServerUpdateTime();
        List<BookingTableListBean> bookingTableReqList = new ArrayList<>();
        bookingReq.bookingTableList = bookingTableReqList;

        BookingDal dal = OperatesFactory.create(BookingDal.class);
        for (BookingTable bookingTable : bookingTableList) {
            if (bookingTable.getStatus() == 0) {
                BookingTableListBean bookingTableReq = new BookingTableListBean();
                bookingTableReq.uuid = bookingTable.getUuid();
                bookingTableReq.status = bookingTable.getStatus();
                bookingTableReq.tableId = bookingTable.getTableID();
                bookingTableReq.id = bookingTable.pkValue();
                bookingTableReq.serverUpdateTime = bookingTable.getServerUpdateTime();
                try {
                    Tables table = dal.findTableById(bookingTable.getTableID());
                    bookingTableReq.tableName = table.getTableName();
                } catch (Exception e) {
                    Log.e("toBookingAndTableReq", e.getMessage(), e);
                }
                bookingTableReqList.add(bookingTableReq);
            }
        }
        return bookingReq;
    }

    public static class BookingTableListBean {
        public String tableId;
        public int status;
        public String tableName;
        public String uuid;
        public Long id;
        public Long serverUpdateTime;
    }
}
