package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;

import java.math.BigDecimal;

/**
 * 实体卡储值记录
 *
 * @version: 1.0
 * @date 2015年5月13日
 */
public class CardAccountResp extends LoyaltyTransferResp<CardAccountResp.CardAccountItem> {

    public static class CardAccountItem {

        public CardBaseInfoBean cardBaseInfo; //实体卡基本信息
        public CardStoreAccountBean cardStoreAccount; //实体卡储值账户
        public CardIntegralAccountBean cardIntegralAccount; //实体卡积分

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
            public Long id; //实体卡Id
            public Long cardKindId; //实体卡卡种ID
            public String cardKindName; //实体卡卡种名称
            public Long brandId; //品牌ID
            public String cardNum; //实体卡卡号
            public Long customerId; //顾客ID
            public int cardType; //实体卡类型 1:权益实体卡 2:匿名实体卡 3:会员实体卡
            public int cardStatus; //实体卡状态（0：未制卡，1:未出售，2：未激活，3：已激活，4：已停用，5：已废除）
        }

        public static class CardStoreAccountBean {
            public BigDecimal remainValue; //可用余额
            public BigDecimal totalValue; //累计储值额
        }

        public static class CardIntegralAccountBean {
            public BigDecimal integral; //可用积分
            public BigDecimal totalIntegral; //累计获取积分
        }
    }
}
