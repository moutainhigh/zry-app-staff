package com.zhongmei.bty.basemodule.session.ver.v2.operate;

import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.bty.basemodule.session.ver.v2.bean.RoleType;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthAccountEntity;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthUserEntity;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.commonmodule.database.entity.EmployeeIdentity;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public interface AuthUserDal extends IOperates {

    /**
     * 根据角色类型获取用户列表
     *
     * @return 返回POS指定角色的用户列表
     */
    List<AuthUserEntity> getUsersByRoleType(RoleType roleType) throws Exception;

    /**
     * 根据权限码获取用户列表
     *
     * @param authCodes 权限码
     * @return 返回POS指定权限码的用户列表
     */
    List<AuthUserEntity> getUsersByAuthCode(String... authCodes) throws Exception;

    List<AuthUserEntity> getUsersByAuthCodeAndFilter(String authCode, Auth.Filter filter) throws Exception;

    /**
     * 根据身份标识码获取用户列表
     *
     * @param employeeIdentity 用户身份标识码
     * @return 返回指定身份标识码的用户列表
     * @throws Exception
     */
    List<AuthUserEntity> getUserByEmployeeIdentity(int employeeIdentity) throws Exception;

    AuthAccountEntity getAuthAccountEntity(Long accountId) throws Exception;

    List<EmployeeIdentity> getEmployeeIdentities(int employeeIdentity) throws Exception;

    /**
     * 根据user表account_id匹配对应的account表数据
     */
    AuthAccountEntity getAccountIdByUserId(Long id) throws Exception;

    /**
     * 获取所有员工
     *
     * @return
     * @throws Exception
     */
    List<AuthAccountEntity> getAuthAccountEntityList() throws Exception;

    /**
     * @param accountIdList
     * @return
     * @throws Exception
     */
    List<AuthUserEntity> getAuthUserEntityList(List<Long> accountIdList) throws Exception;

    /**
     * 根据user表account_id匹配对应的account表数据
     */
    void savePasswordToAccountEntity(Long id, String password) throws Exception;

    List<AuthAccountEntity> searchAuthAcountListByText(String inputText) throws Exception;

    /**
     * 根据face code获取账号表数据
     *
     * @param faceCode face code
     * @return 返回账号表数据
     * @throws Exception
     */
    List<AuthUserEntity> getAuthAccountEntityByFaceCode(String faceCode) throws Exception;

}
