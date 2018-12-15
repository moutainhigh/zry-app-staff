package com.zhongmei.bty.dinner.table.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 桌台区域视图，含操控板与双向滚动
 *
 * @version: 1.0
 * @date 2015年9月7日
 */
public class DinnerZoneView extends ZoneView {

    public DinnerZoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public ZoneContentView getZoneContentView(HVScrollView mScrollView, ZoneContentView.Callback callback) {
        return new DinnerZoneContentView(mScrollView, callback);
    }


}
