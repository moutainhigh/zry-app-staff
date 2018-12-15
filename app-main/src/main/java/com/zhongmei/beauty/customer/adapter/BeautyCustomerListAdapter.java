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

/**
 * 顾客列表
 *
 * @date 2017/3/13 15:09
 */
public class BeautyCustomerListAdapter extends RecyclerView.Adapter<BeautyCustomerListAdapter.ViewHolder> {

    private Context mContext;

    private List<CustomerListResp> mData;

    private OnRecyclerViewListener mOnRecyclerViewListener;

    /* 这两个变量通过set方法写入数据，是为了避免连表查询导致的查询速度变慢 */
    /* 会员等级 */
    private TreeMap<String, String> mLevelMap = new TreeMap<String, String>();

    /* 客户分组 */
    private TreeMap<String, String> mGroupMap = new TreeMap<String, String>();

    private Long mCurrentCustomerID;

    private int mCurrentPosition = RecyclerView.NO_POSITION;

    public interface OnRecyclerViewListener {
        /**
         * item的点击事件
         */
        void onItemClickListener(CustomerListResp bean);

    }

//    public void setCurrentCustomerID(Long customerID){
//        this.mCurrentCustomerID = customerID;
//    }

    /**
     * RecyclerView 的监听事件
     * <p/>
     * 监听root的点击事件，Recycler没有item点击事件需要自己监听
     *
     * @param onRecyclerViewListener 监听事件的实现
     */
    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.mOnRecyclerViewListener = onRecyclerViewListener;
    }

    public BeautyCustomerListAdapter(Context context, List<CustomerListResp> data) {
        this.mData = data;
        this.mContext = context;
    }

    /**
     * 创建view
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.beauty_customer_list_item, null);
        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(LayoutParams);
        return new ViewHolder(view, i);
    }

    /**
     * 赋值数据
     */
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

        /*if (bean.getIsDisable() == CustomerListResp.IsDisable.YES) {
            viewHolder.tvName.setTextColor(mContext.getResources().getColor(R.color.customer_item_frozen));
            viewHolder.tvTel.setTextColor(mContext.getResources().getColor(R.color.customer_item_frozen));
            viewHolder.tvLevel.setTextColor(mContext.getResources().getColor(R.color.customer_item_frozen));
            viewHolder.tvGroup.setTextColor(mContext.getResources().getColor(R.color.customer_item_frozen));
        } else {
            viewHolder.tvName.setTextColor(mContext.getResources().getColor(R.color.customer_item_normal));
            viewHolder.tvTel.setTextColor(mContext.getResources().getColor(R.color.customer_item_normal));
            viewHolder.tvLevel.setTextColor(mContext.getResources().getColor(R.color.customer_item_normal));
            viewHolder.tvGroup.setTextColor(mContext.getResources().getColor(R.color.customer_item_normal));
        }*/

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
//        viewHolder.tvCardNum.setText(String.format(mContext.getString(R.string.beauty_customer_list_item_customer_card_num , 3)));
//        setupLastConsumeView(viewHolder , "2018-04-01 21:01:01");
        setupVipICon(viewHolder, bean);
    }

//    private void setupLastConsumeView(ViewHolder holder , String time){
//        String[] timeSub = time.split(" ");
//        holder.tvTimeYYMMDD.setText(timeSub[0]);
//        holder.tvTimeHHmmss.setText(timeSub[1]);
//    }

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

    /**
     * 刷新全部数据 并 重置选中位置
     */
    public void notifyDataAll() {
        resetCondiction();
        notifyDataSetChanged();
    }

    /**
     * 重置选中位置
     */
    private void resetCondiction() {
        mCurrentCustomerID = null;
        mCurrentPosition = RecyclerView.NO_POSITION;
    }

    /**
     * @description (ViewHolder 绑定Item点击事件 和 item 长按事件)
     * @time 2015年6月1日
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvFaceCode, tvTel, tvGroup, tvLevel /* , tvCardNum ,tvTimeYYMMDD , tvTimeHHmmss */;

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
//            tvCardNum = (TextView) itemView.findViewById(R.id.card_num);
//            tvTimeYYMMDD = (TextView) itemView.findViewById(R.id.time_year);
//            tvTimeHHmmss = (TextView) itemView.findViewById(R.id.time_date);

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
                            /*if (mCurrentCustomerID != null && mCurrentCustomerID == bean.customerId){
                                return;
                            }*/
                            mCurrentCustomerID = bean.customerId; // 记录当前选中项目
                            mOnRecyclerViewListener.onItemClickListener(bean);
                            notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }


    /**
     * 会员等级 key为等级的id，value为等级id对应的name
     *
     * @param levelMap <id, name>
     */
    public void setLevelMap(TreeMap<String, String> levelMap) {
        this.mLevelMap = levelMap;
    }

    /**
     * 分组 key为分组的id，value为分组id对应的name
     *
     * @param groupMap <id, name>
     */
    public void setGroupMap(TreeMap<String, String> groupMap) {
        this.mGroupMap = groupMap;
    }
}
