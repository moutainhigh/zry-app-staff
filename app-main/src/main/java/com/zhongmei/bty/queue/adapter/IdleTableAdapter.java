package com.zhongmei.bty.queue.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author：LiuYang
 * Date：2016/6/28 13:26
 * E-mail：liuy0
 */
public abstract class IdleTableAdapter extends BaseAdapter {

    private Context mContext;

    private List<Tables> listData;

    private Tables selectedTable;
    private List<Tables> selectedTables;

    private boolean multiselect;

    private HashMap<String, BookingTable> currentPeriodBookingTables;

    public void setData(List<Tables> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
        this.notifyDataSetChanged();
    }

    public void setSelectedTables(List<Tables> selectedTables) {
        this.selectedTables = selectedTables;
        this.notifyDataSetChanged();
        if (selectedTables != null && selectedTables.size() > 0) {
            setSelectedTable(selectedTables.get(0));
        }
    }

    private void setSelectedTable(Tables selectedTable) {
        this.selectedTable = selectedTable;
        this.notifyDataSetChanged();
    }

    public void setCurrentPeriodBookingTables(HashMap<String, BookingTable> currentPeriodBookingTables) {
        this.currentPeriodBookingTables = currentPeriodBookingTables;
        this.notifyDataSetChanged();
    }

    public IdleTableAdapter(Context context) {
        mContext = context;
        listData = new ArrayList<Tables>();
        this.multiselect = false;
    }

    public IdleTableAdapter(Context context, boolean multiselect) {
        mContext = context;
        listData = new ArrayList<Tables>();
        this.multiselect = multiselect;
    }

    public void setMultiselect(boolean multiselect) {
        this.multiselect = multiselect;
    }

    @Override
    public int getCount() {
        return listData == null ? 0 : listData.size();
    }

    @Override
    public Tables getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        View rootView;

        TextView tv_name;

        TextView tv_count;

        RelativeLayout layout;

        RelativeLayout middleLayout;

        LinearLayout bottomLayout;

        ImageView personIconIv;

        ImageView lockIconIv;

        TextView tv_states;

        RelativeLayout layout_selected;

        ImageView image_tag;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.booking_create_desk_item, null);
            holder = new ViewHolder();
            holder.rootView = convertView.findViewById(R.id.booking_create_desk);
            holder.tv_name = (TextView) convertView.findViewById(R.id.booking_create_desk_name);
            holder.tv_count = (TextView) convertView.findViewById(R.id.booking_create_desk_count);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.booking_create_desk);
            holder.middleLayout = (RelativeLayout) convertView.findViewById(R.id.booking_desk_middle_layout);
            holder.bottomLayout = (LinearLayout) convertView.findViewById(R.id.booking_desk_bottom_layout);
            holder.personIconIv = (ImageView) convertView.findViewById(R.id.booking_create_desk_person_icon);
            holder.lockIconIv = (ImageView) convertView.findViewById(R.id.booking_create_desk_lock_icon);
            holder.tv_states = (TextView) convertView.findViewById(R.id.table_states);
            holder.layout_selected = (RelativeLayout) convertView.findViewById(R.id.booking_create_selected);
            holder.image_tag = (ImageView) convertView.findViewById(R.id.booking_create_desk_tag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Tables table = listData.get(position);
        holder.tv_name.setText(table.getTableName());
        holder.tv_count.setText("" + table.getTablePersonCount());

        holder.layout.setClickable(true);
        holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.booking_desk_empty_text_color));
        holder.tv_count.setTextColor(mContext.getResources().getColor(R.color.booking_desk_empty_text_color));
        holder.layout.setBackgroundResource(R.drawable.booking_table_item_bg_layout);
        holder.middleLayout.setBackgroundResource(R.drawable.booking_table_item_middle_layout);
        holder.bottomLayout.setBackgroundResource(R.drawable.booking_table_item_bottom_layout);
        holder.personIconIv.setBackgroundResource(R.drawable.booking_desk_empty_person_icon);
        holder.tv_states.setText(mContext.getString(R.string.booking_desk_empty_str));

        if (multiselect) {
            //支持多选
            holder.layout_selected.setVisibility(View.GONE);
            if (selectedTables != null && selectedTables.size() > 0) {
                for (Tables selTable : selectedTables) {
                    if (selTable != null && selTable.getId().equals(table.getId())) {
                        holder.layout_selected.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        } else {
            if (selectedTable != null && selectedTable.getId().equals(table.getId())) {
                holder.layout_selected.setVisibility(View.VISIBLE);
            } else {
                holder.layout_selected.setVisibility(View.GONE);
            }
        }

        if (table.getTableStatus() != TableStatus.EMPTY) {
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.booking_desk_occupy_text_color));
            holder.tv_count.setTextColor(mContext.getResources().getColor(R.color.booking_desk_occupy_text_color));
            holder.layout.setBackgroundResource(R.drawable.booking_table_item_bg_layout);
            holder.middleLayout.setBackgroundResource(R.drawable.booking_table_item_middle_layout);
            holder.bottomLayout.setBackgroundResource(R.drawable.booking_table_item_occupy_bottom_layout);
            holder.personIconIv.setBackgroundResource(R.drawable.booking_desk_occupy_person_icon);
            holder.tv_states.setText(mContext.getString(R.string.queue_desk_occupied));
        } else {
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.booking_desk_empty_text_color));
            holder.tv_count.setTextColor(mContext.getResources().getColor(R.color.booking_desk_empty_text_color));
            holder.layout.setBackgroundResource(R.drawable.booking_table_item_bg_layout);
            holder.middleLayout.setBackgroundResource(R.drawable.booking_table_item_middle_layout);
            holder.bottomLayout.setBackgroundResource(R.drawable.booking_table_item_bottom_layout);
            holder.personIconIv.setBackgroundResource(R.drawable.booking_desk_empty_person_icon);
            holder.tv_states.setText(mContext.getString(R.string.booking_desk_empty_str));
        }

        if (currentPeriodBookingTables != null && currentPeriodBookingTables.get(table.getUuid()) != null) {
            holder.image_tag.setVisibility(View.VISIBLE);
        } else {
            holder.image_tag.setVisibility(View.GONE);
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (multiselect) {
                    //支持多选
                    boolean isSelected = false;
                    if (selectedTables == null)
                        selectedTables = new ArrayList<Tables>();
                    if (selectedTables != null && selectedTables.size() > 0) {
                        for (Tables selTable : selectedTables) {
                            if (selTable != null && selTable.getId().equals(table.getId())) {
                                //已选中
                                isSelected = true;
                                break;
                            }
                        }
                    }
                    if (isSelected) {
                        selectedTables.remove(table);
                    } else {
                        selectedTables.add(table);
                    }
                    selectTables(selectedTables);
                } else {
                    if (selectedTable == null || !selectedTable.getId().equals(table.getId())) {
                        //未选中状态点击，变为选中
                        selectedTable = table;
                        selectTable(true, table);
                        holder.layout_selected.setVisibility(View.VISIBLE);
                    } else {
                        selectedTable = null;
                        selectTable(false, table);
                        holder.layout_selected.setVisibility(View.GONE);
                    }
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void reset() {
        selectedTable = null;
        selectedTables = null;
    }

    public abstract void selectTable(boolean isSelected, Tables table);

    public abstract void selectTables(List<Tables> tables);
}
