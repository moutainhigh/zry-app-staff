package com.zhongmei.bty.basemodule.session.ver.v1.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;

/**
 * AuthDataPerimission is a ORMLite bean type. Corresponds to the database table "auth_data_permission"
 */
@DatabaseTable(tableName = "auth_data_permission")
public class AuthDataPermission extends BasicEntityBase {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "auth_data_permission"
     */
    public interface $ extends BasicEntityBase.$ {

        /**
         * content
         */
        public static final String content = "content";

        /**
         * name
         */
        public static final String name = "name";

        /**
         * permission_id
         */
        public static final String permissionId = "permission_id";

        /**
         * role_id
         */
        public static final String roleId = "role_id";

        /**
         * shop_identy
         */
        public static final String shopIdenty = "shop_identy";

    }

    @DatabaseField(columnName = "content")
    private String content;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    @DatabaseField(columnName = "permission_id", canBeNull = false)
    private Long permissionId;

    @DatabaseField(columnName = "role_id", canBeNull = false)
    private Long roleId;

    @DatabaseField(columnName = "shop_identy")
    private Long shopIdenty;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(name, permissionId, roleId);
    }
}

