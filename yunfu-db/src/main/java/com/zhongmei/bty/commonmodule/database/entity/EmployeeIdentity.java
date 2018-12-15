package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.CommonEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

/**
 * 用户身份信息，talent 提供
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "employee_identity")
public class EmployeeIdentity extends CommonEntityBase implements ICreator, IUpdator {


    public interface $ extends CommonEntityBase.$ {
        /**
         * authUserId
         */
        public static final String authUserId = "authuser_id";
        /**
         * identityCode
         */
        public static final String identityCode = "identity_code";
        /**
         * enableStatus
         */
        String enableStatus = "enable_status";
    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "authuser_id")
    public Long authUserId;

    @DatabaseField(columnName = "authuser_name")
    public String userName;

    @DatabaseField(columnName = "identity_name")
    public String identityName;

    @DatabaseField(columnName = "institution_id")
    public Long institutionId;

    @DatabaseField(columnName = "identity_code")
    public Integer identityCode;

    @DatabaseField(columnName = "institution_identity_id")
    public Long institutionIdentityId;

    @DatabaseField(columnName = "enable_status", defaultValue = "1")
    public Integer enableStatus;//是否停用，1.启用 2.停用

    @Override
    public Long getCreatorId() {
        return this.creatorId;
    }

    @Override
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String getCreatorName() {
        return this.creatorName;
    }

    @Override
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public Long getUpdatorId() {
        return this.updatorId;
    }

    @Override
    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    @Override
    public String getUpdatorName() {
        return this.updatorName;
    }

    @Override
    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public YesOrNo getEnableStatus() {
        return ValueEnums.toEnum(YesOrNo.class, enableStatus);
    }

    public void setEnableStatus(Bool enableStatus) {
        this.enableStatus = ValueEnums.toValue(enableStatus);
    }
}