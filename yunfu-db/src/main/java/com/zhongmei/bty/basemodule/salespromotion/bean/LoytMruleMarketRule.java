package com.zhongmei.bty.basemodule.salespromotion.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;

import java.math.BigDecimal;


@DatabaseTable(tableName = "loyt_mrule_market_rule")
public class LoytMruleMarketRule extends IdEntityBase {

    private static final long serialVersionUID = 1L;


    public interface $ extends IdEntityBase.$ {
        public static final String name = "name";
        public static final String brandId = "brand_id";
        public static final String planId = "plan_id";
        public static final String execType = "exec_type";
        public static final String marketType = "market_type";
        public static final String marketSubjectId = "market_subject_id";
        public static final String ruleSubjectId = "rule_subject_id";
        public static final String ruleLogic = "rule_logic";
        public static final String logicValue = "logic_value";
        public static final String cycleRule = "cycle_rule";
        public static final String cycleType = "cycle_type";
        public static final String policySubjectId = "policy_subject_id";
        public static final String policyDetailId = "policy_detail_id";
        public static final String policyValue1 = "policy_value1";
        public static final String policyValue2 = "policy_value2";
        public static final String description = "description";
        public static final String validFlag = "valid_flag";
        public static final String originalId = "original_id";
        public static final String serverCreateTime = "server_create_time";
        public static final String serverUpdateTime = "server_update_time";
        public static final String updaterId = "updater_id";
        public static final String creatorId = "creator_id";
    }

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "brand_id")
    private Long brandId;

    @DatabaseField(columnName = "plan_id")
    private Long planId;

    @DatabaseField(columnName = "exec_type")
    private int execType;

    @DatabaseField(columnName = "market_type")
    private int marketType;

    @DatabaseField(columnName = "market_subject_id")
    private int marketSubjectId;

    @DatabaseField(columnName = "rule_subject_id")
    private int ruleSubjectId;

    @DatabaseField(columnName = "rule_logic")
    private int ruleLogic;

    @DatabaseField(columnName = "logic_value")
    private BigDecimal logicValue;

    @DatabaseField(columnName = "cycle_rule")
    private int cycleRule;

    @DatabaseField(columnName = "cycle_type")
    private int cycleType;

    @DatabaseField(columnName = "policy_subject_id")
    private int policySubjectId;

    @DatabaseField(columnName = "policy_detail_id")
    private int policyDetailId;

    @DatabaseField(columnName = "policy_value1")
    private String policyValue1;

    @DatabaseField(columnName = "policy_value2")
    private String policyValue2;

    @DatabaseField(columnName = "description")
    private String description;

    @DatabaseField(columnName = "valid_flag")
    private int validFlag;

    @DatabaseField(columnName = "original_id")
    private Long originalId;

    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "updater_id")
    private Long updaterId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public int getExecType() {
        return execType;
    }

    public void setExecType(int execType) {
        this.execType = execType;
    }

    public int getMarketType() {
        return marketType;
    }

    public void setMarketType(int marketType) {
        this.marketType = marketType;
    }

    public int getMarketSubjectId() {
        return marketSubjectId;
    }

    public void setMarketSubjectId(int marketSubjectId) {
        this.marketSubjectId = marketSubjectId;
    }

    public int getRuleSubjectId() {
        return ruleSubjectId;
    }

    public void setRuleSubjectId(int ruleSubjectId) {
        this.ruleSubjectId = ruleSubjectId;
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

    public int getCycleRule() {
        return cycleRule;
    }

    public void setCycleRule(int cycleRule) {
        this.cycleRule = cycleRule;
    }

    public int getCycleType() {
        return cycleType;
    }

    public void setCycleType(int cycleType) {
        this.cycleType = cycleType;
    }

    public int getPolicySubjectId() {
        return policySubjectId;
    }

    public void setPolicySubjectId(int policySubjectId) {
        this.policySubjectId = policySubjectId;
    }

    public int getPolicyDetailId() {
        return policyDetailId;
    }

    public void setPolicyDetailId(int policyDetailId) {
        this.policyDetailId = policyDetailId;
    }

    public String getPolicyValue1() {
        return policyValue1;
    }

    public void setPolicyValue1(String policyValue1) {
        this.policyValue1 = policyValue1;
    }

    public String getPolicyValue2() {
        return policyValue2;
    }

    public void setPolicyValue2(String policyValue2) {
        this.policyValue2 = policyValue2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(int validFlag) {
        this.validFlag = validFlag;
    }

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    @Override
    public boolean isValid() {
        return validFlag == 1;
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }
}
