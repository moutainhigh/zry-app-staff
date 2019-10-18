package com.zhongmei.bty.basemodule.queue;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;


@DatabaseTable(tableName = "commercial_queue_line")
public class CommercialQueueLine extends EntityBase<Long> {


    private static final long serialVersionUID = 1L;

    public interface $ {


        String id = "id";


        String commercialId = "commercial_id";


        String brandId = "brand_id";


        String isDelete = "is_delete";


        String queueName = "queue_name";


        String minPersonCount = "min_person_count";


        String creatorId = "creator_id";


        String memo = "memo";


        String updateTime = "update_ime";


        String queueChar = "queue_char";


        String maxPersonCount = "max_person_count";


        String createTime = "create_time";


        String updaterId = "updater_d";

    }

    @DatabaseField(columnName = "id", id = true, canBeNull = false)
    private Long id;


    @DatabaseField(columnName = "commercial_id")
    private Long commercialId;

    @DatabaseField(columnName = "brand_id")
    private Long brandId;


    @DatabaseField(columnName = "is_delete", canBeNull = false)
    private Integer isDelete;


    @DatabaseField(columnName = "queue_name")
    private String queueName;


    @DatabaseField(columnName = "min_person_count")
    private Integer minPersonCount;


    @DatabaseField(columnName = "max_person_count")
    private Integer maxPersonCount;


    @DatabaseField(columnName = "memo")
    private String memo;


    @DatabaseField(columnName = "queue_char")
    private String queueChar;

    @DatabaseField(columnName = "create_id")
    private Long creatorId;

    @DatabaseField(columnName = "create_time")
    private Long createTime;

    @DatabaseField(columnName = "updater_id")
    private Long updaterId;

    @DatabaseField(columnName = "update_time")
    private Long updateTime;

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long commercialId) {
        this.commercialId = commercialId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public Integer getMinPersonCount() {
        return minPersonCount;
    }

    public void setMinPersonCount(Integer minPersonCount) {
        this.minPersonCount = minPersonCount;
    }

    public Integer getMaxPersonCount() {
        return maxPersonCount;
    }

    public void setMaxPersonCount(Integer maxPersonCount) {
        this.maxPersonCount = maxPersonCount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getQueueChar() {
        return queueChar;
    }

    public void setQueueChar(String queueChar) {
        this.queueChar = queueChar;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
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
        return isDelete == 0;
    }

    @Override
    public Long pkValue() {
        return id;
    }

    @Override
    public Long verValue() {
        return updateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(id, isDelete);
    }
}
