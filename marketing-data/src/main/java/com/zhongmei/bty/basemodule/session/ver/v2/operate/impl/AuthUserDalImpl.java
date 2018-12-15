package com.zhongmei.bty.basemodule.session.ver.v2.operate.impl;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.bty.basemodule.session.ver.v2.bean.RoleType;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthAccountEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthAccountRoleEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthDataPermissionDetailEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthDataPermissionEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthPermissionResourceEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthResourceEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthRoleEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthRolePermissionEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthUserEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.operate.AuthUserDal;
import com.zhongmei.yunfu.context.util.peony.ArgsUtils;
import com.zhongmei.yunfu.context.util.peony.land.Extractable;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.entity.EmployeeIdentity;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public class AuthUserDalImpl extends AbstractOpeartesImpl implements AuthUserDal {

    public AuthUserDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public List<AuthUserEntity> getUsersByRoleType(RoleType roleType) throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<AuthRoleEntity, Long> areDao = helper.getDao(AuthRoleEntity.class);
            QueryBuilder<AuthRoleEntity, Long> areQB = areDao.queryBuilder();
            general(areQB.where()).and().eq(AuthRoleEntity.$.ROLE_TYPE, roleType);
            areQB.selectColumns(AuthRoleEntity.$.ID);

            Dao<AuthAccountRoleEntity, Long> aareDao = helper.getDao(AuthAccountRoleEntity.class);
            QueryBuilder<AuthAccountRoleEntity, Long> aareQB = aareDao.queryBuilder();
            general(aareQB.where()).and().in(AuthAccountRoleEntity.$.ROLE_ID, areQB);
            aareQB.selectColumns(AuthAccountRoleEntity.$.ACCOUNT_ID);

            Dao<AuthUserEntity, Long> aueDao = helper.getDao(AuthUserEntity.class);
            QueryBuilder<AuthUserEntity, Long> aueQB = aueDao.queryBuilder();
            general(aueQB.where()).and().in(AuthUserEntity.$.ACCOUNT_ID, aareQB);
            List<AuthUserEntity> authUserEntities = aueQB.query();

            Dao<AuthAccountEntity, Long> aaeDao = helper.getDao(AuthAccountEntity.class);
            QueryBuilder<AuthAccountEntity, Long> aaeQB = aaeDao.queryBuilder();
            for (AuthUserEntity authUserEntity : authUserEntities) {
                aaeQB.reset();
                general(aaeQB.where()).and().eq(AuthAccountEntity.$.ID, authUserEntity.getId());
                AuthAccountEntity authAccountEntity = aaeQB.queryForFirst();
                authUserEntity.setPassword(authAccountEntity.getPasswordNum());
            }

            return authUserEntities;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<AuthUserEntity> getUsersByAuthCode(String... authCodes) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<AuthResourceEntity, Long> areDao = helper.getDao(AuthResourceEntity.class);
            QueryBuilder<AuthResourceEntity, Long> areQB = areDao.queryBuilder();
            general(areQB.where()).and().in(AuthResourceEntity.$.RESOURCE_CODE, Arrays.asList(authCodes));
            List<AuthResourceEntity> list = areQB.query();
            List<Long> authResourceEntityIds = ArgsUtils.sucker(list, new Extractable<Long, AuthResourceEntity>() {
                @Override
                public Long extract(AuthResourceEntity value) {
                    return value.getId();
                }
            });

            Dao<AuthPermissionResourceEntity, Long> apreDao = helper.getDao(AuthPermissionResourceEntity.class);
            QueryBuilder<AuthPermissionResourceEntity, Long> apreQB = apreDao.queryBuilder();
            general(apreQB.where()).and().in(AuthPermissionResourceEntity.$.RESOURCE_ID, authResourceEntityIds);
            apreQB.selectColumns(AuthPermissionResourceEntity.$.PERMISSION_ID);
            List<AuthPermissionResourceEntity> authPermissionResourceEntities = apreQB.query();
            List<Long> authPermissionIds = ArgsUtils.sucker(authPermissionResourceEntities,
                    new Extractable<Long, AuthPermissionResourceEntity>() {
                        @Override
                        public Long extract(AuthPermissionResourceEntity value) {
                            return value.getPermissionId();
                        }
                    });

            Dao<AuthRolePermissionEntity, Long> arpeDao = helper.getDao(AuthRolePermissionEntity.class);
            List<AuthRolePermissionEntity> authRolePermissionEntities = arpeDao.queryForAll();
            List<Long> roleIds = new ArrayList<>();
            for (Long authPermissionId : authPermissionIds) {
                for (AuthRolePermissionEntity authRolePermissionEntity : authRolePermissionEntities) {
                    Long roleId = fetchRoleId(authRolePermissionEntity, authPermissionId);
                    if (roleId != null) {
                        roleIds.add(roleId);
                    }
                }
            }

//            Dao<AuthRoleEntity, Long> aroeDao = helper.getDao(AuthRoleEntity.class);
//            QueryBuilder<AuthRoleEntity, Long> aroeQB = aroeDao.queryBuilder();
//            general(aroeQB.where()).and().in(AuthRoleEntity.$.ID, roleIds)
//                    .and().ne(AuthRoleEntity.$.ROLE_CODE, 4);
//            List<AuthRoleEntity> authRoleEntities = aroeQB.query();
//            List<Long> newRoleIds = ArgsUtils.sucker(authRoleEntities, new Extractable<Long, AuthRoleEntity>() {
//                @Override
//                public Long extract(AuthRoleEntity value) {
//                    return value.getId();
//                }
//            });

            Dao<AuthAccountRoleEntity, Long> aareDao = helper.getDao(AuthAccountRoleEntity.class);
            QueryBuilder<AuthAccountRoleEntity, Long> aareQB = aareDao.queryBuilder();
            general(aareQB.where()).and().in(AuthAccountRoleEntity.$.ROLE_ID, roleIds);
            aareQB.selectColumns(AuthAccountRoleEntity.$.ACCOUNT_ID);

            Dao<AuthUserEntity, Long> aueDao = helper.getDao(AuthUserEntity.class);
            QueryBuilder<AuthUserEntity, Long> aueQB = aueDao.queryBuilder();
            general(aueQB.where()).and().in(AuthUserEntity.$.ACCOUNT_ID, aareQB);

            return aueQB.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<AuthUserEntity> getUsersByAuthCodeAndFilter(String authCode, Auth.Filter filter) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<AuthResourceEntity, Long> areDao = helper.getDao(AuthResourceEntity.class);
            QueryBuilder<AuthResourceEntity, Long> areQB = areDao.queryBuilder();
            general(areQB.where()).and().eq(AuthResourceEntity.$.RESOURCE_CODE, authCode);
            AuthResourceEntity authResourceEntity = areQB.queryForFirst();

            Dao<AuthPermissionResourceEntity, Long> apreDao = helper.getDao(AuthPermissionResourceEntity.class);
            QueryBuilder<AuthPermissionResourceEntity, Long> apreQB = apreDao.queryBuilder();
            general(apreQB.where()).and().eq(AuthPermissionResourceEntity.$.RESOURCE_ID, authResourceEntity.getId());
            List<AuthPermissionResourceEntity> authPermissionResourceEntities = apreQB.query();
            List<Long> authPermissionIds = ArgsUtils.sucker(authPermissionResourceEntities,
                    new Extractable<Long, AuthPermissionResourceEntity>() {
                        @Override
                        public Long extract(AuthPermissionResourceEntity value) {
                            return value.getPermissionId();
                        }
                    });
            Map<Long, AuthPermissionResourceEntity> authPermissionResourceEntityMap
                    = ArgsUtils.mapOf(authPermissionResourceEntities, new Extractable<Long, AuthPermissionResourceEntity>() {
                @Override
                public Long extract(AuthPermissionResourceEntity value) {
                    return value.getResourceId();
                }
            });

            Dao<AuthRolePermissionEntity, Long> arpeDao = helper.getDao(AuthRolePermissionEntity.class);
            List<AuthRolePermissionEntity> authRolePermissionEntities = arpeDao.queryForAll();
            List<Long> roleIds = new ArrayList<>();
            for (Long authPermissionId : authPermissionIds) {
                for (AuthRolePermissionEntity authRolePermissionEntity : authRolePermissionEntities) {
                    Long roleId = fetchRoleId(authRolePermissionEntity, authPermissionId);
                    if (roleId != null) {
                        roleIds.add(roleId);
                    }
                }
            }

            Dao<AuthAccountRoleEntity, Long> aareDao = helper.getDao(AuthAccountRoleEntity.class);
            QueryBuilder<AuthAccountRoleEntity, Long> aareQB = aareDao.queryBuilder();
            general(aareQB.where()).and().in(AuthAccountRoleEntity.$.ROLE_ID, roleIds);
            List<AuthAccountRoleEntity> authAccountRoleEntities = aareQB.query();
            List<Long> authAccountRoleEntityIds = ArgsUtils.sucker(authAccountRoleEntities,
                    new Extractable<Long, AuthAccountRoleEntity>() {
                        @Override
                        public Long extract(AuthAccountRoleEntity value) {
                            return value.getAccountId();
                        }
                    });
            Map<Long, AuthAccountRoleEntity> authAccountRoleEntityMap = ArgsUtils.mapOf(authAccountRoleEntities,
                    new Extractable<Long, AuthAccountRoleEntity>() {
                        @Override
                        public Long extract(AuthAccountRoleEntity value) {
                            return value.getAccountId();
                        }
                    });

            Dao<AuthUserEntity, Long> aueDao = helper.getDao(AuthUserEntity.class);
            QueryBuilder<AuthUserEntity, Long> aueQB = aueDao.queryBuilder();
            general(aueQB.where()).and().in(AuthUserEntity.$.ACCOUNT_ID, authAccountRoleEntityIds);

            List<AuthUserEntity> temp = new ArrayList<>();
            List<AuthUserEntity> authUserEntities = aueQB.query();
            if (filter != null && !ArgsUtils.isEmpty(authUserEntities)) {
                Dao<AuthDataPermissionEntity, Long> adpeDao = helper.getDao(AuthDataPermissionEntity.class);
                QueryBuilder<AuthDataPermissionEntity, Long> adpeQB = adpeDao.queryBuilder();
                Dao<AuthDataPermissionDetailEntity, Long> adpdeDao = helper.getDao(AuthDataPermissionDetailEntity.class);
                QueryBuilder<AuthDataPermissionDetailEntity, Long> adpdeQB = adpdeDao.queryBuilder();

                for (AuthUserEntity authUserEntity : authUserEntities) {
                    Long accountId = authUserEntity.getAccountId();
                    Long roleId = authAccountRoleEntityMap.get(accountId).getRoleId();
                    Long resourceId = authResourceEntity.getId();
                    Long permissionId = authPermissionResourceEntityMap.get(resourceId).getPermissionId();

                    adpeQB.reset();
                    general(adpeQB.where()).and().eq(AuthDataPermissionEntity.$.ACCOUNT_ID, accountId)
                            .and().eq(AuthDataPermissionEntity.$.ROLE_ID, roleId)
//                            .and().eq(AuthDataPermissionEntity.$.PERMISSION_ID, permissionId)
                            .and().eq(AuthDataPermissionEntity.$.RESOURCE_ID, resourceId);
                    AuthDataPermissionEntity authDataPermissionEntity = adpeQB.queryForFirst();

                    if (authDataPermissionEntity != null) {
                        adpdeQB.reset();
                        general(adpdeQB.where()).and().eq(AuthDataPermissionDetailEntity.$.DATA_PERMISSION_ID, authDataPermissionEntity.getId());
                        AuthDataPermissionDetailEntity authDataPermissionDetailEntity = adpdeQB.queryForFirst();
                        if (authDataPermissionDetailEntity != null) {
                            String value = authDataPermissionDetailEntity.getValue();
                            boolean access = filter.access(authCode, value);
                            if (access) {
                                temp.add(authUserEntity);
                            }
                        }
                    }
                }
            } else {
                temp.addAll(authUserEntities);
            }

            return temp;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<AuthUserEntity> getUserByEmployeeIdentity(int employeeIdentity) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<EmployeeIdentity, Long> identityDao = helper.getDao(EmployeeIdentity.class);
            QueryBuilder<EmployeeIdentity, Long> identityQB = identityDao.queryBuilder();
            identityQB.selectColumns(EmployeeIdentity.$.authUserId);
            identityQB.where().eq(EmployeeIdentity.$.identityCode, employeeIdentity)
                    .and().eq(EmployeeIdentity.$.statusFlag, StatusFlag.VALID)
                    .and().eq(EmployeeIdentity.$.enableStatus, Bool.YES);

            Dao<AuthUserEntity, Long> aueDao = helper.getDao(AuthUserEntity.class);
            QueryBuilder<AuthUserEntity, Long> aueQB = aueDao.queryBuilder();
            general(aueQB.where()).and().in(AuthUserEntity.$.ID, identityQB);
            return aueQB.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public AuthAccountEntity getAuthAccountEntity(Long accountId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<AuthAccountEntity, Long> authAccountEntityDao = helper.getDao(AuthAccountEntity.class);
            QueryBuilder<AuthAccountEntity, Long> authAccountEntityQB = authAccountEntityDao.queryBuilder();
            authAccountEntityQB.where().eq(AuthAccountEntity.$.ACTIVATION_FLAG, YesOrNo.YES)
                    .and().eq(AuthAccountEntity.$.ENABLE, Bool.YES)
                    .and().eq(AuthAccountEntity.$.STATUS, StatusFlag.VALID)
                    .and().eq(AuthAccountEntity.$.ID, accountId);
            return authAccountEntityQB.queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<EmployeeIdentity> getEmployeeIdentities(int employeeIdentity) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<EmployeeIdentity, Long> identityDao = helper.getDao(EmployeeIdentity.class);
            QueryBuilder<EmployeeIdentity, Long> identityQB = identityDao.queryBuilder();
            identityQB.where().eq(EmployeeIdentity.$.identityCode, employeeIdentity)
                    .and().eq(EmployeeIdentity.$.statusFlag, StatusFlag.VALID)
                    .and().eq(EmployeeIdentity.$.enableStatus, Bool.YES);

            return identityQB.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }


    @Override
    public AuthAccountEntity getAccountIdByUserId(Long id) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<AuthAccountEntity, Long> authAccountEntityDao = helper.getDao(AuthAccountEntity.class);
            QueryBuilder<AuthAccountEntity, Long> authAccountEntityQB = authAccountEntityDao.queryBuilder();
            authAccountEntityQB.where().eq(AuthAccountEntity.$.ID, id);
            return authAccountEntityQB.queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public void savePasswordToAccountEntity(Long id, String password) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<AuthAccountEntity, Long> authAccountEntityDao = helper.getDao(AuthAccountEntity.class);
            authAccountEntityDao.updateRaw("update ps_auth_act_account set password_num=? where id=?", new String[]{password, id + ""});

//            QueryBuilder<AuthAccountEntity, Long> authAccountEntityQB = authAccountEntityDao.queryBuilder();
//            authAccountEntityQB.where().eq(AuthAccountEntity.$.ID, id);
//            AuthAccountEntity authAccountEntity = authAccountEntityQB.queryForFirst();
//            authAccountEntity.setPasswordNum(password);
//            authAccountEntityDao.update(authAccountEntity);

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<AuthUserEntity> getAuthAccountEntityByFaceCode(String faceCode) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<AuthAccountEntity, Long> authAccountEntityDao = helper.getDao(AuthAccountEntity.class);
            QueryBuilder<AuthAccountEntity, Long> uthAccountEntityQB = authAccountEntityDao.queryBuilder();
            uthAccountEntityQB.selectColumns(AuthAccountEntity.$.ID);
            uthAccountEntityQB.where().eq(AuthAccountEntity.$.FACE_CODE, faceCode);

            Dao<AuthUserEntity, Long> authUserEntityDao = helper.getDao(AuthUserEntity.class);
            QueryBuilder<AuthUserEntity, Long> authUserEntityQB = authUserEntityDao.queryBuilder();
            authUserEntityQB.where().in(AuthUserEntity.$.ACCOUNT_ID, uthAccountEntityQB);
            return authUserEntityQB.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    private <T extends AuthEntity, ID> Where<T, ID> general(Where<T, ID> where) throws SQLException {
        where.eq(AuthAccountRoleEntity.$.STATUS, StatusFlag.VALID)
                .and().eq(AuthAccountRoleEntity.$.ENABLE, Bool.YES);
        return where;
    }

    private Long fetchRoleId(AuthRolePermissionEntity authRolePermissionEntity, Long permissionId) {
        try {
            if (authRolePermissionEntity != null) {
                String perStr = authRolePermissionEntity.getPermissionIds();
                if (!TextUtils.isEmpty(perStr)) {
                    String[] pers = perStr.split(",");
                    for (String per : pers) {
                        Long perId = Long.valueOf(per);
                        if (perId.equals(permissionId)) {
                            return authRolePermissionEntity.getRoleId();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AuthAccountEntity> getAuthAccountEntityList() throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<AuthAccountEntity, Long> identityDao = helper.getDao(AuthAccountEntity.class);
            QueryBuilder<AuthAccountEntity, Long> identityQB = identityDao.queryBuilder();
            identityQB.where()
                    .eq(AuthAccountEntity.$.ACTIVATION_FLAG, YesOrNo.YES)
                    .and()
                    .eq(AuthAccountEntity.$.STATUS, StatusFlag.VALID)
                    .and()
                    .eq(AuthAccountEntity.$.ENABLE, Bool.YES);
            return identityQB.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<AuthUserEntity> getAuthUserEntityList(List<Long> accountIdList) throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<AuthUserEntity, Long> aueDao = helper.getDao(AuthUserEntity.class);
            QueryBuilder<AuthUserEntity, Long> aueQB = aueDao.queryBuilder();
            aueQB.where().in(AuthUserEntity.$.ACCOUNT_ID, accountIdList);
            return aueQB.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    public List<AuthAccountEntity> searchAuthAcountListByText(String inputText) throws SQLException {
        List<AuthAccountEntity> list = null;
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<AuthAccountEntity, Long> aueDao = helper.getDao(AuthAccountEntity.class);
            QueryBuilder<AuthAccountEntity, Long> aueQB = aueDao.queryBuilder();
            aueQB.where().like(AuthAccountEntity.$.NAME, inputText).or().like(AuthAccountEntity.$.MOBILE, inputText);
            list = aueQB.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return list;
    }
}
