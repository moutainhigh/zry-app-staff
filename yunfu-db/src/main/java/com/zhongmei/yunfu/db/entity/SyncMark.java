package com.zhongmei.yunfu.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.commonmodule.database.entity.local.LocalEntityBase;

/**
 * SyncMark is a ORMLite bean type. Corresponds to the database table "sync_mark"
 */
@DatabaseTable(tableName = "sync_mark")
public class SyncMark extends LocalEntityBase {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "sync_mark"
     */
    public interface $ extends LocalEntityBase.$ {

        /**
         * last_sync_marker
         */
        public static final String lastSyncMarker = "last_sync_marker";

    }

    @DatabaseField(columnName = "last_sync_marker", canBeNull = false)
    private String lastSyncMarker;

    public String getLastSyncMarker() {
        return lastSyncMarker;
    }

    public void setLastSyncMarker(String lastSyncMarker) {
        this.lastSyncMarker = lastSyncMarker;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(lastSyncMarker);
    }
}

