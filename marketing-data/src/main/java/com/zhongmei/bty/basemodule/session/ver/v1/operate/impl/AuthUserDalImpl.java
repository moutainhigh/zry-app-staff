package com.zhongmei.bty.basemodule.session.ver.v1.operate.impl;

import android.annotation.SuppressLint;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.bty.basemodule.session.ver.v1.db.AuthUserEntity;
import com.zhongmei.bty.basemodule.session.ver.v1.operate.AuthUserDal;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.entity.EmployeeIdentity;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.commonmodule.database.enums.PlatformType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.entity.AuthPermission;
import com.zhongmei.yunfu.db.entity.AuthRole;
import com.zhongmei.yunfu.db.entity.AuthRolePermission;
import com.zhongmei.yunfu.db.entity.AuthUser;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@SuppressLint("UseSparseArrays")
public class AuthUserDalImpl extends AbstractOpeartesImpl implements AuthUserDal {

    public AuthUserDalImpl(ImplContext context) {
        super(context);
    }


    @Override
    public List<AuthRole> listRole() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
                        Dao<AuthRole, Long> roleDao = helper.getDao(AuthRole.class);
            QueryBuilder<AuthRole, Long> roleQueryBuilder = roleDao.queryBuilder();
            roleQueryBuilder.selectColumns(AuthRole._id, AuthRole._name);
            roleQueryBuilder.where().eq(AuthPermission._statusFlag, StatusFlag.VALID);


            List<AuthRole> roleList = roleQueryBuilder.query();
            Collections.sort(roleList, new Comparator<AuthRole>() {
                @Override
                public int compare(AuthRole lhs, AuthRole rhs) {
                    return lhs.getId().compareTo(rhs.getId());
                }
            });
            return roleList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public AuthUser getUserByAccount(String account) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<AuthUser, Long> dao = helper.getDao(AuthUser.class);
            QueryBuilder<AuthUser, Long> qb = dao.queryBuilder();
            qb.where().eq(AuthUser._statusFlag, StatusFlag.VALID)
                    .and().eq(AuthUser._enabledFlag, 1)
                    .and().eq(AuthUser._account, account);
            qb.orderBy(AuthUser._account, true);
            return qb.queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<AuthUser> listLoginUser(String permissionCode) throws Exception {
        return listLoginUser(permissionCode, null);
    }

    @Override
    public List<AuthUser> listAllSalesman() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<EmployeeIdentity, Long> identityDao = helper.getDao(EmployeeIdentity.class);
            QueryBuilder<EmployeeIdentity, Long> identityQB = identityDao.queryBuilder();
            identityQB.selectColumns(EmployeeIdentity.$.authUserId);
            identityQB.where().eq(EmployeeIdentity.$.identityCode, 9)
                    .and().eq(EmployeeIdentity.$.statusFlag, StatusFlag.VALID)
                    .and().eq(EmployeeIdentity.$.enableStatus, Bool.YES);
            return queryPosUser(helper, identityQB);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<AuthUser> listLoginUser(String permissionCode, AuthDataFilter filter) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
                        Dao<AuthPermission, Long> permissionDao = helper.getDao(AuthPermission.class);
            QueryBuilder<AuthPermission, Long> permissionQueryBuilder = permissionDao.queryBuilder();
            permissionQueryBuilder.selectColumns(AuthPermission._id);
            permissionQueryBuilder.where().eq(AuthPermission._platform, PlatformType.ON_POS)
                    .and().eq(AuthPermission._statusFlag, StatusFlag.VALID);

                        Dao<AuthRolePermission, Long> rolePermissionDao = helper.getDao(AuthRolePermission.class);
            QueryBuilder<AuthRolePermission, Long> rolePermissionQueryBuilder = rolePermissionDao.queryBuilder();
            rolePermissionQueryBuilder.selectColumns(AuthRolePermission._roleId);
            rolePermissionQueryBuilder.where().eq(AuthRolePermission._statusFlag, StatusFlag.VALID)
                    .and().in(AuthRolePermission._id, permissionQueryBuilder);
            List<AuthUser> userList = queryPosUser(helper, rolePermissionQueryBuilder);
            Collections.sort(userList, new Comparator<AuthUser>() {
                @Override
                public int compare(AuthUser lhs, AuthUser rhs) {
                    return lhs.getAccount().compareTo(rhs.getAccount());
                }
            });
            return userList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<AuthUser> listLoginUser() throws Exception {
        return getPosLoginUser(PlatformType.ON_POS.value());
    }






    private List<AuthUser> getPosLoginUser(int platFrom) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {

            return queryPosUser(helper, null);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

    }


    private List<AuthUser> queryPosUser(DatabaseHelper helper, QueryBuilder<?, Long> includeUserQb) throws Exception {
        Dao<AuthUser, Long> dao = helper.getDao(AuthUser.class);
        QueryBuilder<AuthUser, Long> qb = dao.queryBuilder();
        Where<AuthUser, Long> where = qb.where();
        where.eq(AuthUser._statusFlag, StatusFlag.VALID)
                .and().eq(AuthUser._enabledFlag, 1);
        if (includeUserQb != null) {
            where.and().in(AuthUser._roleId, includeUserQb);
        }
        qb.orderBy(AuthUser._account, true);
        return qb.query();

    }



    private Where<AuthUserEntity, Long> appendWhere(Where<AuthUserEntity, Long> where,
                                                    Where<AuthUserEntity, Long> includeUserWhere) throws Exception {
        Where<AuthUserEntity, Long> subWhere1 = where.eq(AuthUserEntity.$.enabledFlag, Bool.YES);
        Where<AuthUserEntity, Long> subWhere2 = where.eq(AuthUserEntity.$.statusFlag, StatusFlag.VALID);
                Where<AuthUserEntity, Long> subWhere3 = where.ne(AuthUserEntity.$.account, "admin")
                .or().eq(AuthUserEntity.$.shopIdenty, BaseApplication.sInstance.getBrandIdenty());

        if (includeUserWhere != null) {
            where.and(includeUserWhere, subWhere1, subWhere2, subWhere3);
        } else {
            where.and(subWhere1, subWhere2, subWhere3);
        }
        return where;
    }



}
