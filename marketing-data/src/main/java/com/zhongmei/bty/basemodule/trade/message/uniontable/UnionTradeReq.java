package com.zhongmei.bty.basemodule.trade.message.uniontable;

import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;

import java.util.List;



public class UnionTradeReq {

    private SubmitReq submitReq;

    private UnionReq unionReq;

    private TradeUserReq tradeUserReq;

    private List<TradeTax> tradeTaxs;
    private List<TradeInitConfig> tradeInitConfigs;

    public void setSubmitReq(SubmitReq submitReq) {
        this.submitReq = submitReq;
    }

    public void setUnionReq(UnionReq unionReq) {
        this.unionReq = unionReq;
    }

    public void setTradeUserReq(TradeUserReq tradeUserReq) {
        this.tradeUserReq = tradeUserReq;
    }

    public void setTradeTaxs(List<TradeTax> tradeTaxs) {
        this.tradeTaxs = tradeTaxs;
    }

    public void setTradeInitConfigs(List<TradeInitConfig> tradeInitConfigs) {
        this.tradeInitConfigs = tradeInitConfigs;
    }

    public static class SubmitReq {
        public List<UnionTrade> submitList;
    }

    public static class UnionReq {
        public List<DinnerTrade> tradeList;
        public MainTrade mainTrade;
    }

    public static class TradeUserReq {
        public Long userId;
        public String userName;
        public Integer userType;
    }

    public static class UnionTrade extends Trade {
        public TradeTable tradeTable;
    }

    public static class MainTrade {
        public String tradeNo;
        public Long creatorId;
        public String creatorName;
        public Long updatorId;
        public String updatorName;
    }

    public static class DinnerTrade {
        public Long tradeId;
        public Long serverUpdateTime;
    }
}
