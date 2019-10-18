package com.zhongmei.bty.dinner.table.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.dinner.vo.DinnerConnectTablesAreaVo;

import java.util.List;



public class TablesAreaPopupAdapter extends BaseAdapter {
    private List<DinnerConnectTablesAreaVo> areaVoList;

    private LayoutInflater mLayoutInflater;

    Context context;

    public TablesAreaPopupAdapter(Context context, List<DinnerConnectTablesAreaVo> areaVoList) {
        this.context = context;
        this.areaVoList = areaVoList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return areaVoList != null ? areaVoList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return areaVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView name;    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.batch_op_tables_popup_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.batch_op_tables_popup_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DinnerConnectTablesAreaVo areaVo = areaVoList.get(position);
        holder.name.setText(areaVo.area.getAreaName());
        if (areaVo.isSelected) {
            holder.name.setBackgroundResource(R.color.color_E7EAEF);
            holder.name.setTextColor(Color.parseColor("#32ADF6"));
        } else {
            holder.name.setBackgroundResource(R.color.bg_white);
            holder.name.setTextColor(Color.GRAY);
        }
        return convertView;
    }
}
