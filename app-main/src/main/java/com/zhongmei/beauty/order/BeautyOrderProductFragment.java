package com.zhongmei.beauty.order;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.beauty.order.view.BeautyBrandTypeView;
import com.zhongmei.beauty.order.view.BeautyBrandTypeViewEx;
import com.zhongmei.bty.dinner.orderdish.DishHomePageFragment;

import java.util.ArrayList;

/**
 * Created by demo on 2018/12/15
 */

public class BeautyOrderProductFragment extends DishHomePageFragment {

    Button btn_service, btn_product;

    protected void initAdapter() {
        mAdapter = new BeautyOrderProductListPagerAdapter(getActivity(), new ArrayList<DishVo>()) {

            @Override
            public void doItemTouch(DishVo dishVo) {
                onTouch(dishVo);
            }

            @Override
            public void doItemLongClick(DishVo dishVo) {
//                myGridItemLongClicked(dishVo);
            }

        };
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.beauty_order_home_page, null);
    }

    @Override
    protected void assignViews(View view) {
        super.assignViews(view);
        btn_service = (Button) view.findViewById(R.id.beauty_tab_service);
        btn_product = (Button) view.findViewById(R.id.beauty_tab_product);
        btn_service.setSelected(true);
        btn_product.setSelected(false);
        btn_service.setOnClickListener(tabClickListener);
        btn_product.setOnClickListener(tabClickListener);

    }

    View.OnClickListener tabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.beauty_tab_service:
                    changeSelected(btn_service, btn_product);
                    break;
                case R.id.beauty_tab_product:
                    changeSelected(btn_product, btn_service);
                    break;
            }

        }
    };

    private void changeSelected(View btn1, View btn2) {
        if (btn1.isSelected()) {
            btn1.setSelected(false);
            btn2.setSelected(true);
        } else {
            btn1.setSelected(true);
            btn2.setSelected(false);
        }
    }

    @Override
    protected int calculateGridHeight() {
        int height = super.calculateGridHeight();
        //减去titleBar高度
        return height - 45;
    }

    protected int getQuanPopLeftWidth() {
        return DensityUtil.dip2px(getContext(), 520);
    }

    protected int getIndexBg() {
        return R.drawable.beauty_list_index_bg_selector;
    }

    protected int getIndexTextColor() {
        return R.color.beauty_label_text_selector;
    }

    @Override
    public View getBrandTypeView() {
        hideControlBtn();
        int level = SharedPreferenceUtil.getSpUtil().getInt("dinner_meun_level", 2);
        if (level == 2 || isBuyServerBusiness()) {
            return new BeautyBrandTypeView(getActivity(), mDishManager, this,isBuyServerBusiness());
        } else {
            return new BeautyBrandTypeViewEx(getActivity(), mDishManager, this);
        }
    }
}
