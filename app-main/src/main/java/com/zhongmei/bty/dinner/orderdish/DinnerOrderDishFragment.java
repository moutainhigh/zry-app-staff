package com.zhongmei.bty.dinner.orderdish;

import android.view.View;

import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.bty.dinner.orderdish.view.BrandTypeView;
import com.zhongmei.bty.dinner.orderdish.view.BrandTypeViewEx;

/**
 * @Date： 17/6/7
 * @Description:
 * @Version: 1.0
 */
public class DinnerOrderDishFragment extends DishHomePageFragment {

    @Override
    public View getBrandTypeView() {
        View view = null;
        int level = SharedPreferenceUtil.getSpUtil().getInt("dinner_meun_level", 2);
        if (level == 2)
            view = new BrandTypeView(getActivity(), mDishManager, this);
        else
            view = new BrandTypeViewEx(getActivity(), mDishManager, this);
        return view;
    }
}
