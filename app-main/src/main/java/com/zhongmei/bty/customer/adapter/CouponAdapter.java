package com.zhongmei.bty.customer.adapter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
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

/**
 * 优惠券Adapter
 */
@SuppressLint("SimpleDateFormat")
public class CouponAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<CouponVo> mCouponList;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd");

    public CouponAdapter(Context context, List<CouponVo> couponList) {
        this.mContext = context;
        this.mCouponList = couponList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mCouponList != null) {
            return mCouponList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mCouponList != null) {
            return mCouponList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.customer_coupon_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.desc = (TextView) convertView.findViewById(R.id.desc);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.cashprice = (TextView) convertView.findViewById(R.id.cash_price);
            viewHolder.discount = (TextView) convertView.findViewById(R.id.discount);
            viewHolder.timeLimit = (TextView) convertView.findViewById(R.id.time_limit);
            viewHolder.vwLabel = convertView.findViewById(R.id.vwLabel);
            viewHolder.discountLayout = (LinearLayout) convertView.findViewById(R.id.discount_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mCouponList != null) {
            CouponVo vo = mCouponList.get(position);
            // 有效期需格式转换
            try {
//				Date start = formatter.parse(vo.getCouponInfo().getValidStartDate());
                String end = DateUtil.format(vo.getCouponInfo().getEndTime());
                viewHolder.timeLimit.setText(mContext.getString(R.string.customer_coupon_record_time, end));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (vo.getCoupon() != null) {
                switch (vo.getCoupon().getCouponType()) {
                    case CASH:// 现金券
                        viewHolder.cashprice.setText("¥" + vo.getCoupon().getDiscountValue());
                        viewHolder.name.setText(mContext.getString(R.string.cash_ticket_str));
                        viewHolder.vwLabel.setBackgroundResource(R.drawable.customer_coupon_label_purple);
                        viewHolder.discountLayout.setVisibility(View.GONE);
                        if (vo.getCoupon().getFullValue() != null) {
                            viewHolder.desc.setText(String.format(mContext.getString(R.string.discount_full_value), vo.getCoupon().getFullValue().toString()));
                        }
                        viewHolder.desc.setVisibility(View.VISIBLE);
                        viewHolder.price.setVisibility(View.GONE);
                        viewHolder.cashprice.setVisibility(View.VISIBLE);
                        break;
                    case DISCOUNT:// 折扣
                        viewHolder.discount.setText("" + vo.getCoupon().getDiscountValue());
                        if (vo.getCoupon().getFullValue() != null) {
                            viewHolder.desc.setText(String.format(mContext.getString(R.string.discount_full_value), vo.getCoupon().getFullValue().toString()));
                        }
                        viewHolder.name.setText(mContext.getString(R.string.discount_ticket_str));
                        viewHolder.vwLabel.setBackgroundResource(R.drawable.customer_coupon_label_blue);
                        viewHolder.price.setVisibility(View.GONE);
                        viewHolder.cashprice.setVisibility(View.GONE);
                        viewHolder.desc.setVisibility(View.VISIBLE);
                        viewHolder.discountLayout.setVisibility(View.VISIBLE);
                        break;
                    case REBATE:// 满减
                        viewHolder.discount.setText("¥" + vo.getCoupon().getDiscountValue());
                        if (vo.getCoupon().getFullValue() != null) {
                            viewHolder.desc.setText(String.format(mContext.getString(R.string.rebate_full_value), vo.getCoupon().getFullValue().toString()));
                        }
                        viewHolder.name.setText(mContext.getString(R.string.rebate_ticket_str));
                        viewHolder.vwLabel.setBackgroundResource(R.drawable.customer_coupon_label_yellow);
                        viewHolder.cashprice.setVisibility(View.GONE);
                        viewHolder.discountLayout.setVisibility(View.GONE);
                        viewHolder.price.setVisibility(View.VISIBLE);
                        viewHolder.desc.setVisibility(View.VISIBLE);
                        break;
                    case GIFT://礼品券
                        String giftName = vo.getCoupon().getDishName();
                        String giftCount = "1";

                        if (vo.getCoupon().getFullValue() != null) {
                            viewHolder.desc.setText(String.format(mContext.getString(R.string.gift_full_value), vo.getCoupon().getFullValue().toString()));
                        }
                        viewHolder.name.setText(mContext.getString(R.string.gift_ticket_str));
                        viewHolder.vwLabel.setBackgroundResource(R.drawable.customer_coupon_label_red);
                        viewHolder.price.setText(giftName + "  " + giftCount);
                        viewHolder.cashprice.setVisibility(View.GONE);
                        viewHolder.discountLayout.setVisibility(View.GONE);
                        viewHolder.price.setVisibility(View.VISIBLE);
                        viewHolder.desc.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        }


        return convertView;
    }

    class ViewHolder {
        View vwLabel;
        TextView name, desc, price, discount, timeLimit, cashprice;
        LinearLayout discountLayout;
    }

}
