package com.zhongmei.bty.basemodule.discount.bean.salespromotion;

import android.util.SparseArray;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class SalesPromotionRule implements Serializable {
    //规则唯一标示
    private Long id;

    //方案唯一标示(规则属于方案)
    private Long planId;
    //方案名称
    private String planName;
    //是否与优惠券同享
    private boolean isCouponShare;
    //有效期
    private SalesPromotionValidityPeriod validityPeriod;
    //限制时段，为null则不限制；反之则限制
    private SalesPromotionRuleLimitPeriod limitPeriod;
    //星期设置，为null则不限制；反之则限制
    private SparseArray<SalesPromotionWeekday> weekDay;
    //是否叠加
    private boolean stackRule;
    //适用人群
    private int applyCrowd;
    //营销科目类型
    private int marketSubjectType;
    //规则科目类型
    private int ruleSubjectType;
    //规则逻辑
    private int ruleLogic;
    //逻辑比较值
    private BigDecimal logicValue;
    //策略科目类型
    private int policySubjectType;
    //策略细节Id
    private int policyDetailType;
    //策略值1
    private BigDecimal policyValue1 = BigDecimal.ZERO;
    //策略值2
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
