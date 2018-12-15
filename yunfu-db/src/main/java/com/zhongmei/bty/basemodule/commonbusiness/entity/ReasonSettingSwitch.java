package com.zhongmei.bty.basemodule.commonbusiness.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * @date 2016/12/26 15:00
 * <p>
 * CREATE TABLE reason_setting_switch (
 * id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
 * brand_identy bigint(20) DEFAULT NULL COMMENT '品牌id',
 * shop_identy bigint(20) NOT NULL COMMENT '商户id',
 * reason_value varchar(50) NOT NULL COMMENT '理由code',
 * enable_switch tinyint(4) NOT NULL COMMENT '开关属性 1:打开 2:关闭',
 * status_flag tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态标识  1:启用 2:禁用',
 * creator_id bigint(20) NOT NULL COMMENT '创建人id',
 * creator_name varchar(32) NOT NULL COMMENT '创建人名称',
 * updator_id bigint(20) NOT NULL COMMENT '修改人id',
 * updator_name varchar(32) NOT NULL COMMENT '修改人名称',
 * server_create_time timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) COMMENT '服务器创建时间',
 * server_update_time timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3) COMMENT '服务器更新时间',
 * PRIMARY KEY (id),
 * UNIQUE INDEX idx_brand_shop_key (brand_identy, shop_identy, scenario_code),
 * INDEX idx_shop_identy_server_update_time (shop_identy, server_update_time)
 * )
 * ENGINE = INNODB
 * AUTO_INCREMENT = 255879
 * AVG_ROW_LENGTH = 107
 * CHARACTER SET utf8
 * COLLATE utf8_general_ci
 * COMMENT = '理由开关设置表';
 * </p>
 */
@DatabaseTable(tableName = "reason_setting_switch")
public class ReasonSettingSwitch extends IdEntityBase {

    //enableSwitch开关属性 1:打开 2:关闭
    public static final int SWITCH_ON = 1;
    public static final int SWITCH_OFF = 2;

    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty; //'品牌id'

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty; //'商户id'

    @DatabaseField(columnName = "reason_value")
    private String reasonValue; //'理由code'

    @DatabaseField(columnName = "enable_switch")
    private Integer enableSwitch; //'开关属性 1:打开 2:关闭'

    @DatabaseField(columnName = "status_flag")
    private Integer statusFlag; //'状态标识  1:启用 2:禁用'

    @DatabaseField(columnName = "creator_id")
    private Long creatorId; //'创建人id'

    @DatabaseField(columnName = "creator_name")
    private String creatorName; //'创建人名称'

    @DatabaseField(columnName = "updator_id")
    private Long updatorId; //'修改人id',

    @DatabaseField(columnName = "updator_name")
    private String updatorName; //'修改人名称'

    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime; //'服务器创建时间'

    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime; //'服务器更新时间'

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public String getReasonValue() {
        return reasonValue;
    }

    public void setReasonValue(String reasonValue) {
        this.reasonValue = reasonValue;
    }

    public Integer getEnableSwitch() {
        return enableSwitch;
    }

    public void setEnableSwitch(Integer enableSwitch) {
        this.enableSwitch = enableSwitch;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
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

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(brandIdenty, shopIdenty);
    }

    /**
     * The columns of table "reason_setting_switch"
     */
    public interface $ extends IdEntityBase.$ {

        /**
         * brand_identy
         */
        String brandIdenty = "brand_identy";

        String shopIdenty = "shop_identy";

        String reasonValue = "reason_value";

        String enableSwitch = "enable_switch";

        String statusFlag = "status_flag";

        String creatorId = "creator_id";

        String creatorName = "creator_name";

        String updatorId = "updator_id";

        String updatorName = "updator_name";

        String serverCreateTime = "server_create_time";

        String serverUpdateTime = "server_update_time";
    }

}
