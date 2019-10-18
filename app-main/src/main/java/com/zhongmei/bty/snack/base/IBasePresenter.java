package com.zhongmei.bty.snack.base;



public interface IBasePresenter<V extends IBaseView> extends BasePresenter {

    void attachView(V v);

    void detachView();

    V getView();

    boolean isViewAttached();

    void start();
}
