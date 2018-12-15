package com.zhongmei.bty.dinner.orderdish.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;

/**
 * @Date：2015年11月18日 @Description:
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class SlideListView extends ListView {
    private static final String TAG = SlideListView.class.getSimpleName();

    /**
     * 当前滑动的ListView position
     */
    private int slidePosition;

    private int downY;

    private int downX;

    private int screenWidth;

    private View itemView;

    private Scroller scroller;

    private static final int SNAP_VELOCITY = 600;

    private VelocityTracker velocityTracker;

    /**
     * 是否响应滑动，默认为不响应
     */
    private boolean isSlide = false;

    /**
     * 滑动的最小距离
     */
    private int mTouchSlop;

    private RemoveListener mRemoveListener;

    private RemoveDirection removeDirection;

    private boolean enableSlide = false;

    private boolean isSlideBegin = true;//是否是刚开始滑动

    private boolean isSlideToRight = true;//开始滑动方向

    public enum RemoveDirection {
        RIGHT, LEFT;
    }

    private List<Integer> slidePositionList;

    public SlideListView(Context context) {
        this(context, null);
    }

    public SlideListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // screenWidth =
        // ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        scroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        screenWidth = DensityUtil.dip2px(context, 409);
    }

    public void setRemoveListener(RemoveListener removeListener) {
        this.mRemoveListener = removeListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, "oritation:" + event.getOrientation());
        event.getOrientation();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                addVelocityTracker(event);

                // 假如scroller滚动还没有结束，直接返回
                if (!scroller.isFinished()) {
                    return super.dispatchTouchEvent(event);
                }
                downX = (int) event.getX();
                downY = (int) event.getY();

                slidePosition = pointToPosition(downX, downY);

                // 无效的position, 不做任何处理
                if (slidePosition == AdapterView.INVALID_POSITION) {
                    return super.dispatchTouchEvent(event);
                }

                // 获取点击的item view
                itemView = getChildAt(slidePosition - getFirstVisiblePosition());
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (enableSlide) {
                    if (Math.abs(getScrollVelocity()) > SNAP_VELOCITY || (Math.abs(event.getX() - downX) > mTouchSlop
                            && Math.abs(event.getY() - downY) < mTouchSlop)) {
                        // isSlide = true;
                        if (isSlideBegin) {
                            if (event.getX() > downX) {
                                isSlideToRight = true;
                            } else {
                                isSlideToRight = false;
                            }
                            setSlideLayout(true);
                            isSlideBegin = false;
                        }
                        if (isSlideToRight) {//右滑
                            Log.i(TAG,
                                    "滑动position:" + slidePosition + "getFirstVisiblePosition:" + getFirstVisiblePosition());
                            if (canItemViewSlide(slidePosition)) {
                                Log.i(TAG, "item can slide");
                                isSlide = true;
                            } else {
                                isSlide = false;
                            }

                        } else {
                            isSlide = false;
                        }

                    }
                } else {
                    isSlide = false;
                }

                break;
            }
            case MotionEvent.ACTION_UP:
                setSlideLayout(false);
                recycleVelocityTracker();
                isSlideBegin = true;//初始化开始滑动状态
                isSlideToRight = true;
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private void setSlideLayout(boolean isDown) {
        if (itemView == null) {
            return;
        }
        LinearLayout slideView = (LinearLayout) itemView.findViewById(R.id.slide_layout);
        if (slideView == null) {
            return;
        }
        int itemHeight = itemView.getHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) slideView.getLayoutParams();
        if (isDown) {
            layoutParams.height = itemHeight;
        } else {
            layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        }
        slideView.setLayoutParams(layoutParams);
    }

    private void scrollRight() {
        removeDirection = RemoveDirection.RIGHT;
        final int delta = (screenWidth + itemView.getScrollX());
        scroller.startScroll(itemView.getScrollX(), 0, -delta, 0, Math.abs(delta));
        postInvalidate();
    }

    private void scrollLeft() {
        removeDirection = RemoveDirection.LEFT;
        final int delta = (screenWidth - itemView.getScrollX());
        scroller.startScroll(itemView.getScrollX(), 0, delta, 0, Math.abs(delta));
        postInvalidate();
    }

    private void scrollByDistanceX() {
        if (itemView.getScrollX() >= screenWidth / 3) {
            scrollLeft();
        } else if (itemView.getScrollX() <= -screenWidth / 3) {
            scrollRight();
        } else {
            itemView.scrollTo(0, 0);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isSlide && slidePosition != AdapterView.INVALID_POSITION) {
            addVelocityTracker(ev);
            final int action = ev.getAction();
            int x = (int) ev.getX();
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    int deltaX = downX - x;
                    downX = x;

                    // deltaX大于0向左滚动，小于0向右滚
                    itemView.scrollBy(deltaX, 0);
                    break;
                case MotionEvent.ACTION_UP:
                    int velocityX = getScrollVelocity();
                    if (velocityX > SNAP_VELOCITY) {
                        scrollRight();
                    } else if (velocityX < -SNAP_VELOCITY) {
                        scrollLeft();
                    } else {
                        scrollByDistanceX();
                    }

                    recycleVelocityTracker();
                    isSlide = false;
                    break;
            }

            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (scroller.computeScrollOffset()) {
            // 让ListView item根据当前的滚动偏移量进行滚动
            itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());

            postInvalidate();

            // 滚动动画结束的时候调用回调接口
            if (scroller.isFinished()) {
                if (mRemoveListener == null) {
                    throw new NullPointerException("RemoveListener is null, we should called setRemoveListener()");
                }

                itemView.scrollTo(0, 0);
                mRemoveListener.removeItem(removeDirection, slidePosition);
            }
        }
    }

    private void addVelocityTracker(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }

        velocityTracker.addMovement(event);
    }

    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    private int getScrollVelocity() {
        velocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) velocityTracker.getXVelocity();
        return velocity;
    }

    public interface RemoveListener {
        void removeItem(RemoveDirection direction, int position);
    }

    /**
     * @param position
     * @Date 2015年11月19日
     * @Description: 保存滑动item
     * @Return void
     */
    public void addSlideItems(int position) {
        if (slidePositionList == null) {
            slidePositionList = new ArrayList<Integer>();
        }
        if (!slidePositionList.contains(position)) {
            slidePositionList.add(position);
            Log.i(TAG, "add position:" + position);
        }

    }

    public void removeSlideItems() {
        if (slidePositionList != null) {
            int size = slidePositionList.size();
            for (int i = 0; i < size; i++) {
                slidePositionList.remove(0);
            }
        }
    }

    private boolean canItemViewSlide(int itemPosition) {
        if (slidePositionList == null)
            return false;

        if (slidePositionList.contains(itemPosition)) {
            return true;
        } else {
            return false;
        }

    }

    public void enableSlide(boolean enableSlide) {
        this.enableSlide = enableSlide;
    }

}
