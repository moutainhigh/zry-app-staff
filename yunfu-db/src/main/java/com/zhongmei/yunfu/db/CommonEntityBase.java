package com.zhongmei.yunfu.db;

import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.StatusFlag;


public class CommonEntityBase extends IdEntityBase {


    private static final long serialVersionUID = 1L;

    protected interface $ extends IdEntityBase.$ {


        String statusFlag = "status_flag";


        String serverCreateTime = "server_create_time";


        String serverUpdateTime = "server_update_time";

    }


    @DatabaseField(columnName = $.statusFlag, canBeNull = false)
    private Integer statusFlag;


    @DatabaseField(columnName = $.serverCreateTime)
    private Long serverCreateTime;


    @DatabaseField(columnName = $.serverUpdateTime)
    private Long serverUpdateTime;


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
