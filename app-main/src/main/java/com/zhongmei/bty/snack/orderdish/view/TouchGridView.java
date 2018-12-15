package com.zhongmei.bty.snack.orderdish.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.util.DensityUtil;

public class TouchGridView extends GridView {
    private int downPostion;

    private int upPostion = -1;

    private int scrollYDown;

    private int scrollYUp;

    private ItemTouchListener listener;

    private long downTime;

    private long upTime;

    public ItemTouchListener getListener() {
        return listener;
    }

    public void setListener(ItemTouchListener listener) {
        this.listener = listener;
    }

    public TouchGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TouchGridView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public TouchGridView(Context context) {
        super(context, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPostion = pointToPosition((int) ev.getX(), (int) ev.getY());
                downTime = System.currentTimeMillis();
                scrollYDown = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                upPostion = pointToPosition((int) ev.getX(), (int) ev.getY());
                upTime = System.currentTimeMillis();
                scrollYUp = (int) ev.getY();
                break;
            default:
                break;
        }
        if (listener != null && upTime - downTime < 300 && upPostion != -1 && downPostion == upPostion
                && Math.abs(scrollYUp - scrollYDown) < DensityUtil.dip2px(MainApplication.getInstance(), 15)) {
            listener.onTouch(upPostion);
        }
        upPostion = -1;
        return super.onTouchEvent(ev);
    }

    public interface ItemTouchListener {
        void onTouch(int postion);
    }

    @Override
    public boolean isInTouchMode() {
        // TODO Auto-generated method stub
        return !(hasFocus() && !super.isInTouchMode());
    }
}
