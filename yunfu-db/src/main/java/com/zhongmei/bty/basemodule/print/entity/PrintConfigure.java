package com.zhongmei.bty.basemodule.print.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;


@DatabaseTable(tableName = "print_configure")
public class PrintConfigure extends IdEntityBase {
    private static final long serialVersionUID = 1L;



    public interface $ extends IdEntityBase.$ {


        String key = "key";


        String value = "value";


        String enableFlag = "enable_flag";


        String statusFlag = "status_flag";


        String serverCreateTime = "server_create_time";


        String creatorId = "creator_id";


        String creatorName = "creator_name";


        String serverUpdateTime = "server_update_time";


        String updatorId = "updator_id";


        String updatorName = "updator_name";


        String type = "type";
    }

    @DatabaseField(columnName = $.key, canBeNull = false)
    private String key;

    @DatabaseField(columnName = $.value, canBeNull = false)
    private String value;

    @DatabaseField(columnName = $.enableFlag, canBeNull = false)
    private Integer enableFlag;

    @DatabaseField(columnName = $.type)
    private Integer type;

    @DatabaseField(columnName = $.statusFlag, canBeNull = false)
    private Integer statusFlag;

    @DatabaseField(columnName = $.serverCreateTime, canBeNull = false)
    private Long serverCreateTime;

    @DatabaseField(columnName = $.serverUpdateTime, canBeNull = false)
    private Long serverUpdateTime;

    @DatabaseField(columnName = $.updatorId)
    private Long updatorId;

    @DatabaseField(columnName = $.updatorName)
    private String updatorName;

    @DatabaseField(columnName = $.creatorId)
    private Long creatorId;

    @DatabaseField(columnName = $.creatorName)
    private String creatorName;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Integer enableFlag) {
        this.enableFlag = enableFlag;
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

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(key, value, enableFlag, statusFlag, serverCreateTime, serverUpdateTime);
    }
}
