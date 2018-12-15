package com.zhongmei.bty.snack.base;

import android.content.Context;

/**
 * Created by demo on 2018/12/15
 */

public interface IBaseView {
    Context getViewContext();

    void showToast(String content);
}
