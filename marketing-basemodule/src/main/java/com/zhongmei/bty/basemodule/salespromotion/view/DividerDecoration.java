package com.zhongmei.bty.basemodule.salespromotion.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.yunfu.util.DensityUtil;


public class DividerDecoration extends RecyclerView.ItemDecoration {

    public int mColor;
    public int mDimSize;
    private Paint mPaint;

    public DividerDecoration(Context context) {
        Resources res = context.getResources();
        mColor = res.getColor(R.color.retail_line_color);
        mDimSize = DensityUtil.dip2px(context, 0.67f);
        init(context, mColor, mDimSize);
    }

    public DividerDecoration(Context context, int color, int dimSize) {
        init(context, color, dimSize);
    }

    public void init(Context context, int color, int dimSize) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        mDimSize = dimSize;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        super.onDraw(c, parent, state);

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            final int top = view.getBottom() + params.bottomMargin;
            final int bottom = top + mDimSize;

            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, mDimSize);
    }
}
