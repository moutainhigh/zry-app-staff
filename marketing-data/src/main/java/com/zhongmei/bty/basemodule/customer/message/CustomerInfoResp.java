package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.basemodule.database.entity.customer.CustomerCardItem;
import com.zhongmei.bty.basemodule.devices.mispos.enums.CardStatus;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.commonmodule.database.enums.CardRechagingStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class CustomerInfoResp implements Serializable {

    private Double valuecard;// 储值余额

    private Long integral;// 积分余额

    private List<CustomerCouponResp> coupons;

    private List<Card> cards;

    private Long customerId;

    private String customerName;

    private String customerLevel;

    private Double restAmount;

    private Double usageAmount;

    private int isDisable;

    public Double getValuecard() {
        return valuecard;
    }

    public void setValuecard(Double valuecard) {
        this.valuecard = valuecard;
    }

    public Long getIntegral() {
        return integral;
    }

    public void setIntegral(Long integral) {
        this.integral = integral;
    }

    public List<CustomerCouponResp> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CustomerCouponResp> coupons) {
        this.coupons = coupons;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public Double getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(Double restAmount) {
        this.restAmount = restAmount;
    }

    public Double getUsageAmount() {
        return usageAmount;
    }

    public void setUsageAmount(Double usageAmount) {
        this.usageAmount = usageAmount;
    }

    public int getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(int isDisable) {
        this.isDisable = isDisable;
    }


    public static class Card implements Serializable {
        /**
         * @date：2016-5-19 下午2:17:31
         */
        private static final long serialVersionUID = 1L;

        private Long id;

        private Long cardKindId;

        private Long cardLevelId;

        private Long brandId;

        private Integer cardStatus;

        private String cardNum;

        private String cardKindName;

        private Integer cardCost;

        private Integer isIntegral;

        private Integer isDiscount;

        private Integer isValueCard;

        private Long openCardDate;

        private String commercialName;

        private Integer operateStatus;

        private Double remainValue;

        private Integer integral;

        private Integer cardType;

        private Long validStartDay;

        private Long validEndDay;

        /**
         * 卡是否有储值权限 权益状态1：可用 2：不可用，受周期控制，为2时，服务账户冻结，不返积分、不享受储赠
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

        public Integer getIsNeedPwd() {
            return isNeedPwd;
        }

        public void setIsNeedPwd(Integer isNeedPwd) {
            this.isNeedPwd = isNeedPwd;
        }

        private Integer isNeedPwd;

        public Card(String cardNum, String cardKindName) {
            this.cardNum = cardNum;
            this.cardKindName = cardKindName;
        }

        public Card() {
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

        public Long getCardLevelId() {
            return cardLevelId;
        }

        public void setCardLevelId(Long cardLevelId) {
            this.cardLevelId = cardLevelId;
        }

        public Long getBrandId() {
            return brandId;
        }

        public void setBrandId(Long brandId) {
            this.brandId = brandId;
        }

        public CardStatus getCardStatus() {
            return ValueEnums.toEnum(CardStatus.class, cardStatus);
        }

        public void setCardStatus(CardStatus cardStatus) {
            this.cardStatus = ValueEnums.toValue(cardStatus);
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

        public Integer getCardCost() {
            return cardCost;
        }

        public void setCardCost(Integer cardCost) {
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

        public Long getOpenCardDate() {
            return openCardDate;
        }

        public void setOpenCardDate(Long openCardDate) {
            this.openCardDate = openCardDate;
        }

        public String getCommercialName() {
            return commercialName;
        }

        public void setCommercialName(String commercialName) {
            this.commercialName = commercialName;
        }

        public Integer getOperateStatus() {
            return operateStatus;
        }

        public void setOperateStatus(Integer operateStatus) {
            this.operateStatus = operateStatus;
        }

        public Double getRemainValue() {
            return remainValue;
        }

        public void setRemainValue(Double remainValue) {
            this.remainValue = remainValue;
        }

        public Integer getIntegral() {
            return integral;
        }

        public void setIntegral(Integer integral) {
            this.integral = integral;
        }

        public EntityCardType getCardType() {
            return ValueEnums.toEnum(EntityCardType.class, cardType);
        }

        public void setCardType(EntityCardType cardType) {
            this.cardType = ValueEnums.toValue(cardType);
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

        public CustomerCardItem getCustomerCardItem() {
            CustomerCardItem item = new CustomerCardItem();
            item.id = this.id;
            item.cardKindId = this.cardKindId;
            item.cardKindName = this.cardKindName;
            item.cardNum = this.cardNum;
            item.cardStatus = this.cardStatus;
            item.isNeedPwd = this.isNeedPwd;
            item.cardType = this.cardType;
            item.rightStatus = this.rightStatus;
            item.remainValue = remainValue == null ? BigDecimal.ZERO : BigDecimal.valueOf(remainValue);
            return item;
        }
    }

}
