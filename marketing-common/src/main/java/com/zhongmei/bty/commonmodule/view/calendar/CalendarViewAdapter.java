package com.zhongmei.bty.commonmodule.view.calendar;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

/**
 * @date 2015年8月5日上午10:31:26
 */
public class CalendarViewAdapter<V extends View> extends PagerAdapter {
    public static final String TAG = "CalendarViewAdapter";
    private V[] views;
    private LayoutParams mLayoutParams;

    public CalendarViewAdapter(V[] views) {
        super();
        this.views = views;
        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if (((ViewPager) container).getChildCount() == views.length) {
            ((ViewPager) container).removeView(views[position % views.length]);
        }
        ((ViewPager) container).addView(views[position % views.length], 0, mLayoutParams);
        return views[position % views.length];
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) container);
    }

    public V[] getAllItems() {
        return views;
    }

}  
