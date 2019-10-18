package com.zhongmei.bty.basemodule.customer.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "takeaway_memo")
public class TakeawayMemo extends BaseInfoOld {


    private static final long serialVersionUID = 1L;

    public interface $ extends BaseInfoOld.$ {


        String memoContent = "memoContent";

        String memo = "memo";

    }

    public static final String RECORD_STATUS_DIRTY = "1";        public static final String RECORD_STATUS_UNDIRTY = "0";        public static final String RECORD_STATUS_UNDELETE = "0";    public static final String RECORD_STATUS_DELETE = "-1";
    @DatabaseField(columnName = "memoContent")
    private String memoContent;
    @DatabaseField
    private String memo;

    public String getMemoContent() {
        return memoContent;
    }

    public void setMemoContent(String memoContent) {
        this.memoContent = memoContent;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
