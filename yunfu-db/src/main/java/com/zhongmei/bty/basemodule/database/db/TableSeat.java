package com.zhongmei.bty.basemodule.database.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.ServerEntityBase;


@DatabaseTable(tableName = "table_seat")
public class TableSeat extends ServerEntityBase {

    public interface $ extends ServerEntityBase.$ {

        public static final String tableId = "table_id";


        public static final String seatName = "seat_name";


        public static final String sort = "sort";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static String updatorId = "updator_id";


        public static final String updatorName = "updator_name";
    }

    @DatabaseField(columnName = "table_id")
    private Long tableId;

    @DatabaseField(columnName = "seat_name")
    private String seatName;

    @DatabaseField(columnName = "sort")
    private Integer sort;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;


    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
