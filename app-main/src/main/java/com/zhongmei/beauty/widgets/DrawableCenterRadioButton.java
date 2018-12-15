package com.zhongmei.beauty.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.zhongmei.yunfu.R;

/**
 * Created by demo on 2018/12/15
 */

public class DrawableCenterRadioButton extends RadioButton {
    private float drawableCenterMargin = -1f;//-1默认表示居中

    public DrawableCenterRadioButton(Context context) {
        this(context, null);
    }

    public DrawableCenterRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DrawableCenterRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawableCenterRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.drawableCenter);
            drawableCenterMargin = a.getDimension(R.styleable.drawableCenter_drawableMargin, -1l);
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
