package com.zhongmei.beauty.customer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.bean.req.CustomerListResp;
import com.zhongmei.bty.customer.util.AppUtil;

import java.util.List;
import java.util.TreeMap;


public class BeautyCustomerListAdapter extends RecyclerView.Adapter<BeautyCustomerListAdapter.ViewHolder> {

    private Context mContext;

    private List<CustomerListResp> mData;

    private OnRecyclerViewListener mOnRecyclerViewListener;



    private TreeMap<String, String> mLevelMap = new TreeMap<String, String>();


    private TreeMap<String, String> mGroupMap = new TreeMap<String, String>();

    private Long mCurrentCustomerID;

    private int mCurrentPosition = RecyclerView.NO_POSITION;

    public interface OnRecyclerViewListener {

        void onItemClickListener(CustomerListResp bean);

    }



    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.mOnRecyclerViewListener = onRecyclerViewListener;
    }

    public BeautyCustomerListAdapter(Context context, List<CustomerListResp> data) {
        this.mData = data;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.beauty_customer_list_item, null);
        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(LayoutParams);
        return new ViewHolder(view, i);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        CustomerListResp bean = mData.get(i);
        if (TextUtils.isEmpty(bean.name)) {
            viewHolder.tvName.setText(mContext.getString(R.string.customer_no_name2));
        } else {
            viewHolder.tvName.setText(bean.name);
        }
        if (!TextUtils.isEmpty(bean.mobile)) {
            viewHolder.tvTel.setText(AppUtil.getTel(bean.mobile));
        } else {
            viewHolder.tvTel.setText("");
        }



        viewHolder.tvLevel.setText(bean.levelName);
        viewHolder.tvGroup.setText(mContext.getString(R.string.customer_source_label, bean.sourceName));

        if (mCurrentCustomerID != null
                && bean.customerId != null
                && bean.customerId.equals(mCurrentCustomerID)) {
            mCurrentPosition = i;
            viewHolder.llRoot.setSelected(true);
        } else {
            viewHolder.llRoot.setSelected(false);
        }
        if (bean.hasFaceCode != null) {
            if (bean.hasFaceCode == 1) {
                viewHolder.tvFaceCode.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvFaceCode.setVisibility(View.GONE);
            }
        }
        setupVipICon(viewHolder, bean);
    }


    private void setupVipICon(ViewHolder holder, CustomerListResp bean) {
        if (TextUtils.isEmpty(bean.mobile) || bean.getIsDisable() == CustomerListResp.IsDisable.YES) {
            holder.ivVip.setImageResource(R.drawable.customer_vip_icon_disable);
        } else {
            holder.ivVip.setImageResource(R.drawable.customer_vip_icon_new);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public int getCurrentPosition() {
        return mCurrentPosition;
    }


    public void notifyDataAll() {
        resetCondiction();
        notifyDataSetChanged();
    }


    private void resetCondiction() {
        mCurrentCustomerID = null;
        mCurrentPosition = RecyclerView.NO_POSITION;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvFaceCode, tvTel, tvGroup, tvLevel ;

        LinearLayout llRoot;

        ImageView ivVip;

        public ViewHolder(View itemView, final int i) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.name);
            tvFaceCode = (TextView) itemView.findViewById(R.id.face_code);
            tvTel = (TextView) itemView.findViewById(R.id.tel);
            tvLevel = (TextView) itemView.findViewById(R.id.level);
            tvGroup = (TextView) itemView.findViewById(R.id.group);
            ivVip = (ImageView) itemView.findViewById(R.id.vip);
            llRoot = (LinearLayout) itemView.findViewById(R.id.customer_item_layout);

            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ClickManager.getInstance().isClicked()) {
                        return;
                    }
                    if (mOnRecyclerViewListener != null && getPosition() != RecyclerView.NO_POSITION) {
                        mCurrentPosition = getCurrentPosition();
                        if (getPosition() != RecyclerView.NO_POSITION && mData.size() - 1 >= getPosition()) {
                            CustomerListResp bean = mData.get(getPosition());

                            mCurrentCustomerID = bean.customerId;                             mOnRecyclerViewListener.onItemClickListener(bean);
                            notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }



    public void setLevelMap(TreeMap<String, String> levelMap) {
        this.mLevelMap = levelMap;
    }


    public void setGroupMap(TreeMap<String, String> groupMap) {
        this.mGroupMap = groupMap;
    }
}
