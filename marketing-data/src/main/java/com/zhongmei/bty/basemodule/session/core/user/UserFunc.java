package com.zhongmei.bty.basemodule.session.core.user;

import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.bty.baseservice.util.peony.land.Task;
import com.zhongmei.yunfu.context.session.core.user.Role;
import com.zhongmei.yunfu.context.session.core.user.User;

import java.util.List;



public interface UserFunc extends Session.SessionFunc {

    List<Role> getRoles();



    List<User> getUsers();




    List<User> getUsers(String authCode);






    void getUsers(String authCode, Auth.Filter filter, Task.Callback<List<User>> callback);


    List<User> getAllSalesman();


    List<User> getUsersByFaceCode(String faceCode);




    List<User> getUserByIdentity(int identity);


    boolean checkPassword(User user, String password);

    User checkPassword(String account, String password);

    List<User> listSender();

}
