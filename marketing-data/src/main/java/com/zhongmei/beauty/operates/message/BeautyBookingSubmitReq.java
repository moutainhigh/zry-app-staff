package com.zhongmei.beauty.operates.message;

import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.Trade;

import java.util.List;



public class BeautyBookingSubmitReq extends Trade {
    public TradeExtra tradeExtra;
    public List<TradeCustomer> tradeCustomers;
    public TradeBookingRequesst bookingInfo;

    public static class TradeBookingRequesst {
        public Long bookingId;
        public String bookingUuid;
                public Long bookingServerUpdateTime;
                public String shopArriveUser;
                public Long shopArriveTime;

    }
}
