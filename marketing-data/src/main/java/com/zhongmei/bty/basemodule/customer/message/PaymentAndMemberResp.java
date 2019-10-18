package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.trade.entity.MemberValueCard;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;

import java.util.List;


public class PaymentAndMemberResp {
    private List<PaymentItem> paymentItems;    private List<Payment> payments;    private List<Member> members;
    private List<Trade> trades;
    private List<TradeItem> tradeItems;
        private List<MemberValueCard> memberValuecard;

    public List<MemberValueCard> getMemberValuecard() {
        return memberValuecard;
    }

    public void setMemberValuecard(List<MemberValueCard> memberValuecard) {
        this.memberValuecard = memberValuecard;
    }


    public class Member {
                private String name;
        private String sex;
        private String levelName;
        private String nextLevelName;
        private String nextNdGrownVaule;
        private String createdatetime;
        private double remainValue;        private double integral;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public String getNextLevelName() {
            return nextLevelName;
        }

        public void setNextLevelName(String nextLevelName) {
            this.nextLevelName = nextLevelName;
        }

        public String getNextNdGrownVaule() {
            return nextNdGrownVaule;
        }

        public void setNextNdGrownVaule(String nextNdGrownVaule) {
            this.nextNdGrownVaule = nextNdGrownVaule;
        }

        public String getCreatedatetime() {
            return createdatetime;
        }

        public void setCreatedatetime(String createdatetime) {
            this.createdatetime = createdatetime;
        }

        public double getRemainValue() {
            return remainValue;
        }

        public void setRemainValue(double remainValue) {
            this.remainValue = remainValue;
        }

        public double getIntegral() {
            return integral;
        }

        public void setIntegral(double integral) {
            this.integral = integral;
        }

    }

    public List<PaymentItem> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(List<PaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

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


}
