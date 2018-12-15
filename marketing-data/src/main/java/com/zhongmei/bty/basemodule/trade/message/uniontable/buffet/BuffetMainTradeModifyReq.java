package com.zhongmei.bty.basemodule.trade.message.uniontable.buffet;

import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 正餐自助改单请求结构
 */

public class BuffetMainTradeModifyReq {

    public UnionBuffetTrade mainTrade;//主单
    public TradeExtra tradeExtra;//主单
    public List<TradeReasonRel> tradeReasonRels;//主单
    public List<TradeCustomer> tradeCustomers;//主单
    public List<TradeTax> mainTradeTaxs;//主单
    public List<TradePrivilege> mainTradePrivileges;//主单
    public TradeDeposit tradeDeposit;//主单

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
