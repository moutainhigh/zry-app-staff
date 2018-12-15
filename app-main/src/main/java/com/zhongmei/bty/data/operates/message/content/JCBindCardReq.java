package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.devices.mispos.data.CardSaleInfo;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;

/**
 * Created by demo on 2018/12/15
 */

public class JCBindCardReq extends Trade {


    public TradeCustomer getTradeCustomer() {
        return tradeCustomer;
    }

    public void setTradeCustomer(TradeCustomer tradeCustomer) {
        this.tradeCustomer = tradeCustomer;
    }

    private TradeCustomer tradeCustomer;

    public TradeItemVo getTradeItemVo() {
        return tradeItemVo;
    }

    public void setTradeItemVo(TradeItemVo tradeItemVo) {
        this.tradeItemVo = tradeItemVo;
    }

    private TradeItemVo tradeItemVo;

    public JCBindCardTradeItem getTradeItem() {
        return tradeItem;
    }

    public void setTradeItem(JCBindCardTradeItem tradeItem) {
        this.tradeItem = tradeItem;
    }

    JCBindCardTradeItem tradeItem;

    public static class JCBindCardTradeItem extends TradeItem {
        private CardSaleInfo cardSaleInfo;

        public CardSaleInfo getCardSaleInfo() {
            return cardSaleInfo;
        }

        public void setCardSaleInfo(CardSaleInfo cardSaleInfo) {
            this.cardSaleInfo = cardSaleInfo;
        }
    }
}
