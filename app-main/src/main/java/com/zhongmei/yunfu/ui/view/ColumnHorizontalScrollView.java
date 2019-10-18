package com.zhongmei.yunfu.ui.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.util.DensityUtil;

public class ColumnHorizontalScrollView extends HorizontalScrollView {

    public static final float ITEM_IMAGE_SCALE = 1.4f;
    public static final float ITEM_TEXT_SCALE = 1.3f;

    private int lx;

    private int currentItem;

    private int last;

    private boolean flag;

    private int itemWidth;
        private int lastItem;
        private int childCount = 0;

    private int middle = 2;

    private UserChangeListener listener;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if (getScrollX() == lx && !flag) {

                        setLast();

                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };

    public ColumnHorizontalScrollView(Context context) {
        super(context, null);
    }

    public ColumnHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public ColumnHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }

    public void setListener(UserChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                flag = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                flag = false;
                handler.sendEmptyMessageDelayed(0, 16);
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void performClick(int id) {
        smoothScrollBy(itemWidth * (id - currentItem - middle), 0);
    }

    @Override
    protected void onScrollChanged(int currentHScrooll, int paramInt2, int paramInt3, int paramInt4) {
        initChild();
    }

    private void setLast() {
        if (last > itemWidth / 2) {
            smoothScrollBy(itemWidth - last, 0);
        } else {
            smoothScrollBy(-last, 0);
        }
        currentItem = Math.round(getScrollX() / Float.valueOf(itemWidth));
        if (currentItem != lastItem) {
            listener.userChanged();
        }
        lastItem = currentItem;
    }

    public void setParam(int itemWidth, int middle, int count) {
        this.itemWidth = itemWidth;
        this.middle = middle;
        lastItem = middle;
        childCount = count;
    }

    public void initChild() {
        if (itemWidth <= 0) {
            return;
        }
        currentItem = getScrollX() / itemWidth;
        last = getScrollX() % itemWidth;
        for (int i = 0; i < childCount; i++) {
            ViewGroup v1 = (ViewGroup) ((ViewGroup) getChildAt(0)).getChildAt(i);
            TextView v = (TextView) v1.getChildAt(0);
            View v2 = v1.getChildAt(1);
            if (i == currentItem + middle) {
                                                                v.setTextSize(TypedValue.COMPLEX_UNIT_PX, getItemTextSize() * (ITEM_TEXT_SCALE - 0.6f * last / Float.valueOf(itemWidth * 2)));
                v2.setScaleX(ITEM_IMAGE_SCALE - last / Float.valueOf(itemWidth * 2));
                v2.setScaleY(ITEM_IMAGE_SCALE - last / Float.valueOf(itemWidth * 2));
            } else if (i == currentItem + middle + 1) {
                                                v.setTextSize(TypedValue.COMPLEX_UNIT_PX, getItemTextSize() * (1 + 0.6f * last / Float.valueOf(itemWidth * 2)));
                v2.setScaleX(1 + last / Float.valueOf(itemWidth * 2));
                v2.setScaleY(1 + last / Float.valueOf(itemWidth * 2));
            } else {
                                                v.setTextSize(TypedValue.COMPLEX_UNIT_PX, getItemTextSize());
                                                v2.setScaleX(1f);
                v2.setScaleY(1f);
            }

        }
        lx = getScrollX();
        handler.removeMessages(0);
        handler.sendEmptyMessageDelayed(0, 16);

    }

    public static int getItemTextSize() {
        return DensityUtil.sp2px(MainApplication.getInstance(), 15);
    }

    @Override
    public void fling(int velocityX) {
        super.fling(velocityX / 2);
    }

    public int getCurrentUserItem() {
        return currentItem;

    }

    public void goNext() {
        if (currentItem + 1 < childCount) {
            currentItem += 1;
            smoothScrollBy(itemWidth, 0);
        }
    }

    public void goLast() {
        if (currentItem - 1 >= 0) {
            currentItem -= 1;
            smoothScrollBy(-itemWidth, 0);
        }
    }


    public void goToPosition(int position) {
        if (position > currentItem) {
            smoothScrollBy(itemWidth * (position - currentItem), 0);
        } else {
            smoothScrollBy(-itemWidth * (currentItem - position), 0);
        }
        currentItem = position;

    }

    interface UserChangeListener {
        void userChanged();
    }
}
