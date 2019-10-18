package com.zhongmei.bty.commonmodule.data.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.Option;
import com.zhongmei.yunfu.db.UuidEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;


@DatabaseTable(tableName = DictionaryDetail.$.TABLE_NAME)
public class DictionaryDetail extends UuidEntityBase implements Option {

    @Override
    public void onOption() {
        setUuid(pkValue());
    }




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
