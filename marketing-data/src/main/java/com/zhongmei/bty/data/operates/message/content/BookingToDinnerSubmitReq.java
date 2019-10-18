package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;

import java.util.List;


public class BookingToDinnerSubmitReq extends Trade {

    public BookingInfoBean bookingInfo;
    public TradeExtra tradeExtra;
    public List<TradeCustomer> tradeCustomers;
    public List<TradeTax> tradeTaxs;
    public List<TradeInitConfig> tradeInitConfigs;

    public static class BookingInfoBean {
        public long bookingId;
        public String bookingUuid;
        public long bookingServerUpdateTime;
        public String shopArriveUser;
        public long shopArriveTime;
    }
}
