package com.zhongmei.bty.mobilepay.utils;

import android.content.Context;

import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.PayMethodItem;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.yunfu.db.enums.PayModelGroup;



public class PayDisplayUtil {
    public static void updateCashUserInfo(Context context, boolean isShowMoney, IPaymentInfo cashInfo) {

    }


    public static void doUpdateScreen(Context context, PayMethodItem mCurrentMethod, IPaymentInfo cashInfo) {
        CustomerResp customer = cashInfo.getCustomer();
        if (customer != null) {

        } else {
            if (mCurrentMethod != null && mCurrentMethod.payModelGroup == PayModelGroup.VALUE_CARD) {

            } else {
                DisplayServiceManager.updateDisplayPay(context, cashInfo.getActualAmount());
            }
        }
    }
}
