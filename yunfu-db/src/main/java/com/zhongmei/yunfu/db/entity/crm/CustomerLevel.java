package com.zhongmei.yunfu.db.entity.crm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.customer.entity.BaseInfoOld;


@DatabaseTable(tableName = "customer_level")
public class CustomerLevel extends BaseInfoOld {


    private static final long serialVersionUID = 1L;

    public interface $ extends BaseInfoOld.$ {


        String level = "level";


        String name = "name";

    }

    @DatabaseField(columnName = "level")
    private Long level;

    @DatabaseField(columnName = "name")
    private String name;



    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public String getLevelName() {
        return name;
    }

    public void setLevelName(String levelName) {
        this.name = levelName;
    }

}
