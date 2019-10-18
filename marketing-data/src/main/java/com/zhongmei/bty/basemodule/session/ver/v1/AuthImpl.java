package com.zhongmei.bty.basemodule.session.ver.v1;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.basemodule.session.core.auth.AbsAuth;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.enums.PlatformType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.db.entity.AuthPermission;
import com.zhongmei.yunfu.db.entity.AuthRolePermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AuthImpl extends AbsAuth {

    @Override
    protected void onLoad(User user, Map<String, Resource> map) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {


                        Dao<AuthRolePermission, Long> rolePermissionDao = helper.getDao(AuthRolePermission.class);
            QueryBuilder<AuthRolePermission, Long> rolePermissionQueryBuilder = rolePermissionDao.queryBuilder();
            rolePermissionQueryBuilder.selectColumns(AuthRolePermission._permissionId);
            rolePermissionQueryBuilder.where().eq(AuthRolePermission._statusFlag, StatusFlag.VALID)
                    .and().eq(AuthRolePermission._roleId, user.getRoleId());

            List<AuthRolePermission> rolePermissions = rolePermissionQueryBuilder.query();

            Dao<AuthPermission, Long> permissionDao = helper.getDao(AuthPermission.class);
            QueryBuilder<AuthPermission, Long> permissionQueryBuilder = permissionDao.queryBuilder();
            permissionQueryBuilder.selectColumns(AuthPermission._id, AuthPermission._name, AuthPermission._code);
            permissionQueryBuilder.where().eq(AuthPermission._platform, PlatformType.ON_POS)
                    .and().eq(AuthPermission._statusFlag, StatusFlag.VALID)
                    .and().in(AuthPermission._id, rolePermissionQueryBuilder);

            List<AuthPermission> permissions = permissionQueryBuilder.query();
            for (AuthPermission permission : permissions) {
                String code = permission.getCode();
                Map<String, String> detail = new HashMap<>();
                detail.put(permission.getCode(), permission.getName());
                Resource resource = new Resource(code, detail);
                map.put(code, resource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }
}
