package com.zhongmei.bty.dinner.ordercenter.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.dinner.ordercenter.bean.AccountItemBean;
import com.zhongmei.bty.dinner.ordercenter.bean.PayModeBean;
import com.zhongmei.bty.dinner.ordercenter.bean.PayModeBean.Status;
import com.zhongmei.bty.dinner.ordercenter.bean.PayModeBeanComparator;
import com.zhongmei.yunfu.db.entity.trade.PaymentModeShop;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.basemodule.pay.operates.PaymentModeDal;


public class AccountReconciliationDataManager {
    private static final String TAG = AccountReconciliationDataManager.class.getSimpleName();

    Context context;

    public AccountReconciliationDataManager(Context context) {
        this.context = context;
    }

    public List<PayModeBean> getPayModes() {
        PaymentModeDal dal = OperatesFactory.create(PaymentModeDal.class);
        List<PaymentModeShop> paymentModeShops = null;
        try {
            paymentModeShops = dal.findAllPaymentMode();
        } catch (Exception e) {
                        Log.e(TAG, "", e);
        }
        if (paymentModeShops == null || paymentModeShops.size() == 0) return null;
        List<PayModeBean> payModeBeans = new ArrayList<PayModeBean>();
        for (PaymentModeShop paymentModeShop : paymentModeShops) {
            PayModeBean payModeBean = new PayModeBean();
            payModeBean.setPayModeName(paymentModeShop.getName());
            payModeBean.setPayModeId(paymentModeShop.getErpModeId());
            payModeBean.setPayModeGroup(paymentModeShop.getPaymentModeType());
            payModeBean.setValue(getValue(context, paymentModeShop.getName()));
            payModeBeans.add(payModeBean);
        }

        Collections.sort(payModeBeans, new PayModeBeanComparator());
        return payModeBeans;

    }


    public PayModeBean findNotAppearItem(List<AccountItemBean> accountItemBeans, List<PayModeBean> payModeBeans) {
        if (accountItemBeans == null) {            if (payModeBeans != null && payModeBeans.size() > 0) {
                return payModeBeans.get(0);
            } else {
                return new PayModeBean();
            }

        }
        for (PayModeBean payModeBean : payModeBeans) {
            if (!isNameInAccountItemBeanList(payModeBean.getPayModeName(), accountItemBeans)) {
                return payModeBean;
            }

        }
        return null;
    }


    boolean isNameInAccountItemBeanList(String name, List<AccountItemBean> accountItemBeans) {
        for (AccountItemBean accountItemBean : accountItemBeans) {
            if (accountItemBean.getPayModeName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public List<PayModeBean> getStatusPayModeBeans(List<AccountItemBean> accountItemBeans, List<PayModeBean> payModeBeans) {

        for (PayModeBean payModeBean : payModeBeans) {
            if (accountItemBeans == null) {
                payModeBean.setStatus(Status.ENABLE);
            } else {
                if (isNameInAccountItemBeanList(payModeBean.getPayModeName(), accountItemBeans)) {
                    payModeBean.setStatus(Status.DISABLE);
                } else {
                    payModeBean.setStatus(Status.ENABLE);
                }
            }


        }
        return payModeBeans;
    }


    private byte getValue(Context context, String name) {
        String[] arrays = context.getResources().getStringArray(R.array.dinner_ordercenter_account_reconciliation_payways2);
        for (byte index = 0; index < arrays.length; index++) {
            if (name.equals(arrays[index])) {
                return index;
            }
        }
        return 10;


    }


}

