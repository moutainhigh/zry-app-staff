package com.zhongmei.bty.dinner.table.view;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * 支持直接拖放的视图
 *
 * @version: 1.0
 * @date 2015年9月2日
 */
public abstract class DirectDragableView extends LinearLayout {

    private static final int OFFSET_DP = 5;

    private boolean mEnabledDrag = true;
    private float mDownX;
    private float mDownY;
    private boolean mDraging;

    protected DirectDragableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setEnabledDrag(boolean b) {
        mEnabledDrag = b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mEnabledDrag) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
//	    			requestDisallowInterceptTouchEvent(true);
                    mDownX = event.getX();
                    mDownY = event.getY();
                    mDraging = false;
                    return true;

                case MotionEvent.ACTION_MOVE:
                    float x = event.getX();
                    float y = event.getY();
                    if (Math.abs(mDownX - x) > OFFSET_DP || Math.abs(mDownY - y) > OFFSET_DP) {
                        mDraging = true;
                        return startDrag(getClipData(), getDragShadowBuilder(), this, 0);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if (!mDraging) {
                        callOnClick();
                    }
                    mDraging = false;
//	    			requestDisallowInterceptTouchEvent(false);
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    protected DragShadowBuilder getDragShadowBuilder() {
        return new DragShadowBuilder(this);
    }

    protected abstract ClipData getClipData();

}
