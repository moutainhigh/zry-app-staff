package com.zhongmei.yunfu.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.AbsBasicEntityBase;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.IsDelete;

/**
 * CommercialArea is a ORMLite bean type. Corresponds to the database table "commercial_area"
 */
@DatabaseTable(tableName = "table_area")
public class CommercialArea extends AbsBasicEntityBase {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "commercial_area"
     */
    public interface $ extends IdEntityBase.$ {

        /**
         * area_code
         */
        public static final String areaCode = "area_code";

        /**
         * area_name
         */
        public static final String areaName = "area_name";


        /**
         * floor
         */
        public static final String floor = "floor";

        /**
         * is_delete
         */
        public static final String isDelete = "is_delete";

        /**
         * is_smoking
         */
        public static final String isSmoking = "is_smoking";

        /**
         * memo
         */
        public static final String memo = "memo";

        /**
         * table_type_id
         */
        public static final String tableTypeId = "table_type_id";

        /**
         * server_create_time
         */
        String serverCreateTime = "server_create_time";

        /**
         * server_update_time
         */
        String serverUpdateTime = "server_update_time";

        /**
         * shop_identy
         */
        String shopIdenty = "shop_identy";

        /**
         * device_identy
         */
        String deviceIdenty = "device_identy";

    }

    @DatabaseField(columnName = "area_code")
    private String areaCode;

    @DatabaseField(columnName = "area_name")
    private String areaName;


    @DatabaseField(columnName = "floor")
    private Integer floor;

    @DatabaseField(columnName = "is_delete")
    private Integer isDelete;

    @DatabaseField(columnName = "is_smoking")
    private Byte isSmoking;

    @DatabaseField(columnName = "memo")
    private String memo;

    @DatabaseField(columnName = "table_type_id")
    private Long tableTypeId;

    @DatabaseField(columnName = "updater_id")
    private Long updaterId;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }


    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public IsDelete getIsDelete() {
        return ValueEnums.toEnum(IsDelete.class, isDelete);
    }

    public void setIsDelete(IsDelete isDelete) {
        this.isDelete = ValueEnums.toValue(isDelete);
    }

    public Byte getIsSmoking() {
        return isSmoking;
    }

    public void setIsSmoking(Byte isSmoking) {
        this.isSmoking = isSmoking;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getTableTypeId() {
        return tableTypeId;
    }

    public void setTableTypeId(Long tableTypeId) {
        this.tableTypeId = tableTypeId;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }


    @Override
    public Long verValue() {
        return id;
    }


    @Override
    public boolean checkNonNull() {
        return super.checkNonNull();
    }
}

