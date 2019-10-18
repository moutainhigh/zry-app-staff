package com.zhongmei.bty.data.db.common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.IsDelete;
import com.zhongmei.yunfu.util.ValueEnums;


@DatabaseTable(tableName = "sys_file")
public class SysFile extends IdEntityBase {

    private static final long serialVersionUID = 1L;


    public interface $ extends IdEntityBase.$ {


        public static final String fileName = "file_name";


        public static final String fileType = "file_type";


        public static final String fileSize = "file_size";


        public static final String fileUrl = "file_url";


        public static final String commerciaId = "commercia_id";


        public static final String brandId = "brand_id";


        public static final String businessType = "business_type";


        public static final String userType = "user_type";


        public static final String memo = "memo";


        public static final String isDelete = "is_delete";


        public static final String creatorId = "creator_id";


        public static final String createTime = "create_time";


        public static final String updaterId = "updater_id";


        public static final String updateTime = "update_time";

    }

    @DatabaseField(columnName = "file_name")
    private String fileName;

    @DatabaseField(columnName = "file_type")
    private String fileType;

    @DatabaseField(columnName = "file_size")
    private Integer fileSize;

    @DatabaseField(columnName = "file_url")
    private String fileUrl;


    @DatabaseField(columnName = "business_type")
    private Integer businessType;

    @DatabaseField(columnName = "user_type")
    private Integer userType;

    @DatabaseField(columnName = "brand_id")
    private Long brandId;

    @DatabaseField(columnName = "commercial_id")
    private Long commercialId;

    @DatabaseField(columnName = "create_time")
    private Long createTime;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "is_delete")
    private Integer isDelete;

    @DatabaseField(columnName = "memo")
    private String memo;

    @DatabaseField(columnName = "updater_id")
    private Long updaterId;

    @DatabaseField(columnName = "update_time", canBeNull = false)
    private Long updateTime;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public IsDelete getIsDelete() {
        return ValueEnums.toEnum(IsDelete.class, isDelete);
    }

    public void setIsDelete(IsDelete isDelete) {
        this.isDelete = ValueEnums.toValue(isDelete);
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(IsDelete.VALID, isDelete);
    }

    @Override
    public Long verValue() {
        return updateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(updateTime);
    }
}
