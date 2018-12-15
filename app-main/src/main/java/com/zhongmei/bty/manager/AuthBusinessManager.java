package com.zhongmei.bty.manager;

import com.zhongmei.bty.commonmodule.database.entity.AuthBusiness;
import com.zhongmei.bty.data.operates.AuthBusinessDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;

/**
 * 业务权限manager
 * Created by demo on 2018/12/15
 */

public class AuthBusinessManager {

    /**
     * 通过业务code判断是否有业务权限
     *
     * @param code
     * @return
     */
    public static boolean isExistAuthBusiness(String code) {
        AuthBusinessDal dal = OperatesFactory.create(AuthBusinessDal.class);
        AuthBusiness business = dal.queryBusinessByCode(code);
        if (business == null) {
            return false;
        }
        return true;
    }

}
