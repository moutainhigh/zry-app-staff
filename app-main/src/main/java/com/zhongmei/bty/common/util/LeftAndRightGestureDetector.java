package com.zhongmei.bty.common.util;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class LeftAndRightGestureDetector {


    private final int DISTANCE = 150;

    private SimpleGestureListener simpleGestureListener;


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


    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {

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
                        return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
                    }


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


        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }
    };

    public interface SimpleGestureListener {


        void onLeftFling();


        void onRightFling();
    }
}
