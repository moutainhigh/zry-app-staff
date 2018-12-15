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

/**
 * @Date：2015年10月17日
 * @Description:处理调账界面的数据
 * @Version: 1.0
 */
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
            // TODO Auto-generated catch block
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

    /**
     * @param accountItemBeans
     * @param payModeBeans
     * @return
     * @Date 2015年10月20日
     * @Description: 找出未出现的item
     * @Return String
     */
    public PayModeBean findNotAppearItem(List<AccountItemBean> accountItemBeans, List<PayModeBean> payModeBeans) {
        if (accountItemBeans == null) {//第一项数据需要判断
            if (payModeBeans != null && payModeBeans.size() > 0) {
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

    /**
     * @param name
     * @param accountItemBeans
     * @return
     * @Date 2015年10月20日
     * @Description: 判断名称是否出现在list中
     * @Return boolean
     */
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

    /**
     * @param context
     * @param name
     * @return
     * @Date 2015年10月20日
     * @Description: 获取排序索引
     * @Return int
     */
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

