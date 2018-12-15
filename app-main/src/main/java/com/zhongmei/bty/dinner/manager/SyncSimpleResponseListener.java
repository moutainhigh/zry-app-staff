package com.zhongmei.bty.dinner.manager;

import com.zhongmei.yunfu.net.volley.Request;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.basemodule.commonbusiness.listener.SimpleResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 同步回调监听
 *
 * @param <T>
 */
public class SyncSimpleResponseListener<T> extends SimpleResponseListener<T> implements SyncFuture<ResponseObject<T>> {
    private Request<?> mRequest;
    private boolean mResultReceived = false;
    private ResponseObject<T> mResult;
    private VolleyError mException;
    boolean isSynchronize = false;
    ResponseListener<T> listener;

    public SyncSimpleResponseListener(ResponseListener<T> listener) {
        this.listener = listener;
    }

    public void setRequest(Request<?> request) {
        mRequest = request;
    }

    @Override
    public void sync(/*ResponseListener<T> listener*/) {
        isSynchronize = true;
        try {
            ResponseObject<T> responseObject = get();
            if (listener != null) {
                listener.onResponse(responseObject);
            }
        } catch (Throwable e) {
            if (listener != null) {
                listener.onError(new VolleyError(e));
            }
        }
    }

    @Override
    public synchronized boolean cancel(boolean mayInterruptIfRunning) {
        if (mRequest == null) {
            return false;
        }

        if (!isDone()) {
            mRequest.cancel();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ResponseObject<T> get() throws InterruptedException, ExecutionException {
        try {
            return doGet(TimeUnit.MILLISECONDS.toMillis(mRequest.getTimeoutMs() + 20 * 1000));
        } catch (TimeoutException e) {
            throw new ExecutionException(e);
        }
    }

    @Override
    public ResponseObject<T> get(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException {
        return doGet(TimeUnit.MILLISECONDS.convert(timeout, unit));
    }

    private synchronized ResponseObject<T> doGet(Long timeoutMs) throws InterruptedException,
            ExecutionException, TimeoutException {
        if (mException != null) {
            throw new ExecutionException(mException);
        }

        if (mResultReceived) {
            return mResult;
        }

        if (timeoutMs == null) {
            wait(0);
        } else if (timeoutMs > 0) {
            wait(timeoutMs);
        }

        if (mException != null) {
            throw new ExecutionException(mException);
        }

        if (!mResultReceived) {
            throw new TimeoutException();
        }

        return mResult;
    }

    @Override
    public boolean isCancelled() {
        if (mRequest == null) {
            return false;
        }
        return mRequest.isCanceled();
    }

    @Override
    public synchronized boolean isDone() {
        return mResultReceived || mException != null || isCancelled();
    }

    @Override
    public final synchronized void onSuccess(ResponseObject<T> response) {
        mResultReceived = true;
        mResult = response;
        notifyAll();
        if (listener != null) {
            listener.onResponse(response);
        }
    }

    @Override
    public final synchronized void onError(VolleyError error) {
        mException = error;
        notifyAll();
        if (!isSynchronize) {
            if (listener != null) {
                listener.onError(error);
            }
        }
    }
}
