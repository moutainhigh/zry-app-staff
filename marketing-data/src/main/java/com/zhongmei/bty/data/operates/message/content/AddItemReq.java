package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.basemodule.trade.message.TradeReq;
import com.zhongmei.bty.basemodule.orderdish.entity.AddItemRecord;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class AddItemReq {

    private long updatorId;
    private long tableId;
    private long batchId;
    private long tradeId;
    private String updatorName;
    private List<AddItemRecord> weixinAddItemInfo;//只需要id,serviceUpdateTime;
    private TradeReq trade;
    public Integer genBatchNo;

    public long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(long updatorId) {
        this.updatorId = updatorId;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public List<AddItemRecord> getWeixinAddItemInfo() {
        return weixinAddItemInfo;
    }

    public void setWeixinAddItemInfo(List<AddItemRecord> weixinAddItemInfo) {
        this.weixinAddItemInfo = weixinAddItemInfo;
    }

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public TradeReq getTrade() {
        return trade;
    }

    public void setTrade(TradeReq trade) {
        this.trade = trade;
    }
}
