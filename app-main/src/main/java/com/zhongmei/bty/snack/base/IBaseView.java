package com.zhongmei.bty.snack.base;

import android.content.Context;



public interface IBaseView {
    Context getViewContext();

    void showToast(String content);
}
