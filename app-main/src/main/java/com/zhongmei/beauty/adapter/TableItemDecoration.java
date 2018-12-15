package com.zhongmei.beauty.adapter;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by demo on 2018/12/15
 */

public class TableItemDecoration extends RecyclerView.ItemDecoration {
    private int mHorizontalPadding = 0;

    private int mVerticalPadding = 0;

    public TableItemDecoration(int horizontalPadding, int verticalPadding) {
        this.mHorizontalPadding = horizontalPadding;
        this.mVerticalPadding = verticalPadding;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int pos = parent.getChildAdapterPosition(view);
        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();

        outRect.top = pos < spanCount ? 0 : mVerticalPadding;
        outRect.left = mHorizontalPadding;
//        super.getItemOffsets(outRect, view, parent, state);
    }
}
