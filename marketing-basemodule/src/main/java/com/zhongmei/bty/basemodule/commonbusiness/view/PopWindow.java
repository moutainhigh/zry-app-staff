package com.zhongmei.bty.basemodule.commonbusiness.view;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class PopWindow extends PopupWindow {
    private Activity context;

    public PopWindow(View contentView, int width, int height, Activity context) {

        super(contentView, width, height, false);
        this.context = context;
    }

    @Override
    public void dismiss() {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 1.0f;
        context.getWindow().setAttributes(lp);
        super.dismiss();
    }
}
