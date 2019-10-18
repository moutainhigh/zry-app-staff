package com.zhongmei.bty.basemodule.print.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;

@DatabaseTable(tableName = "print_content")
public class PrintContent extends EntityBase<Long> {

    private static final long serialVersionUID = 1L;

    public interface $ {


        public static final String id = "id";


        public static final String status = "status";


        public static final String content = "content";


        public static final String createDateTime = "createDateTime";


        public static final String modifyDateTime = "modifyDateTime";


        public static final String commercialId = "commercialID";

    }

    @DatabaseField(columnName = "id", canBeNull = false, id = true)
    private Long id;

    @DatabaseField(columnName = "content")
    private String content;

    @DatabaseField(columnName = "commercialId")
    private String commercialID;

    @DatabaseField(columnName = "status")
    private Integer status;

    @DatabaseField(columnName = "createDateTime")
    private Long createDateTime;

    @DatabaseField(columnName = "modifyDateTime")
    private Long modifyDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommercialId() {
        return commercialID;
    }

    public void setCommercialId(String commercialId) {
        this.commercialID = commercialId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public boolean isValid() {
        return status == 0;
    }

    @Override
    public Long pkValue() {
        return id;
    }

    @Override
    public Long verValue() {
        return modifyDateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(id);
    }
}
