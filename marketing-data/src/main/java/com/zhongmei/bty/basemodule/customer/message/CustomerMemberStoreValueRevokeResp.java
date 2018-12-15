package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Date：2016年4月5日
 * @Description:会员储值撤销请求
 * @Version: 1.0
 */
public class CustomerMemberStoreValueRevokeResp {
    private BigDecimal value;
    private BigDecimal remainValue;
    private RelDatas relDatas;

    public class RelDatas {
        List<Trade> trades;
        List<Payment> payments;
        List<PaymentItem> paymentItems;

        //List<Customer> customers;
        public List<Trade> getTrades() {
            return trades;
        }

        public void setTrades(List<Trade> trades) {
            this.trades = trades;
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
		/*public List<Customer> getCustomers() {
			return customers;
		}
		public void setCustomers(List<Customer> customers) {
			this.customers = customers;
		}*/


    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getRemainValue() {
        return remainValue;
    }

    public void setRemainValue(BigDecimal remainValue) {
        this.remainValue = remainValue;
    }

    public RelDatas getRelDatas() {
        return relDatas;
    }

    public void setRelDatas(RelDatas relDatas) {
        this.relDatas = relDatas;
    }


}
