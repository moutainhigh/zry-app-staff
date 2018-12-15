package com.zhongmei.bty.basemodule.customer.entity;

import android.database.Cursor;

import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.yunfu.db.EntityBase;

/**
 * @Date：2015-8-28 上午11:59:08
 * @Description: 会员等级
 * @Version: 1.0
 */
public abstract class BaseInfoOld extends EntityBase<Long> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public interface $ {

        /**
         * id
         */
        String id = "_id";

        /**
         * localCreateDateTime
         */
        String createDateTime = "createDateTime";

        /**
         * status
         */
        String status = "status";

        /**
         * dirty
         */
        String dirty = "dirty";

        /**
         * localModifyDateTime
         */
        String modifyDateTime = "modifyDateTime";

        /**
         * uuid
         */
        String uuid = "uuid";

    }

    @DatabaseField(columnName = "_id", id = true, canBeNull = false)
    private Long id;

    @DatabaseField(columnName = "status")
    private Integer status; //0有效 -1无效

    @DatabaseField(columnName = "createDateTime")
    private Long createDateTime;

    //@DatabaseField(columnName = "dirty")
    //private String dirty;

    @DatabaseField(columnName = "modifyDateTime")
    private Long modifyDateTime;

    @DatabaseField(columnName = "uuid")
    private String uuid;

    public void initFromCursor(Cursor cursor) {
        this.id = cursor.getLong(cursor.getColumnIndex($.id));
        this.status = cursor.getInt(cursor.getColumnIndex($.status));
        //this._id = cursor.getLong(cursor.getColumnIndex("_id"));
        this.createDateTime = cursor.getLong(cursor.getColumnIndex($.createDateTime));
        //this.dirty = cursor.getString(cursor.getColumnIndex("dirty"));
        this.modifyDateTime = cursor.getLong(cursor.getColumnIndex($.modifyDateTime));
        this.uuid = cursor.getString(cursor.getColumnIndex($.uuid));
    }

    @Override
    public boolean isValid() {
        return true; //status == 0;
    }

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
