package com.zhongmei.bty.dinner.table.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.dinner.table.model.ZoneModel;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTradeMoveDish;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;

import java.util.HashMap;


public abstract class ZoneView extends LinearLayout {

    private static final int CONSOLE_WIDTH = DensityUtil.dip2px(MainApplication.getInstance(), 409);

    private final Scroller mScroller;            protected final HVScrollView mScrollView;        private ZoneContentView mZoneContentView;

    private OnControlListener mOnControlListener;

    private ViewGroup mControlView;
    private int mWidth;
    private int mHeight;
    private boolean mShowingControl;
    private boolean mNeedScroll;

    protected DeleteCallback mDeleteCallback;

    public ZoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        mScrollView = new HVScrollView(context);
        mScrollView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                mScrollView.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT));
        addView(mScrollView);
        int paddingLeft = DensityUtil.dip2px(context, OpentablePopWindow.ADD_BUTTON_WIDTH);
        int paddingTop = DensityUtil.dip2px(context, OpentablePopWindow.ADD_BUTTON_HEIGHT);

    }

    public void initChildren() {
        if (mZoneContentView == null) {
            mZoneContentView = getZoneContentView(mScrollView, new ZoneContentView.Callback() {

                @Override
                public void onDragStarted() {
                    if (mDeleteCallback != null) {
                        mDeleteCallback.onDragStarted();
                    }
                }

                @Override
                public void onEntered(View view) {
                    if (mDeleteCallback != null) {
                        mDeleteCallback.onEntered(view);
                    }
                }

                @Override
                public void onExited(View view) {
                    if (mDeleteCallback != null) {
                        mDeleteCallback.onExited(view);
                    }
                }

                @Override
                public void onDragEnded() {
                    if (mDeleteCallback != null) {
                        mDeleteCallback.onDragEnded();
                    }
                }

                @Override
                public void onDrop(IDinnertableTrade orginal, TableTradeViewBase dest) {
                    if (mOnControlListener != null) {
                        mOnControlListener.onMerge(orginal, dest.getModel());
                    }
                }

                @Override
                public void onDrop(IDinnertableTrade orginal, TableViewBase dest) {
                    if (mOnControlListener != null) {
                        mOnControlListener.onTransfer(orginal, dest.getModel());
                    }
                }

                @Override
                public void onDelete(IDinnertableTrade orginal) {
                    if (mOnControlListener != null) {
                        mOnControlListener.onDelete(orginal);
                    }
                }

                @Override
                public void onClick(TableViewBase view) {
                    if (mOnControlListener == null || mOnControlListener.onSelect(view.getModel(), view)) {
                        showControl(view);
                    }
                }

                @Override
                public void onDropMoveDish(IDinnertableTradeMoveDish orginal, TableViewBase dest) {
                    if (mOnControlListener != null) {
                        mOnControlListener.onTransferMoveDish(orginal, dest.getModel());
                    }
                }

                @Override
                public void onDropMoveDish(IDinnertableTradeMoveDish orginal, TableTradeViewBase dest) {
                    if (mOnControlListener != null) {
                        mOnControlListener.onMergeMoveDish(orginal, dest.getModel());
                    }
                }
            });
            mZoneContentView.setVisibility(View.INVISIBLE);

            mScrollView.addView(mZoneContentView);
            mControlView = (ViewGroup) findViewById(R.id.control_view);
        }
    }

    private void checkChildren() {
        if (mZoneContentView == null) {
            throw new IllegalStateException("Not init children.");
        }
    }

    public void setModel(ZoneModel model) {
        checkChildren();
        if (model == null) {
            mZoneContentView.setVisibility(View.INVISIBLE);
        } else {
            mZoneContentView.setVisibility(View.VISIBLE);
            mZoneContentView.setModel(model);
            mZoneContentView.setMinimumWidth(mWidth);
            mZoneContentView.setMinimumHeight(mHeight);
        }
    }

    public ZoneModel getModel() {
        checkChildren();
        return mZoneContentView.getModel();
    }

    public void setOnControlListener(OnControlListener listener) {
        mOnControlListener = listener;
    }


    public void bindDragDeleteView(View view, DeleteCallback deleteCallback) {
        checkChildren();
        this.mDeleteCallback = deleteCallback;
        mZoneContentView.bindDragDeleteView(view);
    }


    public TableViewBase setSelectState(Long dinnertableId) {
        checkChildren();
        return mZoneContentView.setSelectState(dinnertableId);
    }


    public boolean selectDinnertable(Long dinnertableId) {
        checkChildren();
        return mZoneContentView.selectDinnertable(dinnertableId);
    }


    public void refreshDinnertable(Long dinnertableId) {
        checkChildren();
        mZoneContentView.refreshDinnertable(dinnertableId);
    }


    public void refreshDinnertable() {
        checkChildren();
        mZoneContentView.refreshDinnertable();
    }


    public void refreshSpendTime() {
        checkChildren();
        mZoneContentView.refreshSpendTime();
    }


    public void refresReserveStatus(HashMap<String, BookingTable> bookingMap) {
        checkChildren();
        mZoneContentView.refreshReserveStatus(bookingMap);
    }


    public void refresOpenTableStauts() {
        checkChildren();
        mZoneContentView.refreshOpenTableStatus();
    }

    public int getControlViewId() {
        checkChildren();
        return mControlView.getId();
    }

    private void showControl(TableViewBase tableView) {
                if (!mScroller.isFinished()) {
            return;
        }

                if (tableView.getModel().isEmpty()) {
            int offset = DensityUtil.dip2px(getContext(), OpentablePopWindow.ADD_BUTTON_WIDTH);
            int offset2 = DensityUtil.dip2px(getContext(), OpentablePopWindow.ADD_BUTTON_HEIGHT);

            int tableRight = tableView.getRight();
            int tableTop = tableView.getTop();
            int tableBottom = tableView.getBottom();

            int maxRight = mScrollView.getRight();
            int maxBottom = mScrollView.getBottom();

            int scrollX = mScrollView.getScrollX();
            int scrollY = mScrollView.getScrollY();
            int widthTable = tableView.getWidth();

            if (tableRight - scrollX > maxRight - offset) {                moveDistance(tableRight - scrollX - (maxRight - offset), 0, 0);

            }

            if (tableRight - scrollX < widthTable + offset) {                moveDistance(tableRight - scrollX - (widthTable + offset), 0, 0);
            }

            if (tableTop - scrollY < offset2) {                moveDistance((tableTop - scrollY) - offset2, 1, 100);
            }

            if (tableBottom - scrollY > maxBottom) {                moveDistance(tableBottom - scrollY - maxBottom, 1, 100);
            }


            return;
        }

        if (mShowingControl) {
            return;
        }


        if (mOnControlListener != null) {
            mOnControlListener.onShowControl(tableView.getModel());
        }

                int tableRight = tableView.getRight();
        int maxRight = mScrollView.getRight();
        int scrollX = mScrollView.getScrollX();
        mNeedScroll = (CONSOLE_WIDTH + tableRight - scrollX > maxRight);

        mShowingControl = true;
        mScroller.startScroll(getScrollX(), 0, -CONSOLE_WIDTH, 0, 500);
        int width = mWidth - CONSOLE_WIDTH;
        mScrollView.layout(0, 0, width, mHeight);

        postInvalidate();
    }

    public void hideControl() {
        checkChildren();
        if (!mScroller.isFinished()) {
            return;
        }
        if (!mShowingControl) {
            return;
        }
        mShowingControl = false;
        mScroller.startScroll(getScrollX(), 0, CONSOLE_WIDTH, 0, 500);
        mScrollView.layout(0, 0, mWidth, mHeight);
        postInvalidate();

        if (mOnControlListener != null) {
            mOnControlListener.onHideControl();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mZoneContentView.getModel() != null) {
            int width = Math.max(mWidth, mZoneContentView.getModel().getWidth());
            int height = Math.max(mHeight, mZoneContentView.getModel().getHeight());
            ViewGroup.LayoutParams layoutParams = mZoneContentView.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            mZoneContentView.setLayoutParams(layoutParams);
            mZoneContentView.setMinimumWidth(mWidth);
            mZoneContentView.setMinimumHeight(mHeight);
        }

        super.onLayout(changed, l, t, r, b);

        checkChildren();
        mControlView.layout(-CONSOLE_WIDTH, 0, 0, mHeight);
        int width = mWidth;
        if (mShowingControl) {
            width -= CONSOLE_WIDTH;
        }
        mScrollView.layout(0, 0, width, mHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
            if (mShowingControl && mScroller.isFinished()) {
                post(new Runnable() {

                    @Override
                    public void run() {
                                                if (mNeedScroll) {
                            mScrollView.smoothScrollBy(CONSOLE_WIDTH, 0);
                        }
                    }

                });
            }
        }
    }


    public static interface DeleteCallback {

        void onDragStarted();

        void onEntered(View view);

        void onExited(View view);

        void onDragEnded();
    }

    public ZoneContentView getmZoneContentView() {
        return mZoneContentView;
    }

    public boolean ismShowingControl() {
        return mShowingControl;
    }


    private void moveDistance(final int distance, final int type, long delay) {
        postDelayed(new Runnable() {

            @Override
            public void run() {
                if (type == 0) {
                    mScrollView.smoothScrollBy(distance, 0);
                } else {
                    mScrollView.smoothScrollBy(0, distance);
                }

            }

        }, delay);
    }


    public abstract ZoneContentView getZoneContentView(HVScrollView mScrollView, ZoneContentView.Callback callback);
}
