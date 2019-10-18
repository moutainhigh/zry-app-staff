package com.zhongmei.yunfu.init.sync;

import android.content.Context;

import java.util.Set;

public abstract class Check {

    protected static final int CHECK_MSG_TYPE_START = 0;

    protected static final int CHECK_MSG_TYPE_UPDATE = 1;

    protected static final int CHECK_MSG_TYPE_SUCCESS = 2;

    protected static final int CHECK_MSG_TYPE_ERROR = 3;

    private String mTitle;

        private boolean canContinue = false;

    private CheckListener mListener;

    protected Context mContext;

    public Check(Context context, String title, boolean canContinue) {
        mTitle = title;
        mContext = context;
        this.canContinue = canContinue;
    }

    public String getmTitle() {
        return mTitle;
    }

    public boolean isCanContinue() {
        return canContinue;
    }

    public void setCheckListener(CheckListener listener) {
        mListener = listener;
    }

    public void start() {
        running(null);
    }

    public void update(String msg) {
        if (mListener != null) {
            mListener.update(this, msg);
        }
    }

    public void success(String msg) {
        if (mListener != null) {
            mListener.onSuccess(this, msg);
        }
    }

    public void error(String msg) {
        error(msg, null);
    }

    public void error(String msg, Throwable error) {
        if (mListener != null) {
            mListener.onError(this, msg, error);
        }
    }


    public abstract void running(Set<String> modules);


    interface CheckCompletedCallback {
        void onCheckCompleted();
    }
}
