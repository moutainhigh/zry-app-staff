package com.zhongmei.bty.snack.orderdish.presenter;



public interface Presenter<V> {

    void attachView(V view);

    void detachView();
}
