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

/**
 * 桌台区域视图，含操控板与双向滚动
 *
 * @version: 1.0
 * @date 2015年9月7日
 */
public abstract class ZoneView extends LinearLayout {

    private static final int CONSOLE_WIDTH = DensityUtil.dip2px(MainApplication.getInstance(), 409);

    private final Scroller mScroller;        // 用于控制操控板
    protected final HVScrollView mScrollView;    // 用于使桌台区域内容可以双向滚动
    private ZoneContentView mZoneContentView;

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
        // width一定要为0
        mScrollView.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT));
        addView(mScrollView);
        int paddingLeft = DensityUtil.dip2px(context, OpentablePopWindow.ADD_BUTTON_WIDTH);
        int paddingTop = DensityUtil.dip2px(context, OpentablePopWindow.ADD_BUTTON_HEIGHT);
        //设置padding防止开台弹框被截
//		mScrollView.setPadding(paddingLeft,paddingTop,paddingLeft,0);

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

    /**
     * 设置接受拖放事件的view
     *
     * @param view
     * @param deleteCallback
     */
    public void bindDragDeleteView(View view, DeleteCallback deleteCallback) {
        checkChildren();
        this.mDeleteCallback = deleteCallback;
        mZoneContentView.bindDragDeleteView(view);
    }

    /**
     * 设置桌台为选中状态
     *
     * @param dinnertableId
     */
    public TableViewBase setSelectState(Long dinnertableId) {
        checkChildren();
        return mZoneContentView.setSelectState(dinnertableId);
    }

    /**
     * 选中指定的桌台，选中成功返回true
     *
     * @param dinnertableId
     * @return
     */
    public boolean selectDinnertable(Long dinnertableId) {
        checkChildren();
        return mZoneContentView.selectDinnertable(dinnertableId);
    }

    /**
     * 刷新指定ID的桌台
     *
     * @param dinnertableId
     */
    public void refreshDinnertable(Long dinnertableId) {
        checkChildren();
        mZoneContentView.refreshDinnertable(dinnertableId);
    }

    /**
     * 刷新所有桌台
     */
    public void refreshDinnertable() {
        checkChildren();
        mZoneContentView.refreshDinnertable();
    }

    /**
     * 刷新所有桌台单据上展示的耗时
     */
    public void refreshSpendTime() {
        checkChildren();
        mZoneContentView.refreshSpendTime();
    }

    /**
     * 刷新所有桌台单据上展示的耗时
     */
    public void refresReserveStatus(HashMap<String, BookingTable> bookingMap) {
        checkChildren();
        mZoneContentView.refreshReserveStatus(bookingMap);
    }

    /**
     * 刷新所有桌台单据上展示的耗时
     */
    public void refresOpenTableStauts() {
        checkChildren();
        mZoneContentView.refreshOpenTableStatus();
    }

    public int getControlViewId() {
        checkChildren();
        return mControlView.getId();
    }

    private void showControl(TableViewBase tableView) {
        // 未清台的不响应点击
        if (!mScroller.isFinished()) {
            return;
        }

        //空桌台不显示开台信息页
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

            if (tableRight - scrollX > maxRight - offset) {//右边遮挡，向左移动
                moveDistance(tableRight - scrollX - (maxRight - offset), 0, 0);

            }

            if (tableRight - scrollX < widthTable + offset) {//左边遮挡，向右移动
                moveDistance(tableRight - scrollX - (widthTable + offset), 0, 0);
            }

            if (tableTop - scrollY < offset2) {//上边遮挡，向下移动
                moveDistance((tableTop - scrollY) - offset2, 1, 100);
            }

            if (tableBottom - scrollY > maxBottom) {//底部遮挡，向上移动
                moveDistance(tableBottom - scrollY - maxBottom, 1, 100);
            }


            return;
        }

        if (mShowingControl) {
            return;
        }


        if (mOnControlListener != null) {
            mOnControlListener.onShowControl(tableView.getModel());
        }

        // 判断展现后此桌台是否还处于可视范围内，如果不在可视范围内在展开后滚动视图
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
//		invalidate();
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
                        // 滚动视图使选中的桌台在可视范围内
                        if (mNeedScroll) {
                            mScrollView.smoothScrollBy(CONSOLE_WIDTH, 0);
                        }
                    }

                });
            }
        }
    }

    /**
     * @version: 1.0
     * @date 2015年9月10日
     */
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

    /**
     * @Date 2016/7/29
     * @Description:平移距离
     * @Param type 0:水平移动，type 1：垂直移动
     * @Return
     */
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
