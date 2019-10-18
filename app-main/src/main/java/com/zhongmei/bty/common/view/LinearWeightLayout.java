package com.zhongmei.bty.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class LinearWeightLayout extends HorizontalScrollView {

    private int childWeight = 3;

    public LinearWeightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LinearWeightLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        ViewTreeObserver vto2 = getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int count = getChildCount();
                if (count >= 1) {
                    ViewGroup root = (ViewGroup) getChildAt(0);
                    count = root.getChildCount();
                    for (int i = 0; i < count; ++i) {
                        View child = root.getChildAt(i);
                        if (!View.class.getName().equals(child.getClass().getName())) {
                            final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                            int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
                            int usableWidth = getWidth() - pad * (childWeight - 1);
                            lp.width = usableWidth / childWeight;
                            requestLayout();
                                                                                }
                    }
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
