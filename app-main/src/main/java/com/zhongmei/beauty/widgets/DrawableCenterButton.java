package com.zhongmei.beauty.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.zhongmei.yunfu.R;



public class DrawableCenterButton extends Button {
    private float drawableCenterMargin = -1f;
    public DrawableCenterButton(Context context) {
        this(context, null);
    }

    public DrawableCenterButton(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DrawableCenterButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.drawableCenter);
            drawableCenterMargin = a.getDimension(R.styleable.drawableCenter_drawableMargin, -1f);
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
            int padding = getPaddingLeft() + getPaddingRight();
            float bodyWidth = textWidth + drawableWidth + drawablePadding + padding;
            drawableMarginLeft = (getWidth() - bodyWidth) / 2;
        } else {
            drawableMarginLeft = drawableCenterMargin;
        }

        canvas.translate(drawableMarginLeft, 0);
        super.onDraw(canvas);
    }
}
