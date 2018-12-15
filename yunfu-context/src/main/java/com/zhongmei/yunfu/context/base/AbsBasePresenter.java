package com.zhongmei.yunfu.context.base;

import com.zhongmei.yunfu.context.UILoadingController;

public abstract class AbsBasePresenter<V extends UILoadingController> {

    protected V uiView;

    public AbsBasePresenter(V view) {
        this.uiView = view;
    }
}
