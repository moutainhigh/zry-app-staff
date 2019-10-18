package com.zhongmei.bty.snack.orderdish.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Scroller;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.commonmodule.view.VerticalViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DishTypeInflateEx {
    private static final String TAG = DishTypeInflateEx.class.getSimpleName();

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

    private int mPageSize = 6;    private int mLayoutResId = R.layout.item_dish_type_ex;
    private int mResId = R.drawable.dish_type_item_bg;
    private int mIvArrBg = R.drawable.dish_type_arr;
    private int mMoreResId = -1;

    private int mEmptyResId = -1;
    private float mTextSize = 22;

    private ColorStateList mTextColors;

    private String mSelectTxt = MainApplication.getInstance().getResources().getString(R.string.snack_all);

    public void setSelectTxt(String selectTxt) {
        this.mSelectTxt = selectTxt;
        setSelectedView(lastSelectView, true);
    }

    public void setPageSize(int pageSize) {
        this.mPageSize = pageSize;
    }

    public void setItemBg(int resId) {
        mResId = resId;
    }

    public void setMoreBg(int resId) {
        mMoreResId = resId;
    }

    public void setLayoutResId(int layoutResId) {
        mLayoutResId = layoutResId;
    }

    public void setEmptyItemBg(int resId) {
        mEmptyResId = resId;
    }

    public void setmIvArrBg(int ivArrBg) {
        mIvArrBg = ivArrBg;
    }

    public void setItemTextSize(float size) {
        mTextSize = size;
    }

    public void setItemTextColor(int resId) {
        mTextColors = mContext.getResources().getColorStateList(resId);
    }


    private int getPageCount() {
        return (list.size() + mPageSize - 1) / mPageSize;
    }

    public DishTypeInflateEx(Context context, ChangeTypeListener listener) {
        mContext = context;
        this.listener = listener;
        list = new ArrayList<DishBrandType>();
        mTextColors = context.getResources().getColorStateList(R.drawable.dish_type_text_selector);
    }

    public interface ChangeTypeListener {
        void onChangeTypeListener(DishBrandType dishBrandType, boolean sw);
    }

    public void setData(List<DishBrandType> list) {
        this.list.clear();
        if (list != null) {
            this.list.addAll(list);
        }
    }

    private String getName(DishBrandType type) {
        String name = type.getName();
        if (SpHelper.getDefault().getBoolean(SpHelper.DINNER_DISH_LANGUAGE, false)
                && !TextUtils.isEmpty(type.getAliasName()))
            name = type.getAliasName();
        return name;
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
            View view = LayoutInflater.from(mContext).inflate(mLayoutResId, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_title);
            textView.setText(getName(list.get(i)));
            view.setId(i);
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                                                            boolean sw = v.getId() != currentItem;
                                                                                setSelectedView(lastSelectView, false);
                                        if (sw)                            mSelectTxt = MainApplication.getInstance().getResources().getString(R.string.snack_all);
                    setSelectedView(v, true);
                    currentItem = v.getId();
                    lastSelectView = v;
                    lasType = list.get(v.getId());

                    if (listener != null) {
                        listener.onChangeTypeListener(lasType, sw);
                    }
                }
            });
            view.findViewById(R.id.layout_item).setBackgroundResource(mResId);
            if (mMoreResId != -1) {
                view.findViewById(R.id.iv_more_icon).setBackgroundResource(mMoreResId);
            }
            linearLayout.addView(view, i % mPageSize, getItemLayoutParams(i % mPageSize));
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

                viewPager.setCurrentItem(currentPage, true);
        lastSelectView = ((LinearLayout) views.get(currentPage)).getChildAt(currentItem % mPageSize);
        setSelectedView(lastSelectView, true);
    }

    private void setSelectedView(View view, boolean en) {
        if (en) {
            view.setSelected(true);
            view.findViewById(R.id.iv_more_icon).setVisibility(View.VISIBLE);
            view.findViewById(R.id.iv_left_arr).setVisibility(View.VISIBLE);
            view.findViewById(R.id.iv_left_arr).setBackgroundResource(mIvArrBg);
            TextView selType = (TextView) view.findViewById(R.id.tv_select_type);
            selType.setVisibility(View.VISIBLE);
            selType.setText(mSelectTxt);
        } else {
            view.setSelected(false);
            view.findViewById(R.id.iv_more_icon).setVisibility(View.GONE);
            view.findViewById(R.id.iv_left_arr).setVisibility(View.INVISIBLE);
            TextView selType = (TextView) view.findViewById(R.id.tv_select_type);
            selType.setVisibility(View.GONE);
        }
    }

    public void hideSelectedViewArr() {
        lastSelectView.findViewById(R.id.iv_left_arr).setVisibility(View.INVISIBLE);
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
        params.leftMargin = 0;        params.rightMargin = DensityUtil.dip2px(MainApplication.getInstance(), 12);
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
