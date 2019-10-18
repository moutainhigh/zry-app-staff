package com.zhongmei.bty.cashier.ordercenter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {

    private ScrollListener mListener;

    public static interface ScrollListener {
        public void scrollYPostion(int position);
    }

    public static final int SCROLL_UP = 0x01;

    public static final int SCROLL_DOWN = 0x10;

    private static final int SCROLLLIMIT = 40;

    public ObservableScrollView(Context context) {
        super(context, null);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public ObservableScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mListener != null) {
            mListener.scrollYPostion(t);
        }

    }

    public void setScrollListener(ScrollListener l) {
        this.mListener = l;
    }

}
