package com.zhongmei.bty.basemodule.customer.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "cashType")
public class CashType extends BaseInfoOld {


    private static final long serialVersionUID = 1L;

    public interface $ extends BaseInfoOld.$ {


        String cashType = "cashType";

        String commercialGroupId = "commercialGroupId";

    }

    @DatabaseField(columnName = "cashType")
    public String cashType;

    @DatabaseField(columnName = "commercialGroupId")
    public Long commercialGroupId;

}
