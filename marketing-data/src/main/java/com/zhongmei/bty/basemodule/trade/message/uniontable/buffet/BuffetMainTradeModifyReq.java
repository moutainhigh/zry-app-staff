package com.zhongmei.bty.basemodule.trade.message.uniontable.buffet;

import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;

import java.math.BigDecimal;
import java.util.List;



public class BuffetMainTradeModifyReq {

    public UnionBuffetTrade mainTrade;    public TradeExtra tradeExtra;    public List<TradeReasonRel> tradeReasonRels;    public List<TradeCustomer> tradeCustomers;    public List<TradeTax> mainTradeTaxs;    public List<TradePrivilege> mainTradePrivileges;    public TradeDeposit tradeDeposit;
    public List<UnionBuffetTrade> subTrades;
    public List<TradePrivilege> subTradePrivileges;
    public List<TradeTax> subTradeTaxs;

    public static class UnionBuffetTrade {
        public Long id;
        public String uuid;
        public Long clientUpdateTime;
        public Long serverUpdateTime;
        public BigDecimal privilegeAmount;
        public BigDecimal saleAmount;
        public Integer skuKindCount;
        public BigDecimal tradeAmount;
        public BigDecimal tradeAmountBefore;
        public String tradeMemo;
        public Integer tradePayForm;
        public Integer tradePeopleCount;
    }
}
