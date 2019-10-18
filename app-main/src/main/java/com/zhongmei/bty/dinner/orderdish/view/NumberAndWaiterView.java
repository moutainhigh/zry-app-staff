package com.zhongmei.bty.dinner.orderdish.view;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.LinearLayout;


public abstract class NumberAndWaiterView extends LinearLayout {

    protected FragmentActivity mActivity;

    protected StatusChangeListener onChangeListener;

    public interface StatusChangeListener {
        void onDataChanged();

        void onSubmit();

        void onCancel();
    }

    public abstract void updateNumberAndWaiter();

    public void setStatusChangeListener(StatusChangeListener listener) {
        this.onChangeListener = listener;
    }

    public NumberAndWaiterView(Context context) {
        super(context);
        this.mActivity = (FragmentActivity) context;
    }
}
