package com.zhongmei.bty.basemodule.database.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.ServerEntityBase;

/**
 * 交接设置
 */
@DatabaseTable(tableName = "cash_handover_config")
public class CashHandoverConfig extends ServerEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "cash_handover_config"
     */
    public interface $ extends ServerEntityBase.$ {

        /**
         * uuid
         */
        String uuid = "uuid";

        /**
         * config_key
         */
        String configKey = "config_key";

        /**
         * config_value
         */
        String configValue = "config_value";

        /**
         * creator_id
         */
        String creatorId = "creator_id";

        /**
         * creator_name
         */
        String creatorName = "creator_name";

        /**
         * updator_id
         */
        String updatorId = "updator_id";

        /**
         * updator_name
         */
        String updatorName = "updator_name";

    }

    @DatabaseField(columnName = "uuid")
    private String uuid;

    /**
     * key = 1标识交班的交接方式
     * key = 2标识厨打的出票方式
     * key = 3标识打印定时时间
     * key = 4收银小数点保留位数
     * key = 5收银进位规则
     * key = 6正餐一台多单设置
     */
    @DatabaseField(columnName = "config_key")
    private Integer configKey;

    /**
     * key = 1(value = 1：表示交接方式是累加交接，2。表示是清零交接)
     * key = 2(value = 1：表示是网口打印机, 2：表示出票方式是一体机)
     * key = 3(0. 0min  1. 30min  2. 60min  3. 90min  4. 120min)
     * key=4(0|1|2|3表示小数点保留0|1|2|3)
     * key=5(1.四舍五入,2.无条件进位,3无条件摸零)
     * key=6(1.允许,2.不允许)
     */
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
