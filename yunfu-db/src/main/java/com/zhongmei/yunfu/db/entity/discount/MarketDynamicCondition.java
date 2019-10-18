package com.zhongmei.yunfu.db.entity.discount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.ConditionType;


@DatabaseTable(tableName = "market_dynamic_condition")
public class MarketDynamicCondition extends IdEntityBase {


    private static final long serialVersionUID = 1L;

    public interface $ extends IdEntityBase.$ {
        public static final String planId = "plan_id";

        public static final String conditionType = "condition_type";

        public static final String conditionValue = "condition_value";

        public static final String creatorId = "creator_id";

        public static final String serverCreateTime = "server_create_time";

        public static final String updatorId = "updator_id";

        public static final String serverUpdateTime = "server_update_time";

        public static final String isDelete = "is_delete";
    }

    @DatabaseField(columnName = "plan_id")
    private Long planId;

    @DatabaseField(columnName = "condition_type")
    private Integer conditionType;

    @DatabaseField(columnName = "condition_value")
    private Integer conditionValue;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    @DatabaseField(columnName = "updater_id")
    private Long updaterId;

    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    @DatabaseField(columnName = "is_delete")
    private Integer isDelete;

    @Override
    public boolean isValid() {
        if (isDelete == null)
            return false;
        return isDelete == 0;
    }

    @Override
    public Long verValue() {

        return serverUpdateTime;
    }

    public ConditionType getConditionType() {
        return ValueEnums.toEnum(ConditionType.class, conditionType);
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = ValueEnums.toValue(conditionType);
    }

    public Integer getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(Integer conditionValue) {
        this.conditionValue = conditionValue;
    }


    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
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

    public void setConditionType(Integer conditionType) {
        this.conditionType = conditionType;
    }


}
