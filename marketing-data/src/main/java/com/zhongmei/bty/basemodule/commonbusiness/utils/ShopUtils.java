package com.zhongmei.bty.basemodule.commonbusiness.utils;

import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;

import java.util.Date;

/**
 * Created by demo on 2018/12/15
 */

public class ShopUtils {

    /**
     * 获取营业日期。 营业日期根据OpenTime表中的设置计算
     */
    public static Date getBizDate() {
        SystemSettingDal dal = OperatesFactory.create(SystemSettingDal.class);
        return dal.getCurrentBizDate();
    }

}
