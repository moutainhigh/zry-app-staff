package com.zhongmei.bty.basemodule.shopmanager.closing.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.DataEntityBase;


@DatabaseTable(tableName = "closing_account_record")
public class ClosingAccountRecord extends DataEntityBase {
    private static final long serialVersionUID = 1L;


    public interface $ extends DataEntityBase.$ {

        public static final String belongDate = "belong_date";

        public static final String startTime = "start_time";

        public static final String endTime = "end_time";

        public static final String creatorId = "creator_id";

        public static final String creatorName = "creator_name";

        public static final String creatorAccount = "creator_account";
    }

    @DatabaseField(columnName = "belong_date", canBeNull = false)
    private Long belongDate;

    @DatabaseField(columnName = "start_time", canBeNull = false)
    private Long startTime;

    @DatabaseField(columnName = "end_time", canBeNull = false)
    private Long endTime;

    @DatabaseField(columnName = "creator_id", canBeNull = false)
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "creator_account")
    private Long creatorAccount;

    public Long getCreatorAccount() {
        return creatorAccount;
    }

    public void setCreatorAccount(Long creatorAccount) {
        this.creatorAccount = creatorAccount;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getBelongDate() {
        return belongDate;
    }

    public void setBelongDate(Long belongDate) {
        this.belongDate = belongDate;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(belongDate, startTime, endTime, creatorId);
    }
}
