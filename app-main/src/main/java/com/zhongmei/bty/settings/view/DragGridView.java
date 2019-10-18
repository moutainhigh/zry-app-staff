package com.zhongmei.bty.settings.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.zhongmei.yunfu.R;

public class DragGridView extends GridView {

    private static final String TAG = "DragGridView";

    private static final int scrollSpeed = 20;

    private long dragRespondTime = 300;


    private int mDownX, mDownY;


    private int moveX, moveY;


    private int mPoint2ItemTop, mPoint2ItemLeft;


    private int mStartUpScrollHeight, mStartDownScrollHeight;


    private int mStatusHeight = 0;


    private int mOffsetTop, mOffsetLeft;


    private int mDragPosition = 0;


    private View mStartDragItemView, moveView;


    private Bitmap mDragBitmap;


    private ImageView mDragImageView;

    WindowManager.LayoutParams winLayoutParams;

    public boolean isDrag() {
        return isDrag;
    }


    private boolean isDrag = false;

    OnItemChangerListener listener;



    private WindowManager windowManager;

    private Handler handler = new Handler();

    private Runnable dragRunnable = new Runnable() {

        @Override
        public void run() {
            isDrag = true;
                                    if (moveView != null)
                moveView.setVisibility(View.INVISIBLE);
                                                createDragImage(mDragBitmap, mDownX, mDownY);
        }

    };

    private Runnable scrollRunnable = new Runnable() {

        @Override
        public void run() {
            int speed = 0;
            if (moveY < mStartDownScrollHeight) {
                speed = -scrollSpeed;
                handler.postDelayed(scrollRunnable, 25);
            } else if (moveY > mStartUpScrollHeight) {
                speed = scrollSpeed;
                handler.postDelayed(scrollRunnable, 25);
            } else {
                speed = 0;
                handler.removeCallbacks(scrollRunnable);
            }

            swapItem(moveX, moveY);
            smoothScrollBy(speed, 10);
        }
    };

    public DragGridView(Context context) {
        this(context, null);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mStatusHeight = getStatusHeight(context);
    }

    @Override
    protected void onDetachedFromWindow() {
        this.removeDragImage();
        super.onDetachedFromWindow();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();

                mDragPosition = pointToPosition(mDownX, mDownY);

                if (mDragPosition == AdapterView.INVALID_POSITION) {
                    return super.dispatchTouchEvent(ev);
                }

                mStartDragItemView = getChildAt(mDragPosition
                        - getFirstVisiblePosition());
                moveView = mStartDragItemView.findViewById(R.id.pay_method_item_layout);
                if (moveView != null) {
                    moveView.setSelected(true);
                }
                                handler.postDelayed(dragRunnable, dragRespondTime);

                                mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
                mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();
                                mOffsetLeft = (int) ev.getRawX() - mDownX;
                mOffsetTop = (int) ev.getRawY() - mDownY;

                                mStartDownScrollHeight = getHeight() / 4;
                                mStartUpScrollHeight = getHeight() * 3 / 4;
                if (moveView != null) {
                                        moveView.setDrawingCacheEnabled(true);
                                        mDragBitmap = Bitmap.createBitmap(moveView
                            .getDrawingCache());
                                        moveView.destroyDrawingCache();
                }
            }
            break;

            case MotionEvent.ACTION_UP: {
                if (moveView != null) {
                    moveView.setSelected(false);
                    moveView.setPressed(false);
                }
                handler.removeCallbacks(dragRunnable);
                handler.removeCallbacks(scrollRunnable);
            }

            break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (isDrag && mDragImageView != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                }
                break;

                case MotionEvent.ACTION_UP: {
                    isDrag = false;
                    onStopDrag();
                }
                break;

                case MotionEvent.ACTION_MOVE: {
                    moveX = (int) ev.getX();
                    moveY = (int) ev.getY();
                    updateDragImage(moveX, moveY);

                }
                break;
                default:
                    break;
            }
        }

        return super.onTouchEvent(ev);
    }


    private void createDragImage(Bitmap mDragBitmap, int mDownX, int mDownY) {

        winLayoutParams = new WindowManager.LayoutParams();
                winLayoutParams.format = PixelFormat.TRANSLUCENT;
        winLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;

        winLayoutParams.x = mDownX - mPoint2ItemLeft + mOffsetLeft;
        winLayoutParams.y = mDownY - mPoint2ItemTop + mOffsetTop
                - mStatusHeight;
        winLayoutParams.alpha = 0.55f;
        winLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        winLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        winLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(mDragBitmap);
                windowManager.addView(mDragImageView, winLayoutParams);

    }


    private void onStopDrag() {
                View view = getChildAt(mDragPosition - getFirstVisiblePosition());
        if (view != null) {
            view.findViewById(R.id.pay_method_item_layout).setVisibility(View.VISIBLE);
        }
        if (moveView != null) {
            moveView.setVisibility(View.VISIBLE);
        }
                ((BaseAdapter) this.getAdapter()).notifyDataSetChanged();
                removeDragImage();
    }


    private void removeDragImage() {
        if (mDragImageView != null) {
            windowManager.removeView(mDragImageView);
            mDragImageView = null;
        }
    }


    private void updateDragImage(int moveX, int moveY) {
        winLayoutParams.x = moveX - mPoint2ItemLeft + mOffsetLeft;
        winLayoutParams.y = moveY - mPoint2ItemTop + mOffsetTop - mStatusHeight;
        windowManager.updateViewLayout(mDragImageView, winLayoutParams);

                swapItem(moveX, moveY);
        handler.post(scrollRunnable);
    }


    private void swapItem(int moveX2, int moveY2) {
        int tempPositon = pointToPosition(moveX2, moveY2);
        if (mDragPosition != tempPositon
                && tempPositon != AdapterView.INVALID_POSITION) {

                        if (listener != null) {
                listener.onChange(mDragPosition, tempPositon);
            }

            getChildAt(mDragPosition - getFirstVisiblePosition())
                    .findViewById(R.id.pay_method_item_layout).setVisibility(View.VISIBLE);
            getChildAt(tempPositon - getFirstVisiblePosition()).findViewById(R.id.pay_method_item_layout).setVisibility(
                    View.INVISIBLE);

            mDragPosition = tempPositon;
        }
    }


    public static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect rect = new Rect();
        ((Activity) context).getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(rect);
        statusHeight = rect.top;
        if (statusHeight == 0) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return statusHeight;
    }


    public void setOnItemChangeListener(OnItemChangerListener listener) {
        this.listener = listener;
    }


    public interface OnItemChangerListener {
        public void onChange(int from, int to);
    }

}
