package com.zhongmei.bty.basemodule.session.core.user;

import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.bty.baseservice.util.peony.land.Task;
import com.zhongmei.yunfu.context.session.core.user.Role;
import com.zhongmei.yunfu.context.session.core.user.User;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public interface UserFunc extends Session.SessionFunc {

    List<Role> getRoles();


    /**
     * 同步获取POS云收银用户,即pos端可以登录的用户(收银小妹儿)
     *
     * @return 返回用户列表
     */
    List<User> getUsers();

    /**异步获取POS云收银用户,即pos端可以登录的用户(收银小妹儿)
     * @param callback 异步回调
     */
    //void getUser(Task.Callback<List<User>> callback);

    /**
     * 同步获取指定权限的用户
     *
     * @param authCode 指定权限编码
     * @return 返回用户列表
     */
    List<User> getUsers(String authCode);

    /**异步获取指定权限的用户
     * @param authCode 指定权限编码
     * @param callback 异步回调
     */
    //void getUsers(String authCode, Task.Callback<List<User>> callback);

    /**同步获取指定权限和过滤器的用户
     * @param authCode 指定权限编码
     * @param filter 过滤器
     * @return 返回用户列表
     */
    //List<User> getUsers(String authCode, Auth.Filter filter);

    /**
     * 异步获取指定权限和过滤器的用户
     *
     * @param authCode 指定权限编码
     * @param filter   过滤器
     * @param callback 返回用户列表
     */
    void getUsers(String authCode, Auth.Filter filter, Task.Callback<List<User>> callback);

    /**
     * 所有销售员
     *
     * @return
     */
    List<User> getAllSalesman();

    /**
     * 根据face code同步获取用户列表
     *
     * @param faceCode face code
     * @return 返回用户列表
     */
    List<User> getUsersByFaceCode(String faceCode);

    /**根据face code异步获取用户列表
     * @param faceCode face code
     * @return 返回有用户列表
     */
    //void getUsersByFaceCode(String faceCode, Task.Callback<List<User>> callback);

    /**
     * 根据用户身份获取用户列表
     *
     * @param identity
     * @return
     */
    List<User> getUserByIdentity(int identity);

    /**
     * 根据指定用户，即密码字符串检查密码是否正确
     *
     * @param user     被检查的用户
     * @param password 密码字符串
     * @return true:检查通过；反之返回false
     */
    boolean checkPassword(User user, String password);

    User checkPassword(String account, String password);

    List<User> listSender();

}
