package com.zhongmei.bty.commonmodule.util.manager;

import android.annotation.SuppressLint;
import android.view.KeyEvent;

import java.util.HashMap;

@SuppressLint("UseSparseArrays")
public class ClickManager {
    private static ClickManager sClickManager = new ClickManager();

    private HashMap<Integer, Long> mViewClickedMap = new HashMap<Integer, Long>();

    private Long INTERVAL_TIME = 500L;

    long lastClickTime = 0L;

    private ClickManager() {
    }

    public static ClickManager getInstance() {
        return sClickManager;
    }

    public Long getINTERVAL_TIME() {
        return INTERVAL_TIME;
    }

    public void setINTERVAL_TIME(Long iNTERVAL_TIME) {
        INTERVAL_TIME = iNTERVAL_TIME;
    }

    /**
     * @Title: isClicked
     * @Description: TODO
     * @Param @param resId
     * @Param @return 同一个按键500毫秒内只能点击一次
     * @Return boolean 返回类型
     */
    public boolean isClicked(int resId) {
        long lastClickTime = 0L;
        if (mViewClickedMap.containsKey(resId)) {
            lastClickTime = mViewClickedMap.get(resId);
        }
        long current = System.currentTimeMillis();
        long timeD = current - lastClickTime;
        if (0 < timeD && timeD < INTERVAL_TIME) {
            return true;
        }
        mViewClickedMap.put(resId, current);
        return false;
    }


    /**
     * @Title: isClicked
     * @Description: TODO
     * @Param @param resId
     * @Param @return 同一个按键500毫秒内只能点击一次
     * @Return boolean 返回类型
     */
    public boolean isClicked(int resId, long time) {
        long lastClickTime = 0L;
        if (mViewClickedMap.containsKey(resId)) {
            lastClickTime = mViewClickedMap.get(resId);
        }
        long current = System.currentTimeMillis();
        long timeD = current - lastClickTime;
        if (0 < timeD && timeD < time) {
            return true;
        }
        mViewClickedMap.put(resId, current);
        return false;
    }

    /**
     * @Title: isClicked
     * @Description: 所有点击事件在500毫秒内部能重复点击
     * @Param @return TODO
     * @Return boolean 返回类型
     */
    public boolean isClicked() {
        long current = System.currentTimeMillis();
        long timeD = current - lastClickTime;
        if (0 < timeD && timeD < INTERVAL_TIME) {
            return true;
        }
        lastClickTime = current;
        return false;
    }

    /**
     * 1秒限制
     *
     * @param resId
     * @return
     */
    public boolean isClickedLong(int resId) {
        long lastClickTime = 0;
        if (mViewClickedMap.containsKey(resId)) {
            lastClickTime = mViewClickedMap.get(resId);
        }
        long current = System.currentTimeMillis();
        long timeD = current - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        mViewClickedMap.put(resId, current);
        return false;
    }

    public static boolean isNumber(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_NUMPAD_0 || keyCode == KeyEvent.KEYCODE_NUMPAD_1
                || keyCode == KeyEvent.KEYCODE_NUMPAD_2 || keyCode == KeyEvent.KEYCODE_NUMPAD_3
                || keyCode == KeyEvent.KEYCODE_NUMPAD_4 || keyCode == KeyEvent.KEYCODE_NUMPAD_5
                || keyCode == KeyEvent.KEYCODE_NUMPAD_6 || keyCode == KeyEvent.KEYCODE_NUMPAD_7
                || keyCode == KeyEvent.KEYCODE_NUMPAD_8 || keyCode == KeyEvent.KEYCODE_NUMPAD_9;
    }
}