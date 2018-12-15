package com.zhongmei.bty.basemodule.shoppingcart.utils;

import com.zhongmei.bty.basemodule.discount.bean.AppletPrivilegeVo;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;

/**
 * 小程序处理工具类
 * Created by demo on 2018/12/15
 */

public class AppletUtil {

    /**
     * 移除单品项的小程序
     *
     * @param mShopcartItem
     */
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
