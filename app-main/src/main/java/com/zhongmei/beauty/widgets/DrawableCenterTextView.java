package com.zhongmei.beauty.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zhongmei.yunfu.R;


public class DrawableCenterTextView extends TextView {
    private int drawableCenterMargin = -1;
    public DrawableCenterTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DrawableCenterTextView(Context context) {
        this(context, null);
    }


    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.drawableCenter);
            drawableCenterMargin = a.getInt(R.styleable.drawableCenter_drawableMargin, -1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        float drawableMarginLeft = 0;
        if (drawableCenterMargin < 0) {
            float textWidth = getPaint().measureText(getText().toString());
            int drawablePadding = 0;
            int drawableWidth = 0;
            Drawable drawableLeft = drawables[0];
            if (drawableLeft != null) {
                drawablePadding = getCompoundDrawablePadding();
                drawableWidth = drawableLeft.getIntrinsicWidth();
            }
            float bodyWidth = textWidth + drawableWidth + drawablePadding;
            drawableMarginLeft = (getWidth() - bodyWidth) / 2;
        } else {
            drawableMarginLeft = drawableCenterMargin;
        }

        canvas.translate(drawableMarginLeft, 0);
        super.onDraw(canvas);
    }
}
