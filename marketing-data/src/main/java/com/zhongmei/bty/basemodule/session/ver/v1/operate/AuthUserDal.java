package com.zhongmei.bty.basemodule.session.ver.v1.operate;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.db.entity.AuthRole;
import com.zhongmei.yunfu.db.entity.AuthUser;

import java.sql.SQLException;
import java.util.List;


public interface AuthUserDal extends IOperates {

    String TAG = AuthUserDal.class.getSimpleName();

    AuthUser getUserByAccount(String account) throws Exception;


    List<AuthRole> listRole() throws Exception;


    List<AuthUser> listLoginUser(String permissionCode) throws Exception;


    List<AuthUser> listLoginUser(String permissionCode, AuthDataFilter filter) throws Exception;


    List<AuthUser> listLoginUser() throws Exception;







    List<AuthUser> listAllSalesman() throws Exception;




    interface AuthDataFilter {


        boolean accept(String dataName, String dataContent);

    }
}
