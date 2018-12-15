package com.zhongmei.bty.basemodule.session.ver.v2.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = AuthDataPermissionDetailEntity.$.TABLE_NAME)
public class AuthDataPermissionDetailEntity extends AuthEntity {

//    CREATE TABLE `ps_auth_data_permission_details` (
//            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
//            `data_per_id` bigint(20) NOT NULL COMMENT '数据权限头表ID',
//            `value` varchar(20) DEFAULT NULL COMMENT '数据值',
//            `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
//            `server_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
//            `creator_id` bigint(20) NOT NULL COMMENT '创建者ID',
//            `updator_id` bigint(20) DEFAULT NULL COMMENT '更新者ID',
//            `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '删除标识:1正常，0逻辑删除',
//            `enable_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '启用标识:1启用，0禁用',
//    PRIMARY KEY (`id`),
//    KEY `idx_data_per_id` (`data_per_id`)
//            ) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='数据权限明细表'

    public interface $ extends AuthEntity.$ {
        String TABLE_NAME = "ps_auth_data_permission_details";
        String DATA_PERMISSION_ID = "data_per_id";
        String VALUE = "value";
    }

    @DatabaseField(columnName = $.DATA_PERMISSION_ID, canBeNull = false)
    private Long dataPerId;
    @DatabaseField(columnName = $.VALUE)
    private String value;

    public Long getDataPerId() {
        return dataPerId;
    }

    public void setDataPerId(Long dataPerId) {
        this.dataPerId = dataPerId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
