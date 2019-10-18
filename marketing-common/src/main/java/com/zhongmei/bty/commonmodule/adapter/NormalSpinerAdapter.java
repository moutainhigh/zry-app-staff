package com.zhongmei.bty.commonmodule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.commonmodule.R;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;

import java.util.ArrayList;
import java.util.List;


public class NormalSpinerAdapter extends BaseAdapter {


    private Context mContext;
    private List<? extends IAuthUser> mObjects = new ArrayList<>();
    private int mSelectItem = 0;

    private LayoutInflater mInflater;

    public NormalSpinerAdapter(Context context) {
        init(context);
    }

    public void refreshData(List<? extends IAuthUser> objects, int selIndex) {
        mObjects = objects;
        if (selIndex < 0) {
            selIndex = 0;
        }
        if (selIndex >= mObjects.size()) {
            selIndex = mObjects.size() - 1;
        }

        mSelectItem = selIndex;
    }

    private void init(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {

        return mObjects.size();
    }

    @Override
    public Object getItem(int pos) {
        return mObjects.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.commonmodule_spiner_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        IAuthUser item = (IAuthUser) getItem(pos);
        viewHolder.mTextView.setText(item.getName());

        return convertView;
    }

    public static class ViewHolder {
        public TextView mTextView;
    }
}
