package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.yunfu.db.entity.trade.Trade;

import java.math.BigDecimal;



public class JCReturnAnonymousCardResp {
    public void setCardInfo(JCReturnCardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }

    JCReturnCardInfo cardInfo;

    public JCReturnCardInfo getCardInfo() {
        return cardInfo;
    }

    public class JCReturnCardInfo {
        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        BigDecimal amount;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    Trade trade;
}
