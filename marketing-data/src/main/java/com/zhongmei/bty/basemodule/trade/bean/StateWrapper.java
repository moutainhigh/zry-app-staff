package com.zhongmei.bty.basemodule.trade.bean;


import java.util.Collection;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class StateWrapper {
    public TradeTableInfo unionTableMainTrade;

    public List<TradeTableInfo> unionTableSubTrades;

    public final TableStateInfo tableStateInfo;

    public final Collection<TradeTableInfo> tradeTableInfos;

    public StateWrapper(TableStateInfo tableStateInfo, Collection<TradeTableInfo> tradeTableInfos) {
        this.tableStateInfo = tableStateInfo;
        this.tradeTableInfos = tradeTableInfos;
    }


    public StateWrapper(TableStateInfo tableStateInfo, Collection<TradeTableInfo> tradeTableInfos, TradeTableInfo unionTableMainTrade, List<TradeTableInfo> unionTableSubTrades) {
        this.tableStateInfo = tableStateInfo;
        this.tradeTableInfos = tradeTableInfos;
        this.unionTableMainTrade = unionTableMainTrade;
        this.unionTableSubTrades = unionTableSubTrades;
    }

}
