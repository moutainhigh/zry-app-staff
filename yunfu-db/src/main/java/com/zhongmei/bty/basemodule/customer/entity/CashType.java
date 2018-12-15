package com.zhongmei.bty.basemodule.customer.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @Date：2015-8-28 上午11:59:08
 * @Description: 会员等级
 * @Version: 1.0
 * @see com.zhongmei.bty.entity.bean.cash.CashType 替换原始类
 */
@DatabaseTable(tableName = "cashType")
public class CashType extends BaseInfoOld {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public interface $ extends BaseInfoOld.$ {

        /**
         * name
         */
        String cashType = "cashType";

        String commercialGroupId = "commercialGroupId";

    }

    @DatabaseField(columnName = "cashType")
    public String cashType;

    @DatabaseField(columnName = "commercialGroupId")
    public Long commercialGroupId;

}
