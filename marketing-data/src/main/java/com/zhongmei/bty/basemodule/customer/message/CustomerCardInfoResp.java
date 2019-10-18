package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.IEcCardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardBaseResp;
import com.zhongmei.bty.basemodule.devices.mispos.enums.CardStatus;
import com.zhongmei.bty.basemodule.devices.mispos.enums.WorkStatus;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.commonmodule.database.enums.CardRechagingStatus;

import java.math.BigDecimal;



public class CustomerCardInfoResp extends CardBaseResp<CustomerCardInfoResp.CustomerCardInfoResult> {

    public class CustomerCardInfoResult implements IEcCardInfo {
        private Long id;
        private Long cardLevelId;
        private Long cardKindId;
        private Integer cardStatus;
        private String cardNum;
        private String cardKindName;
        private Integer cardType;
        private BigDecimal cardCost;
        private Integer isIntegral;
        private Integer isDiscount;
        private Integer isValueCard;
        private BigDecimal remainValue;
        private BigDecimal integral;
        private Integer workStatus;

        private Integer isBind;
                private BigDecimal remainRealValue;         private BigDecimal remainSendValue;         private BigDecimal remainPrepareValue;
        private BigDecimal price;
        public BigDecimal getRemainRealValue() {
            return remainRealValue;
        }



        private Integer rightStatus;


        public void setRightStatus(Integer rightStatus) {
            this.rightStatus = rightStatus;
        }

        public void setRightStatus(CardRechagingStatus rightStatus) {
            this.rightStatus = rightStatus.value();
        }

        public CardRechagingStatus getRightStatus() {
            return ValueEnums.toEnum(CardRechagingStatus.class, rightStatus);
        }


        public void setRemainRealValue(BigDecimal remainRealValue) {
            this.remainRealValue = remainRealValue;
        }

        public BigDecimal getRemainSendValue() {
            return remainSendValue;
        }

        public void setRemainSendValue(BigDecimal remainSendValue) {
            this.remainSendValue = remainSendValue;
        }

        public BigDecimal getRemainPrepareValue() {
            return remainPrepareValue;
        }

        public void setRemainPrepareValue(BigDecimal remainPrepareValue) {
            this.remainPrepareValue = remainPrepareValue;
        }

        public Integer getIsBind() {
            return isBind;
        }

        public void setIsBind(Integer isBind) {
            this.isBind = isBind;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getCardLevelId() {
            return cardLevelId;
        }

        public void setCardLevelId(Long cardLevelId) {
            this.cardLevelId = cardLevelId;
        }

        public Long getCardKindId() {
            return cardKindId;
        }

        public void setCardKindId(Long cardKindId) {
            this.cardKindId = cardKindId;
        }

        public Integer getCardStatus() {
            return cardStatus;
        }

        public void setCardStatus(Integer cardStatus) {
            this.cardStatus = cardStatus;
        }

        public String getCardNum() {
            return cardNum;
        }

        public void setCardNum(String cardNum) {
            this.cardNum = cardNum;
        }

        public String getCardKindName() {
            return cardKindName;
        }

        public void setCardKindName(String cardKindName) {
            this.cardKindName = cardKindName;
        }

        public Integer getCardType() {
            return cardType;
        }

        public void setCardType(Integer cardType) {
            this.cardType = cardType;
        }

        public BigDecimal getCardCost() {
            return cardCost;
        }

        public void setCardCost(BigDecimal cardCost) {
            this.cardCost = cardCost;
        }

        public Integer getIsIntegral() {
            return isIntegral;
        }

        public void setIsIntegral(Integer isIntegral) {
            this.isIntegral = isIntegral;
        }

        public Integer getIsDiscount() {
            return isDiscount;
        }

        public void setIsDiscount(Integer isDiscount) {
            this.isDiscount = isDiscount;
        }

        public Integer getIsValueCard() {
            return isValueCard;
        }

        public void setIsValueCard(Integer isValueCard) {
            this.isValueCard = isValueCard;
        }

        public BigDecimal getRemainValue() {
            return remainValue;
        }

        public void setRemainValue(BigDecimal remainValue) {
            this.remainValue = remainValue;
        }

        public BigDecimal getIntegral() {
            return integral;
        }

        public void setIntegral(BigDecimal integral) {
            this.integral = integral;
        }

        public Integer getWorkStatus() {
            return workStatus;
        }

        public void setWorkStatus(Integer workStatus) {
            this.workStatus = workStatus;
        }

        @Override
        public EcCardInfo getEcCardInfo() {
            EcCardInfo info = new EcCardInfo();
            info.setCardStatus(ValueEnums.toEnum(CardStatus.class, cardStatus));
            info.setId(id);
            info.setCardLevelId(cardLevelId);
            info.setCardKindId(cardKindId);
            info.setCardStatus(ValueEnums.toEnum(CardStatus.class, cardStatus));
            info.setCardNum(cardNum);
            info.setCardKindName(cardKindName);
            info.setCardType(cardType);
            info.setCardCost(cardCost);
            info.setIsDiscount(ValueEnums.toEnum(Bool.class, isDiscount));
            info.setIsIntegral(ValueEnums.toEnum(Bool.class, isIntegral));
            info.setIsValueCard(ValueEnums.toEnum(Bool.class, isValueCard));
            info.setIntegral(integral);
            info.setWorkStatus(ValueEnums.toEnum(WorkStatus.class, workStatus));
            info.setIsBind(ValueEnums.toEnum(EcCardInfo.CardIsBind.class, isBind));
            info.setRemainValue(remainValue);
                        info.setRemainPrepareValue(remainPrepareValue);
            info.setRemainRealValue(remainRealValue);
            info.setRemainSendValue(remainSendValue);
            info.setRightStatus(rightStatus);
            info.setPrice(price);
            return info;
        }
    }
}
