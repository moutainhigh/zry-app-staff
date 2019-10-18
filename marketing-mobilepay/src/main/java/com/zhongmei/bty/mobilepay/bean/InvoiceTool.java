package com.zhongmei.bty.mobilepay.bean;

import android.app.Activity;


import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;



public class InvoiceTool {


    public static void setInvoiceParams(Activity activity, CashInfoManager cashInfoManager) {
        if (cashInfoManager != null) {
            cashInfoManager.setPrintInvoice(printInvoiceEnable(activity));
            cashInfoManager.setElectronicInvoiceVo(getElectronicInvoiceVo(activity));
        }
    }

    public static boolean printInvoiceEnable(Activity activity) {

        return false;
    }

    public static ElectronicInvoiceVo getElectronicInvoiceVo(Activity activity) {

        return null;
    }
}
