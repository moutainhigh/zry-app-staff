package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;

import java.util.List;



public class BookingToUnionSubmitReq {

    public Long bookingId;
    public List<BookingToUnionTable> bookingTableList;
    public List<Trade> subList;
    public Trade mainTrade;
    public Long modifyDateTime;
    public Long shopArriveUserId;
    public List<TradeTax> tradeTaxs;
    public List<TradeInitConfig> tradeInitConfigs;

    public final static class BookingToUnionTable {
        public Long id;
        public String tableId;
        public Integer status;
        public String tableName;
        public String uuid;
        public Long serverUpdateTime;
    }

}
