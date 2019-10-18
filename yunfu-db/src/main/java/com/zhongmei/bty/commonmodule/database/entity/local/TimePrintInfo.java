package com.zhongmei.bty.commonmodule.database.entity.local;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;


@DatabaseTable(tableName = "time_print_info")
public class TimePrintInfo extends EntityBase<Long> {

    private static final long serialVersionUID = 1L;


    @DatabaseField(columnName = "id", canBeNull = false, generatedId = true)
    private Long id;

    @DatabaseField(columnName = "code")
    private Integer code;

    @DatabaseField(columnName = "time")
    private Long time;

    @DatabaseField(columnName = "uuid")
    private String uuid;

    @DatabaseField(columnName = "trade_id")
    private Long tradeId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    @Override
    public boolean isValid() {

                return true;
    }

    @Override
    public Long pkValue() {
                return id;
    }

    @Override
    public Long verValue() {
                return null;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(id);
    }
}
