package com.zhongmei.bty.basemodule.session.ver.v1;

import com.zhongmei.bty.basemodule.session.ver.BaseUserFunc;
import com.zhongmei.bty.basemodule.session.ver.v1.operate.AuthUserDal;
import com.zhongmei.bty.baseservice.util.peony.land.Task;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.Role;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.peony.ArgsUtils;
import com.zhongmei.yunfu.context.util.peony.land.Extractable;
import com.zhongmei.yunfu.db.entity.AuthRole;
import com.zhongmei.yunfu.db.entity.AuthUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class UserFuncImpl extends BaseUserFunc {

    @Override
    public List<Role> getRoles() {
        return new InnerFetchRoleTask().execute();
    }

    @Override
    public User checkPassword(String account, String password) {
        try {
            AuthUserDal authUserDal = OperatesFactory.create(AuthUserDal.class);
            AuthUser authUser = authUserDal.getUserByAccount(account);
            if (authUser != null) {
                User user = toUser(authUser);
                if (super.checkPassword(user, password)) {
                    return user;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        return new InnerFetchUserTask().execute();
    }

    @Override
    public List<User> getUsers(String authCode) {
        return new InnerFetchUserTask4AuthCode(authCode, null).execute();
    }

    @Override
    public void getUsers(String authCode, Auth.Filter filter, Task.Callback<List<User>> callback) {
        new InnerFetchUserTask4AuthCode(authCode, filter).execute(callback);
    }

    @Override
    public List<User> getAllSalesman() {
        try {
            AuthUserDal authUserDal = OperatesFactory.create(AuthUserDal.class);
            List<AuthUser> authEntities = authUserDal.listAllSalesman();
            return ArgsUtils.sucker(authEntities, new Extractable<User, AuthUser>() {
                @Override
                public User extract(AuthUser value) {
                    User user = toUser(value);
                    return user;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static User toUser(AuthUser value) {
        User user = new User();
        user.setId(value.getId());
        user.setAccount(value.getAccount());
        user.setName(value.getName());
        user.setPassword(value.getPassword());
        user.setMobile(value.getMobile());
        user.setSalt(value.getSalt());
        user.setRoleId(value.getRoleId());
        return user;
    }

    private static Role toRole(AuthRole role) {
        Role tmpRole = new Role();
        tmpRole.setId(role.getId());
        tmpRole.setRoleName(role.getName());
        return tmpRole;
    }

    @Override
    public List<User> getUsersByFaceCode(String faceCode) {
        throw new UnsupportedOperationException("");
    }

    /**
     * V1版本为旧权限，V8.4之后就不需要兼容了
     *
     * @param identity
     * @return
     */
    @Override
    public List<User> getUserByIdentity(int identity) {
        try {
            AuthUserDal authUserDal = OperatesFactory.create(AuthUserDal.class);
            List<AuthUser> authUsers = authUserDal.listLoginUser();
            return ArgsUtils.sucker(authUsers, new Extractable<User, AuthUser>() {
                @Override
                public User extract(AuthUser value) {
                    return toUser(value);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<User> listSender() {
        return new ArrayList<>();
    }

    //-----------------------------------------------------------------------------------UTILS----------
    private static class InnerFetchUserTask extends Task<List<User>> {

        @Override
        protected List<User> onExecute() {
            try {
                AuthUserDal authUserDal = OperatesFactory.create(AuthUserDal.class);
                List<AuthUser> authEntities = authUserDal.listLoginUser();
                return ArgsUtils.sucker(authEntities, new Extractable<User, AuthUser>() {
                    @Override
                    public User extract(AuthUser value) {
                        return toUser(value);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private static class InnerFetchRoleTask extends Task<List<Role>> {

        @Override
        protected List<Role> onExecute() {
            try {
                AuthUserDal authUserDal = OperatesFactory.create(AuthUserDal.class);
                List<AuthRole> roleList = authUserDal.listRole();
                return ArgsUtils.sucker(roleList, new Extractable<Role, AuthRole>() {
                    @Override
                    public Role extract(AuthRole value) {
                        return toRole(value);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class InnerFetchUserTask4AuthCode extends Task<List<User>> {

        final Auth.Filter filter;
        final String authCode;

        InnerFetchUserTask4AuthCode(String authCode, Auth.Filter filter) {
            this.filter = filter;
            this.authCode = authCode;
        }

        @Override
        protected List<User> onExecute() {
            AuthUserDal authUserDal = OperatesFactory.create(AuthUserDal.class);
            try {
                List<AuthUser> authUserEntities
                        = authUserDal.listLoginUser(authCode, filter == null ? null : new FilterProxy(filter));
                return ArgsUtils.sucker(authUserEntities, new UserExtractable());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private static class FilterProxy implements AuthUserDal.AuthDataFilter {

            final Auth.Filter filter;

            FilterProxy(Auth.Filter filter) {
                this.filter = filter;
            }

            @Override
            public boolean accept(String dataName, String dataContent) {
                return filter != null && filter.access(dataName, dataContent);
            }
        }
    }

    private static class UserExtractable implements Extractable<User, AuthUser> {
        @Override
        public User extract(AuthUser value) {
            return toUser(value);
        }
    }
}
