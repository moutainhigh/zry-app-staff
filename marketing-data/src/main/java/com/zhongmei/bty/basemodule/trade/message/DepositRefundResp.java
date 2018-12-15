package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;

import java.util.List;

/**
 * @Date： 16/5/19
 * @Description:
 * @Version: 1.0
 */
public class DepositRefundResp {

    private List<Trade> trades;

    private List<Tables> tables;

    private List<TradeDeposit> tradeDeposit;

    private List<Payment> payments;

    private List<PaymentItem> paymentItems;

    private List<TradeReasonRel> tradeReasonRels;

    private List<TradeStatusLog> tradeStatusLogs;

    public List<TradeReasonRel> getTradeReasonRels() {
        return tradeReasonRels;
    }

    public void setTradeReasonRels(List<TradeReasonRel> tradeReasonRels) {
        this.tradeReasonRels = tradeReasonRels;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public List<TradeDeposit> getTradeDeposit() {
        return tradeDeposit;
    }

    public void setTradeDeposit(List<TradeDeposit> tradeDeposit) {
        this.tradeDeposit = tradeDeposit;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<PaymentItem> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(List<PaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }

    public List<Tables> getTables() {
        return tables;
    }

    public void setTables(List<Tables> tables) {
        this.tables = tables;
    }

    public List<TradeStatusLog> getTradeStatusLogs() {
        return tradeStatusLogs;
    }

    public void setTradeStatusLogs(List<TradeStatusLog> tradeStatusLogs) {
        this.tradeStatusLogs = tradeStatusLogs;
    }
}
