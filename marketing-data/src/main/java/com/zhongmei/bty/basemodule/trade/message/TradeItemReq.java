package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.devices.mispos.data.CardSaleInfo;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;

import java.util.List;

/**
 * @date：2016-3-18 上午10:16:08
 * @Description:
 */
public class TradeItemReq extends TradeItem {

    private static final long serialVersionUID = 1L;

    private List<CardSaleInfo> cardSaleInfos;

    public List<CardSaleInfo> getCardSaleInfos() {
        return cardSaleInfos;
    }

    public void setCardSaleInfos(List<CardSaleInfo> cardSaleInfos) {
        this.cardSaleInfos = cardSaleInfos;
    }
}
