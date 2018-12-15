package com.zhongmei.bty.dinner.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;

import java.util.List;

public class DinnerDishSearchAdapter extends DinnerDishAdapter {

    private int currentSelected;

    public int getCurrentSelected() {
        return currentSelected;
    }

    public void setCurrentSelected(int selected) {
        currentSelected = selected;
        notifyDataSetChanged();
    }

    public DinnerDishSearchAdapter(Context context, List<DishVo> dishList, int columns) {
        super(context, dishList, columns);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        if (position == currentSelected) {
            convertView.setBackgroundResource(R.drawable.order_dish_item_bg);
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
        return convertView;
    }
}
