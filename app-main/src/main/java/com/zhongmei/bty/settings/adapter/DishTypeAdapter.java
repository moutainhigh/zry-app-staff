package com.zhongmei.bty.settings.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.yunfu.db.enums.Bool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("InflateParams")
public class DishTypeAdapter extends BaseAdapter {

    private Context context;

    private List<DishBrandType> listData;

    // 同一单据类型商品被选次数
    private Map<String, Integer> map = new HashMap<String, Integer>();

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    public DishTypeAdapter(Context context, List<DishBrandType> dishList) {
        this.context = context;
        this.listData = dishList;
    }

    public List<DishBrandType> getListData() {
        return listData;
    }

    public void setListData(List<DishBrandType> listData) {
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData == null ? 0 : listData.size();
    }

    @Override
    public DishBrandType getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.settings_kitchen_dish_list, null);
            holder.name = (TextView) convertView.findViewById(R.id.dish_name);
            holder.tipnum = (TextView) convertView.findViewById(R.id.tip_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DishBrandType dish = getItem(position);
        holder.name.setText(dish.getName());
        if (dish.isShow() == Bool.YES.value()) {
            holder.name.setTextColor(context.getResources().getColor(R.color.text_blue));
        } else {
            holder.name.setTextColor(context.getResources().getColor(R.color.text_gray));
        }
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView tipnum;
    }

}
