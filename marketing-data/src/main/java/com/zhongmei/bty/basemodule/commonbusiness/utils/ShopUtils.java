package com.zhongmei.bty.basemodule.commonbusiness.utils;

import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;

import java.util.Date;



public class ShopUtils {


    public static Date getBizDate() {
        SystemSettingDal dal = OperatesFactory.create(SystemSettingDal.class);
        return dal.getCurrentBizDate();
    }

}
