package com.zhongmei.bty.snack.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by demo on 2018/12/15
 */

public abstract class AbstractBasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    private Reference<V> mViewRef;//View接口类型的弱引用

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
