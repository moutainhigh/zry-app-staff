package com.zhongmei.bty.customer.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.R;


public class CustomerEnjoyPopWindowAdapter extends BaseAdapter {

    private List<String> mList;
    private Context mContext;

    public CustomerEnjoyPopWindowAdapter(Context context, List<String> list) {
        mContext = context;
        if (null == list) {
            mList = new ArrayList<String>();
        } else {
            mList = list;
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {

        return arg0;
    }


    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {

        Holder holder = null;

        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.customer_pop_list_item, null);
            holder = new Holder();
            holder.tv = (TextView) convertView.findViewById(R.id.groupname);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.tv.setText(mList.get(pos) + "");

        return convertView;
    }

    private class Holder {
        TextView tv;
    }


}
