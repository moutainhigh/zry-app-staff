package com.zhongmei.bty.basemodule.discount.bean.salespromotion;

import android.util.SparseArray;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class SalesPromotionRule implements Serializable {
        private Long id;

        private Long planId;
        private String planName;
        private boolean isCouponShare;
        private SalesPromotionValidityPeriod validityPeriod;
        private SalesPromotionRuleLimitPeriod limitPeriod;
        private SparseArray<SalesPromotionWeekday> weekDay;
        private boolean stackRule;
        private int applyCrowd;
        private int marketSubjectType;
        private int ruleSubjectType;
        private int ruleLogic;
        private BigDecimal logicValue;
        private int policySubjectType;
        private int policyDetailType;
        private BigDecimal policyValue1 = BigDecimal.ZERO;
        private String policyValue2;

    private Long serverCreateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public boolean isCouponShare() {
        return isCouponShare;
    }

    public void setCouponShare(boolean couponShare) {
        isCouponShare = couponShare;
    }

    public SalesPromotionValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(SalesPromotionValidityPeriod validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public SalesPromotionRuleLimitPeriod getLimitPeriod() {
        return limitPeriod;
    }

    public void setLimitPeriod(SalesPromotionRuleLimitPeriod limitPeriod) {
        this.limitPeriod = limitPeriod;
    }

    public SparseArray<SalesPromotionWeekday> getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(SparseArray<SalesPromotionWeekday> weekDay) {
        this.weekDay = weekDay;
    }

    public boolean isStackRule() {
        return stackRule;
    }

    public void setStackRule(boolean stackRule) {
        this.stackRule = stackRule;
    }

    public int getApplyCrowd() {
        return applyCrowd;
    }

    public void setApplyCrowd(int applyCrowd) {
        this.applyCrowd = applyCrowd;
    }

    public int getMarketSubjectType() {
        return marketSubjectType;
    }

    public void setMarketSubjectType(int marketSubjectType) {
        this.marketSubjectType = marketSubjectType;
    }

    public int getRuleSubjectType() {
        return ruleSubjectType;
    }

    public void setRuleSubjectType(int ruleSubjectType) {
        this.ruleSubjectType = ruleSubjectType;
    }

    public int getRuleLogic() {
        return ruleLogic;
    }

    public void setRuleLogic(int ruleLogic) {
        this.ruleLogic = ruleLogic;
    }

    public BigDecimal getLogicValue() {
        return logicValue;
    }

    public void setLogicValue(BigDecimal logicValue) {
        this.logicValue = logicValue;
    }

    public int getPolicySubjectType() {
        return policySubjectType;
    }

    public void setPolicySubjectType(int policySubjectType) {
        this.policySubjectType = policySubjectType;
    }

    public int getPolicyDetailType() {
        return policyDetailType;
    }

    public void setPolicyDetailType(int policyDetailType) {
        this.policyDetailType = policyDetailType;
    }

    public BigDecimal getPolicyValue1() {
        return policyValue1;
    }

    public void setPolicyValue1(BigDecimal policyValue1) {
        this.policyValue1 = policyValue1;
    }

    public String getPolicyValue2() {
        return policyValue2;
    }

    public void setPolicyValue2(String policyValue2) {
        this.policyValue2 = policyValue2;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }
}
