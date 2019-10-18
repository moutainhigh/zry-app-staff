package com.zhongmei.yunfu.db.entity.discount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.enums.ActivityRuleEffective;

import java.math.BigDecimal;


@DatabaseTable(tableName = "trade_plan_activity")
public class TradePlanActivity extends DataEntityBase {

    public interface $ extends DataEntityBase.$ {
        public static final String uuid = "uuid";

        public static final String tradeId = "trade_id";

        public static final String tradeUuid = "trade_uuid";

        public static final String ruleId = "rule_id";

        public static final String ruleName = "rule_name";

        public static final String offerValue = "offer_value";

        public static final String planId = "plan_id";

        public static final String planUsageCount = "plan_usage_count";

        public static final String clientCreateTime = "client_create_time";

        public static final String clientUpdateTime = "client_update_time";

        public static final String ruleEffective = "rule_effective";
    }


    @DatabaseField(columnName = "trade_id")
    private Long tradeId;


    @DatabaseField(columnName = "trade_uuid")
    private String tradeUuid;


    @DatabaseField(columnName = "rule_id", canBeNull = false)
    private Long ruleId;


    @DatabaseField(columnName = "rule_name")
    private String ruleName;


    @DatabaseField(columnName = "offer_value", canBeNull = false)
    private BigDecimal offerValue;


    @DatabaseField(columnName = "plan_id", canBeNull = false)
    private Long planId;

    @DatabaseField(columnName = "plan_usage_count")
    private Integer planUsageCount;

        @DatabaseField(columnName = "rule_effective")
    private Integer ruleEffective;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public BigDecimal getOfferValue() {
        return offerValue;
    }

    public void setOfferValue(BigDecimal offerValue) {
        this.offerValue = offerValue;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Integer getPlanUsageCount() {
        return planUsageCount;
    }

    public void setPlanUsageCount(Integer planUsageCount) {
        this.planUsageCount = planUsageCount;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public ActivityRuleEffective getRuleEffective() {
        return ValueEnums.toEnum(ActivityRuleEffective.class, ruleEffective);
    }

    public void setRuleEffective(ActivityRuleEffective ruleEffective) {
        this.ruleEffective = ValueEnums.toValue(ruleEffective);
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


    public TradePlanActivity copyTradePlanActivity(TradePlanActivity fromTradePlanActivity, TradePlanActivity toTradePlanActivity) {
        toTradePlanActivity.setId(fromTradePlanActivity.getId());
        toTradePlanActivity.setUuid(fromTradePlanActivity.getUuid());
        toTradePlanActivity.setTradeId(fromTradePlanActivity.getTradeId());
        toTradePlanActivity.setTradeUuid(fromTradePlanActivity.getTradeUuid());
        toTradePlanActivity.setRuleId(fromTradePlanActivity.getRuleId());
        toTradePlanActivity.setRuleName(fromTradePlanActivity.getRuleName());
        toTradePlanActivity.setOfferValue(fromTradePlanActivity.getOfferValue());
        toTradePlanActivity.setBrandIdenty(fromTradePlanActivity.getBrandIdenty());
        toTradePlanActivity.setShopIdenty(fromTradePlanActivity.getShopIdenty());
        toTradePlanActivity.setPlanId(fromTradePlanActivity.getPlanId());
        toTradePlanActivity.setPlanUsageCount(fromTradePlanActivity.getPlanUsageCount());
        toTradePlanActivity.setClientCreateTime(fromTradePlanActivity.getClientCreateTime());
        toTradePlanActivity.setClientUpdateTime(fromTradePlanActivity.getClientUpdateTime());
        toTradePlanActivity.setStatusFlag(fromTradePlanActivity.getStatusFlag());
        toTradePlanActivity.setRuleEffective(fromTradePlanActivity.getRuleEffective());
        return toTradePlanActivity;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(ruleId, offerValue, planId);
    }
}
