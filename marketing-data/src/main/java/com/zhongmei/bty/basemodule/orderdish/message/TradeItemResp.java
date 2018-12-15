/**
 * @Title: ChangeTradeItemPrintStatusResp.java
 * @Package com.zhongmei.bty.data.operates.message.content
 * @Description: TODO
 * @date 2015-9-25 下午4:41:29
 * @version V1.0
 */
package com.zhongmei.bty.basemodule.orderdish.message;

import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRelExtra;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;

import java.util.List;

/**
 * 对返回的TradeItem数据的封装
 */
public class TradeItemResp {

    private List<Trade> trades;

    private List<TradeItem> tradeItems;

    private List<PrintOperation> printOperations;

    private List<TradeItemOperation> tradeItemOperations;

    private List<TradeItemProperty> tradeItemProperties;

    private List<TradeItemMainBatchRelExtra> tradeItemMainBatchRelExtras;

    private List<TradeItemMainBatchRel> tradeItemMainBatchRels;

    private List<TradeExtra> tradeExtras;

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public List<TradeItem> getTradeItems() {
        return tradeItems;
    }

    public void setTradeItems(List<TradeItem> tradeItems) {
        this.tradeItems = tradeItems;
    }

    public List<PrintOperation> getPrintOperations() {
        return printOperations;
    }

    public void setPrintOperations(List<PrintOperation> printOperations) {
        this.printOperations = printOperations;
    }

    public List<TradeItemOperation> getTradeItemOperations() {
        return tradeItemOperations;
    }

    public void setTradeItemOperations(List<TradeItemOperation> tradeItemOperations) {
        this.tradeItemOperations = tradeItemOperations;
    }

    public List<TradeExtra> getTradeExtras() {
        return tradeExtras;
    }

    public void setTradeExtras(List<TradeExtra> tradeExtras) {
        this.tradeExtras = tradeExtras;
    }

    public List<TradeItemMainBatchRelExtra> getTradeItemMainBatchRelExtras() {
        return tradeItemMainBatchRelExtras;
    }

    public void setTradeItemMainBatchRelExtras(List<TradeItemMainBatchRelExtra> tradeItemMainBatchRelExtras) {
        this.tradeItemMainBatchRelExtras = tradeItemMainBatchRelExtras;
    }

    public List<TradeItemMainBatchRel> getTradeItemMainBatchRels() {
        return tradeItemMainBatchRels;
    }

    public void setTradeItemMainBatchRels(List<TradeItemMainBatchRel> tradeItemMainBatchRels) {
        this.tradeItemMainBatchRels = tradeItemMainBatchRels;
    }

    public List<TradeItemProperty> getTradeItemProperties() {
        return tradeItemProperties;
    }

    public void setTradeItemProperties(List<TradeItemProperty> tradeItemProperties) {
        this.tradeItemProperties = tradeItemProperties;
    }
}
