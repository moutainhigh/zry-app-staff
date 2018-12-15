package com.zhongmei.bty.mobilepay.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.adapter.MobilePayModeChoosePagerAdapter;
import com.zhongmei.bty.mobilepay.bean.MobilePayMenuItem;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class MobilePayModeChooseView extends LinearLayout implements View.OnClickListener {

    protected ViewPager mViewPager;

    protected ImageView mIvLeft;

    protected ImageView mIvRight;

    private List<MobilePayMenuItem> menuItemList = new ArrayList<>();

    private MobilePayModeChoosePagerAdapter adapter;

    private OnCheckMobilePayModeListener checkMobilePayModeListener;

    int pagerIndex = 0;

    public void setCheckMobilePayModeListener(OnCheckMobilePayModeListener checkMobilePayModeListener) {
        this.checkMobilePayModeListener = checkMobilePayModeListener;
    }

    public MobilePayModeChooseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.mobile_pay_mode_choose_view, null);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mIvLeft = (ImageView) view.findViewById(R.id.iv_to_left);
        mIvRight = (ImageView) view.findViewById(R.id.iv_to_right);
        mIvLeft.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        adapter = new MobilePayModeChoosePagerAdapter(context) {
            @Override
            public void selectArea(MobilePayMenuItem data) {
                if (checkMobilePayModeListener != null) {
                    checkMobilePayModeListener.onCheck(data);
                }
            }
        };
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(listener);
        this.addView(view);
    }

    private void updatePager() {
        if (pagerIndex == 0) {
            mIvLeft.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mobile_pay_mode_choose_left_disable));
            mIvLeft.setClickable(false);
        } else {
            mIvLeft.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mobile_pay_mode_choose_left_normal));
            mIvLeft.setClickable(true);
        }
        if (pagerIndex == adapter.getCount() - 1) {
            mIvRight.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mobile_pay_mode_choose_right_disable));
            mIvRight.setClickable(false);
        } else {
            mIvRight.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mobile_pay_mode_choose_right_normal));
            mIvRight.setClickable(true);
        }
    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            pagerIndex = position;
            if (adapter != null) {
                if (pagerIndex == 0) {
                    mIvLeft.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mobile_pay_mode_choose_left_disable));
                    mIvLeft.setClickable(false);
                } else {
                    mIvLeft.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mobile_pay_mode_choose_left_normal));
                    mIvLeft.setClickable(true);
                }
                if (pagerIndex == adapter.getCount() - 1) {
                    mIvRight.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mobile_pay_mode_choose_right_disable));
                    mIvRight.setClickable(false);
                } else {
                    mIvRight.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mobile_pay_mode_choose_right_normal));
                    mIvRight.setClickable(true);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void setMenuItemList(List<MobilePayMenuItem> menuItemList) {
        this.menuItemList = menuItemList;
        adapter.setDataSet(this.menuItemList);
        updatePager();
    }

    @Override
    public void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) return;
        if (v.getId() == R.id.iv_to_left) {
            mViewPager.setCurrentItem(pagerIndex - 1);
        } else if (v.getId() == R.id.iv_to_right) {
            mViewPager.setCurrentItem(pagerIndex + 1);
        }
    }

    public interface OnCheckMobilePayModeListener {
        void onCheck(MobilePayMenuItem data);
    }
}
