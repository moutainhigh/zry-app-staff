package com.zhongmei.bty.basemodule.database.queue;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "queue_detail_image")
public class QueueDetailImage extends EntityBase<Long> {

    public interface $ {

        String id = "id";

        String status = "status";

        String queueImgId = "queue_img_id";

        String imageUrl = "image_url";

        String modifyDateTime = "modify_date_time";

        String createDateTime = "create_date_time";

    }

    @DatabaseField(columnName = "id", id = true, canBeNull = false)
    private Long id;

    /**
     * 图片URL
     */
    @DatabaseField(columnName = "queue_img_id")
    private String queueImgId;

    /**
     * 图片URL
     */
    @DatabaseField(columnName = "image_url")
    private String imageUrl;

    /**
     * 创建时间
     */
    @DatabaseField(columnName = "create_date_time")
    private Long createDateTime;

    /**
     * 更新时间
     */
    @DatabaseField(columnName = "modify_date_time")
    private Long modifyDateTime;

    /**
     * 状态 -1错误 0正常
     */
    @DatabaseField(columnName = "status")
    private Integer status;

    @DatabaseField(columnName = "synFlag")
    private String synFlag;

    public Long getId() {
        return id;
    }

    public String getQueueImgId() {
        return queueImgId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getCreateDateTime() {
        return createDateTime;
    }

    public Long getModifyDateTime() {
        return modifyDateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public String getSynFlag() {
        return synFlag;
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
}
