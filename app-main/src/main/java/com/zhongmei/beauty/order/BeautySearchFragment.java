package com.zhongmei.beauty.order;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.beauty.order.adapter.BeautyProductAdapter;
import com.zhongmei.bty.dinner.orderdish.DinnerDishSearchFragment;

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;


/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.dinner_dish_search)
public class BeautySearchFragment extends DinnerDishSearchFragment {

    @Override
    protected void initAdapter() {
        mAdapter = new BeautyProductAdapter(getActivity(), new ArrayList<DishVo>(), 4);
    }
}
