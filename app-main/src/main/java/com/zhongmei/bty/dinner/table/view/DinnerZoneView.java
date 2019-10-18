package com.zhongmei.bty.dinner.table.view;

import android.content.Context;
import android.util.AttributeSet;


public class DinnerZoneView extends ZoneView {

    public DinnerZoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public ZoneContentView getZoneContentView(HVScrollView mScrollView, ZoneContentView.Callback callback) {
        return new DinnerZoneContentView(mScrollView, callback);
    }


}
