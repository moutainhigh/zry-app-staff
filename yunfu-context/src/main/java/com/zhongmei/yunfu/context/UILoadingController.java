package com.zhongmei.yunfu.context;

import android.content.Context;

public interface UILoadingController {

    Context getContext();

    void showLoadingDialog();

    void dismissLoadingDialog();
}
