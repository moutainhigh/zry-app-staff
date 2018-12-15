package com.zhongmei.yunfu.http;

import android.database.Observable;

public class RequestObjectObservable extends Observable<RequestObjectObservable.InterceptCreateObserver> {

    public void notifyChanged(RequestObject<?> request) {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged(request);
            }
        }
    }

    public interface InterceptCreateObserver {

        void onChanged(RequestObject<?> request);
    }
}
