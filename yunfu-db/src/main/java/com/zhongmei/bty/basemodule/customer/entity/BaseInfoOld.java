package com.zhongmei.bty.basemodule.customer.entity;

import android.database.Cursor;

import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.yunfu.db.EntityBase;


public abstract class BaseInfoOld extends EntityBase<Long> {


    private static final long serialVersionUID = 1L;

    public interface $ {


        String id = "_id";


        String createDateTime = "createDateTime";


        String status = "status";


        String dirty = "dirty";


        String modifyDateTime = "modifyDateTime";


        String uuid = "uuid";

    }

    @DatabaseField(columnName = "_id", id = true, canBeNull = false)
    private Long id;

    @DatabaseField(columnName = "status")
    private Integer status;
    @DatabaseField(columnName = "createDateTime")
    private Long createDateTime;


    @DatabaseField(columnName = "modifyDateTime")
    private Long modifyDateTime;

    @DatabaseField(columnName = "uuid")
    private String uuid;

    public void initFromCursor(Cursor cursor) {
        this.id = cursor.getLong(cursor.getColumnIndex($.id));
        this.status = cursor.getInt(cursor.getColumnIndex($.status));
                this.createDateTime = cursor.getLong(cursor.getColumnIndex($.createDateTime));
                this.modifyDateTime = cursor.getLong(cursor.getColumnIndex($.modifyDateTime));
        this.uuid = cursor.getString(cursor.getColumnIndex($.uuid));
    }

    @Override
    public boolean isValid() {
        return true;     }

    @Override
    public Long pkValue() {
        return id;
    }

    @Override
    public Long verValue() {
        return modifyDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Long createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Long getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(Long modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(id);
    }
}
