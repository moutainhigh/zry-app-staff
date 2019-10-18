package com.zhongmei.bty.dinner.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.snack.orderdish.adapter.OrderDishAdapter;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public class DinnerDishAdapter extends OrderDishAdapter {

    private int currentSelected;

    public int getCurrentSelected() {
        return currentSelected;
    }

    public void setCurrentSelected(int selected) {
        currentSelected = selected;
        notifyDataSetChanged();
    }

    public DinnerDishAdapter(Context context, List<DishVo> dishList, int columns) {
        super(context, dishList, columns);
    }

    @Override
    protected BigDecimal getSelectedQty(DishVo dishVo) {
        Map<String, BigDecimal> map = DinnerShoppingCart.getInstance().getDinnerDishSelectQTY();
        BigDecimal qty = BigDecimal.ZERO;
        for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
            if (dishVo.isSameSeries(entry.getKey())) {
                qty = qty.add(entry.getValue());
            }
        }
        return qty;
    }

    @Override
    protected String getDishName(DishVo vo) {
        String name = vo.getName();
        if (SpHelper.getDefault().getBoolean(SpHelper.DINNER_DISH_LANGUAGE, false)
                && !TextUtils.isEmpty(vo.getAliasName()))
            name = vo.getAliasName();
        return name;
    }

    @Override
    protected String getShortName(DishVo vo) {
        String name = vo.getShortName();
        if (SpHelper.getDefault().getBoolean(SpHelper.DINNER_DISH_LANGUAGE, false)
                && !TextUtils.isEmpty(vo.getAliasShortName()))
            name = vo.getAliasShortName();
        return name;
    }


}
