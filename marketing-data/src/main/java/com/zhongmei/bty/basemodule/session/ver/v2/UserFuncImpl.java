package com.zhongmei.bty.basemodule.session.ver.v2;

import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.bty.basemodule.session.core.auth.Code;
import com.zhongmei.yunfu.context.session.core.user.Role;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.basemodule.session.ver.BaseUserFunc;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthAccountEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthUserEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.operate.AuthUserDal;
import com.zhongmei.yunfu.context.util.peony.ArgsUtils;
import com.zhongmei.yunfu.context.util.peony.land.Extractable;
import com.zhongmei.bty.baseservice.util.peony.land.Task;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.EmployeeIdentity;
import com.zhongmei.yunfu.db.entity.AuthRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class UserFuncImpl extends BaseUserFunc implements UserFunc {


    @Override
    public List<Role> getRoles() {
        return new InnerFetchRoleTask().execute();
    }

    @Override
    public User checkPassword(String account, String password) {
        List<User> users = getUsers();
        return users != null ? users.get(0) : null;
    }

    @Override
    public List<User> getUsers() {
        return new InnerFetchUserTask(Code.ZC.AUTH_CODE_POS_LOGIN,
                Code.KC.AUTH_CODE_POS_LOGIN).execute();
    }

    @Override
    public List<User> getUsers(String authCode) {
        return new InnerFetchUserTaskWithFilter(authCode, null).execute();
    }

    @Override
    public void getUsers(String authCode, Auth.Filter filter, Task.Callback<List<User>> callback) {
        new InnerFetchUserTaskWithFilter(authCode, filter).execute(callback);
    }

    @Override
    public List<User> getAllSalesman() {
        AuthUserDal authUserDal = OperatesFactory.create(AuthUserDal.class);
        try {
            List<EmployeeIdentity> employeeIdentities = authUserDal.getEmployeeIdentities(9);
            return ArgsUtils.sucker(employeeIdentities, new Extractable<User, EmployeeIdentity>() {
                @Override
                public User extract(EmployeeIdentity value) {
                    User user = new User();
                    user.setId(value.authUserId);
                    user.setName(value.userName);
                    return user;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static class InnerFetchRoleTask extends Task<List<Role>> {

        @Override
        protected List<Role> onExecute() {
            try {
                com.zhongmei.bty.basemodule.session.ver.v1.operate.AuthUserDal authUserDal = OperatesFactory.create(com.zhongmei.bty.basemodule.session.ver.v1.operate.AuthUserDal.class);
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

    private static Role toRole(AuthRole role) {
        Role tmpRole = new Role();
        tmpRole.setId(role.getId());
        tmpRole.setRoleName(role.getName());
        return tmpRole;
    }

    @Override
    public List<User> getUsersByFaceCode(String faceCode) {
        return new InnerFetchUserWithFaceCodeTask(faceCode).execute();
    }

    @Override
    public List<User> getUserByIdentity(int identity) {
        AuthUserDal authUserDal = OperatesFactory.create(AuthUserDal.class);
        try {
            List<EmployeeIdentity> employeeIdentities = authUserDal.getEmployeeIdentities(identity);
            return ArgsUtils.sucker(employeeIdentities, new Extractable<User, EmployeeIdentity>() {
                @Override
                public User extract(EmployeeIdentity value) {
                    User user = new User();
                    user.setId(value.authUserId);
                    user.setName(value.userName);
                    return user;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> listSender() {
        return new ArrayList<>();
    }

    //-----------------------------------------------------------------------------------UTILS----------
    private static class InnerFetchUserTask extends Task<List<User>> {

        final String[] authCodes;

        InnerFetchUserTask(String... authCodes) {
            this.authCodes = authCodes;
        }

        @Override
        protected List<User> onExecute() {
            AuthUserDal authUserDal = OperatesFactory.create(AuthUserDal.class);
            try {
                List<AuthUserEntity> authUserEntities = authUserDal.getUsersByAuthCode(authCodes);
                return ArgsUtils.sucker(authUserEntities, new UserExtractable(authUserDal));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private static class InnerFetchUserTaskWithFilter extends Task<List<User>> {

        final Auth.Filter filter;
        final String authCode;

        InnerFetchUserTaskWithFilter(String authCode, Auth.Filter filter) {
            this.authCode = authCode;
            this.filter = filter;
        }

        @Override
        protected List<User> onExecute() {
            AuthUserDal authUserDal = OperatesFactory.create(AuthUserDal.class);
            try {
                List<AuthUserEntity> authUserEntities = authUserDal.getUsersByAuthCodeAndFilter(authCode, filter);
                return ArgsUtils.sucker(authUserEntities, new UserExtractable(authUserDal));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class InnerFetchUserWithFaceCodeTask extends Task<List<User>> {

        final String faceCode;

        private InnerFetchUserWithFaceCodeTask(String faceCode) {
            this.faceCode = faceCode;
        }

        @Override
        protected List<User> onExecute() {
            AuthUserDal authUserDal = OperatesFactory.create(AuthUserDal.class);
            try {
                List<AuthUserEntity> authUserEntities = authUserDal.getAuthAccountEntityByFaceCode(faceCode);
                return ArgsUtils.sucker(authUserEntities, new UserExtractable(authUserDal));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class UserExtractable implements Extractable<User, AuthUserEntity> {

        final AuthUserDal authUserDal;

        UserExtractable(AuthUserDal authUserDal) {
            this.authUserDal = authUserDal;
        }

        @Override
        public User extract(AuthUserEntity value) {
            try {
                AuthAccountEntity authAccountEntity = authUserDal.getAuthAccountEntity(value.getAccountId());
                if (authAccountEntity != null) {
                    User user = new User();
                    user.setId(value.getId());
                    user.setAccountId(value.getAccountId());
                    user.setAccount(value.getAccount());
                    user.setName(value.getName());
                    user.setSalt(value.getSalt());
                    user.setMobile(value.getMobile());
                    user.setPassword(authAccountEntity.getPasswordNum());
                    user.setSalt(authAccountEntity.getPasswordSalt());
                    user.setFaceCode(authAccountEntity.getFaceCode());
                    return user;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
