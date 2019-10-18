package com.zhongmei.bty.basemodule.commonbusiness.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.CommonEntityBase;


@DatabaseTable(tableName = "sync_dict")
public class SyncDict extends CommonEntityBase {
    private static final long serialVersionUID = 1L;


    public interface $ extends CommonEntityBase.$ {

        String type = "type";


        String code = "code";


        String name = "name";
    }

    @DatabaseField(columnName = "type", canBeNull = false)
    private Integer type;

    @DatabaseField(columnName = "code", canBeNull = false)
    private Integer code;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(type, code, name);
    }
}
