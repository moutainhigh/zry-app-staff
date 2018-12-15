package com.zhongmei.bty.basemodule.session.ver.v1.operate;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.db.entity.AuthRole;
import com.zhongmei.yunfu.db.entity.AuthUser;

import java.sql.SQLException;
import java.util.List;

/**
 * @version: 1.0
 * @date 2015年11月19日
 */
public interface AuthUserDal extends IOperates {

    String TAG = AuthUserDal.class.getSimpleName();

    AuthUser getUserByAccount(String account) throws Exception;


    List<AuthRole> listRole() throws Exception;

    /**
     * 获取有指定权限的用户 列表。 此方法会阻塞调用线程。
     *
     * @param permissionCode
     * @return
     * @throws Exception
     */
    List<AuthUser> listLoginUser(String permissionCode) throws Exception;

    /**
     * 获取有指定数据权限的用户 列表。 此方法会阻塞调用线程。
     *
     * @param permissionCode
     * @param filter
     * @return
     * @throws Exception
     */
    List<AuthUser> listLoginUser(String permissionCode, AuthDataFilter filter) throws Exception;

    /**
     * 返回所有有效的登录用户列表。 此方法会阻塞调用线程。
     *
     * @return
     * @throws Exception
     */
    List<AuthUser> listLoginUser() throws Exception;

    /**
     * 返回所有有效的外卖送餐员列表。 此方法会阻塞调用线程。
     *
     * @return
     * @throws Exception
     */
    //List<AuthUserEntity> listSender() throws Exception;

    /**
     * 返回所有有效操作员
     *
     * @return
     * @throws Exception
     */
    //List<AuthUserEntity> listAllShopUser() throws Exception;


    /**
     * 返回所有销售员
     *
     * @return
     * @throws Exception
     */
    List<AuthUser> listAllSalesman() throws Exception;

    /**
     * 查询品牌业务
     *
     * @param businessCode 业务码
     * @return
     * @throws SQLException
     */
    //AuthBrandBusiness queryBrandBusiness(String businessCode) throws SQLException;

    /**
     * 用于判断指定的数据权限是否符合要求
     *
     * @version: 1.0
     * @date 2015年11月19日
     */
    interface AuthDataFilter {

        /**
         * 返回true表示符合要求
         *
         * @param dataName
         * @param dataContent
         * @return
         */
        boolean accept(String dataName, String dataContent);

    }
}
