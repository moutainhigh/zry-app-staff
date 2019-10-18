package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;


@DatabaseTable(tableName = "qr_code_info")
public class QrCodeInfo extends EntityBase<Long> {
    private static final long serialVersionUID = 1L;

    public interface $ {

        String id = "id";

        String status = "status";

        String commercialID = "commercial_id";

        String name = "name";

        String image = "image";

        String type = "type";

        String content = "content";

        String modifyDateTime = "modify_date_time";

        String createDateTime = "create_date_time";

    }


    @DatabaseField(columnName = "commercial_id")
    private Long commercialID;


    @DatabaseField(columnName = "create_date_time")
    private Long createDateTime;


    @DatabaseField(columnName = "modify_date_time")
    private Long modifyDateTime;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "id", id = true, canBeNull = false)
    private Long id;

    @DatabaseField(columnName = "type")
    private Integer type;

    @DatabaseField(columnName = "image")
    private String image;

    @DatabaseField(columnName = "content")
    private String content;

    @DatabaseField(columnName = "status", canBeNull = false)
    private Integer status;

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

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Long getCommercialID() {
        return commercialID;
    }

    public void setCommercialID(Long commercialID) {
        this.commercialID = commercialID;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Long verValue() {
        return modifyDateTime;
    }

    @Override
    public boolean isValid() {
        return status == 0;
    }

    @Override
    public Long pkValue() {
        return id;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(id, status);
    }
}
