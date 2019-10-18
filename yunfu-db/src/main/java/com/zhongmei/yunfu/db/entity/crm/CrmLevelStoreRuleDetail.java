package com.zhongmei.yunfu.db.entity.crm;

import java.math.BigDecimal;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;

@DatabaseTable(tableName = "crm_level_store_rule_detail")
public class CrmLevelStoreRuleDetail extends CrmBasicEntityBase implements ICreator, IUpdator {


    private static final long serialVersionUID = 1L;


    public interface $ extends CrmBasicEntityBase.$ {


        public static final String levelStoreRuleId = "level_store_rule_id";


        public static final String fullValue = "full_value";


        public static final String sendValue = "send_value";


        public static final String rate = "rate";


        public static final String startValue = "start_value";


        public static final String endValue = "end_value";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";

    }


    @DatabaseField(columnName = "level_store_rule_id")
    private Long levelStoreRuleId;


    @DatabaseField(columnName = "full_value")
    private BigDecimal fullValue;


    @DatabaseField(columnName = "send_value")
    private BigDecimal sendValue;


    @DatabaseField(columnName = "rate")
    private BigDecimal rate;


    @DatabaseField(columnName = "start_value")
    private BigDecimal startValue;


    @DatabaseField(columnName = "end_value")
    private BigDecimal endValue;


    @DatabaseField(columnName = "creator_id")
    private Long creatorId;


    @DatabaseField(columnName = "creator_name")
    private String creatorName;


    @DatabaseField(columnName = "updator_id")
    private Long updatorId;


    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public Long getLevelStoreRuleId() {
        return levelStoreRuleId;
    }

    public void setLevelStoreRuleId(Long levelStoreRuleId) {
        this.levelStoreRuleId = levelStoreRuleId;
    }

    public BigDecimal getFullValue() {
        return fullValue;
    }

    public void setFullValue(BigDecimal fullValue) {
        this.fullValue = fullValue;
    }

    public BigDecimal getSendValue() {
        return sendValue;
    }

    public void setSendValue(BigDecimal sendValue) {
        this.sendValue = sendValue;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getStartValue() {
        return startValue;
    }

    public void setStartValue(BigDecimal startValue) {
        this.startValue = startValue;
    }

    public BigDecimal getEndValue() {
        return endValue;
    }

    public void setEndValue(BigDecimal endValue) {
        this.endValue = endValue;
    }

    @Override
    public Long getUpdatorId() {
        return updatorId;
    }

    @Override
    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    @Override
    public String getUpdatorName() {
        return updatorName;
    }

    @Override
    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    @Override
    public Long getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String getCreatorName() {
        return creatorName;
    }

    @Override
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}
