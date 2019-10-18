package com.zhongmei.bty.basemodule.customer.entity;

import android.database.Cursor;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "takeaway_address")
public class TakeawayAddress extends BaseInfoOld {


    private static final long serialVersionUID = 1L;

    public interface $ extends BaseInfoOld.$ {


        String addressContent = "addressContent";

        String parentID = "parentID";

    }

    public static final String RECORD_STATUS_DIRTY = "1";        public static final String RECORD_STATUS_UNDIRTY = "0";        public static final String RECORD_STATUS_UNDELETE = "0";    public static final String RECORD_STATUS_DELETE = "-1";
    @DatabaseField
    private String addressContent;
    @DatabaseField
    private String parentID;

    @Override
    public void initFromCursor(Cursor cursor) {
        super.initFromCursor(cursor);
        this.addressContent = cursor.getString(cursor.getColumnIndex("addressContent"));
        this.parentID = cursor.getString(cursor.getColumnIndex("parentID"));
    }

    public String getAddressContent() {
        return addressContent;
    }

    public void setAddressContent(String addressContent) {
        this.addressContent = addressContent;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }
}
