package com.zhongmei.bty.basemodule.database.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.ServerEntityBase;


@DatabaseTable(tableName = "cash_handover_config")
public class CashHandoverConfig extends ServerEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends ServerEntityBase.$ {


        String uuid = "uuid";


        String configKey = "config_key";


        String configValue = "config_value";


        String creatorId = "creator_id";


        String creatorName = "creator_name";


        String updatorId = "updator_id";


        String updatorName = "updator_name";

    }

    @DatabaseField(columnName = "uuid")
    private String uuid;


    @DatabaseField(columnName = "config_key")
    private Integer configKey;


    @DatabaseField(columnName = "config_value")
    private Integer configValue;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getConfigKey() {
        return configKey;
    }

    public void setConfigKey(Integer configKey) {
        this.configKey = configKey;
    }

    public Integer getConfigValue() {
        return configValue;
    }

    public void setConfigValue(Integer configValue) {
        this.configValue = configValue;
    }

    public Long getCreatorId() {
        return creatorId;
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

}
