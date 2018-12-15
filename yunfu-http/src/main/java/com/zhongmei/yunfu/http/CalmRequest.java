package com.zhongmei.yunfu.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.zhongmei.yunfu.context.UILoadingController;
import com.zhongmei.yunfu.monitor.EventListener;
import com.zhongmei.yunfu.http.R;
import com.zhongmei.yunfu.context.AppBuildConfig;
import com.zhongmei.yunfu.net.RequestManagerCompat;
import com.zhongmei.yunfu.net.volley.NetworkError;
import com.zhongmei.yunfu.net.volley.NoConnectionError;
import com.zhongmei.yunfu.net.volley.Request;
import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.Response.Listener;
import com.zhongmei.yunfu.net.volley.ServerError;
import com.zhongmei.yunfu.net.volley.TimeoutError;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.util.NetworkUtil;
import com.zhongmei.yunfu.context.util.manager.SwitchServerManager;

import java.lang.ref.WeakReference;

public abstract class CalmRequest<T> extends EventListener<T> implements ErrorListener {

    private static final String TAG = CalmRequest.class.getSimpleName();

    protected Request<T> mRequest;
    protected Listener<T> mSuccessListener;
    protected ErrorListener mErrorListener;
    protected Context mContext;
    public static int STATUS_CRAETE = 0;
    public static int STATUS_RUNNING = 1;
    public static int STATUS_FINISH = 2;
    public static int STATUS_CANCELLED = 3;
    int status = STATUS_CRAETE;
    WeakReference<Context> mFragmentManager;
    Handler mhandler;

    protected boolean isErpReq = false;//是否为erp的请求，erp请求不需要记录失败次数

    private String mHint = "";
    public static final int HANDLER_MESSAGE_DISPLAY_DIALOG = 0;
    public static final int DISMISS_MESSAGE_DISPLAY_DIALOG = 1;

    private UILoadingController mDialogFragment = null;

    public CalmRequest(Listener<T> successlistener, ErrorListener errorListener) {
        super(EventListener.getEventName(successlistener));
        mErrorListener = errorListener;
        mSuccessListener = successlistener;
        mContext = BaseApplication.sInstance;
    }

    public CalmRequest(Context context, Listener<T> successlistener,
                       ErrorListener errorListener) {
        super(EventListener.getEventName(successlistener));
        mErrorListener = errorListener;
        mSuccessListener = successlistener;
        mContext = context;
    }

    public void setErpReq(boolean erpReq) {
        isErpReq = erpReq;
    }

    public void setInterceptEnable(boolean enable) {
        mRequest.setInterceptEnable(enable);
    }

    public void executeRequest(String tag) {
        status = STATUS_RUNNING;
        RequestManagerCompat.addRequest(BaseApplication.sInstance, mRequest, tag);
    }

    public boolean isRunning() {
        if (status == STATUS_CRAETE || status == STATUS_RUNNING) {
            return true;
        }
        return false;
    }

    public void executeRequest(String tag, String hint, Context fm) {
        status = STATUS_RUNNING;
        RequestManagerCompat.addRequest(BaseApplication.sInstance, mRequest, tag);
        mFragmentManager = new WeakReference<>(fm);
        mHint = hint;
        if (fm != null && mhandler == null) {
            mhandler = new Handler(Looper.getMainLooper()) {

                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case HANDLER_MESSAGE_DISPLAY_DIALOG:
                            Context context = mFragmentManager.get();
                            if (context != null) {
                                try {
								/*mDialogFragment = CalmLoadingDialogFragment.show(mFragmentManager);
								mFragmentManager.executePendingTransactions();*/
                                    if (context instanceof UILoadingController) {
                                        mDialogFragment = (UILoadingController) context;
                                        mDialogFragment.showLoadingDialog();
                                    }
                                } catch (Exception e) {
                                    Log.w(TAG, "" + e);
                                }
                            }

                            break;

                    }
                }
            };
        }
        if (mFragmentManager != null) {
            Message message = mhandler.obtainMessage(
                    HANDLER_MESSAGE_DISPLAY_DIALOG, 0, 0, hint);
            mhandler.sendMessage(message);
        }

    }

    public int getStatus() {
        return status;
    }

    public void cancel() {
        if (getStatus() == STATUS_RUNNING || getStatus() == STATUS_CRAETE) {
            mRequest.cancel();
            status = STATUS_CANCELLED;
            dismissHintDialog();
        }
    }

    protected void dismissHintDialog() {

        mHint = "";

        if (mhandler != null) {
            mhandler.removeMessages(HANDLER_MESSAGE_DISPLAY_DIALOG);
        }
        if (mFragmentManager != null) {
            if (mDialogFragment != null) {
                //CalmLoadingDialogFragment.hide(mDialogFragment);
                mDialogFragment.dismissLoadingDialog();
                mDialogFragment = null;
            }
            mFragmentManager = null;
        }
        mhandler = null;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //先确定网络是可用的
        if (NetworkUtil.isNetworkConnected() && !isErpReq) {
            //同步服务器请求失败，失败次数＋1
            if (SwitchServerManager.getInstance().isServerError(error)) {
                SwitchServerManager.getInstance().retryFailCount();
            }
        }

        status = STATUS_FINISH;
        dismissHintDialog();
		/*if (mErrorListener != null) {
			mErrorListener.onErrorResponse(error);
		}*/
        if (this.mErrorListener != null) {
            VolleyError newError = null;
            if (error != null) {
                String msg;
                if (error instanceof NoConnectionError || error instanceof NetworkError) {
                    msg = this.mContext.getResources().getString(R.string.connect_network_error);
                } else if (error instanceof ServerError) {
                    msg = this.mContext.getResources().getString(R.string.connect_server_error);
                } else if (error instanceof TimeoutError) {
                    msg = this.mContext.getResources().getString(R.string.connect_timeout_error);
                } else {
                    msg = this.mContext.getResources().getString(R.string.connect_exception);
                }
                newError = new VolleyError(msg, error.getCause());
            }
            mErrorListener.onErrorResponse(newError);
        }
    }

    @Override
    public void onResponse(T response) {
        //同步服务器请求成功，失败次数置为0
        if (!isErpReq) {
            SwitchServerManager.getInstance().reset();
        }

        status = STATUS_FINISH;
        dismissHintDialog();
        if (mSuccessListener != null) {
            mSuccessListener.onResponse(response);
        }
    }

    public String getHint() {
        return mHint;
    }
}
