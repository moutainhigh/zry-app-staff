package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.StatusFlag;


@DatabaseTable(tableName = "commercial_queue_config_file")
public class CommercialQueueConfigFile extends IdEntityBase {

    private static final long serialVersionUID = 1L;


    public interface $ extends IdEntityBase.$ {


        public static final String fileId = "file_id";


        public static final String sort = "sort";


        public static final String commerciaId = "commercia_id";


        public static final String brandId = "brand_id";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        String serverCreateTime = "server_create_time";


        String serverUpdateTime = "server_update_time";


        String statusFlag = "status_flag";


        String uuid = "uuid";


        String name = "name";


        String size = "size";


        String url = "url";

    }

    @DatabaseField(columnName = "uuid")
    private String uuid;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "size")
    private Integer size;

    @DatabaseField(columnName = "url")
    private String url;

    @DatabaseField(columnName = "file_id")
    private Long fileId;

    @DatabaseField(columnName = "sort")
    private Integer sort;

    @DatabaseField(columnName = "brand_id")
    private Long brandId;

    @DatabaseField(columnName = "commercial_id")
    private Long commercialId;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updater_id")
    private Long updaterId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;


    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;


    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;


    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long commercialId) {
        this.commercialId = commercialId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(statusFlag);
    }
}
