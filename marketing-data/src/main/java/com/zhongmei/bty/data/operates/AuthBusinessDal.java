package com.zhongmei.bty.data.operates;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.commonmodule.database.entity.AuthBusiness;

/**
 * Created by demo on 2018/12/15
 */

public interface AuthBusinessDal extends IOperates {

    /**
     * 通过业务的code查询业务权限
     *
     * @param code
     * @return
     */
    public AuthBusiness queryBusinessByCode(String code);

}
