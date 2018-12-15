package com.zhongmei.bty.dinner.orderdish;


import android.view.MotionEvent;

import com.zhongmei.yunfu.R;
import com.zhongmei.util.SettingManager;
import com.zhongmei.bty.basemodule.trade.settings.DinnerWesternIPanelSettings;

public class DinnerOrderDishMainActivity extends OrderDishMainActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.dinner_order_dish_mian;
    }

    @Override
    protected DishLeftFragment getLeftFragment() {
        return new DinnerLeftFragment_();
    }

    @Override
    protected DishHomePageFragment getDishPageFragment() {
        return new DinnerOrderDishFragment();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mLeftFragment != null && mLeftFragment.isVisible()) {
            mLeftFragment.onActivityTouched(event);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public int getOrderDishMode() {
        DinnerWesternIPanelSettings iPanelSettings = SettingManager.getSettings(DinnerWesternIPanelSettings.class);
        int panelValue = iPanelSettings.getPanel();
        if (panelValue == DinnerWesternIPanelSettings.PANEL_TYPE_2) {
            return DinnerDishMiddleFragment.WESTERN_DISH_MODE;
        }
        return DinnerDishMiddleFragment.DINNER_ORDER_MODE;
    }
}
