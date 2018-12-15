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

/**
 * @date 2016/12/27 10:27
 */
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
                            //int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.EXACTLY);
                            //child.measure(childWidthMeasureSpec, MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY));
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*int count = getChildCount();
        if (count >= 1) {
            ViewGroup root = (ViewGroup) getChildAt(0);
            count = root.getChildCount();
            int width = getMeasuredWidth();
            for (int i = 0; i < count; ++i) {
                View child = root.getChildAt(i);
                if (!View.class.getName().equals(child.getClass().getName())) {
                    final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                    //lp.width = getMeasuredWidth() / 3;
                    int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.EXACTLY);
                    child.measure(childWidthMeasureSpec, MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY));
                }
            }
        }*/
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
