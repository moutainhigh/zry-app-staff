package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * 会员登录的请求数据
 *
 * @version: 1.0
 * @date 2015年5月13日
 */
public class CustomerSellcardResp {
    private Trade trade;//订单数据
    private String cardNos;//卡号列表
    private Integer padNo;
    private Integer cardType;//实体卡类型 1:会员实体卡 2:匿名实体卡

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
