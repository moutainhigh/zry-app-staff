package com.zhongmei.bty.dinner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.dinner.vo.EmptyTableVo;

import java.util.ArrayList;

/**
 * Created by demo on 2018/12/15
 */

public class EmptyTablesListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<EmptyTableVo> emptyTables;
    private OnItemBtnClickListener listener;

    public void setListener(OnItemBtnClickListener listener) {
        this.listener = listener;
    }

    public EmptyTablesListAdapter(Context context, ArrayList<EmptyTableVo> emptyTables) {
        this.context = context;
        this.emptyTables = emptyTables;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return emptyTables.size();
    }

    @Override
    public Object getItem(int position) {
        return emptyTables.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.empty_table_list_item, parent, false);
            holder = new MyHolder();
            holder.mTvNum = (TextView) convertView.findViewById(R.id.tv_number);
            holder.mTvName = (TextView) convertView.findViewById(R.id.tv_table_name);
            holder.mTvPeople = (TextView) convertView.findViewById(R.id.tv_table_people);
            holder.mTvArea = (TextView) convertView.findViewById(R.id.tv_table_area);
            holder.mBtOpen = (Button) convertView.findViewById(R.id.bt_open);
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }
        final EmptyTableVo vo = emptyTables.get(position);
        holder.mTvNum.setText(position + 1 + "");
        holder.mTvName.setText(emptyTables.get(position).getEmptyTables().getTableName());
        holder.mTvPeople.setText(String.format(context.getString(R.string.batch_operation_table_people),
                emptyTables.get(position).getEmptyTables().getTablePersonCount())
        );
        holder.mTvArea.setText(emptyTables.get(position).getTableArea().getAreaName());
        holder.mBtOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(vo);
                }
            }
        });
        return convertView;
    }

    private class MyHolder {
        TextView mTvNum;
        TextView mTvName;
        TextView mTvPeople;
        TextView mTvArea;
        Button mBtOpen;
    }

    public interface OnItemBtnClickListener {
        void onClick(EmptyTableVo vo);
    }
}
