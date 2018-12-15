package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.bty.basemodule.database.entity.pay.PaymentItemUnionpay;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGrouponDish;
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.bty.basemodule.trade.entity.TradeDepositPayRelation;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilegeExtra;
import com.zhongmei.yunfu.db.entity.discount.TradePromotion;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Date： 16/11/29
 * @Description:v3支付请求专用
 * @Version: 1.0
 */
public class PayResp implements Serializable {
    private List<Trade> trades;
    private List<TradeTable> tradeTables;
    private List<Tables> tables;
    private List<Payment> payments;
    private List<PaymentItem> paymentItems;
    private List<PaymentItemExtra> paymentItemExtras;

    private List<PItemResults> paymentItemResults;
    private List<PrivilegeRes> tradePrivilegeResults;
    private List<TradePromotion> tradePromotions;
    private List<TradeStatusLog> tradeStatusLogs;
    private List<PrintOperation> printOperations;
    private List<PaymentItemUnionpay> paymentItemUnionpays;//add 20170313
    private List<TradeDepositPayRelation> tradeDepositPayRelations;//add 20170707 押金关联表
    private List<PaymentItemGrouponDish> paymentItemGrouponDishes;//add v8.3美团券与菜品关联表
    private List<TradeCustomer> tradeCustomers;
    private TradeDeposit tradeDeposit;
    private List<Long> promoIds;//仅微信卡券验证失败(返回码1302)时使用此字段、礼品券
    private int status;
    private String message;

    public List<TradeCustomer> getTradeCustomers() {
        return tradeCustomers;
    }

    public void setTradeCustomers(List<TradeCustomer> tradeCustomers) {
        this.tradeCustomers = tradeCustomers;
    }

    public List<Long> getPromoIds() {
        return promoIds;
    }

    public void setPromoIds(List<Long> promoIds) {
        this.promoIds = promoIds;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public List<TradeTable> getTradeTables() {
        return tradeTables;
    }

    public void setTradeTables(List<TradeTable> tradeTables) {
        this.tradeTables = tradeTables;
    }

    public List<Tables> getTables() {
        return tables;
    }

    public void setTables(List<Tables> tables) {
        this.tables = tables;
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

    public List<TradePromotion> getTradePromotions() {
        return tradePromotions;
    }

    public void setTradePromotions(List<TradePromotion> tradePromotions) {
        this.tradePromotions = tradePromotions;
    }

    public List<PItemResults> getPaymentItemResults() {
        return paymentItemResults;
    }

    public void setPaymentItemResults(List<PItemResults> paymentItemResults) {
        this.paymentItemResults = paymentItemResults;
    }

    public List<PrivilegeRes> getTradePrivilegeResults() {
        return tradePrivilegeResults;
    }

    public void setTradePrivilegeResults(List<PrivilegeRes> tradePrivilegeResults) {
        this.tradePrivilegeResults = tradePrivilegeResults;
    }

    public List<PaymentItemExtra> getPaymentItemExtras() {
        return paymentItemExtras;
    }

    public void setPaymentItemExtras(List<PaymentItemExtra> paymentItemExtras) {
        this.paymentItemExtras = paymentItemExtras;
    }

    public List<TradeStatusLog> getTradeStatusLogs() {
        return tradeStatusLogs;
    }

    public void setTradeStatusLogs(List<TradeStatusLog> tradeStatusLogs) {
        this.tradeStatusLogs = tradeStatusLogs;
    }

    public List<PrintOperation> getPrintOperations() {
        return printOperations;
    }

    public void setPrintOperations(List<PrintOperation> printOperations) {
        this.printOperations = printOperations;
    }

    public TradeDeposit getTradeDeposit() {
        return tradeDeposit;
    }

    public void setTradeDeposit(TradeDeposit tradeDeposit) {
        this.tradeDeposit = tradeDeposit;
    }

    //add 20170516 start
    public String getTopPaymentItemResultMsg() {
        String resultMsg = null;

        if (paymentItemResults != null && paymentItemResults.size() > 0) {
            PItemResults it = paymentItemResults.get(0);
            resultMsg = it.resultMsg;
        }
        return resultMsg;
    }

    public int getTopPaymentItemResultStatus() {
        int resultStatus = 0;

        if (paymentItemResults != null && paymentItemResults.size() > 0) {
            PItemResults it = paymentItemResults.get(0);
            resultStatus = it.resultStatus;
        }
        return resultStatus;
    }
//add 20170516 end

    public List<TradeDepositPayRelation> getTradeDepositPayRelations() {
        return tradeDepositPayRelations;
    }

    public void setTradeDepositPayRelations(List<TradeDepositPayRelation> tradeDepositPayRelations) {
        this.tradeDepositPayRelations = tradeDepositPayRelations;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PaymentItemUnionpay> getPaymentItemUnionpays() {
        return paymentItemUnionpays;
    }

    public void setPaymentItemUnionpays(List<PaymentItemUnionpay> paymentItemUnionpays) {
        this.paymentItemUnionpays = paymentItemUnionpays;
    }

    public List<PaymentItemGrouponDish> getPaymentItemGrouponDishes() {
        return paymentItemGrouponDishes;
    }

    public static class PItemResults implements Serializable {

        private long paymentItemId;
        private String paymentItemUuid;
        private int resultStatus;
        private int subResultStatus;//add v8.15    1表示用户正在输入密码
        private Map addition;
        private String resultMsg;

        public long getPaymentItemId() {
            return paymentItemId;
        }

        public void setPaymentItemId(long paymentItemId) {
            this.paymentItemId = paymentItemId;
        }

        public String getPaymentItemUuid() {
            return paymentItemUuid;
        }

        public void setPaymentItemUuid(String paymentItemUuid) {
            this.paymentItemUuid = paymentItemUuid;
        }

        public Map getAddition() {
            return addition;
        }

        public void setAddition(Map addition) {
            this.addition = addition;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }

        public long getResultStatus() {
            return resultStatus;
        }

        public void setResultStatus(int resultStatus) {
            this.resultStatus = resultStatus;
        }

    }

    public static class PrivilegeRes implements Serializable {
        private long tradePrivilegeId;
        private String tradePrivilegeUuid;
        private String resultMsg;
        private TradePrivilegeExtra tradePrivilegeExtra;

        public long getTradePrivilegeId() {
            return tradePrivilegeId;
        }

        public void setTradePrivilegeId(long tradePrivilegeId) {
            this.tradePrivilegeId = tradePrivilegeId;
        }

        public String getTradePrivilegeUuid() {
            return tradePrivilegeUuid;
        }

        public void setTradePrivilegeUuid(String tradePrivilegeUuid) {
            this.tradePrivilegeUuid = tradePrivilegeUuid;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }

        public TradePrivilegeExtra getTradePrivilegeExtra() {
            return tradePrivilegeExtra;
        }

        public void setTradePrivilegeExtra(TradePrivilegeExtra tradePrivilegeExtra) {
            this.tradePrivilegeExtra = tradePrivilegeExtra;
        }
    }
}
