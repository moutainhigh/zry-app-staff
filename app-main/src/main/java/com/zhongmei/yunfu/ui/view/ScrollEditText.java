package com.zhongmei.yunfu.ui.view;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;



public class ScrollEditText extends EditText {


    private int mOffsetHeight;


    private boolean mBottomFlag = false;

    public ScrollEditText(Context context) {
        super(context);
    }

    public ScrollEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int paddingTop;

        int paddingBottom;

        int mHeight;

        int mLayoutHeight;

                mLayoutHeight = getLayout().getHeight();
                paddingTop = getTotalPaddingTop();
                paddingBottom = getTotalPaddingBottom();
                mHeight = getHeight();
                mOffsetHeight = mLayoutHeight + paddingTop + paddingBottom - mHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
                if (this.getLineCount() > 4)
            getParent().requestDisallowInterceptTouchEvent(true);

        return result;
    }

    @Override
    protected void onScrollChanged(int horiz, int vert, int oldHoriz, int oldVert) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
        if (vert == mOffsetHeight || vert == 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
        }
    }
}


