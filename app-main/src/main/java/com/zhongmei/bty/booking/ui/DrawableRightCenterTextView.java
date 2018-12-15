package com.zhongmei.bty.booking.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 *

 *
 */
public class DrawableRightCenterTextView extends TextView {

    private final static String TAG = "RightDrawableButton";
    private float textWidth;
    private float bodyWidth;

    public DrawableRightCenterTextView(Context context) {
        super(context);
    }

    public DrawableRightCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableRightCenterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        textWidth = getPaint().measureText(getText().toString());
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawableRight = drawables[2];
        int totalWidth = getWidth();
        if (drawableRight != null) {
            int drawableWidth = drawableRight.getIntrinsicWidth();
            int drawablePadding = getCompoundDrawablePadding();
            bodyWidth = textWidth + drawableWidth + drawablePadding;
            setPadding(0, 0, (int) (totalWidth - bodyWidth), 0);
        }
    }

    public void setText(String text) {
        if (text.equals(getText().toString()))
            return;
        super.setText(text);
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        canvas.translate((width - bodyWidth) / 2, 0);
        super.onDraw(canvas);
    }
}