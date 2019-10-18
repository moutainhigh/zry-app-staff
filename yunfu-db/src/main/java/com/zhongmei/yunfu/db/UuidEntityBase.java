package com.zhongmei.yunfu.db;

import com.j256.ormlite.field.DatabaseField;


public abstract class UuidEntityBase extends EntityBase<String> {
    private static final long serialVersionUID = 1L;

    protected interface $ {

        String uuid = "uuid";
    }

    @DatabaseField(columnName = "uuid", id = true)
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String pkValue() {
        return uuid;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(uuid);
    }
}
