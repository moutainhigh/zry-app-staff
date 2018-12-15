package com.zhongmei.bty.settings.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.settings.bean.SexOrSpeedVo;

/**
 *

 *
 */
public class SexorSpeedAdapter extends BaseAdapter {

    private Context mContext;

    private List<SexOrSpeedVo> voList;

    private String titleStr;

    public SexorSpeedAdapter(Context context, List<SexOrSpeedVo> voList, String titleStr) {
        // TODO Auto-generated constructor stub
        mContext = context;
        this.voList = voList;
        this.titleStr = titleStr;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (voList == null) {
            return 0;
        }
        return voList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.setting_queue_spinner_item_layout, parent, false);
        }
        TextView titleName = (TextView) convertView.findViewById(R.id.tv_title_name);
        TextView property = (TextView) convertView.findViewById(R.id.tv_property);
        RelativeLayout btn = (RelativeLayout) convertView.findViewById(R.id.btn);
        SexOrSpeedVo userEx = voList.get(position);
        property.setText(userEx.getName());
        titleName.setText(titleStr);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.setting_queue_spinner_item_layout, parent, false);
        }
        TextView titleName = (TextView) convertView.findViewById(R.id.tv_title_name);
        TextView property = (TextView) convertView.findViewById(R.id.tv_property);
        RelativeLayout btn = (RelativeLayout) convertView.findViewById(R.id.btn);
        SexOrSpeedVo userEx = voList.get(position);
        property.setText(userEx.getName());
        titleName.setVisibility(View.GONE);
        btn.setVisibility(View.GONE);
        return convertView;
    }

}
