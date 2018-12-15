package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.TradeEarnestMoney;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.bty.commonmodule.database.entity.TradeItemLog;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;

import java.util.List;

/**
 * 团餐开台Trade回执
 * <p>
 * Created by demo on 2018/12/15
 */
public class OpenTableResp {

    public List<Trade> trades;
    public List<TradeExtra> tradeExtras;
    public List<TradeCustomer> tradeCustomers;
    public List<TradePrivilege> tradePrivileges;
    public List<TradeItem> tradeItems;
    public List<TradeItemProperty> tradeItemProperties;
    public List<TradeItemLog> tradeItemLogs;
    public List<TradeStatusLog> tradeStatusLogs;
    public List<TradeTable> tradeTables;
    //public List<Tables> tables;
    public List<TradeTax> tradeTaxs;
    public List<TradeInitConfig> tradeInitConfigs;
    public List<TradeEarnestMoney> tradeEarnestMoneys;

}
