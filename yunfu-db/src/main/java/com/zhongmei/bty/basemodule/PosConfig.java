package com.zhongmei.bty.basemodule;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;

@DatabaseTable(tableName = "pos_config")
public class PosConfig extends IdEntityBase {

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public interface $ extends IdEntityBase.$ {
        String key = "key";
        String value = "value";
        String brand_id = "brand_id";
        String shop_id = "shop_id";
        String device_id = "device_id";
        String serverCreateTime = "server_create_time";
        String serverUpdateTime = "server_update_time";
    }

    @DatabaseField(columnName = "key", canBeNull = false)
    private String key;
    @DatabaseField(columnName = "value", canBeNull = false)
    private String value;
    @DatabaseField(columnName = "brand_id", canBeNull = false)
    private Long brandId;
    @DatabaseField(columnName = "shop_id", canBeNull = false)
    private Long shopId;
    @DatabaseField(columnName = "device_id")
    private String deviceId;
    @DatabaseField(columnName = "server_create_time", canBeNull = false)
    private Long serverCreateTime;
    @DatabaseField(columnName = "server_update_time", canBeNull = false)
    private Long serverUpdateTime;

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }
}