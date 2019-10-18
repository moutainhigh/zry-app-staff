package com.zhongmei.beauty.customer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerListResp;

import java.util.List;


public class BeautyCustomerDetailProjectAdapter1 extends RecyclerView.Adapter<BeautyCustomerDetailProjectAdapter1.ViewHolder> {

    private final int S_HEADER_VIEW = 0x01;
    private final int S_CONTENT_VIEW = 0x02;
    private final int S_FOOTER_VIEW = 0x03;

    private Context mContext;

    private List<CustomerListResp> mData;

    public BeautyCustomerDetailProjectAdapter1(Context context, List<CustomerListResp> data) {
        this.mData = data;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == S_HEADER_VIEW) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.beauty_customer_detail_card_project_item, null);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.beauty_customer_detail_card_project_item, null);
        }
        FrameLayout.LayoutParams LayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(LayoutParams);
        return new ViewHolder(view, viewType);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.line.setVisibility(View.VISIBLE);
        if (getItemViewType(position) == S_HEADER_VIEW) {
            viewHolder.name.setText(R.string.beauty_customer_detail_project_item_title_name);
            viewHolder.remainCount.setText(R.string.beauty_customer_detail_project_item_title_remain_count);
        } else {
            if (position - 1 == 7) {
                                viewHolder.line.setVisibility(View.GONE);
            }
            viewHolder.name.setText("护理 " + position);
            viewHolder.remainCount.setText(String.format(mContext.getString(R.string.beauty_customer_detail_card_project_item_remain_count), position));
            viewHolder.totalCount.setText(String.format(mContext.getString(R.string.beauty_customer_detail_card_project_item_total_count), mData.size()));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() == 0 ? 7 + 1 : mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return S_HEADER_VIEW;
        } else {
            return S_CONTENT_VIEW;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, totalCount, remainCount;

        View line;

        public ViewHolder(View itemView, final int viewType) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_card_project_item_name);
            totalCount = (TextView) itemView.findViewById(R.id.tv_card_project_item_total_count);
            remainCount = (TextView) itemView.findViewById(R.id.tv_card_project_item_remain_count);
            line = itemView.findViewById(R.id.vw_card_project_item_divide_line);
            if (viewType == S_HEADER_VIEW) {
                totalCount.setVisibility(View.GONE);
                name.setTextColor(mContext.getResources().getColor(R.color.beauty_color_999999));
                remainCount.setTextColor(mContext.getResources().getColor(R.color.beauty_color_999999));
            } else {
                totalCount.setVisibility(View.VISIBLE);
                remainCount.setTextColor(mContext.getResources().getColor(R.color.beauty_color_333333));
                totalCount.setTextColor(mContext.getResources().getColor(R.color.beauty_color_333333));
            }
        }
    }
}
