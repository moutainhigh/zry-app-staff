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

/**
 * Created by demo on 2018/12/15
 */

public class CustomerCardInfoResp extends CardBaseResp<CustomerCardInfoResp.CustomerCardInfoResult> {

    public class CustomerCardInfoResult implements IEcCardInfo {
        private Long id;//实体卡Id

        private Long cardLevelId;//卡种等级Id

        private Long cardKindId;//卡种Id

        private Integer cardStatus;//实体卡状态(实体卡状态 0：未制卡，1:未出售，2：未激活，3：已激活，4：已停用，5：已废除)

        private String cardNum;//实体卡号

        private String cardKindName;//卡种名称

        private Integer cardType;//实体卡类型 1:权益实体卡 2:匿名实体卡 3:会员实体卡

        private BigDecimal cardCost;//卡费 (0表示不需要卡费)

        private Integer isIntegral;//是否积分（仅权益卡返回）

        private Integer isDiscount;//是否折扣（仅权益卡返回）

        private Integer isValueCard;//是否储值（仅权益卡返回）

        private BigDecimal remainValue;//储值余额

        private BigDecimal integral;//积分余额

        private Integer workStatus;

        private Integer isBind; // 即售即用标记

        // v8.5.0 新增储赠余额
        private BigDecimal remainRealValue; // 剩余实储金额
        private BigDecimal remainSendValue; // 剩余赠送金额
        private BigDecimal remainPrepareValue; // 剩余预储金额

        private BigDecimal price; // 起售价

        public BigDecimal getRemainRealValue() {
            return remainRealValue;
        }


        /**
         * 卡是否有储值  权限 权益状态1：可用 2：不可用，受周期控制，为2时，服务账户冻结，不返积分、不享受储赠
         */
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
            // v8.5.0 新增赠送余额
            info.setRemainPrepareValue(remainPrepareValue);
            info.setRemainRealValue(remainRealValue);
            info.setRemainSendValue(remainSendValue);
            info.setRightStatus(rightStatus);
            info.setPrice(price);
            return info;
        }
    }
}
