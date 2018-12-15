package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.yunfu.db.entity.trade.Trade;


public class JCReturnAnonymousCardReq extends Trade {

    Trade trade;
    JCCardSaleInfo cardSaleInfo;


    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public JCCardSaleInfo getCardSaleInfo() {
        return cardSaleInfo;
    }

    public void setCardSaleInfo(JCCardSaleInfo cardSaleInfo) {
        this.cardSaleInfo = cardSaleInfo;
    }

    public static class JCCardSaleInfo {
        private String cardKind;// 卡类型
        private String cardNum; //卡号

        public String getCardKind() {
            return cardKind;
        }

        public void setCardKind(String cardKind) {
            this.cardKind = cardKind;
        }

        public String getCardNum() {
            return cardNum;
        }

        public void setCardNum(String cardNum) {
            this.cardNum = cardNum;
        }

    }
}
