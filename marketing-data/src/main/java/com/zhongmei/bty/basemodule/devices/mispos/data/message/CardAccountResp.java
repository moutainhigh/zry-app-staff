package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;

import java.math.BigDecimal;


public class CardAccountResp extends LoyaltyTransferResp<CardAccountResp.CardAccountItem> {

    public static class CardAccountItem {

        public CardBaseInfoBean cardBaseInfo;         public CardStoreAccountBean cardStoreAccount;         public CardIntegralAccountBean cardIntegralAccount;
        public BigDecimal getRemainValue() {
            if (cardStoreAccount != null) {
                if (cardStoreAccount.remainValue != null) {
                    return cardStoreAccount.remainValue;
                }
            }
            return BigDecimal.ZERO;
        }

        public EntityCardType getCardType() {
            return ValueEnums.toEnum(EntityCardType.class, cardBaseInfo.cardType);
        }

        public BigDecimal getIntegral() {
            if (cardIntegralAccount != null) {
                if (cardIntegralAccount.integral != null) {
                    return cardIntegralAccount.integral;
                }
            }
            return BigDecimal.ZERO;
        }

        public String getCardKindKame() {
            return cardBaseInfo.cardKindName;
        }

        public int getCardStatus() {
            return cardBaseInfo.cardStatus;
        }

        public static class CardBaseInfoBean {
            public Long id;             public Long cardKindId;             public String cardKindName;             public Long brandId;             public String cardNum;             public Long customerId;             public int cardType;             public int cardStatus;         }

        public static class CardStoreAccountBean {
            public BigDecimal remainValue;             public BigDecimal totalValue;         }

        public static class CardIntegralAccountBean {
            public BigDecimal integral;             public BigDecimal totalIntegral;         }
    }
}
