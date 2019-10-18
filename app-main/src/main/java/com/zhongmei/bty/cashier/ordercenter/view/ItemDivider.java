package com.zhongmei.bty.cashier.ordercenter.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.util.DensityUtil;



public class ItemDivider extends RecyclerView.ItemDecoration {
    private Drawable mDrawable;

    public ItemDivider(Context context, int resId) {
        mDrawable = context.getResources().getDrawable(resId);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + DensityUtil.dip2px(MainApplication.getInstance(), 6);
        int right = parent.getWidth() - parent.getPaddingRight() - DensityUtil.dip2px(MainApplication.getInstance(), 6);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, mDrawable.getIntrinsicHeight());
    }
}
