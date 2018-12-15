package com.zhongmei.bty.dinner.table.view;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.dinner.table.bean.UserHeaderBean;

/**
 * @Date：2015年9月24日
 * @Description:垂直滑动控件
 * @Version: 1.0
 */
public class VerticalSlideView extends ScrollView {
    private int lx;

    private int currentItem;

    private int last;

    private boolean flag;//是否时滑动状态

    private int itemWidth;

    private int lastItem;

    private int realHeight, itemHeight;

    private Context context;

    private int scrollTo = 2;//2

    private UserChangeListener listener;

    private LinearLayout mContentLL;

    Activity activity;

    List<UserHeaderBean> headBeans;// 头像数据

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if (getScrollY() == lx && !flag) {
                        setLast();
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };

    public VerticalSlideView(Context context) {
        this(context, null, 0);
    }

    public VerticalSlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalSlideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        lastItem = 2;
        initial(context);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * @param listener
     * @Date 2015年9月24日
     * @Description: 监听响应
     * @Return void
     */
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
                handler.sendEmptyMessageDelayed(0, 0);
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * @param id
     * @Date 2015年9月24日
     * @Description: 点击处理
     * @Return void
     */
    public void performClick(int id) {
        smoothScrollBy(0, itemHeight * (id - currentItem - 2));
    }

    @Override
    protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        initChild();
    }

    private void setLast() {
        if (last > itemHeight / 2) {
            smoothScrollBy(0, itemHeight - last);
        } else {
            smoothScrollBy(0, -last);
        }
        currentItem = Math.round(getScrollY() / Float.valueOf(itemHeight));
        if (currentItem != lastItem) {
            UserHeaderBean userHeaderBean = headBeans.get(currentItem);
            listener.userChanged(userHeaderBean.getName(), userHeaderBean.getId());
        }
        lastItem = currentItem;
    }

    /**
     * @Date 2015年9月24日
     * @Description: 图片缩放处理
     * @Return void
     */
    private void initChild() {
        int childCount = mContentLL.getChildCount();
        currentItem = getScrollY() / itemHeight;
        last = getScrollY() % itemHeight;
        for (int i = 0; i < childCount; i++) {
            ViewGroup v1 = (ViewGroup) mContentLL.getChildAt(i);
            TextView v = (TextView) v1.getChildAt(0);
            View v2 = v1.getChildAt(1);
            if (i == currentItem + 2) {
                v2.setScaleX(1.3f - 0.6f * last / Float.valueOf(itemWidth * 2));
                v2.setScaleY(1.3f - 0.6f * last / Float.valueOf(itemWidth * 2));
            } else if (i == currentItem + 3) {
                v2.setScaleX(1 + 0.6f * last / Float.valueOf(itemWidth * 2));
                v2.setScaleY(1 + 0.6f * last / Float.valueOf(itemWidth * 2));
            } else {
                v2.setScaleX(1f);
                v2.setScaleY(1f);
            }

        }
        lx = getScrollY();
        handler.removeMessages(0);
        handler.sendEmptyMessageDelayed(0, 0);
    }

    @Override
    public void fling(int velocityX) {
        super.fling(velocityX / 2);
    }

    public int getCurrentUser() {
        return currentItem;

    }

    public interface UserChangeListener {
        void userChanged(String currentName, String userId);
    }

    private void initial(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.dinner_table_info_waiter_vertial_slideview, this);
    }

    /**
     * @param activity
     * @param size
     * @Date 2015年9月24日
     * @Description: 添加item
     * @Return void
     */
    private void initScrollView(Activity activity, int size) {
        //获取item宽度高度
        this.activity = activity;
        getParentHeight();

        if (size <= 0) {
            return;
        }
        int[] items =
                new int[]{R.drawable.login_icon_1, R.drawable.login_icon_2, R.drawable.login_icon_3, R.drawable.login_icon_4, R.drawable.login_icon_5};
        mContentLL = (LinearLayout) findViewById(R.id.content_ll);
        mContentLL.removeAllViews();

        //循环添加view
        for (int i = 0; i < size + 4; i++) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(itemHeight, itemHeight);//每一项布局
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(-2, -2);//文字layout

            //初始化头像view
            int imageHeight = (int) (itemHeight * 1.7 / 3);//头像高度
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(imageHeight, imageHeight);//头像layout
            params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params2.topMargin = 10;
            ImageView iv = new ImageView(context);
            iv.setId(mContentLL.getId() + 1);
            Random random = new Random();
            iv.setBackgroundResource(items[random.nextInt(5)]);

            //初始化textview
            params1.addRule(RelativeLayout.BELOW, iv.getId());
            params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params1.topMargin = 0;
            TextView columnTextView = new TextView(context);

            columnTextView.setTextColor(Color.BLACK);
            columnTextView.setTextSize(sp2px(10));
            columnTextView.setSingleLine(true);
            columnTextView.setPadding(5, 5, 5, 5);
            if (i >= 2 && i < size + 2) {
                columnTextView.setText(headBeans.get(i - 2).getName());
            }

            //初始化每一项
            RelativeLayout ll = new RelativeLayout(context);
            ll.setGravity(Gravity.CENTER);
            ll.setId(i);
            ll.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    performClick(v.getId());
                }
            });
            ll.addView(columnTextView, params1);
            ll.addView(iv, params2);
            mContentLL.addView(ll, i, params);

            //初始化选中头像大小
            if (i == 2) {
                iv.setScaleX(1.3f);
                iv.setScaleY(1.3f);
            }
            if (i < 2 || i >= size + 2) {
                ll.setVisibility(View.INVISIBLE);
            }
        }

        //初始化显示位置
        if (scrollTo > 0) {
            post(new Runnable() {

                @Override
                public void run() {
                    smoothScrollBy(0, scrollTo * itemHeight);
                }
            });
        }
        // setListener(this);
    }

    private int sp2px(float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * @param headBeans
     * @Date 2015-9-8
     * @Description: 设置头像数据
     * @Return void
     */
    public void setData(Activity activity, List<UserHeaderBean> headBeans) {
        this.headBeans = headBeans;
        initScrollView(activity, headBeans.size());
    }

    /**
     * @Date 2015-9-8
     * @Description: 获取整个view的宽高
     * @Return void
     */
    private void getParentHeight() {
        // 获取item宽度高度
        this.activity = activity;
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        realHeight = (int) (metrics.heightPixels * 1 / 2);
        getLayoutParams().height = realHeight;
        itemHeight = realHeight / 5;
        this.itemWidth = itemHeight;
    }

    /**
     * @param itemNum
     * @Date 2015-9-9
     * @Description: 向上或向下移动几个头像
     * @Return void
     */
    public void move(int itemNum) {
        smoothScrollBy(0, itemHeight * itemNum);

    }

    public void setScrollTo(int scrollTo) {
        this.scrollTo = scrollTo;
    }


}
