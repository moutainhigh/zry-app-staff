package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import java.math.BigDecimal;
import java.util.Formatter;
import java.util.List;


public interface IDinnertableTrade {


    String getUuid();


    String getSn();


    TradeStatus getTradeStatus();


    int getSpendTime();


    void refreshSpendTime();


    int getNumberOfMeals();


    TradePayStatus getTradePayStatus();


    DinnertableStatus getStatus();


    Long getId();


    Long getServerUpdateTime();


    String getTradeUuid();


    Long getTradeId();


    Long getTradeServerUpdateTime();


    Long getTradeClientUpdateTime();


    IDinnertable getDinnertable();


    String getTradeAmount();


    void setTradeAmount(BigDecimal tradeAmount);


    UpContentType getUpContent();


    public BusinessType getBusinessType();

    enum UpContentType {
        SN,        TRADE_AMOUNT    }


    YesOrNo getPreCashPrintStatus();


    List<TableSeat> getTableSeats();

    TradeType getTradeType();

    public String getTableName();

}
