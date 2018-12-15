package com.zhongmei.bty.data.db.common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.bty.entity.enums.CommercialStatus;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "commercial")
public class Commercial extends EntityBase<Long> {

    public interface $ {
        /**
         * commercialID
         */
        public static final String commercialID = "commercial_id";

        /**
         * status
         */
        public static final String status = "status";

        /**
         * server_update_time
         */
        public static final String modifyDateTime = "server_update_time";
    }

    @DatabaseField(columnName = "commercial_id", id = true, canBeNull = false)
    private Long commercialID;

    @DatabaseField(columnName = "status", canBeNull = false)
    private Integer status;

    @DatabaseField(columnName = "server_update_time", canBeNull = false)
    private Long modifyDateTime;

    public Long getCommercialID() {
        return commercialID;
    }

    public void setCommercialID(Long commercialID) {
        this.commercialID = commercialID;
    }

    public CommercialStatus getStatus() {
        return ValueEnums.toEnum(CommercialStatus.class, status);
    }

    public void setStatus(CommercialStatus status) {
        this.status = ValueEnums.toValue(status);
    }

    public Long getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(Long modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Long pkValue() {
        return commercialID;
    }

    @Override
    public Long verValue() {
        return modifyDateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(commercialID, status, modifyDateTime);
    }
}
