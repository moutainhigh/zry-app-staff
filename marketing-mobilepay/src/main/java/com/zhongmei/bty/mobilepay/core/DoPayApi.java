package com.zhongmei.bty.mobilepay.core;

import android.support.v4.app.FragmentActivity;

import com.zhongmei.bty.mobilepay.IOnlinePayBreakCallback;
import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.mobilepay.ISavedCallback;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;

import java.io.Serializable;



public abstract class DoPayApi<R> extends OnlinePayApi<R> implements Serializable {
    private static final long serialVersionUID = 1L;

    public abstract void doPay(final FragmentActivity context, final IPaymentInfo paymentInfo, final IPayOverCallback callback);

    public abstract void saveTrade(final FragmentActivity context, final IPaymentInfo paymentInfo, final IPayOverCallback callback, final ISavedCallback savedCallback);

    public abstract void showOnlinePayDialog(final FragmentActivity context, PayModelItem payModelItem, final IPaymentInfo paymentInfo, int scanType, final IOnlinePayBreakCallback callback);

    public abstract void doPrint(IPaymentInfo paymentInfo, String tradeUuid, boolean isPrintLabel, boolean isPrintKitchen, boolean isOpenMoneyBox, boolean isPintPayTick);
}
