package com.zhongmei.beauty.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;



public class marqueeText extends TextView {
    public marqueeText(Context context) {
        super(context);
    }

    public marqueeText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public marqueeText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public marqueeText(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
