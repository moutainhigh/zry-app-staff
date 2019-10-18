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


public class SlideListView extends ListView {
    private static final String TAG = SlideListView.class.getSimpleName();


    private int slidePosition;

    private int downY;

    private int downX;

    private int screenWidth;

    private View itemView;

    private Scroller scroller;

    private static final int SNAP_VELOCITY = 600;

    private VelocityTracker velocityTracker;


    private boolean isSlide = false;


    private int mTouchSlop;

    private RemoveListener mRemoveListener;

    private RemoveDirection removeDirection;

    private boolean enableSlide = false;

    private boolean isSlideBegin = true;
    private boolean isSlideToRight = true;
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

                                if (!scroller.isFinished()) {
                    return super.dispatchTouchEvent(event);
                }
                downX = (int) event.getX();
                downY = (int) event.getY();

                slidePosition = pointToPosition(downX, downY);

                                if (slidePosition == AdapterView.INVALID_POSITION) {
                    return super.dispatchTouchEvent(event);
                }

                                itemView = getChildAt(slidePosition - getFirstVisiblePosition());
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (enableSlide) {
                    if (Math.abs(getScrollVelocity()) > SNAP_VELOCITY || (Math.abs(event.getX() - downX) > mTouchSlop
                            && Math.abs(event.getY() - downY) < mTouchSlop)) {
                                                if (isSlideBegin) {
                            if (event.getX() > downX) {
                                isSlideToRight = true;
                            } else {
                                isSlideToRight = false;
                            }
                            setSlideLayout(true);
                            isSlideBegin = false;
                        }
                        if (isSlideToRight) {                            Log.i(TAG,
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
                isSlideBegin = true;                isSlideToRight = true;
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
                if (scroller.computeScrollOffset()) {
                        itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());

            postInvalidate();

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
