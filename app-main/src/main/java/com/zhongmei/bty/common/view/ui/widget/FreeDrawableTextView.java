package com.zhongmei.bty.common.view.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;



public class FreeDrawableTextView extends TextView {
    public FreeDrawableTextView(Context context) {
        super(context);
    }

    public FreeDrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FreeDrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
