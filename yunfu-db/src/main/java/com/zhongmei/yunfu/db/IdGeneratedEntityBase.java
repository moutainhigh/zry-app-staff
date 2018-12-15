package com.zhongmei.yunfu.db;

import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.yunfu.db.EntityBase;

/**
 * @version: 1.0
 * @date 2015年9月11日
 */
public abstract class IdGeneratedEntityBase extends EntityBase<Long> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public interface $ {

        /**
         * id 为"_id"是为了支持
         */
        String id = "_id";

    }

    @DatabaseField(columnName = $.id, generatedId = true, canBeNull = false)
    protected Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long pkValue() {
        return id;
    }

    @Override
    public String toString() {
        return "IdEntityBase{" +
                "id=" + id +
                '}';
    }
}
