package com.zhongmei.bty.data.db.common.queue;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;

/**
 * 排队
 */
@DatabaseTable(tableName = "queue_image")
public class QueueImage extends EntityBase<Long> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public interface $ {

        String id = "id";

        String status = "status";

        String commercialID = "commercial_id";

        String synFlag = "syn_flag";

        String backgroudImgUrl = "backgroud_img_url";

        String type = "type";

        String voice = "voice";

        String video = "video";

        String modifyDateTime = "modify_date_time";

        String createDateTime = "create_date_time";

    }

    /**
     * 商户id
     */
    @DatabaseField(columnName = "commercial_id")
    private Long commercialID;

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

    @DatabaseField(columnName = "synFlag")
    private String synFlag;

    @DatabaseField(columnName = "id", id = true, canBeNull = false)
    private Long id;

    //1视频 0图片
    @DatabaseField(columnName = "type")
    private Integer type;

    @DatabaseField(columnName = "backgroud_img_url")
    private String backgroudImgUrl;

    @DatabaseField(columnName = "voice")
    private String voice;

    @DatabaseField(columnName = "video")
    private String video;

    @DatabaseField(columnName = "status", canBeNull = false)
    private Integer status;

    @DatabaseField(columnName = "uuid")
    private String uuid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
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

    public String getSynFlag() {
        return synFlag;
    }

    public void setSynFlag(String synFlag) {
        this.synFlag = synFlag;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBackgroudImgUrl() {
        return backgroudImgUrl;
    }

    public void setBackgroudImgUrl(String backgroudImgUrl) {
        this.backgroudImgUrl = backgroudImgUrl;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
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
