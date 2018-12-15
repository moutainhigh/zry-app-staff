package com.zhongmei.bty.basemodule.session.ver.v2.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = AuthRolePermissionEntity.$.TABLE_NAME)
public class AuthRolePermissionEntity extends EntityBase<Long> {

    public interface $ extends AuthEntity.$ {
        String TABLE_NAME = "ps_auth_role_permission";
        String ROLE_ID = "role_id";
        String PERMISSION_ID = "permission_id";
        String CREATE_TIME = "create_time";
        String UPDATE_TIME = "update_time";
    }

    @DatabaseField(columnName = $.ROLE_ID, canBeNull = false, index = true, id = true)
    private Long roleId;
    @DatabaseField(columnName = $.PERMISSION_ID)
    private String permissionIds;
    @DatabaseField(columnName = $.CREATE_TIME)
    private Long createTime;
    @DatabaseField(columnName = $.UPDATE_TIME, canBeNull = false)
    private Long updateTime;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(String permissionIds) {
        this.permissionIds = permissionIds;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Long pkValue() {
        return roleId;
    }

    @Override
    public Long verValue() {
        return updateTime;
    }
}
