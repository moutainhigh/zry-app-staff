package com.zhongmei.bty.mobilepay.bean;

import android.app.Activity;

/*import com.zhongmei.bty.cashier.pay.activity.PayMainActivityDialog;*/
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;

/**
 * Created by demo on 2018/12/15
 */

public class InvoiceTool {


    public static void setInvoiceParams(Activity activity, CashInfoManager cashInfoManager) {
        if (cashInfoManager != null) {
            cashInfoManager.setPrintInvoice(printInvoiceEnable(activity));
            cashInfoManager.setElectronicInvoiceVo(getElectronicInvoiceVo(activity));
        }
    }

    public static boolean printInvoiceEnable(Activity activity) {
        /*if (activity instanceof PayMainActivityDialog) {
            return ((PayMainActivityDialog) activity).printInvoiceEnable();
        }*/
        return false;
    }

    public static ElectronicInvoiceVo getElectronicInvoiceVo(Activity activity) {
        /*if (activity instanceof PayMainActivityDialog) {
            return ((PayMainActivityDialog) activity).getElectronicInvoiceVo();
        }*/
        return null;
    }
}
