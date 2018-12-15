package com.zhongmei.bty.data.operates;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.basemodule.discount.entity.CrmCustomerLevelRights;

public interface IntergralRightsDal extends IOperates {

    /**
     * 查询指定等级的会员积分抵现规则
     *
     * @param levelId
     * @return
     */
    CrmCustomerLevelRights findIntergralRightsByLevel(long levelId);

}
