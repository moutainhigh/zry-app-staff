package com.zhongmei.bty.snack.base;

import android.content.Context;
import android.os.Bundle;

import com.zhongmei.yunfu.util.Checks;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;



public abstract class AbstractMVPBaseDialogFragment<V extends IBaseView, P extends IBasePresenter<V>> extends BasicDialogFragment implements IBaseView {
    private static final String TAG = AbstractMVPBaseDialogFragment.class.getSimpleName();
    private P mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        Checks.verifyNotNull(mPresenter, "mPresenter");
        mPresenter.attachView((V) this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void showToast(String content) {
    }

    protected abstract P createPresenter();

    public P getPresenter() {
        return mPresenter;
    }
}
