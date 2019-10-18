package com.zhongmei.bty.snack.orderdish.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class CommonPagerAdapter extends PagerAdapter {
    private List<View> views;

    public CommonPagerAdapter(List<View> viewList) {
        views = viewList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public int getCount() {
        return views != null ? views.size() : 0;
    }
}
