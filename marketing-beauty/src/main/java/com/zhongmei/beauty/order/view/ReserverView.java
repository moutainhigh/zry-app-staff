package com.zhongmei.beauty.order.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.beauty.booking.bean.BeautyBookingVo;
import com.zhongmei.beauty.booking.interfaces.BeautyBookingTradeControlListener;
import com.zhongmei.beauty.booking.interfaces.IHVScrollListener;
import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.beauty.entity.ReserverItemVo;

import java.util.List;



public class ReserverView extends LinearLayout implements BeautyBookingTradeControlListener {
    private final String TAG = ReserverView.class.getSimpleName();
    protected final HVScrollView mScrollView;
    private ReserverBoardView mReserverBoardView;
    private IHVScrollListener mHVScrollListener;
    private BeautyBookingTradeControlListener mControlListener;
    private int mWidth;
    private int mHeight;

    private int mActualWidth;
    private int mActualHeight;

    public ReserverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScrollView = new HVScrollView(context);
        mScrollView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mScrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        addView(mScrollView);
    }

    public void setHVScrollListener(IHVScrollListener listener) {
        this.mHVScrollListener = listener;
    }

    public void setControlListener(BeautyBookingTradeControlListener listener) {
        this.mControlListener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = Math.max(mWidth, mActualWidth);
        int height = Math.max(mHeight, mActualHeight);
        if (mReserverBoardView != null) {
            ViewGroup.LayoutParams layoutParams = mReserverBoardView.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            mReserverBoardView.setLayoutParams(layoutParams);
            mReserverBoardView.setMinimumWidth(width);
            mReserverBoardView.setMinimumHeight(height);
            mReserverBoardView.measure(layoutParams.width, layoutParams.height);

            Log.e("onLayout", "mHeight=>" + mHeight + ";mWidth=>" + mWidth);
        }
        super.onLayout(changed, l, t, r, b);

        mScrollView.layout(0, 0, mWidth, mHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.e("ReserverView", "mHeight=>" + mHeight + ";mWidth=>" + mWidth);


    }


    public void initReserverView() {
        Log.e(TAG, "initReserverView");
        if (mReserverBoardView == null) {
            mReserverBoardView = new ReserverBoardView(getContext(), mScrollView, this);
            mReserverBoardView.setBackgroundColor(getResources().getColor(R.color.beauty_bg_white));
            mScrollView.addView(mReserverBoardView);
        }
    }


    public void onSizeChange(int width, int height) {
        mActualHeight=height;
        mActualWidth=width;

        width = Math.max(mWidth, width);
        height = Math.max(mHeight, height);

        ViewGroup.LayoutParams layoutParams = mReserverBoardView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        mReserverBoardView.setLayoutParams(layoutParams);
        mReserverBoardView.setMinimumWidth(width);
        mReserverBoardView.setMinimumHeight(height);
        mReserverBoardView.measure(layoutParams.width, layoutParams.height);
        Log.e("onSizeChange", "mHeight=>" + height + ";mWidth=>" + width);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mHVScrollListener != null && mScrollView != null) {
            mHVScrollListener.scroll(mScrollView.getScrollX(), mScrollView.getScrollY());
            Log.e("ReserverView", "ScrollX=>" + mScrollView.getScrollX() + ";ScrollY=>" + mScrollView.getScrollY());
        }


    }

    public void refreshReserverData(List<ReserverItemVo> mReserverItemVos) {
        if (mReserverBoardView != null) {
            mReserverBoardView.refreshReserverTradeVos(mReserverItemVos);
        }
    }

    @Override
    public void editTrade(BeautyBookingVo reserverTradeVo) {
        if (mControlListener != null) {
            mControlListener.editTrade(reserverTradeVo);
        }
    }
}
