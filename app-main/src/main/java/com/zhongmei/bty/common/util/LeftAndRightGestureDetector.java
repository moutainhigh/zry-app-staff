package com.zhongmei.bty.common.util;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 左右滑动手势监听
 */
public class LeftAndRightGestureDetector {

    /**
     * 滑动的间隙，当滑动的开始位置和结束位置之间 的距离不小于该值时，这个类才认为是一次滑动事件
     */
    private final int DISTANCE = 150;

    private SimpleGestureListener simpleGestureListener;

    /**
     * 注册监听
     *
     * @param view     需要监听的控件
     * @param listener 手势监听器监听器
     */
    public void registerListener(View view, SimpleGestureListener listener) {
        this.simpleGestureListener = listener;
        final GestureDetector gestureDetector = new GestureDetector(gestureListener);
        view.setLongClickable(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    /**
     * 手势监听
     */
    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        /**
         * up事件
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            //System.out.println("onScroll");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            //System.out.println("onLongPress");
        }

        /**
         * 左右滑动事件
         * MotionEvent e1   可能为null 所以做容错处理
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (simpleGestureListener != null) {
                if (e1 != null && e2 != null) {
                    if (e1.getX() - e2.getX() > DISTANCE && Math.abs(velocityX) > 0) {
                        simpleGestureListener.onLeftFling();
                    } else if (e2.getX() - e1.getX() > DISTANCE && Math.abs(velocityX) > 0) {
                        simpleGestureListener.onRightFling();
                    }
                }
            }
            return false;
        }

        /**
         * Down事件
         */
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }
    };

    public interface SimpleGestureListener {

        /**
         * 当手势向左滑时触发
         */
        void onLeftFling();

        /**
         * 当手势向右滑时触发
         */
        void onRightFling();
    }
}
