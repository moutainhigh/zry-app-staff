package com.zhongmei.bty.mobilepay.utils;

import android.content.Context;

import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.PayMethodItem;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.yunfu.db.enums.PayModelGroup;

/**
 * Created by demo on 2018/12/15
 */

public class PayDisplayUtil {
    public static void updateCashUserInfo(Context context, boolean isShowMoney, IPaymentInfo cashInfo) {
        /*DisplayUserInfo dUserInfo = null;
        long integral = cashInfo.getMemberIntegral();
        if (cashInfo.getCustomer() != null) {
//			dUserInfo=DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, cashInfo
//					.getCustomer(), integral,isShowMoney,cashInfo.getActualAmount());
            dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, cashInfo.getCustomer(),
                    integral, isShowMoney, cashInfo.getActualAmount());
        } else if (cashInfo.getEcCard() != null) {
            dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, cashInfo
                    .getEcCard(), integral, isShowMoney, cashInfo.getActualAmount());
        }
        if (dUserInfo != null) {
            DisplayServiceManager.updateDisplay(context, dUserInfo);
        }*/
    }

    /**
     * 抹零 抹分时更新界面 update sencond screen
     *
     * @Title: doUpdateScreen
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    public static void doUpdateScreen(Context context, PayMethodItem mCurrentMethod, IPaymentInfo cashInfo) {
        CustomerResp customer = cashInfo.getCustomer();
        if (customer != null) {
            /*DisplayUserInfo dUserInfo =
                    DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW,
                            customer,
                            cashInfo.getMemberIntegral(), true, cashInfo.getActualAmount());
            DisplayServiceManager.updateDisplay(context, dUserInfo);*/
        } else {
            if (mCurrentMethod != null && mCurrentMethod.payModelGroup == PayModelGroup.VALUE_CARD) {
                /*DisplayUserInfo dUserInfo =
                        DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_ACCOUNT_INPUT, "", null, 0, true, cashInfo.getActualAmount());
                DisplayServiceManager.updateDisplay(context, dUserInfo);*/
            } else {
                DisplayServiceManager.updateDisplayPay(context, cashInfo.getActualAmount());
            }
        }
    }
}
