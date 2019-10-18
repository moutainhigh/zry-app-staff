package com.zhongmei.bty.snack.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;



public abstract class AbstractBasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    private Reference<V> mViewRef;
    @Override
    public void attachView(V v) {
        mViewRef = new WeakReference<>(v);
    }

    @Override
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    @Override
    public V getView() {
        return mViewRef != null ? mViewRef.get() : null;
    }

    @Override
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }
}
