package com.zhongmei.beauty.order;

import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealGroupVo;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.beauty.order.adapter.BeautyDishSetmealAdapter;
import com.zhongmei.bty.dinner.orderdish.DinnerDishSetmealFragment;

import org.androidannotations.annotations.EFragment;

/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.beauty_dish_setmeal)
public class BeautySetmealFragment extends DinnerDishSetmealFragment {

    @Override
    protected void initAdapter() {
        mAdapter = new BeautyDishSetmealAdapter(getActivity(), 3);
    }

    @Override
    protected void setFinishBg() {
        btnFinish.setBackgroundResource(R.drawable.beauty_setmeal_submit_bg_selector);
    }

    @Override
    protected Drawable getBackground(DishSetmealGroupVo groupVo) {
        return getResources().getDrawable(R.drawable.beauty_dish_type_item_bg);
    }

    protected int getButtonTextStyle() {
        return getResources().getColor(R.color.beauty_setmeal_text_selector);
    }

    protected FrameLayout.LayoutParams getSetmealGroupParams() {
        int height = DensityUtil.dip2px(MainApplication.getInstance(), 82);
        int width = DensityUtil.dip2px(MainApplication.getInstance(), 130);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height, Gravity.CENTER);
        layoutParams.bottomMargin = DensityUtil.dip2px(MainApplication.getInstance(), 20);
        return layoutParams;
    }
}
