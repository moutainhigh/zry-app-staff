package com.zhongmei.bty.snack.base;


/**
 * MVP基类接口
 *
 * @version 1.0.0
 * @since 2018.04.02.
 */
public interface IBasePresenter<V extends IBaseView> extends BasePresenter {

    void attachView(V v);

    void detachView();

    V getView();

    boolean isViewAttached();

    void start();
}
