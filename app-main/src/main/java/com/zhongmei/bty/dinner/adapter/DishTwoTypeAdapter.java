package com.zhongmei.bty.dinner.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.dinner.orderdish.view.DishTwoTypeItemView;

import java.util.List;

/**
 * @Date： 17/3/6
 * @Description:
 * @Version: 1.0
 */
public class DishTwoTypeAdapter extends BaseAdapter {

    private List<DishBrandType> mDatas;
    private Context mContext;
    private int bgResId = R.drawable.dinner_dish_two_type_item_bg;
    private int mLayoutResId = R.layout.listitem_dish_two_type;

    public DishTwoTypeAdapter(List<DishBrandType> datas, Context context) {
        this.mDatas = datas;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DishTwoTypeItemView view = new DishTwoTypeItemView(mContext, bgResId, mLayoutResId);
        view.setText(getName(mDatas.get(position)));
        return view;

    }

    public void setItemBgResId(int resId) {
        this.bgResId = resId;
    }

    public void setItemLayoutRes(int layoutId) {
        this.mLayoutResId = layoutId;
    }

    private String getName(DishBrandType type) {
        String name = type.getName();
        if (SpHelper.getDefault().getBoolean(SpHelper.DINNER_DISH_LANGUAGE, false)
                && !TextUtils.isEmpty(type.getAliasName()))
            name = type.getAliasName();
        return name;
    }


}
