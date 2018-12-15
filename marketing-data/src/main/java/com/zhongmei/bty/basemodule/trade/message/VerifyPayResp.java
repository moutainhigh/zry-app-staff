package com.zhongmei.bty.basemodule.trade.message;

import java.math.BigDecimal;
import java.util.List;

import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.yunfu.db.enums.TradePayStatus;

/**
 * @version: 1.0
 * @date 2015年6月4日
 */
public class VerifyPayResp extends TradePaymentResp {

    /**
     * 为TradePayStatus.PAID时才有Trade和Payment记录
     * 注意：由于接口返回的是List，所以使用List
     */

    private BigDecimal valueCard = BigDecimal.ZERO;//余额

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
