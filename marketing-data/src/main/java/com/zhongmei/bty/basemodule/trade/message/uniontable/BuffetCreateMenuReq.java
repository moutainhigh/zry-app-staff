package com.zhongmei.bty.basemodule.trade.message.uniontable;

import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;

import java.util.List;



public class BuffetCreateMenuReq {

    private Trade mainTrade;

    private List<TradeUser> tradeUsers;

    private List<TradeBuffetPeople> tradeBuffetPeoples;

    private TradeItem menuTradeItem;
    private TradeDeposit tradeDeposit;

    public void setMainTrade(Trade mainTrade) {
        this.mainTrade = mainTrade;
    }

    public void setTradeUsers(List<TradeUser> tradeUsers) {
        this.tradeUsers = tradeUsers;
    }

    public void setTradeBuffetPeoples(List<TradeBuffetPeople> tradeBuffetPeoples) {
        this.tradeBuffetPeoples = tradeBuffetPeoples;
    }

    public void setMenuTradeItem(TradeItem menuTradeItem) {
        this.menuTradeItem = menuTradeItem;
    }

    public void setTradeDeposit(TradeDeposit tradeDeposit) {
        this.tradeDeposit = tradeDeposit;
    }
}
