package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.basemodule.devices.mispos.data.bean.CardChangeInfo;

/**
 * Created by demo on 2018/12/15
 */

public class ChangeCardResp {

    private Trade trade;

    private CardChangeInfo cardChangeInfo;

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public CardChangeInfo getCardChangeInfo() {
        return cardChangeInfo;
    }

    public void setCardChangeInfo(CardChangeInfo cardChangeInfo) {
        this.cardChangeInfo = cardChangeInfo;
    }
}
