package com.zhongmei.bty.dinner.adapter;

import android.content.Context;
import android.widget.ListAdapter;

import com.zhongmei.bty.snack.orderdish.adapter.OrderDishListPagerAdapter;
import com.zhongmei.util.SettingManager;
import com.zhongmei.bty.settings.component.panel.IDinnerPanelSettings;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;

import java.util.List;

/**
 * @Date：2015年9月6日 上午10:12:04
 * @Description: 正餐点菜首页分页适配器
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public abstract class DinnerDishListPagerAdapter extends OrderDishListPagerAdapter {

    private int panelType = IDinnerPanelSettings.PANEL_TYPE_1;

    public DinnerDishListPagerAdapter(Context context, List<DishVo> dataSet) {
        super(context, dataSet);
        IDinnerPanelSettings panelSettings = SettingManager.getSettings(IDinnerPanelSettings.class);
        panelType = panelSettings.getPanel();
    }

    @Override
    protected int getNumColumns() {
        return panelType == IDinnerPanelSettings.PANEL_TYPE_1 ? 5 : 4;
    }

    @Override
    protected int getNumRows() {
        return panelType == IDinnerPanelSettings.PANEL_TYPE_1 ? 5 : 4;
    }

    @Override
    protected ListAdapter getAdapter(List<DishVo> subDataSet) {
        DinnerDishAdapter orderDishAdapter = new DinnerDishAdapter(mContext, subDataSet, getNumColumns());
        orderDishAdapter.setDishCardBg(this.dishCardType);
        orderDishAdapter.setEditMode(this.isEditMode());//yutang modify 20160811
        orderDishAdapter.setHidClearNumber(this.isHidClearNumber());//yutang modify 20160816
        if (getGridHeight() > 0 && getNumRows() > 0) {
            orderDishAdapter.setItemHeight(getGridHeight() / getNumRows());
        }
        return orderDishAdapter;
    }
}
