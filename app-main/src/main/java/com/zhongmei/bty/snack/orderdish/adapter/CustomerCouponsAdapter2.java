package com.zhongmei.bty.snack.orderdish.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;
import com.zhongmei.yunfu.db.entity.discount.CoupRule;
import com.zhongmei.yunfu.db.enums.CouponType;
import com.zhongmei.bty.dinner.orderdish.CouponDatailDialog;

/**
 * 仅用于礼品券
 *
 * @Date：2015-8-7 下午3:28:03
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class CustomerCouponsAdapter2 extends BaseAdapter {
    protected Context mContext;

    private LayoutInflater mInflater;

    private CouponType mType;
    ;

    private Drawable item_bg;

    private List<CouponVo> mData;

    public CustomerCouponsAdapter2(Context context, List<CouponVo> data, CouponType type) {
        this.mContext = context;
        this.mData = data;
        this.setmType(type);
        this.mInflater = LayoutInflater.from(mContext);
        item_bg = context.getResources().getDrawable(R.drawable.coupons_item_bg_gift);// 礼品
    }

    @Override
    public int getCount() {

        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int arg0) {

        return mData == null ? null : mData.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {

        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final CouponVo vo = mData.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.customer_coupons_grid_item2, parent, false);

            viewHolder.vMainContent = convertView.findViewById(R.id.v_main_content);

            viewHolder.tvGiftName = (TextView) convertView.findViewById(R.id.tv_coupons_gift_name);

            viewHolder.tvCouponsName = (TextView) convertView.findViewById(R.id.tv_coupons_type_name);

            viewHolder.tvTimeLimit = (TextView) convertView.findViewById(R.id.tv_time_limit);

            viewHolder.tvSelected = (TextView) convertView.findViewById(R.id.tv_selected);

            viewHolder.llDetail = (LinearLayout) convertView.findViewById(R.id.item_bottom_layout);
            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.llDetail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CouponDatailDialog.show(mContext, vo);
            }
        });
        if (vo.isEnabled()) {
            // 设置背景图
            viewHolder.vMainContent.setBackgroundDrawable(this.item_bg);
        } else {
            viewHolder.vMainContent.setBackgroundResource(R.drawable.coupons_item_bg_disable);
        }

        // 是否被选中
        if (vo.isSelected()) {
            viewHolder.tvSelected.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvSelected.setVisibility(View.GONE);
        }

        Resources res = mContext.getResources();

        viewHolder.tvTimeLimit.setText(res.getString(R.string.period_of_validity) + "" + DateUtil.formatDate(vo.getCouponInfo().getEndTime()));

        // 优惠券名称
        viewHolder.tvCouponsName.setText(vo.getCouponInfo().getName());
        // 礼品名称

        viewHolder.tvGiftName.setText(vo.getCouponInfo().getDishName() + "    " + 1);

        return convertView;
    }

    public List<CouponVo> getmData() {
        return mData;
    }

    public void setmData(List<CouponVo> mData) {
        this.mData = mData;
    }

    public CouponType getmType() {
        return mType;
    }

    public void setmType(CouponType mType) {
        this.mType = mType;
    }

    static class ViewHolder {

        View vMainContent;

        TextView tvGiftName;

        TextView tvCouponsName;

        TextView tvTimeLimit;

        TextView tvSelected;

        LinearLayout llDetail;
    }
}
