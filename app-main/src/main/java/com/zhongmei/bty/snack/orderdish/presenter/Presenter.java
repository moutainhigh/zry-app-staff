package com.zhongmei.bty.snack.orderdish.presenter;

/**
 * Created by demo on 2018/12/15
 */

public interface Presenter<V> {

    void attachView(V view);

    void detachView();
}
