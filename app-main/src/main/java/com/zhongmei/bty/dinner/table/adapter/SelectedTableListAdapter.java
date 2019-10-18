package com.zhongmei.bty.dinner.table.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.dinner.vo.DinnerConnectTablesVo;

import java.util.ArrayList;
import java.util.List;



public class SelectedTableListAdapter extends BaseAdapter {

    private Context mContext;

    private List<DinnerConnectTablesVo> dinnerConnectTablesVoList;

    private LayoutInflater inflater;

    private DeleteSelectedTablesListener deleteListener;

    public void setDeleteListener(DeleteSelectedTablesListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public SelectedTableListAdapter(Context mContext) {
        this.mContext = mContext;
        this.dinnerConnectTablesVoList = new ArrayList<>();
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return dinnerConnectTablesVoList.size();
    }

    @Override
    public DinnerConnectTablesVo getItem(int position) {
        return dinnerConnectTablesVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dinner_selected_tables_operation_item, parent, false);
            holder = new ViewHolder();
            holder.tvId = (TextView) convertView.findViewById(R.id.tv_id);
            holder.tvTableName = (TextView) convertView.findViewById(R.id.tv_table_name);
            holder.tvAreaName = (TextView) convertView.findViewById(R.id.tv_area_name);
            holder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvId.setText(position + 1 + "");
        holder.tvTableName.setText(getItem(position).tables.getTableName());
        holder.tvAreaName.setText(getItem(position).areaName);
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteListener != null) {
                    deleteListener.delete(getItem(position));
                }
            }
        });
        return convertView;
    }

    public void setTablesVoList(List<DinnerConnectTablesVo> tablesVoList) {
        dinnerConnectTablesVoList.clear();
        dinnerConnectTablesVoList.addAll(tablesVoList);
        notifyDataSetChanged();
    }

    class ViewHolder {

        TextView tvId;

        TextView tvTableName;

        TextView tvAreaName;

        ImageView ivDelete;
    }

    public interface DeleteSelectedTablesListener {
        void delete(DinnerConnectTablesVo vo);
    }
}
