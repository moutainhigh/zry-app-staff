package com.zhongmei.bty.basemodule.booking.message;

import com.zhongmei.yunfu.db.entity.booking.Booking;

import java.math.BigDecimal;
import java.util.List;

/**
 * Desc
 *
 * @created 2017/8/24
 */
public class BookingListResp {

    public List<BookingResposeBean> bookingRespose;
    public PageBean page;


    public static class BookingResposeBean {
        public Booking booking;
        public int isPreOrder;
        public List<String> tableIds;
        public BigDecimal deposit;
    }


    public static class PageBean {
        public int additionalPageNum;
        public int additionalOffset;
    }
}
