package com.zhongmei.bty.basemodule.discount.message;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;

import java.math.BigDecimal;
import java.util.List;

/**
 * 接口请求体
 */

public class UsePrivilegeReq {
    private BaseInfo base;//基本信息
    private List<IntegralInfo> integrals;//积分抵现信息
    private List<PromoInfo> promos;//优惠券信息
    private List<WeixinCardInfo> weixinCards;//微信卡券信息

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
        private Long bizDate;//营业日
        private Integer bizType;// 业务类型
        private Long operateId;//操作人ID
        private String operateName;//操作人姓名
        private String tradeUuid;//订单uuid

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
        private Long customerId;//顾客或会员id
        private String entityCardNo;//实体卡卡号
        private BigDecimal privilegeAmount;//优惠金额
        private Integer privilegeType;//优惠类型
        private BigDecimal privilegeValue;//优惠值
        private String uuid;//uuid

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
        private Long customerId;//顾客或会员id
        private BigDecimal privilegeAmount;//优惠金额
        private Integer privilegeType;//优惠类型
        private Long promoId;//优惠活动或者优惠券id
        private String uuid;//uuid

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
        private BigDecimal privilegeAmount;//优惠金额
        private Integer privilegeType;//优惠类型
        private Long promoId;//优惠活动或者优惠券id
        private String uuid;//uuid
        private BigDecimal tradeAmount;

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
