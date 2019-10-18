package com.zhongmei.bty.basemodule.booking.message;

import com.zhongmei.bty.basemodule.trade.bean.TradeGroupInfo;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.commonmodule.database.entity.TradeEarnestMoney;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;

import java.util.List;


public class BookingGroupTradeResp {
    public Trade trade;

    public TradeExtra tradeExtra;

    public List<TradeCustomer> tradeCustomers;

    public List<TradeItem> tradeItems;

    public List<TradeTable> tradeTables;

    public TradeGroupInfo tradeGroup;
    public TradeUser tradeUser;
    public List<TradeTax> tradeTaxs;
    public List<TradeInitConfig> tradeInitConfigs;
    public List<TradeEarnestMoney> tradeEarnestMoneys;

}
