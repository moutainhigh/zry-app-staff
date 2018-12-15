package com.zhongmei.bty.mobilepay.views.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.OSLog;


/**
 * Created by demo on 2018/12/15
 */

public class ViewPagerIndicator extends LinearLayout implements Indicator {

    private static final String TAG = ViewPagerIndicator.class.getSimpleName();

    private static final int DEFAULT_PAGE_SIZE = 6;
    private static final int ACTION_MOD_HIDE_ALWAYS = 1;
    private static final int ACTION_MOD_SHOW_ALWAYS = 2;
    private static final int ACTION_MOD_CONDITION = 3;

    private View prevActionView;
    private View nextActionView;
    private InnerPagerAdapter innerPagerAdapter;
    private IViewPager viewPager;
    private Adapter adapter;
    private OnItemClickListener onItemClickListener;

    private int pageSize;
    private int actionMod;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator, defStyleAttr, 0);
        pageSize = ta.getInteger(R.styleable.ViewPagerIndicator_page_size, DEFAULT_PAGE_SIZE);
        actionMod = ta.getInteger(R.styleable.ViewPagerIndicator_action_mod, ACTION_MOD_CONDITION);
        ta.recycle();

        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            viewPager = new HorizontalViewPager(context, attrs);
        } else {
            viewPager = new VerticalViewPager(context, attrs);
        }
        viewPager.setOnPageChangListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateActionEnable();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        try {
            super.onRestoreInstanceState(state);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        state = null;
    }

    @Override
    public void setShowDividers(int showDividers) {
//        Always
        super.setShowDividers(SHOW_DIVIDER_MIDDLE);
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
        prepareViews();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("The adapter must not be null");
        }
        if (this.adapter == adapter) {
            return;
        }
        this.adapter = adapter;
        long start = System.currentTimeMillis();
        prepareViews();
        OSLog.info("ViewPagerIndicator:  " + (System.currentTimeMillis() - start));
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        if (innerPagerAdapter != null) {
            innerPagerAdapter.onItemClickListener = onItemClickListener;
        }
    }

    private void prepareViews() {
        if (adapter == null) {
            return;
        }
        removeAllViews();

        int orientation = getOrientation();
        prevActionView = adapter.getPrevActionView(this, orientation);
        nextActionView = adapter.getNextActionView(this, orientation);

        int index = 0;

        if (actionIsEnable() && prevActionView != null) {
            prevActionView.setId(R.id.action_prev);
            prevActionView.setOnClickListener(actionClickListener);
            ViewGroup.LayoutParams layoutParams = prevActionView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            super.addView(prevActionView, index, layoutParams);
            index++;
        }
        ViewGroup.LayoutParams viewPagerLayoutParams;
        if (orientation == HORIZONTAL) {
            viewPagerLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        } else {
            viewPagerLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1);
        }
        super.addView(viewPager.getDelegate(), index, viewPagerLayoutParams);
        index++;

        if (actionIsEnable() && nextActionView != null) {
            nextActionView.setId(R.id.action_next);
            nextActionView.setOnClickListener(actionClickListener);
            ViewGroup.LayoutParams layoutParams = nextActionView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            super.addView(nextActionView, index, layoutParams);
            index++;
        }

        innerPagerAdapter = new InnerPagerAdapter(getContext(), this, adapter, pageSize, orientation);
        innerPagerAdapter.onItemClickListener = this.onItemClickListener;

        viewPager.setAdapter(innerPagerAdapter);

        updateActionEnable();
    }

    private boolean actionIsEnable() {
        return actionMod != ACTION_MOD_HIDE_ALWAYS &&
                (actionMod == ACTION_MOD_SHOW_ALWAYS ||
                        (actionMod == ACTION_MOD_CONDITION
                                && adapter != null
                                && adapter.getCount() > pageSize));
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        throw new RuntimeException("Can`t support add view");
    }

    private void updateActionEnable() {
        prevActionView.setEnabled(viewPager.enableActionPrev());
        nextActionView.setEnabled(viewPager.enableActionNext());
    }

    private OnClickListener actionClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.action_prev) {
                viewPager.moveToPrevItem();
            } else if (v.getId() == R.id.action_next) {
                viewPager.moveToNextItem();
            }
        }
    };

    static class InnerPagerAdapter extends PagerAdapter {

        final static int DEFAULT_PAGE_SIZE = 3;

        Context context;
        Indicator indicator;
        Adapter adapter;
        int pageSize;
        int orientation;

        OnItemClickListener onItemClickListener;

        int chileTempCount = 0;

        public InnerPagerAdapter(Context context, Indicator indicator,
                                 Adapter adapter, int pageSize, int orientation) {
            this.context = context;
            this.indicator = indicator;
            this.adapter = adapter;
            this.pageSize = Math.max(DEFAULT_PAGE_SIZE, pageSize);
            this.orientation = orientation;

            this.adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    notifyDataSetChanged();
                }

                @Override
                public void onInvalidated() {
//                    empty
                }
            });
        }

        @Override
        public void notifyDataSetChanged() {
            chileTempCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (chileTempCount > 0) {
                chileTempCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            int itemCount = adapter.getCount();
            return (itemCount + pageSize - 1) / pageSize;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
//            super.destroyItem(container, position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LinearLayout linearLayout = getLinearLayoutForPage(position);
            container.addView(linearLayout);
            return linearLayout;
        }

        private LinearLayout getLinearLayoutForPage(int position) {
//            TODO:add view pool
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.setOrientation(orientation);
            linearLayout.setWeightSum(pageSize);
            if (indicator instanceof LinearLayout) {
                LinearLayout proxy = (LinearLayout) indicator;
                linearLayout.setDividerDrawable(proxy.getDividerDrawable());
                linearLayout.setShowDividers(proxy.getShowDividers());
            }
//            linearLayout.setDividerDrawable(context.getResources()
//                    .getDrawable(R.drawable.shape_pay_method_divider_line_horizental));
//            linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

            ViewGroup.LayoutParams layoutParams = null;

            for (int i = 0; i < pageSize; i++) {
                int index = position * pageSize + i;
                if (index >= adapter.getCount()) {
                    break;
                }
                View view = adapter.getView(indicator, index);
                if (view == null) {
                    throw new NullPointerException("getView() return a view = null");
                }

                if (layoutParams == null) {
                    layoutParams = generateLayoutParams();
                }

                view.setOnClickListener(new OnItemClickListenerProxy(indicator, adapter,
                        onItemClickListener, index));
                linearLayout.addView(view, layoutParams);
            }

            return linearLayout;
        }

        private LayoutParams generateLayoutParams() {
            if (orientation == LinearLayout.HORIZONTAL) {
                return new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            }
            return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1);
        }
    }

    static class HorizontalViewPager extends ViewPager implements IViewPager {

        public HorizontalViewPager(Context context) {
            super(context);
        }

        public HorizontalViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }


        @Override
        public View getDelegate() {
            return this;
        }

        @Override
        public boolean enableActionPrev() {
            return getCurrentItem() > 0;
        }

        @Override
        public boolean enableActionNext() {
            return getCurrentItem() < getAdapter().getCount() - 1;
        }

        @Override
        public void setOnPageChangListener(OnPageChangeListener onPageChangedListener) {
            super.setOnPageChangeListener(onPageChangedListener);
        }

        @Override
        public void moveToPrevItem() {
            if (enableActionPrev()) {
                setCurrentItem(getCurrentItem() - 1, true);
            }
        }

        @Override
        public void moveToNextItem() {
            if (enableActionNext()) {
                setCurrentItem(getCurrentItem() + 1, true);
            }
        }
    }

    static class VerticalViewPager extends com.zhongmei.bty.commonmodule.view.VerticalViewPager
            implements IViewPager {

        public VerticalViewPager(Context context) {
            super(context);
        }

        public VerticalViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public View getDelegate() {
            return this;
        }

        @Override
        public boolean enableActionPrev() {
            return getCurrentItem() > 0;
        }

        @Override
        public boolean enableActionNext() {
            return getCurrentItem() < getAdapter().getCount() - 1;
        }

        @Override
        public void setOnPageChangListener(ViewPager.OnPageChangeListener onPageChangedListener) {
            super.setOnPageChangeListener(onPageChangedListener);
        }

        @Override
        public void moveToPrevItem() {
            if (enableActionPrev()) {
                setCurrentItem(getCurrentItem() - 1, true);
            }
        }

        @Override
        public void moveToNextItem() {
            if (enableActionNext()) {
                setCurrentItem(getCurrentItem() + 1, true);
            }
        }

    }

    public interface IViewPager {

        View getDelegate();

        void setAdapter(PagerAdapter adapter);

        boolean enableActionPrev();

        boolean enableActionNext();

        void setOnPageChangListener(ViewPager.OnPageChangeListener onPageChangedListener);

        void moveToPrevItem();

        void moveToNextItem();
    }

    static class OnItemClickListenerProxy implements OnClickListener {

        final Indicator indicator;
        final Adapter adapter;
        final OnItemClickListener delegate;
        final int position;

        OnItemClickListenerProxy(Indicator indicator, Adapter adapter,
                                 OnItemClickListener delegate, int position) {
            this.indicator = indicator;
            this.adapter = adapter;
            this.delegate = delegate;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (delegate != null) {
                delegate.onItemClick(indicator, adapter, position);
            }
        }
    }
}
