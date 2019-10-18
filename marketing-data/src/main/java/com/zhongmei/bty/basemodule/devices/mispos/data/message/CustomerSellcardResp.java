package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.util.ValueEnums;


public class CustomerSellcardResp {
    private Trade trade;    private String cardNos;    private Integer padNo;
    private Integer cardType;
    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public String getCardNos() {
        return cardNos;
    }

    public void setCardNos(String cardNos) {
        this.cardNos = cardNos;
    }

    public Integer getPadNo() {
        return padNo;
    }

    public void setPadNo(Integer padNo) {
        this.padNo = padNo;
    }

    public EntityCardType getCardType() {
        return ValueEnums.toEnum(EntityCardType.class, cardType);
    }

    public void setCardType(EntityCardType entityCardType) {
        this.cardType = ValueEnums.toValue(entityCardType);
    }
}
