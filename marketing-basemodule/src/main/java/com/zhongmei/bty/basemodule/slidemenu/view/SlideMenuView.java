package com.zhongmei.bty.basemodule.slidemenu.view;

import android.content.Context;
import android.widget.RelativeLayout;

import com.zhongmei.bty.basemodule.slidemenu.listener.SlideMenuListener;

/**
 * Created by demo on 2018/12/15
 */

public abstract class SlideMenuView extends RelativeLayout {

    public final static int SLIDE_MENU_TYPE_TRADE = 1;
    public final static int SLIDE_MENU_TYPE_GROUP = 2;

    public SlideMenuListener slideMenuListener;

    public SlideMenuView(Context context) {
        super(context);
    }

    public abstract void onModelClick(int pageNo);

}
