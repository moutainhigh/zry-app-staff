package com.zhongmei.bty.commonmodule.database.entity.local;

import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.UuidEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;


public class LocalEntityBase extends UuidEntityBase {


    private static final long serialVersionUID = 1L;

    protected interface $ extends UuidEntityBase.$ {


        String statusFlag = "status_flag";


        String clientCreateTime = "client_create_time";


        String clientUpdateTime = "client_update_time";

    }


    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;


    @DatabaseField(columnName = "client_create_time", canBeNull = false)
    private Long clientCreateTime;


    @DatabaseField(columnName = "client_update_time")
    private Long clientUpdateTime;

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    public Long getClientCreateTime() {
        return clientCreateTime;
    }

    public void setClientCreateTime(Long clientCreateTime) {
        this.clientCreateTime = clientCreateTime;
    }

    public Long getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
    }

    @Override
    public Long verValue() {
        return clientUpdateTime;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    public void validateCreate() {
        setStatusFlag(StatusFlag.VALID);
        setClientCreateTime(System.currentTimeMillis());
        setClientUpdateTime(getClientCreateTime());
    }

    public void validateUpdate() {
        setClientUpdateTime(System.currentTimeMillis());
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(statusFlag, clientCreateTime);
    }
}
