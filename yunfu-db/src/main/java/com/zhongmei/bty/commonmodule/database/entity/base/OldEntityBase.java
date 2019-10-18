package com.zhongmei.bty.commonmodule.database.entity.base;

import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.database.enums.Status;
import com.zhongmei.yunfu.db.EntityBase;


public abstract class OldEntityBase extends EntityBase<Long> {


    private static final long serialVersionUID = 1L;

    protected interface $ {


        String id = "id";


        String status = "status";

    }

    @DatabaseField(columnName = "id", id = true, canBeNull = false)
    private Long id;

    @DatabaseField(columnName = "status")
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return ValueEnums.toEnum(Status.class, status);
    }

    public void setStatus(Status status) {
        this.status = ValueEnums.toValue(status);
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(Status.VALID, status);
    }

    @Override
    public Long pkValue() {
        return id;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(id, status);
    }
}
