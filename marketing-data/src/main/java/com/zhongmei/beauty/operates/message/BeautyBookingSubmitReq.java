package com.zhongmei.beauty.operates.message;

import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.Trade;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class BeautyBookingSubmitReq extends Trade {
    public TradeExtra tradeExtra;
    public List<TradeCustomer> tradeCustomers;
    public TradeBookingRequesst bookingInfo;

    public static class TradeBookingRequesst {
        public Long bookingId;
        public String bookingUuid;
        //        预定最后更新时间
        public Long bookingServerUpdateTime;
        //到店操作人
        public String shopArriveUser;
        //        到店时间
        public Long shopArriveTime;

    }
}
