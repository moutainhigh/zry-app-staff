package com.zhongmei.bty.snack.orderdish.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Scroller;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.commonmodule.view.VerticalViewPager;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.yunfu.context.util.Utils;

public class DishTypeInflate {
    private static final String TAG = DishTypeInflate.class.getSimpleName();

    private Context mContext;

    private ArrayList<DishBrandType> list;

    private LinearLayout linearLayout;

    private int page;

    private DishBrandType lasType;

    private View lastSelectView;

    private int currentPage;

    private int currentItem;

    private ChangeTypeListener listener;

    private VerticalViewPager viewPager;

    private int mPageSize = 6;
    private int mResId = R.drawable.dish_type_item_bg;

    private int mEmptyResId = -1;
    private float mTextSize = 22;

    private ColorStateList mTextColors;

    private boolean isDinner;

    public void setPageSize(int pageSize) {
        this.mPageSize = pageSize;
    }

    public void setItemBg(int resId) {
        mResId = resId;
    }

    public void setEmptyItemBg(int resId) {
        mEmptyResId = resId;
    }

    public void setItemTextSize(float size) {
        mTextSize = size;
    }

    public void setItemTextColor(int resId) {
        mTextColors = mContext.getResources().getColorStateList(resId);
    }

    public void setDinner(boolean dinner) {
        isDinner = dinner;
    }


    private int getPageCount() {
        return (list.size() + mPageSize - 1) / mPageSize;
    }

    public DishTypeInflate(Context context, ChangeTypeListener listener) {
        mContext = context;
        this.listener = listener;
        list = new ArrayList<DishBrandType>();
        mTextColors = context.getResources().getColorStateList(R.drawable.dish_type_text_selector);
    }

    public interface ChangeTypeListener {
        void onChangeTypeListener(DishBrandType dishBrandType);
    }

    public void setData(List<DishBrandType> list) {
        this.list.clear();
        if (list != null) {
            this.list.addAll(list);
        }
    }

    public void inflateView(final VerticalViewPager viewPager) {
        if (viewPager == null || Utils.isEmpty(list)) {
            return;
        }

        this.viewPager = viewPager;
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                currentPage = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        ViewPagerScroller scroller = new ViewPagerScroller(mContext);
        scroller.initViewPagerScroll(viewPager);

        page = getPageCount();
        List<View> views = new ArrayList<View>();
        for (int i = 0; i < list.size(); i++) {
            if (i % mPageSize == 0) {
                linearLayout = new LinearLayout(mContext);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
            }
            TextView textView = new TextView(mContext);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(mTextSize);
            textView.setText(getName(list.get(i)));
            textView.setTextColor(mTextColors);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setBackgroundResource(mResId);
            textView.setId(i);
            textView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v.getId() == currentItem)
                        return;
                    lastSelectView.setSelected(false);
                    v.setSelected(true);

                    currentItem = v.getId();
                    lastSelectView = v;
                    lasType = list.get(v.getId());
                    if (listener != null) {
                        listener.onChangeTypeListener(lasType);
                    }
                }
            });
            linearLayout.addView(textView, i % mPageSize, getItemLayoutParams(i % mPageSize));
            if (i % mPageSize == mPageSize - 1 || i == list.size() - 1) {
                views.add(linearLayout);
            }
        }

        if (page * mPageSize > list.size()) {
            for (int j = list.size(); j < page * mPageSize; j++) {
                View view = new View(mContext);
                if (mEmptyResId != -1) {
                    view.setBackgroundResource(mEmptyResId);
                }

                LinearLayout parent = (LinearLayout) views.get(views.size() - 1);
                parent.addView(view, j % mPageSize, getItemLayoutParams(j % mPageSize));
            }
        }
        viewPager.setAdapter(new CommonPagerAdapter(views));

                currentItem = 0;
        currentPage = 0;
        boolean isLastTypeMap = false;
        if (lasType != null && Utils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUuid().equals(lasType.getUuid())) {
                    isLastTypeMap = true;
                    currentItem = i;
                    currentPage = i / mPageSize;
                    break;
                }
            }
        }
                if (!isLastTypeMap) {
            lasType = list.get(0);
        }
        if (listener != null) {
            listener.onChangeTypeListener(lasType);
        }
                viewPager.setCurrentItem(currentPage, true);
        lastSelectView = ((LinearLayout) views.get(currentPage)).getChildAt(currentItem % mPageSize);
        lastSelectView.setSelected(true);
    }

    private String getName(DishBrandType type) {
        String name = type.getName();
        if (SpHelper.getDefault().getBoolean(SpHelper.DINNER_DISH_LANGUAGE, false)
                && !TextUtils.isEmpty(type.getAliasName())
                && isDinner)
            name = type.getAliasName();
        return name;
    }

    public void scrollToPreviousPage() {

        if (viewPager != null) {
            if (currentPage > 0) {
                currentPage -= 1;
                viewPager.setCurrentItem(currentPage, true);
            }

        }

    }

    public void scrollToNextPage() {
        if (viewPager == null)
            return;
        if (currentPage < page - 1) {
            currentPage += 1;
            viewPager.setCurrentItem(currentPage, true);
        }

    }

    public DishBrandType getLastType() {
        return lasType;
    }

    private LayoutParams getItemLayoutParams(int position) {
        LayoutParams params =
                new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;
        params.gravity = Gravity.CENTER;
        params.topMargin = DensityUtil.dip2px(MainApplication.getInstance(), 14);
        params.leftMargin = DensityUtil.dip2px(MainApplication.getInstance(), 14);
        params.rightMargin = DensityUtil.dip2px(MainApplication.getInstance(), 14);
        if (position == mPageSize - 1) {
            params.bottomMargin = DensityUtil.dip2px(MainApplication.getInstance(), 5);
        } else {
            params.bottomMargin = DensityUtil.dip2px(MainApplication.getInstance(), 0);
        }

        return params;
    }

    private class ViewPagerScroller extends Scroller {
        private int mScrollDuration = 400;

        public void setScrollDuration(int duration) {
            this.mScrollDuration = duration;
        }

        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }

        public void initViewPagerScroll(VerticalViewPager viewPager) {
            try {
                Field mScroller = VerticalViewPager.class.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                mScroller.set(viewPager, this);
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    }
}
