package com.zhongmei.bty.commonmodule.data.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.Option;
import com.zhongmei.yunfu.db.UuidEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = DictionaryDetail.$.TABLE_NAME)
public class DictionaryDetail extends UuidEntityBase implements Option {

    @Override
    public void onOption() {
        setUuid(pkValue());
    }

//    CREATE TABLE `dictionary_data` (
//            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
//            `dict_id` bigint(20) NOT NULL COMMENT '字典主表ID',
//            `dict_data_name` varchar(255) NOT NULL COMMENT '字典数据值名称',
//            `dict_data_value` varchar(255) NOT NULL COMMENT '字典数据值',
//            `dict_data_code` varchar(255) NOT NULL,
//              `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
//            `creator_id` bigint(20) NOT NULL COMMENT '创建者ID',
//            `updator_id` bigint(20) DEFAULT NULL COMMENT '修改者ID',
//            `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '服务器创建时间',
//            `server_update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '服务器更新时间',
//            `enabled_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '启用停用标识 : 1:启用;2:停用',
//            `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态标识 1 有效 2无效',
//    PRIMARY KEY (`id`),
//    KEY `index_dict_id` (`dict_id`)
//            ) ENGINE=InnoDB AUTO_INCREMENT=93365237599724545 DEFAULT CHARSET=utf8


//    CREATE TABLE `dictionary_detail` (
//            `dict_id` int(10) NOT NULL COMMENT '字典ID',
//            `dict_type_id` int(10) NOT NULL COMMENT '类型Id',
//            `dict_name` varchar(100) DEFAULT NULL COMMENT '字典名称',
//            `dict_type_code` varchar(20) NOT NULL COMMENT '类型编码',
//            `sort` int(10) DEFAULT NULL COMMENT '排序',
//            `creator_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
//            `updater_id` bigint(20) DEFAULT NULL COMMENT '更新者ID',
//            `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '服务器创建时间',
//            `server_update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '服务器更新时间',
//            `enabled_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '启用停用标识 : 1:启用; 0:停用',
//            `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态标识 1 有效 0无效',
//    PRIMARY KEY (`dict_id`,`dict_type_id`),
//    UNIQUE KEY `uni_dict_type_code_dict_id` (`dict_type_code`,`dict_id`) USING BTREE
//) ENGINE=InnoDB DEFAULT CHARSET=utf8

    public interface $ extends UuidEntityBase.$ {
        String TABLE_NAME = "dictionary_detail";
        String DICT_ID = "dict_id";
        String DICT_TYPE_ID = "dict_type_id";
        String DICT_NAME = "dict_name";
        String DICT_TYPE_CODE = "dict_type_code";
        String SORT = "sort";
        String CREATOR_ID = "creator_id";
        String UPDATER_ID = "updater_id";
        String SERVER_CREATE_TIME = "server_create_time";
        String SERVER_UPDATE_TIME = "server_update_time";
        String ENABLE_FLAG = "enable_flag";
        String STATUS_FLAG = "status_flag";
    }

    @DatabaseField(columnName = $.DICT_ID)
    private Long dictId;
    @DatabaseField(columnName = $.DICT_TYPE_ID)
    private Integer dictTypeId;
    @DatabaseField(columnName = $.DICT_NAME)
    private String dictName;
    @DatabaseField(columnName = $.DICT_TYPE_CODE)
    private String dictTypeCode;
    @DatabaseField(columnName = $.SORT)
    private Integer sort;
    @DatabaseField(columnName = $.CREATOR_ID)
    private Long creatorId;
    @DatabaseField(columnName = $.UPDATER_ID)
    private Long updaterId;
    @DatabaseField(columnName = $.SERVER_CREATE_TIME)
    private Long serverCreateTime;
    @DatabaseField(columnName = $.SERVER_UPDATE_TIME)
    private Long serverUpdateTime;
    @DatabaseField(columnName = $.ENABLE_FLAG)
    private Integer enableFlag;
    @DatabaseField(columnName = $.STATUS_FLAG)
    private Integer statusFlag;

    public Long getDictId() {
        return dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    public Integer getDictTypeId() {
        return dictTypeId;
    }

    public void setDictTypeId(Integer dictTypeId) {
        this.dictTypeId = dictTypeId;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictTypeCode() {
        return dictTypeCode;
    }

    public void setDictTypeCode(String dictTypeCode) {
        this.dictTypeCode = dictTypeCode;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
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

    public Integer getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Integer enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public String pkValue() {
        return String.valueOf(dictId) + String.valueOf(dictTypeId);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }
}
