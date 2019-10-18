package com.zhongmei.bty.basemodule.discount.message;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;

import java.math.BigDecimal;
import java.util.List;



public class UsePrivilegeReq {
    private BaseInfo base;    private List<IntegralInfo> integrals;    private List<PromoInfo> promos;    private List<WeixinCardInfo> weixinCards;
    public BaseInfo getBase() {
        return base;
    }

    public void setBase(BaseInfo base) {
        this.base = base;
    }

    public List<IntegralInfo> getIntegrals() {
        return integrals;
    }

    public void setIntegrals(List<IntegralInfo> integrals) {
        this.integrals = integrals;
    }

    public List<PromoInfo> getPromos() {
        return promos;
    }

    public void setPromos(List<PromoInfo> promos) {
        this.promos = promos;
    }

    public List<WeixinCardInfo> getWeixinCards() {
        return weixinCards;
    }

    public void setWeixinCards(List<WeixinCardInfo> weixinCards) {
        this.weixinCards = weixinCards;
    }

    public class BaseInfo {
        private Long bizDate;        private Integer bizType;        private Long operateId;        private String operateName;        private String tradeUuid;
        public Long getOperateId() {
            return operateId;
        }

        public void setOperateId(Long operateId) {
            this.operateId = operateId;
        }

        public String getOperateName() {
            return operateName;
        }

        public void setOperateName(String operateName) {
            this.operateName = operateName;
        }

        public String getTradeUuid() {
            return tradeUuid;
        }

        public void setTradeUuid(String tradeUuid) {
            this.tradeUuid = tradeUuid;
        }

        public Long getBizDate() {
            return bizDate;
        }

        public void setBizDate(Long bizDate) {
            this.bizDate = bizDate;
        }

        public BusinessType getBizType() {
            return ValueEnums.toEnum(BusinessType.class, bizType);
        }

        public void setBizType(BusinessType bizType) {
            this.bizType = ValueEnums.toValue(bizType);
        }
    }

    public class IntegralInfo {
        private Long customerId;        private String entityCardNo;        private BigDecimal privilegeAmount;        private Integer privilegeType;        private BigDecimal privilegeValue;        private String uuid;
        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }

        public String getEntityCardNo() {
            return entityCardNo;
        }

        public void setEntityCardNo(String entityCardNo) {
            this.entityCardNo = entityCardNo;
        }

        public BigDecimal getPrivilegeAmount() {
            return privilegeAmount;
        }

        public void setPrivilegeAmount(BigDecimal privilegeAmount) {
            this.privilegeAmount = privilegeAmount;
        }

        public PrivilegeType getPrivilegeType() {
            return ValueEnums.toEnum(PrivilegeType.class, privilegeType);
        }

        public void setPrivilegeType(PrivilegeType privilegeType) {
            this.privilegeType = ValueEnums.toValue(privilegeType);
        }

        public BigDecimal getPrivilegeValue() {
            return privilegeValue;
        }

        public void setPrivilegeValue(BigDecimal privilegeValue) {
            this.privilegeValue = privilegeValue;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }

    public class PromoInfo {
        private Long customerId;        private BigDecimal privilegeAmount;        private Integer privilegeType;        private Long promoId;        private String uuid;
        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }

        public BigDecimal getPrivilegeAmount() {
            return privilegeAmount;
        }

        public void setPrivilegeAmount(BigDecimal privilegeAmount) {
            this.privilegeAmount = privilegeAmount;
        }

        public PrivilegeType getPrivilegeType() {
            return ValueEnums.toEnum(PrivilegeType.class, privilegeType);
        }

        public void setPrivilegeType(PrivilegeType privilegeType) {
            this.privilegeType = ValueEnums.toValue(privilegeType);
        }

        public Long getPromoId() {
            return promoId;
        }

        public void setPromoId(Long promoId) {
            this.promoId = promoId;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }

    public class WeixinCardInfo {
        private BigDecimal privilegeAmount;        private Integer privilegeType;        private Long promoId;        private String uuid;        private BigDecimal tradeAmount;

        public BigDecimal getPrivilegeAmount() {
            return privilegeAmount;
        }

        public void setPrivilegeAmount(BigDecimal privilegeAmount) {
            this.privilegeAmount = privilegeAmount;
        }

        public PrivilegeType getPrivilegeType() {
            return ValueEnums.toEnum(PrivilegeType.class, privilegeType);
        }

        public void setPrivilegeType(PrivilegeType privilegeType) {
            this.privilegeType = ValueEnums.toValue(privilegeType);
        }

        public Long getPromoId() {
            return promoId;
        }

        public void setPromoId(Long promoId) {
            this.promoId = promoId;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public BigDecimal getTradeAmount() {
            return tradeAmount;
        }

        public void setTradeAmount(BigDecimal tradeAmount) {
            this.tradeAmount = tradeAmount;
        }
    }
}
