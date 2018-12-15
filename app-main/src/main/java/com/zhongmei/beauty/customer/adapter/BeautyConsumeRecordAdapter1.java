package com.zhongmei.beauty.customer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

import java.util.List;

/**
 * 消费记录
 *
 * @date 2017/3/13 15:09
 */
public class BeautyConsumeRecordAdapter1 extends RecyclerView.Adapter<BeautyConsumeRecordAdapter1.ViewHolder> {

    private Context mContext;

    private List<String> mData;

    public BeautyConsumeRecordAdapter1(Context context, List<String> data) {
        this.mData = data;
        this.mContext = context;
    }

    /**
     * 创建view
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.beauty_consume_record_item, null);
        FrameLayout.LayoutParams LayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(LayoutParams);
        return new ViewHolder(view, viewType);
    }

    /**
     * 赋值数据
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (mData.size() == 1) { // 只有一条数据
            viewHolder.bottomLine.setVisibility(View.INVISIBLE);
            viewHolder.topLine.setVisibility(View.INVISIBLE);
            viewHolder.middlePoint.setImageResource(R.drawable.beauty_consume_record_stepview_pink_point);
        } else {
            if (position + 1 == mData.size()) { // 最后一条
                viewHolder.bottomLine.setVisibility(View.INVISIBLE);
                viewHolder.topLine.setVisibility(View.VISIBLE);
                viewHolder.middlePoint.setImageResource(R.drawable.beauty_consume_record_stepview_gray_point);
            } else if (position == 0) { // 第一条
                viewHolder.bottomLine.setVisibility(View.VISIBLE);
                viewHolder.topLine.setVisibility(View.INVISIBLE);
                viewHolder.middlePoint.setImageResource(R.drawable.beauty_consume_record_stepview_pink_point);
            } else { // 中间
                viewHolder.bottomLine.setVisibility(View.VISIBLE);
                viewHolder.topLine.setVisibility(View.VISIBLE);
                viewHolder.middlePoint.setImageResource(R.drawable.beauty_consume_record_stepview_gray_point);
            }
        }
        viewHolder.time.setText("2018-03-03");
        viewHolder.proiects.setText("脸部护理*1 ／身体*1");
        viewHolder.remark.setText("备注：脸部为过敏体质，下次使用 香婆婆系列的产品");
    }

    @Override
    public int getItemCount() {
        return mData.size() == 0 ? 0 : mData.size();
    }

    /**
     * @description (ViewHolder 绑定Item点击事件 和 item 长按事件)
     * @time 2015年6月1日
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView time, proiects, remark;

        View topLine, bottomLine;

        ImageView middlePoint;

        public ViewHolder(View itemView, final int viewType) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.iv_consume_record_time);
            proiects = (TextView) itemView.findViewById(R.id.iv_consume_record_projects);
            remark = (TextView) itemView.findViewById(R.id.iv_consume_record_remark);
            topLine = itemView.findViewById(R.id.iv_consume_record_top_line);
            bottomLine = itemView.findViewById(R.id.iv_consume_record_bottom_line);
            middlePoint = (ImageView) itemView.findViewById(R.id.iv_consume_record_middle_point);
        }
    }
}
