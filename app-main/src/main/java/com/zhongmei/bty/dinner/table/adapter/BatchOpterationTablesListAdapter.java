package com.zhongmei.bty.dinner.table.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.vo.DinnerConnectTablesVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class BatchOpterationTablesListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<DinnerConnectTablesVo> tablesVos;
    private OnItemBtnClickListener listener;

    public void setListener(OnItemBtnClickListener listener) {
        this.listener = listener;
    }

    public BatchOpterationTablesListAdapter(Context context) {
        this.context = context;
        this.tablesVos = new ArrayList<>();
        Collections.sort(tablesVos, comparator);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tablesVos.size();
    }

    @Override
    public Object getItem(int position) {
        return tablesVos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.batch_opteration_table_list_item, parent, false);
            holder = new MyHolder();
            holder.mTvStatus = (TextView) convertView.findViewById(R.id.tv_table_status);
            holder.mTvName = (TextView) convertView.findViewById(R.id.tv_table_name);
            holder.mTvPeople = (TextView) convertView.findViewById(R.id.tv_table_people);
            holder.mTvArea = (TextView) convertView.findViewById(R.id.tv_table_area);
            holder.mIbTableSelect = (TextView) convertView.findViewById(R.id.ib_table_select);
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }
        final DinnerConnectTablesVo tablesVo = tablesVos.get(position);
        holder.mTvStatus.setText(tablesVo.tables.getTableStatus() == TableStatus.EMPTY ? context.getString(R.string.batch_operation_table_empty_table) : context.getString(R.string.batch_operation_table_occupy_table));
        switch (tablesVo.tables.getTableStatus()) {
            case DONE:
                holder.mTvStatus.setText(context.getString(R.string.batch_operation_table_done_table));
                break;
            case EMPTY:
                holder.mTvStatus.setText(context.getString(R.string.batch_operation_table_empty_table));
                break;
            case OCCUPIED:
                if (Utils.isEmpty(tablesVo.tradeTableList)) {
                    holder.mTvStatus.setText(context.getString(R.string.batch_operation_table_done_table));
                } else {
                    holder.mTvStatus.setText(context.getString(R.string.batch_operation_table_occupy_table));
                }
                break;
        }
        holder.mTvName.setText(tablesVo.tables.getTableName());
        holder.mTvPeople.setText(String.format(context.getString(R.string.batch_operation_table_people), tablesVo.tables.getTablePersonCount()));
        holder.mTvArea.setText(tablesVo.areaName);
        if (tablesVo.isSelected) {
            holder.mIbTableSelect.setSelected(true);
        } else {
            holder.mIbTableSelect.setSelected(false);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tablesVo.tables.getTableStatus() == TableStatus.DONE || (tablesVo.tables.getTableStatus() == TableStatus.OCCUPIED && Utils.isEmpty(tablesVo.tradeTableList))) {
                    ToastUtil.showShortToast(R.string.batch_operation_table_not_operation);
                    return;
                }
                if (tablesVo.isSelected) {
                    tablesVo.isSelected = false;
                } else {
                    tablesVo.isSelected = true;
                }
                notifyDataSetChanged();
                if (listener != null) {
                    listener.onClick(tablesVo);
                }
            }
        });
        return convertView;
    }

    public void setData(List<DinnerConnectTablesVo> tablesVoList) {
        tablesVos.clear();
        if (tablesVoList == null) {
            notifyDataSetChanged();
            return;
        }
        tablesVos.addAll(tablesVoList);
        Collections.sort(tablesVos, comparator);
        notifyDataSetChanged();
    }

    private class MyHolder {
        TextView mTvStatus;
        TextView mTvName;
        TextView mTvPeople;
        TextView mTvArea;
        TextView mIbTableSelect;
    }

    public interface OnItemBtnClickListener {
        void onClick(DinnerConnectTablesVo vo);
    }

    Comparator comparator = new Comparator<DinnerConnectTablesVo>() {
        @Override
        public int compare(DinnerConnectTablesVo o1, DinnerConnectTablesVo o2) {
            return o1.tables.getAreaId().compareTo(o2.tables.getAreaId());
        }
    };
}
