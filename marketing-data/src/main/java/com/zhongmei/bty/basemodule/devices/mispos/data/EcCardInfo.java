package com.zhongmei.bty.basemodule.devices.mispos.data;

import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceAccount;
import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceHistory;
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerCardItem;
import com.zhongmei.bty.basemodule.database.entity.customer.ICustomerCardItem;
import com.zhongmei.bty.basemodule.devices.mispos.enums.CardStatus;
import com.zhongmei.bty.basemodule.devices.mispos.enums.WorkStatus;
import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.commonmodule.database.enums.CardRechagingStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class EcCardInfo implements Serializable, ICustomerCardItem {


    private static final long serialVersionUID = 1L;

    private Long id;

    private Long cardKindId;

    private Long brandId;

    private Long cardLevelId;

    private Integer cardStatus;

    private String cardNum;

    private String cardKindName;

    private BigDecimal cardCost;

    private Integer isIntegral;

    private Integer isDiscount;

    private Integer isValueCard;

    private String username;

    private String sexEnum;

    private String mobile;

    private String openCardDate;

    private String commercialName;

    private Long operateStatus;

    private BigDecimal remainValue;

    private BigDecimal integral;


    private Integer cardType;

    private Integer workStatus;

    private boolean isSelected;

    private Integer isBind;

            private BigDecimal remainRealValue;
    private BigDecimal remainSendValue;
    private BigDecimal remainPrepareValue;


    private BigDecimal price;

    private Long validStartDay;
    private Long validEndDay;

    private Integer allTimes;
    private Integer remainderTimes;



    private Integer rightStatus;


    private List<BeautyCardServiceAccount> cardServiceAccountList;

    private BeautyCardServiceHistory cardServiceHistoryDto;


    public BigDecimal getRemainRealValue() {
        return remainRealValue == null ? BigDecimal.ZERO : remainRealValue;
    }

    public void setRemainRealValue(BigDecimal remainRealValue) {
        this.remainRealValue = remainRealValue;
    }

    public BigDecimal getRemainSendValue() {
        return remainSendValue == null ? BigDecimal.ZERO : remainSendValue;
    }

    public void setRemainSendValue(BigDecimal remainSendValue) {
        this.remainSendValue = remainSendValue;
    }

    public BigDecimal getRemainPrepareValue() {
        return remainPrepareValue == null ? BigDecimal.ZERO : remainPrepareValue;
    }

    public void setRemainPrepareValue(BigDecimal remainPrepareValue) {
        this.remainPrepareValue = remainPrepareValue;
    }

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


    public void setIsBind(CardIsBind isBind) {
        this.isBind = ValueEnums.toValue(isBind);
    }

    public CardIsBind getIsBind() {
        if (isBind == null) {
            return CardIsBind.NO;
        }
        return ValueEnums.toEnum(CardIsBind.class, isBind);
    }

    public CardStatus getCardStatus() {
        return ValueEnums.toEnum(CardStatus.class, cardStatus);
    }

    public void setCardStatus(CardStatus cardStatus) {
        this.cardStatus = ValueEnums.toValue(cardStatus);
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
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

    public BigDecimal getCardCost() {
        if (cardCost == null) {
            return BigDecimal.ZERO;
        }
        return cardCost;
    }

    public void setCardCost(BigDecimal cardCost) {
        this.cardCost = cardCost;
    }

    public Bool getIsIntegral() {
        return ValueEnums.toEnum(Bool.class, isIntegral);
    }

    public void setIsIntegral(Bool isIntegral) {
        this.isIntegral = ValueEnums.toValue(isIntegral);
    }

    public Bool getIsDiscount() {
        return ValueEnums.toEnum(Bool.class, isDiscount);
    }

    public void setIsDiscount(Bool isDiscount) {
        this.isDiscount = ValueEnums.toValue(isDiscount);
    }

    public Bool getIsValueCard() {
        return ValueEnums.toEnum(Bool.class, isValueCard);
    }

    public void setIsValueCard(Bool isValueCard) {
        this.isValueCard = ValueEnums.toValue(isValueCard);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSexEnum() {
        return sexEnum;
    }

    public void setSexEnum(String sexEnum) {
        this.sexEnum = sexEnum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOpenCardDate() {
        return openCardDate;
    }

    public void setOpenCardDate(String openCardDate) {
        this.openCardDate = openCardDate;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public Long getOperateStatus() {
        return operateStatus;
    }

    public void setOperateStatus(Long operateStatus) {
        this.operateStatus = operateStatus;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Long getCardLevelId() {
        return cardLevelId;
    }

    public void setCardLevelId(Long cardLevelId) {
        this.cardLevelId = cardLevelId;
    }

    public void setWorkStatus(WorkStatus workStatus) {
        this.workStatus = ValueEnums.toValue(workStatus);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getValidStartDay() {
        return validStartDay;
    }

    public void setValidStartDay(Long validStartDay) {
        this.validStartDay = validStartDay;
    }

    public Long getValidEndDay() {
        return validEndDay;
    }

    public void setValidEndDay(Long validEndDay) {
        this.validEndDay = validEndDay;
    }

    public Integer getAllTimes() {
        return allTimes;
    }

    public void setAllTimes(Integer allTimes) {
        this.allTimes = allTimes;
    }

    public Integer getRemainderTimes() {
        return remainderTimes;
    }

    public void setRemainderTimes(Integer remainderTimes) {
        this.remainderTimes = remainderTimes;
    }

    public List<BeautyCardServiceAccount> getCardServiceAccountList() {
        return cardServiceAccountList;
    }

    public void setCardServiceAccountList(List<BeautyCardServiceAccount> cardServiceAccountList) {
        this.cardServiceAccountList = cardServiceAccountList;
    }

    public BeautyCardServiceHistory getCardServiceHistoryDto() {
        return cardServiceHistoryDto;
    }

    public void setCardServiceHistoryDto(BeautyCardServiceHistory cardServiceHistoryDto) {
        this.cardServiceHistoryDto = cardServiceHistoryDto;
    }

    public void setRightStatus(Integer rightStatus) {
        this.rightStatus = rightStatus;
    }

    public void setRightStatus(CardRechagingStatus rightStatus) {
        this.rightStatus = rightStatus.value();
    }

    public CardRechagingStatus getRightStatus() {
        return ValueEnums.toEnum(CardRechagingStatus.class, rightStatus);
    }


    public WorkStatus getWorkStatus() {
        if (workStatus == null) {
            return WorkStatus.AVAILABLE_AFTER_ACTIVATION;
        }
        return ValueEnums.toEnum(WorkStatus.class, workStatus);
    }


    public static EcCardInfo toEcCard(EcCard ecCard) {
        EcCardInfo ecCardInfo = new EcCardInfo();
        ecCardInfo.setCardNum(ecCard.getCardNum());
        ecCardInfo.setCardStatus(ecCard.getCardStatus());
        ecCardInfo.setCardKindId(ecCard.getCardKindId());
        ecCardInfo.setCardLevelId(ecCard.getCardLevelId());
        ecCardInfo.setCardCost(new BigDecimal(0));
        ecCardInfo.setRightStatus(ecCard.getRightStatus().value());
        return ecCardInfo;
    }

    @Override
    public CustomerCardItem getCustomerCardItem() {
        CustomerCardItem item = new CustomerCardItem();
        item.cardKindId = cardKindId;
        item.cardKindName = cardKindName;
        item.cardNum = cardNum;
        item.cardStatus = cardStatus;
        item.cardType = cardType;
        item.rightStatus = rightStatus;
        return item;
    }





    public enum CardIsBind implements ValueEnum<Integer> {


        YES(1),


        NO(2),



        @Deprecated
        __UNKNOWN__;

        private final Helper<Integer> helper;

        CardIsBind(Integer value) {
            helper = Helper.valueHelper(value);
        }

        CardIsBind() {
            helper = Helper.unknownHelper();
        }

        @Override
        public Integer value() {
            return helper.value();
        }

        @Override
        public boolean equalsValue(Integer value) {
            return helper.equalsValue(this, value);
        }

        @Override
        public boolean isUnknownEnum() {
            return helper.isUnknownEnum();
        }

        @Override
        public void setUnknownValue(Integer value) {
            helper.setUnknownValue(value);
        }

        @Override
        public String toString() {
            return "" + value();
        }
    }
}
