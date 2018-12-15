package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardBaseInfoResp.BaseCardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.enums.CardStatus;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * @Date： 2017/5/10
 * @Description:卡基本信息返回
 * @Version: 1.0
 */
public class CardBaseInfoResp extends CardBaseResp<BaseCardInfo> {

    public class BaseCardInfo {
        private Long id;

        private Long cardKindId;

        private Long brandId;

        private String cardNum;

        private Long customerId;

        private Integer cardType;

        private Integer cardStatus;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getCardKindId() {
            return cardKindId;
        }

        public void setCardKindId(Long cardKindId) {
            this.cardKindId = cardKindId;
        }

        public Long getBrandId() {
            return brandId;
        }

        public void setBrandId(Long brandId) {
            this.brandId = brandId;
        }

        public String getCardNum() {
            return cardNum;
        }

        public void setCardNum(String cardNum) {
            this.cardNum = cardNum;
        }

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }

        public EntityCardType getCardType() {
            return ValueEnums.toEnum(EntityCardType.class, cardType);
        }

        public void setCardType(EntityCardType cardType) {
            this.cardType = ValueEnums.toValue(cardType);
        }

        public CardStatus getCardStatus() {
            return ValueEnums.toEnum(CardStatus.class, cardStatus);
        }

        public void setCardStatus(CardStatus cardStatus) {
            this.cardStatus = ValueEnums.toValue(cardStatus);
        }
    }


}
