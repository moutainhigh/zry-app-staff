package com.zhongmei.bty.basemodule.commonbusiness.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;


@DatabaseTable(tableName = "commercial_custom_settings")
public class CommercialCustomSettings extends BasicEntityBase {
    private static final long serialVersionUID = 1L;

    public interface $ extends BasicEntityBase.$ {

        public static final String key = "setting_key";

        public static final String value = "setting_value";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";

    }


    @DatabaseField(columnName = "setting_key", canBeNull = false)
    private String settingKey;

    @DatabaseField(columnName = "setting_value", canBeNull = false)
    private String settingValue;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;


    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String key) {
        this.settingKey = key;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String value) {
        this.settingValue = value;
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

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(settingKey, settingValue);
    }
}
