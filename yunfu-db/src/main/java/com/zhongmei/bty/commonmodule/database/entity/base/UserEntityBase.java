package com.zhongmei.bty.commonmodule.database.entity.base;

import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.ValueEnums;


public abstract class UserEntityBase extends IdEntityBase {


    private static final long serialVersionUID = 1L;

    public interface $ extends IdEntityBase.$ {
        public static final String creatorId = "creator_id";

        public static final String creatorName = "creator_name";

        public static final String updatorId = "updator_id";

        public static final String updatorName = "updator_name";

        public static final String statusFlag = "status_flag";


        public static final String serverCreateTime = "server_create_time";


        public static final String serverUpdateTime = "server_update_time";
    }


    @DatabaseField(columnName = "creator_id")
    private Long creatorId;


    @DatabaseField(columnName = "creator_name")
    private String creatorName;


    @DatabaseField(columnName = "updator_id")
    private Long updatorId;


    @DatabaseField(columnName = "updator_name")
    private String updatorName;


    @DatabaseField(columnName = "status_flag")
    private Integer statusFlag;


    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;


    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;


    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }


    public Long getCreatorId() {
        return this.creatorId;
    }


    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }


    public String getCreatorName() {
        return this.creatorName;
    }


    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }


    public Long getUpdatorId() {
        return this.updatorId;
    }


    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }


    public String getUpdatorName() {
        return this.updatorName;
    }

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
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

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }
}
