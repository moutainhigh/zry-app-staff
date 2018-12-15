package com.zhongmei.bty.snack.orderdish.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CouponsGridView extends GridView {

    public CouponsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
