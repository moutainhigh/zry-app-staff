package com.zhongmei.bty.basemodule.session.ver.v2;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.bty.basemodule.session.core.auth.AbsAuth;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthAccountRoleEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthDataPermissionDetailEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthDataPermissionEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthPermissionResourceEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthResourceEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthRolePermissionEntity;
import com.zhongmei.yunfu.context.util.peony.ArgsUtils;
import com.zhongmei.yunfu.context.util.peony.land.Extractable;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public class AuthImpl extends AbsAuth {

    @Override
    protected void onLoad(User user, Map<String, Auth.Resource> map) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<AuthAccountRoleEntity, Long> aareDao = helper.getDao(AuthAccountRoleEntity.class);
            QueryBuilder<AuthAccountRoleEntity, Long> aareQB = aareDao.queryBuilder();
            aareQB.selectColumns(AuthAccountRoleEntity.$.ROLE_ID);
            general(aareQB.where()).and().eq(AuthAccountRoleEntity.$.ACCOUNT_ID, user.getAccountId());
            List<AuthAccountRoleEntity> authAccountRoleEntities = aareQB.query();
            List<Long> authAccountRoleIds = ArgsUtils.sucker(authAccountRoleEntities, new Extractable<Long, AuthAccountRoleEntity>() {
                @Override
                public Long extract(AuthAccountRoleEntity value) {
                    return value.getRoleId();
                }
            });

            Dao<AuthRolePermissionEntity, Long> arpeDao = helper.getDao(AuthRolePermissionEntity.class);
            QueryBuilder<AuthRolePermissionEntity, Long> arpeqb = arpeDao.queryBuilder();
            arpeqb.where().in(AuthRolePermissionEntity.$.ROLE_ID, authAccountRoleIds);
            List<AuthRolePermissionEntity> authRolePermissionEntities = arpeqb.query();

            List<String> authRolePermissionEntityStrIds = ArgsUtils.sucker(authRolePermissionEntities,
                    new Extractable<String, AuthRolePermissionEntity>() {
                        @Override
                        public String extract(AuthRolePermissionEntity value) {
                            return value.getPermissionIds();
                        }
                    });
            if (!ArgsUtils.isEmpty(authRolePermissionEntityStrIds)) {
                List<Long> authRolePermissionEntityIds = new ArrayList<>();
                for (String authRolePermissionEntityStrId : authRolePermissionEntityStrIds) {
                    if (!TextUtils.isEmpty(authRolePermissionEntityStrId)) {
                        String[] ids = authRolePermissionEntityStrId.split(",");
                        for (String str : ids) {
                            authRolePermissionEntityIds.add(Long.valueOf(str));
                        }
                    }
                }

                Dao<AuthPermissionResourceEntity, Long> apreDao = helper.getDao(AuthPermissionResourceEntity.class);
                QueryBuilder<AuthPermissionResourceEntity, Long> apreQB = apreDao.queryBuilder();
                general(apreQB.where()).and().in(AuthPermissionResourceEntity.$.PERMISSION_ID, authRolePermissionEntityIds);
                List<AuthPermissionResourceEntity> authPermissionResourceEntities = apreQB.query();

                Map<Long, List<Long>> rpMap = new HashMap<>();

                for (AuthPermissionResourceEntity entity : authPermissionResourceEntities) {
                    Long resourceId = entity.getResourceId();
                    List<Long> list = rpMap.get(resourceId);
                    if (list == null) {
                        list = new ArrayList<>();
                        rpMap.put(resourceId, list);
                    }
                    list.add(entity.getPermissionId());
                }

                Map<Long, AuthPermissionResourceEntity> authResourcePermissionMap
                        = ArgsUtils.mapOf(authPermissionResourceEntities, new Extractable<Long, AuthPermissionResourceEntity>() {
                    @Override
                    public Long extract(AuthPermissionResourceEntity value) {
                        return value.getResourceId();
                    }
                });
                List<Long> authResourceIds = ArgsUtils.sucker(authPermissionResourceEntities, new Extractable<Long, AuthPermissionResourceEntity>() {
                    @Override
                    public Long extract(AuthPermissionResourceEntity value) {
                        return value.getResourceId();
                    }
                });
                Dao<AuthResourceEntity, Long> areDao = helper.getDao(AuthResourceEntity.class);
                QueryBuilder<AuthResourceEntity, Long> areQB = areDao.queryBuilder();
                general(areQB.where()).and().in(AuthResourceEntity.$.ID, authResourceIds);
                List<AuthResourceEntity> authResourceEntities = areQB.query();

                Dao<AuthDataPermissionEntity, Long> adpeDao = helper.getDao(AuthDataPermissionEntity.class);
                QueryBuilder<AuthDataPermissionEntity, Long> adpeQB = adpeDao.queryBuilder();
                Dao<AuthDataPermissionDetailEntity, Long> adpdeDao = helper.getDao(AuthDataPermissionDetailEntity.class);
                QueryBuilder<AuthDataPermissionDetailEntity, Long> adpdeQB = adpdeDao.queryBuilder();

                for (AuthResourceEntity authResourceEntity : authResourceEntities) {
                    adpeQB.reset();
                    general(adpeQB.where())
//                            .and().eq(AuthDataPermissionEntity.$.PERMISSION_ID, authResourcePermissionMap.get(authResourceEntity.getId()).getPermissionId())
                            .and().eq(AuthDataPermissionEntity.$.RESOURCE_ID, authResourceEntity.getId())
                            .and().eq(AuthDataPermissionEntity.$.ACCOUNT_ID, user.getAccountId())
                            .and().in(AuthDataPermissionEntity.$.PERMISSION_ID, rpMap.get(authResourceEntity.getId()));
//                            .and().eq(AuthDataPermissionEntity.$.ROLE_ID, authAccountRoleEntity.getRoleId())
                    adpeQB.selectColumns(AuthDataPermissionEntity.$.ID);

                    Map<String, String> detail = new HashMap<>();

                    general(adpdeQB.where()).and().in(AuthDataPermissionDetailEntity.$.DATA_PERMISSION_ID, adpeQB);
                    List<AuthDataPermissionDetailEntity> authDataPermissionDetailEntities = adpdeQB.query();
                    for (AuthDataPermissionDetailEntity authDataPermissionDetailEntity : authDataPermissionDetailEntities) {
//                        detail.put(authResourceEntity.getResourceCode(), authDataPermissionDetailEntity.getValue());
                        detail.put(authDataPermissionDetailEntity.getValue(), authResourceEntity.getResourceCode());
                    }


                    Resource resource = new Resource(authResourceEntity.getResourceCode(), detail);
                    map.put(authResourceEntity.getResourceCode(), resource);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    private <T extends AuthEntity, ID> Where<T, ID> general(Where<T, ID> where) throws SQLException {
        where.eq(AuthEntity.$.STATUS, StatusFlag.VALID)
                .and().eq(AuthEntity.$.ENABLE, Bool.YES);
        return where;
    }
}
