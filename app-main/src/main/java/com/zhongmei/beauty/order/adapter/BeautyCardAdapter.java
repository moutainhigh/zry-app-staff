package com.zhongmei.beauty.order.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceAccount;
import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceInfo;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.bty.commonmodule.database.enums.CardRechagingStatus;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;

import java.util.List;

/**
 * 开单界面 会员卡选择
 *
 * @date 2018/4/13 17:44
 */
public class BeautyCardAdapter extends RecyclerView.Adapter<BeautyCardAdapter.ViewHolder> {

    private Context mContext;

    private List<BeautyCardServiceInfo> mData;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onCardItemClickListener(BeautyCardServiceInfo info);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public BeautyCardAdapter(Context context, List<BeautyCardServiceInfo> data) {
        this.mData = data;
        this.mContext = context;
    }

    /**
     * 创建view
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.beauty_order_membership_card_item, null);
        FrameLayout.LayoutParams LayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(LayoutParams);
        return new ViewHolder(view, viewType);
    }

    /**
     * 赋值数据
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        BeautyCardServiceInfo info = mData.get(position);
        viewHolder.cardKind.setText(info.cardName);
        viewHolder.cardNum.setText(info.cardNo);
        if (info.rightStatus != null && info.rightStatus == CardRechagingStatus.INVALID.value()) {
            viewHolder.cardKind.setTextColor(Color.parseColor("#B3FFFFFF"));
            viewHolder.cardNum.setTextColor(Color.parseColor("#B3FFFFFF"));
            viewHolder.surplusCount.setTextColor(Color.parseColor("#B3FFFFFF"));
            viewHolder.projects.setTextColor(Color.parseColor("#B3FFFFFF"));
            viewHolder.validityTime.setTextColor(Color.parseColor("#B0FFFFFF"));
            viewHolder.surplusCount.setText("服务已过期");
        } else {
            viewHolder.cardKind.setTextColor(mContext.getResources().getColor(R.color.beauty_color_white));
            viewHolder.cardNum.setTextColor(mContext.getResources().getColor(R.color.beauty_color_white));
            viewHolder.surplusCount.setTextColor(mContext.getResources().getColor(R.color.beauty_color_white));
            viewHolder.projects.setTextColor(mContext.getResources().getColor(R.color.beauty_color_white));
            viewHolder.validityTime.setTextColor(Color.parseColor("#CCFFFFFF"));
            if (info.remainderTimes == null) {
                viewHolder.surplusCount.setText("");
            } else {
                viewHolder.surplusCount.setText(String.format(mContext.getString(R.string.beauty_card_service_surplus_count), info.remainderTimes));
            }
        }
        String str = "";
        if (info.cardServiceAccountList != null) {
            str = mContext.getString(R.string.beauty_card_service_projects_label);
            for (int i = 0; i < info.cardServiceAccountList.size(); i++) {
                BeautyCardServiceAccount account = info.cardServiceAccountList.get(i);
                if (i == info.cardServiceAccountList.size() - 1) {
                    str += account.serviceName + account.serviceRemainderTime + mContext.getString(R.string.beauty_card_service_count_label2);
                } else {
                    str += account.serviceName + account.serviceRemainderTime + mContext.getString(R.string.beauty_card_service_count_label);
                }
            }
        }
        viewHolder.projects.setText(str);
        viewHolder.validityTime.setText(formatTime(info.validStartDay, info.validEndDay));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickManager.getInstance().isClicked() || mOnItemClickListener == null) {
                    return;
                }
                mOnItemClickListener.onCardItemClickListener(mData.get(position));
            }
        });
    }

    private String formatTime(Long startTime, Long endTime) {
        if (startTime == null || endTime == null) {
            return mContext.getString(R.string.beauty_card_time_never_expires);
        }
        String startTimeStr = DateTimeUtils.formatDateTime(startTime, DateTimeUtils.QUERY_DATE_FORMAT);
        String endTimeStr = DateTimeUtils.formatDateTime(endTime, DateTimeUtils.QUERY_DATE_FORMAT);
        return String.format(mContext.getResources().getString(R.string.beauty_order_card_time, startTimeStr + "-" + endTimeStr));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * @description (ViewHolder 绑定Item点击事件 和 item 长按事件)
     * @time 2015年6月1日
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cardKind, projects, surplusCount, validityTime, cardNum;

        public ViewHolder(View itemView, final int viewType) {
            super(itemView);
            cardKind = (TextView) itemView.findViewById(R.id.beauty_order_card_kind);
            surplusCount = (TextView) itemView.findViewById(R.id.beauty_order_card_surplus_count);
            validityTime = (TextView) itemView.findViewById(R.id.beauty_order_card_validity_time);
            projects = (TextView) itemView.findViewById(R.id.beauty_order_card_projects);
            cardNum = (TextView) itemView.findViewById(R.id.beauty_order_card_num);
        }

    }
}
