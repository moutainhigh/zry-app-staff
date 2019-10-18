package com.zhongmei.bty.basemodule.shoppingcart.utils;

import com.zhongmei.bty.basemodule.discount.bean.AppletPrivilegeVo;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;



public class AppletUtil {


    public static void removeApplet(IShopcartItemBase mShopcartItem) {

        if (mShopcartItem.getAppletPrivilegeVo() == null) {
            return;
        }

        AppletPrivilegeVo appletPrivilegeVo = mShopcartItem.getAppletPrivilegeVo();
        if (appletPrivilegeVo.getTradePrivilege().getId() == null) {
            mShopcartItem.setAppletPrivilegeVo(null);
            return;
        }
        if (appletPrivilegeVo.getTradePrivilege() != null) {
            appletPrivilegeVo.getTradePrivilege().setInValid();
        }

    }
}
