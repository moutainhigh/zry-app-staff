package com.zhongmei.bty.basemodule.database.queue;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 * 排队扩展表
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "queue_extra")
public class QueueExtra extends IdEntityBase {

    @DatabaseField(columnName = "commercial_id")
    public Long commercialID;
    @DatabaseField(columnName = "queue_id")
    public Long queueID;
    @DatabaseField(columnName = "url")
    public String url;
    @DatabaseField(columnName = "open_ids")
    public String openIds;
    @DatabaseField(columnName = "server_create_time")
    public Long serverCreateTime;
    @DatabaseField(columnName = "server_update_time")
    public Long serverUpdateTime;
    @DatabaseField(columnName = "status_flag")
    public Integer statusFlag;
    @DatabaseField(columnName = "call_count")
    public Integer callCount;
    @DatabaseField(columnName = "last_call_number_time")
    public Long lastCallNumberTime;
    @DatabaseField(columnName = "meida_url")
    public String meidaUrl;

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }


    public interface $ extends IdEntityBase.$ {

        String commercial_id = "commercial_id";
        String queue_id = "queue_id";
        String url = "url";
        String open_ids = "open_ids";
        String server_create_time = "server_create_time";
        String server_update_time = "server_update_time";
        String status_flag = "status_flag";
        String call_count = "call_count";
        String last_call_number_time = "last_call_number_time";
        String meidaUrl = "meida_url";

    }
}
