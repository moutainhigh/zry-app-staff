package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.table.model.ZoneModel;

import java.util.List;



public class TableZonesChoiceAdapter extends BaseAdapter {

    private List<ZoneModel> datas;
    private Context mContext;
    private ZoneModel mCheckZoneModel = null;

    public TableZonesChoiceAdapter(Context context, List<ZoneModel> datas) {
        this.mContext = context;
        this.datas = datas;
    }

    public void setCheckZoneModel(ZoneModel zoneModel) {
        this.mCheckZoneModel = zoneModel;
    }

    @Override
    public int getCount() {
        if (Utils.isEmpty(datas)) {
            return 0;
        }
        return datas.size();
    }

    @Override
    public ZoneModel getItem(int position) {
        if (datas == null || datas.size() <= position) {
            return null;
        }
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv_text;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.beauty_lv_item_textview, null);
            tv_text = (TextView) convertView.findViewById(R.id.name_tv);
            convertView.setTag(tv_text);
        }

        tv_text = (TextView) convertView.getTag();

        ZoneModel zoneMode = getItem(position);

        if (zoneMode != null) {
            tv_text.setText(zoneMode.getName());
        }


        if (mCheckZoneModel != null && mCheckZoneModel.getId().equals(zoneMode.getId())) {
            tv_text.setTextColor(Color.parseColor("#33adf6"));
        } else {
            tv_text.setTextColor(Color.parseColor("#333333"));
        }
        return convertView;
    }
}
