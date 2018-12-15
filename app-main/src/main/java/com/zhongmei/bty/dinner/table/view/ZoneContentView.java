package com.zhongmei.bty.dinner.table.view;

import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;

import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTradeMoveDish;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 桌台区域内容视图，不带滚动功能
 *
 * @version: 1.0
 * @date 2015年9月6日
 */
@SuppressWarnings("deprecation")
public abstract class ZoneContentView extends ZoneContentViewBase {

    private static final String TAG = ZoneContentView.class.getSimpleName();

    /**
     * @version: 1.0
     * @date 2015年9月10日
     */
    public static interface Callback extends ZoneView.DeleteCallback {

        void onClick(TableViewBase view);

        void onDrop(IDinnertableTrade orginal, TableViewBase dest);

        void onDrop(IDinnertableTrade orginal, TableTradeViewBase dest);

        void onDropMoveDish(IDinnertableTradeMoveDish orginal, TableViewBase dest);

        void onDropMoveDish(IDinnertableTradeMoveDish orginal, TableTradeViewBase dest);

        void onDelete(IDinnertableTrade orginal);


    }

    private final DragHoverHelper mDragHoverHelper;
    private final Callback mCallback;
    private final TradesSelector mTradesSelector;
    private View mDeleteView;

    public ZoneContentView(HVScrollView scrollView, Callback callback) {
        super(scrollView);
        mDragHoverHelper = new DragHoverHelper();
        mCallback = callback;
        mTradesSelector = new TradesSelector(getContext(), mOnDragListener);
        addView(mTradesSelector);
        mTradesSelector.setVisibility(View.INVISIBLE);

        setOnDragListener(mOnDragListener);
        setOnClickListener(mOnClickListener);
    }

    void bindDragDeleteView(View view) {
        mDeleteView = view;
        mDeleteView.setOnDragListener(mOnDragListener);
    }

    @Override
    public OnDragListener getOnDragListener() {
        return mOnDragListener;
    }

    @Override
    public OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

//	@Override
//	protected DinnertableView createDinnertableView(DinnertableModel dinnertableModel) {
//		DinnertableView view = super.createDinnertableView(dinnertableModel);
//		view.setOnDragListener(mOnDragListener);
//		view.setOnClickListener(mOnClickListener);
//		return view;
//	}


    @Override
    protected void removeDinnertableView(TableViewBase view) {
        view.setOnDragListener(null);
        view.setOnClickListener(null);
        super.removeDinnertableView(view);
    }


    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view instanceof TableViewBase) {
                TableViewBase tableView = (TableViewBase) view;
                mCallback.onClick(tableView);
            }
        }
    };

    private OnDragListener mOnDragListener = new OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent event) {
            return handleDragEvent(view, event);
        }
    };

    private boolean handleDragEvent(View view, DragEvent event) {
        try {
            Object localState = event.getLocalState();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (view == ZoneContentView.this) {
                        mDragHoverHelper.onCancelHover();
                    }
                    boolean b = event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
                    if (b) {
                        if (isDragCard(localState)) {
                            mCallback.onDragStarted();
                        }

                    }
                    return b;

                case DragEvent.ACTION_DRAG_ENTERED:
                    if (view instanceof TradeLayout) {
                        ((TradeLayout) view).onEntered();
                    } else {
                        mDragHoverHelper.onEntered(view);
                    }
                    if (mDeleteView == view) {
                        if (isDragCard(localState)) {
                            mCallback.onEntered(view);
                        }

                    }
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    if (!(view instanceof TradeLayout)) {
                        mDragHoverHelper.onHover(view, event);
                    }
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    if (view instanceof TradeLayout) {
                        ((TradeLayout) view).onExited();
                    } else {
                        mDragHoverHelper.onExited(view);
                    }
                    if (mDeleteView == view) {
                        if (isDragCard(localState)) {
                            mCallback.onExited(view);
                        }

                    }
                    return true;

                case DragEvent.ACTION_DROP:
                    Log.i("zhubo", "ACTION_DROP");
                    if (view != ZoneContentView.this) {
//						Object localState = event.getLocalState();
                        if (isDragCard(localState)) {
                            IDinnertableTrade orginal = getDragTradeModel(localState);
                            if (orginal.getDinnertable() != null) {
                                if (view instanceof TableViewBase) {
                                    TableViewBase dest = (TableViewBase) view;
                                    // 源桌台与目标桌台相同时不响应拖放换桌
                                    if (!orginal.getDinnertable().getId().equals(dest.getModel().getId())) {
                                        mCallback.onDrop(orginal, dest);
                                    }
                                } else if (view instanceof TradeLayout) {
                                    TradeLayout tradeLayout = (TradeLayout) view;
                                    TableTradeViewBase dest = tradeLayout.mTradeView;
                                    // 源单据与目标单据相同时不响应拖放合单
                                    if (!orginal.equals(dest.getModel())) {
                                        mCallback.onDrop(orginal, dest);
                                    }
                                } else if (view == mDeleteView) {
                                    if (isDragCard(localState)) {
                                        mCallback.onDelete(orginal);
                                    }

                                }
                                return true;
                            }
                        } else if (localState instanceof MovedishDragView) {
                            MovedishDragView movedishDragView = (MovedishDragView) localState;
                            IDinnertableTradeMoveDish orginal = movedishDragView.getModel();
                            if (orginal.getDinnertable() != null) {
                                if (view instanceof TableViewBase) {
                                    TableViewBase dest = (TableViewBase) view;
                                    if (dest.getDinnertableStatus() == DinnertableStatus.DONE) {
                                        ToastUtil.showShortToast(R.string.dinner_move_dish_to_done_dinnertable);
                                    } else {
                                        // 允许移菜到同一个桌子
                                        mCallback.onDropMoveDish(orginal, dest);
                                    }

                                } else if (view instanceof TradeLayout) {
                                    TradeLayout tradeLayout = (TradeLayout) view;
                                    TableTradeViewBase dest = tradeLayout.mTradeView;
                                    // 源单据与目标单据相同时不响应拖放合单
                                    if (!orginal.getTradeId().equals(dest.getModel().getTradeId())) {
                                        mCallback.onDropMoveDish(orginal, dest);
                                    } else {
                                        ToastUtil.showShortToast(R.string.dinner_move_dish_to_sametrade);
                                    }
                                }
                                return true;
                            }
                        }
                    }
                    return false;

                case DragEvent.ACTION_DRAG_ENDED:
                    Log.i("zhubo", "ACTION_DRAG_ENDED");
                    if (view instanceof TradeLayout) {
                        ((TradeLayout) view).onExited();
                    } else {
                        mDragHoverHelper.onCancelHover();
                    }
                    if (mDeleteView == view) {
                        if (isDragCard(localState)) {
                            mCallback.onDragEnded();
                        }

                    }
                    return true;

                default:
                    Log.e(TAG, "Unknown action type received by OnDragListener.");
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    /**
     * 卡片是否可以拖动
     *
     * @param viewObject
     * @return
     */
    private boolean isDragCard(Object viewObject) {
        return viewObject instanceof TableTradeViewBase || viewObject instanceof UnionTradeGroupView;
    }

    private IDinnertableTrade getDragTradeModel(Object viewObject) {
        if (viewObject instanceof TableTradeViewBase) {
            return ((TableTradeViewBase) viewObject).getModel();
        }

        if (viewObject instanceof UnionTradeGroupView) {
            return ((UnionTradeGroupView) viewObject).getModel();
        }

        return null;
    }

    /**
     * @version: 1.0
     * @date 2015年9月6日
     */
    private class DragHoverHelper {

        private static final int DINNERTABLE_TIMEOUT_MS = 400;
        private static final int MARGIN_TIMEOUT_MS = 300;
        private static final int SCROLL_SETP_VAULE = 20;

        private TableViewBase hoverDinnertableView;
        private Long hoverTimeMillis = null;

        private int hoverMarginX;
        private int hoverMarginY;
        private Long marginTimeMillis = null;
        private boolean allowScroll = false;

        private Runnable showSelectorRunnable = new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "show selector..." + hoverTimeMillis);
                if (hoverTimeMillis == null || allowScroll) {
                    return;
                }
                if (System.currentTimeMillis() - hoverTimeMillis > DINNERTABLE_TIMEOUT_MS) {
                    int scrollRight = mScrollView.getScrollX() + mScrollView.getWidth();
                    int contentRight = ZoneContentView.this.getScrollX() + ZoneContentView.this.getWidth();
                    int parentWidth = Math.min(contentRight, scrollRight);
                    mTradesSelector.show(hoverDinnertableView, parentWidth);
                }
            }
        };

        private Runnable allowScrollRunnable = new Runnable() {

            @Override
            public void run() {
                if (marginTimeMillis == null || mTradesSelector.isShown()) {
                    return;
                }
                if (System.currentTimeMillis() - marginTimeMillis > MARGIN_TIMEOUT_MS) {
                    allowScroll = true;
                    Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {100, 100};
                    vibrator.vibrate(pattern, -1);
                }
            }
        };

        /**
         * 移到边界悬停一段时间后滚动视图
         *
         * @param view
         * @param event
         */
        public void onHover(View view, DragEvent event) {
            int eventX = Math.round(event.getX());
            int eventY = Math.round(event.getY());
            // 单据选取器如果已经显示，在桌台到选取器之间的区域移动时不隐藏单据选取器
            if (mTradesSelector.isShown()) {
                if (view != hoverDinnertableView) {
                    int left;
                    int right;
                    if (mTradesSelector.getX() < hoverDinnertableView.getX()) {
                        // 单据选取器在桌台左边
                        left = Math.round(mTradesSelector.getX());
                        right = hoverDinnertableView.getLeft() + 1;
                    } else {
                        left = hoverDinnertableView.getRight() - 1;
                        right = left + mTradesSelector.getWidth();
                    }
                    int top = Math.round(mTradesSelector.getY());
                    int bottom = top + mTradesSelector.getHeight();
                    Rect rect = new Rect(left, top, right, bottom);
                    if (!rect.contains(eventX, eventY)) {
                        stopDinnertableHover();
                    }
                }
            } else {
                int x = eventX;
                int y = eventY;
                if (view instanceof TableViewBase) {
                    startDinnertableHover((TableViewBase) view);
                    x += view.getX();
                    y += view.getY();
                }
                int marginWidth = 0;
                int marginHeight = 0;
                Object localState = event.getLocalState();
                if (localState instanceof View) {
                    View dropView = (View) localState;
                    marginWidth = dropView.getWidth();
                    marginHeight = dropView.getHeight();
                }
                marginWidth += SCROLL_SETP_VAULE;
                marginHeight += SCROLL_SETP_VAULE;
                int[] scrollValues = computeScrollValues(x, y, marginWidth, marginHeight);
                if (scrollValues == null) {
                    stopMarginHover();
                } else {
                    if (marginTimeMillis == null) {
                        startMarginHover(x, y);
                    } else if (allowScroll) {
                        Log.i(TAG, "scrollBy: x=" + scrollValues[0] + ", y=" + scrollValues[1]);
                        mScrollView.scrollBy(scrollValues[0], scrollValues[1]);
                    } else {
                        if (Math.abs(hoverMarginX - x) > 10 || Math.abs(hoverMarginY - y) > 10) {
                            Log.i(TAG, "cancel hover: x=" + x + ", y=" + y);
                            stopMarginHover();
                        }
                    }
                }
            }
        }

        public void onEntered(View view) {
            if (view == ZoneContentView.this) {
                stopDinnertableHover();
            } else if (view instanceof TableViewBase) {
                startDinnertableHover((TableViewBase) view);
            }
        }

        public void onExited(View view) {
            if (view instanceof TableViewBase && !mTradesSelector.isShown()) {
                stopDinnertableHover();
            }
        }

        public void onCancelHover() {
            stopDinnertableHover();
            stopMarginHover();
        }

        private void startDinnertableHover(TableViewBase view) {
            /*if (view instanceof BuffetTableView) {//自助餐不允许合单
                return;
            }*/
            if (hoverDinnertableView != view) {
                if (hoverDinnertableView != null) {
                    stopDinnertableHover();
                }
                hoverTimeMillis = System.currentTimeMillis();
                hoverDinnertableView = view;
                if (!hoverDinnertableView.getModel().isEmpty()) {
                    postDelayed(showSelectorRunnable, DINNERTABLE_TIMEOUT_MS + 50);
                }
            }
        }

        private void stopDinnertableHover() {
            removeCallbacks(showSelectorRunnable);
            mTradesSelector.hide();
            hoverTimeMillis = null;
            hoverDinnertableView = null;
        }

        private void startMarginHover(int x, int y) {
            hoverMarginX = x;
            hoverMarginY = y;
            marginTimeMillis = System.currentTimeMillis();
            allowScroll = false;
            Log.i(TAG, "mHoverX=" + hoverMarginX + ", mHoverY=" + hoverMarginY);
            postDelayed(allowScrollRunnable, MARGIN_TIMEOUT_MS + 50);
        }

        private void stopMarginHover() {
            marginTimeMillis = null;
            allowScroll = false;
            removeCallbacks(allowScrollRunnable);
        }

        private int[] computeScrollValues(int x, int y, int marginWidth, int marginHeight) {
            int scrollWidth = mScrollView.getWidth();
            int scrollHeight = mScrollView.getHeight();
            int scrollX = mScrollView.getScrollX();
            int scrollY = mScrollView.getScrollY();
            int viewWidth = scrollX + scrollWidth;
            int viewHeight = scrollY + scrollHeight;
            int left = scrollX + marginWidth;
            int top = scrollY + marginHeight;
            int right = viewWidth - marginWidth;
            int bottom = viewHeight - marginHeight;
            Rect rect = new Rect(left, top, right, bottom);

            int deltaX = 0;
            int deltaY = 0;
            if (x < rect.left && scrollX > 0) {
                deltaX = -SCROLL_SETP_VAULE;
            } else if (x > rect.right && viewWidth < getWidth()) {
                deltaX = SCROLL_SETP_VAULE;
            }
            if (y < rect.top && scrollY > 0) {
                deltaY = -SCROLL_SETP_VAULE;
            } else if (y > rect.bottom && viewHeight < getHeight()) {
                deltaY = SCROLL_SETP_VAULE;
            }
            if (deltaX != 0 || deltaY != 0) {
                return new int[]{deltaX, deltaY};
            }
            return null;
        }

    }

    /**
     * @version: 1.0
     * @date 2015年9月9日
     */
    private static class TradesSelector extends LinearLayout {

        private final List<TradeLayout> tradeLayouts;

        TradesSelector(Context context, OnDragListener onDragListener) {
            super(context);
            setOrientation(LinearLayout.HORIZONTAL);
            setBackground(false);

            tradeLayouts = new ArrayList<TradeLayout>();
            for (int i = 0; i < 4; i++) {
                TradeLayout tradeLayout = new TradeLayout(getContext());
                tradeLayout.setModel(null);
                tradeLayout.setOnDragListener(onDragListener);
                tradeLayouts.add(tradeLayout);
                addView(tradeLayout);
            }
        }

        void show(TableViewBase tableView, int parentWidth) {
            List<IDinnertableTrade> trades = tableView.getModel().getDinnertableTrades();
            int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            int width = 25;
            for (int i = tradeLayouts.size() - 1; i >= 0; i--) {
                TradeLayout tradeLayout = tradeLayouts.get(i);
                if (i >= trades.size()) {
                    tradeLayout.setModel(null);
                    tradeLayout.setVisibility(View.GONE);
                } else {
                    IDinnertableTrade tradeModel = trades.get(i);
                    tradeLayout.setModel(tradeModel);
                    tradeLayout.setVisibility(View.VISIBLE);
                    tradeLayout.measure(widthMeasureSpec, heightMeasureSpec);
                    width += tradeLayout.getMeasuredWidth();
                }
            }

            // 在适当位置显示
            float dinnertableX = tableView.getX();
            float dinnertableY = tableView.getY();
            float x = dinnertableX + tableView.getWidth();
            if (x + width > parentWidth) {
                x = dinnertableX - width;
                setBackground(true);
            } else {
                setBackground(false);
            }

            AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(
                    width, tableView.getHeight(), 0, 0);
            setLayoutParams(layoutParams);
            setX(x);
            setY(dinnertableY);
            setVisibility(View.VISIBLE);
            bringToFront();
        }

        void hide() {
            setX(-getWidth());
            setBackground(false);
            setVisibility(View.INVISIBLE);
        }

        private void setBackground(boolean left) {
            if (left) {
                setPadding(5, 5, 20, 5);
                setBackgroundResource(R.drawable.dinnertable_selector_left);
            } else {
                setPadding(20, 5, 5, 5);
                setBackgroundResource(R.drawable.dinnertable_selector_right);
            }
        }
    }

    /**
     * @version: 1.0
     * @date 2015年9月8日
     */
    private static class TradeLayout extends LinearLayout {

        final TableTradeViewBase mTradeView;

        TradeLayout(Context context) {
            super(context);

            setOrientation(LinearLayout.HORIZONTAL);
            setBackgroundColor(ViewUtils.COLOR_TRADES_SELECTOR_BACKGROUND);
            setPadding(10, 10, 10, 10);

            LayoutParams layoutParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            setLayoutParams(layoutParams);

            mTradeView = ViewUtils.inflateTradeView(getContext());
            LayoutParams layoutParams2 = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams2.gravity = Gravity.CENTER_VERTICAL;
            mTradeView.setLayoutParams(layoutParams2);
            mTradeView.setEnabledDrag(false);
            mTradeView.enablePreCashDisplay(true);
            addView(mTradeView);
        }

        void setModel(IDinnertableTrade tradeModel) {
            mTradeView.setModel(tradeModel);
        }

        void onEntered() {
            setBackgroundColor(ViewUtils.COLOR_TRADES_SELECTOR_BACKGROUND_ENTERED);
        }

        void onExited() {
            setBackgroundColor(ViewUtils.COLOR_TRADES_SELECTOR_BACKGROUND);
        }

    }

    public OnClickListener getmOnClickListener() {
        return mOnClickListener;
    }
}
