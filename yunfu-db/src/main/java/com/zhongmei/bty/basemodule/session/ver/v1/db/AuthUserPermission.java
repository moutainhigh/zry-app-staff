package com.zhongmei.bty.basemodule.session.ver.v1.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.bty.commonmodule.database.enums.PerimissionType;
import com.zhongmei.bty.commonmodule.database.enums.PlatformType;


@DatabaseTable(tableName = "auth_user_permission")
public class AuthUserPermission extends BasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends BasicEntityBase.$ {


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String permissionCode = "permission_code";


        public static final String permissionId = "permission_id";


        public static final String platform = "platform";


        public static final String shopIdenty = "shop_identy";


        public static final String type = "type";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String userId = "user_id";


        public static final String roleId = "role_id";

    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "permission_code", canBeNull = false)
    private String permissionCode;

    @DatabaseField(columnName = "permission_id", canBeNull = false)
    private Long permissionId;

    @DatabaseField(columnName = "platform", canBeNull = false)
    private Integer platform;

    @DatabaseField(columnName = "type", canBeNull = false)
    private Integer type;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "user_id", canBeNull = false)
    private Long userId;

    @DatabaseField(columnName = "role_id", canBeNull = false)
    private Long roleId = 0L;

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

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public PlatformType getPlatform() {
        return ValueEnums.toEnum(PlatformType.class, platform);
    }

    public void setPlatform(PlatformType platform) {
        this.platform = ValueEnums.toValue(platform);
    }

    public PerimissionType getType() {
        return ValueEnums.toEnum(PerimissionType.class, type);
    }

    public void setType(PerimissionType type) {
        this.type = ValueEnums.toValue(type);
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(permissionCode, permissionId, platform, type, userId, roleId);
    }
}

