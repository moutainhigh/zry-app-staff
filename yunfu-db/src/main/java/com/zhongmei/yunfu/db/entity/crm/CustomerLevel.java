package com.zhongmei.yunfu.db.entity.crm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.customer.entity.BaseInfoOld;

/**
 * @Date：2015-8-28 上午11:59:08
 * @Description: 会员等级
 * @Version: 1.0
 * @see com.zhongmei.bty.entity.bean.customer.CustomerLevel 替换原始类
 */
@DatabaseTable(tableName = "customer_level")
public class CustomerLevel extends BaseInfoOld {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public interface $ extends BaseInfoOld.$ {

        /**
         * level
         */
        String level = "level";

        /**
         * name
         */
        String name = "name";

    }

    @DatabaseField(columnName = "level")
    private Long level;

    @DatabaseField(columnName = "name")
    private String name;

    //@DatabaseField
    //private Long brandId;

    //@DatabaseField
    //private Integer isEnable;

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
