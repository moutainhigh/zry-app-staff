package com.zhongmei.bty.dinner.ordercenter.adapter;

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
import com.zhongmei.bty.basemodule.trade.bean.TablesVo;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class TablesVoAdapter extends BaseAdapter {

    private Context mContext;

    private List<TablesVo> tablesVoList;

    private OnClickItemListener onClickItemListener;

    public TablesVoAdapter(Context mContext, OnClickItemListener onClickItemListener) {
        this.mContext = mContext;
        this.onClickItemListener = onClickItemListener;
        tablesVoList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return tablesVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return tablesVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.dinner_choose_desk_item, null);
            viewHolder = new ViewHolder();
            bindData(convertView, viewHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) (convertView.getTag());
        }
        initData(viewHolder, position);
        return convertView;
    }

    /**
     * 绑定View
     *
     * @param convertView
     * @param viewHolder
     */
    private void bindData(View convertView, ViewHolder viewHolder) {
        viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.desk_layout);
        viewHolder.middleLayout = (LinearLayout) convertView.findViewById(R.id.desk_middle_layout);
        viewHolder.personIconIv = (ImageView) convertView.findViewById(R.id.desk_person_icon);
        viewHolder.tv_name = (TextView) convertView.findViewById(R.id.desk_name);
        viewHolder.tv_count = (TextView) convertView.findViewById(R.id.desk_count);
        viewHolder.bottomLayout = (LinearLayout) convertView.findViewById(R.id.desk_bottom_layout);
        viewHolder.tv_states = (TextView) convertView.findViewById(R.id.table_states);
        viewHolder.selectedLayout = (RelativeLayout) convertView.findViewById(R.id.selected);
        convertView.setTag(viewHolder);
    }


    private void initData(ViewHolder viewHolder, int position) {
        final TablesVo tablesVo = tablesVoList.get(position);
        setDeskStyleByState(viewHolder, tablesVo.getTable().getTableStatus());
        if (tablesVo.getSelected()) {
            viewHolder.selectedLayout.setVisibility(View.VISIBLE);
        } else {
            viewHolder.selectedLayout.setVisibility(View.GONE);
        }
        viewHolder.tv_name.setText(tablesVo.getTableName());
        viewHolder.tv_count.setText(tablesVo.getTable().getTablePersonCount().toString());
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TablesVo t : tablesVoList) {
                    t.setSelected(false);
                }
                tablesVo.setSelected(true);
                onClickItemListener.onClickItem(tablesVo);
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 根据不同的状态设置不同背景与文字颜色
     *
     * @param viewHolder
     * @param status
     */
    private void setDeskStyleByState(ViewHolder viewHolder, TableStatus status) {
        switch (status) {
            case EMPTY:
                viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.booking_desk_empty_text_color));
                viewHolder.tv_count.setTextColor(mContext.getResources().getColor(R.color.booking_desk_empty_text_color));
                viewHolder.layout.setBackgroundResource(R.drawable.booking_table_item_bg_layout);
                viewHolder.middleLayout.setBackgroundResource(R.drawable.booking_table_item_middle_layout);
                viewHolder.bottomLayout.setBackgroundResource(R.drawable.booking_table_item_bottom_layout);
                viewHolder.personIconIv.setBackgroundResource(R.drawable.booking_desk_empty_person_icon);
                viewHolder.tv_states.setText(mContext.getString(R.string.dinner_free_table));
                break;
            case OCCUPIED:
                viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.booking_desk_occupy_text_color));
                viewHolder.tv_count.setTextColor(mContext.getResources().getColor(R.color.booking_desk_occupy_text_color));
                viewHolder.layout.setBackgroundResource(R.drawable.booking_table_item_bg_layout);
                viewHolder.middleLayout.setBackgroundResource(R.drawable.booking_table_item_middle_layout);
                viewHolder.bottomLayout.setBackgroundResource(R.drawable.booking_table_item_occupy_bottom_layout);
                viewHolder.personIconIv.setBackgroundResource(R.drawable.booking_desk_occupy_person_icon);
                viewHolder.tv_states.setText(R.string.accept_order_choose_tables_status_dinner);
                viewHolder.selectedLayout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public void setData(List<TablesVo> tablesVos) {
        this.tablesVoList.clear();
        if (Utils.isNotEmpty(tablesVos)) this.tablesVoList.addAll(tablesVos);
        notifyDataSetChanged();
    }

    class ViewHolder {

        TextView tv_name;

        TextView tv_count;

        RelativeLayout layout;

        LinearLayout middleLayout;

        LinearLayout bottomLayout;

        ImageView personIconIv;

        TextView tv_states;

        RelativeLayout selectedLayout;

    }

    public interface OnClickItemListener {
        abstract void onClickItem(TablesVo tablesVo);
    }
}
