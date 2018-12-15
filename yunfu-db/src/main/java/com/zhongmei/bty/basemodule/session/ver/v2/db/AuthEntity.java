package com.zhongmei.bty.basemodule.session.ver.v2.db;

import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 * Created by demo on 2018/12/15
 */

public abstract class AuthEntity extends EntityBase<Long> {

    @DatabaseField(columnName = $.ID, canBeNull = false, index = true, id = true)
    private Long id;
    @DatabaseField(columnName = $.STATUS, canBeNull = false)
    private Integer statusFlag;
    @DatabaseField(columnName = $.ENABLE)
    private Integer enableFlag;
    @DatabaseField(columnName = $.CREATOR_ID)
    private Long creatorId;
    @DatabaseField(columnName = $.UPDATER_ID)
    private Long updatorId;
    @DatabaseField(columnName = $.SERVER_UPDATE_TIME)
    private Long serverUpdateTime;
    @DatabaseField(columnName = $.SERVER_CREATE_TIME, canBeNull = false)
    private Long serverCreateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    public Bool getEnableFlag() {
        return ValueEnums.toEnum(Bool.class, enableFlag);
    }

    public void setEnableFlag(Bool enableFlag) {
        this.enableFlag = ValueEnums.toValue(enableFlag);
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long pkValue() {
        return id;
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    public interface $ {
        String ID = "id";
        String STATUS = "status_flag";
        String ENABLE = "enable_flag";
        String CREATOR_ID = "creator_id";
        String UPDATER_ID = "updator_id";
        String SERVER_UPDATE_TIME = "server_update_time";
        String SERVER_CREATE_TIME = "server_create_time";
    }

}
