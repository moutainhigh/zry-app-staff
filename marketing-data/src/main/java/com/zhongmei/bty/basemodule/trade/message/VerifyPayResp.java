package com.zhongmei.bty.basemodule.trade.message;

import java.math.BigDecimal;
import java.util.List;

import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.yunfu.db.enums.TradePayStatus;


public class VerifyPayResp extends TradePaymentResp {



    private BigDecimal valueCard = BigDecimal.ZERO;
    private List<Integer> tradePayStatus;

    public boolean isPaid() {
        if (tradePayStatus != null && !tradePayStatus.isEmpty()) {
            return TradePayStatus.PAID.value().equals(tradePayStatus.get(0));
        }
        return false;
    }

    public List<Integer> getTradePayStatus() {
        return tradePayStatus;
    }

    public void setTradePayStatus(List<Integer> tradePayStatus) {
        this.tradePayStatus = tradePayStatus;
    }

    public BigDecimal getValueCard() {
        return valueCard;
    }

    public void setValueCard(BigDecimal valueCard) {
        this.valueCard = valueCard;
    }
}
