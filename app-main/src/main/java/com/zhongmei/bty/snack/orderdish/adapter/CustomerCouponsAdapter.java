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

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.discount.CoupRule;
import com.zhongmei.yunfu.db.enums.CouponType;
import com.zhongmei.bty.dinner.orderdish.CouponDatailDialog;

/**
 * 使用于积分卷，优惠劵，折扣卷
 *
 * @Date：2015-8-7 下午3:28:31
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class CustomerCouponsAdapter extends BaseAdapter {
    protected Context mContext;

    private LayoutInflater mInflater;

    private CouponType mType;

    private List<CouponVo> mData;

    private Drawable item_bg;

    public CustomerCouponsAdapter(Context context, List<CouponVo> data, CouponType type) {
        this.mContext = context;
        this.mData = data;
        this.mType = type;
        this.mInflater = LayoutInflater.from(mContext);
        switch (this.mType) {
            case CASH:
                item_bg = context.getResources().getDrawable(R.drawable.coupons_item_bg_cash);// 代金券
                break;
            case DISCOUNT:
                item_bg = context.getResources().getDrawable(R.drawable.coupons_item_bg_discount);// 折扣
                break;
            case REBATE:
                item_bg = context.getResources().getDrawable(R.drawable.coupons_item_bg_rebate);// 满减
                break;
            default:
                item_bg = context.getResources().getDrawable(R.drawable.coupons_item_bg_rebate);
                break;
        }
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

            convertView = mInflater.inflate(R.layout.customer_coupons_grid_item, parent, false);

            viewHolder.vMainContent = convertView.findViewById(R.id.v_main_content);

            viewHolder.tvYuan = (TextView) convertView.findViewById(R.id.tv_yuan);

            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_coupons_price);

            viewHolder.tvDiscount = (TextView) convertView.findViewById(R.id.tv_discount);

            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);

            viewHolder.tvValueLimit = (TextView) convertView.findViewById(R.id.tv_value_limit);

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

        Resources res = MainApplication.getInstance().getResources();

        String valueLimit = " ";
        if (vo.getCouponInfo().getFullValue() != null) {
            valueLimit = res.getString(R.string.full) + " ¥" + vo.getCouponInfo().getFullValue().toString();
        }

        //金额限制
        viewHolder.tvValueLimit.setText(valueLimit);
        // 有效期
//		if(vo.getCouponInfo().isFixedPeriod()){
//			String time = String.format(mContext.getString(R.string.customer_send_coupon_period_of_validity),vo.getCouponInfo().getValidDayNum());
//			String limit = vo.getCouponInfo().isCurDay() ? mContext.getString(R.string.customer_send_coupon_today_valid) : mContext.getString(R.string.customer_send_coupon_today_invalid);
//			viewHolder.tvTimeLimit.setText(time + limit);
//		}else {
        viewHolder.tvTimeLimit.setText(res.getString(R.string.period_of_validity) + ":" + DateUtil.format(vo.getCouponInfo().getEndTime()));
//		}
        viewHolder.tvYuan.setText(ShopInfoCfg.getInstance().getCurrencySymbol());
        switch (vo.getCouponInfo().getCouponType()) {
            case CASH:// 代金券
                viewHolder.tvYuan.setVisibility(View.VISIBLE);
                viewHolder.tvName.setText(vo.getCouponInfo().getName());
                viewHolder.tvPrice.setText(vo.getCouponInfo().getDiscountValue() + "");

                viewHolder.tvDiscount.setVisibility(View.GONE);
                break;
            case DISCOUNT:// 折扣
                viewHolder.tvYuan.setVisibility(View.GONE);
                viewHolder.tvName.setText(vo.getCouponInfo().getName());
                viewHolder.tvPrice.setText(vo.getCouponInfo().getDiscountValue().toString());
                viewHolder.tvDiscount.setVisibility(View.VISIBLE);
                break;
            case REBATE:// 满减
                viewHolder.tvYuan.setVisibility(View.VISIBLE);
                viewHolder.tvName.setText(vo.getCouponInfo().getName());
                viewHolder.tvPrice.setText(vo.getCouponInfo().getDiscountValue().toString());
                viewHolder.tvDiscount.setVisibility(View.GONE);
                break;
            default:

                break;
        }

        return convertView;
    }

    static class ViewHolder {

        View vMainContent;

        TextView tvYuan;

        TextView tvPrice;

        TextView tvDiscount;

        TextView tvName;

        TextView tvValueLimit;

        TextView tvTimeLimit;

        TextView tvSelected;

        LinearLayout llDetail;
    }
}
