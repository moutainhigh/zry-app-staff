package com.zhongmei.bty.mobilepay.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.meituan.IGroupBuyingCoupon;
import com.zhongmei.bty.mobilepay.bean.meituan.MeituanDishItem;
import com.zhongmei.bty.mobilepay.bean.meituan.MeituanDishItemVo;

import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.List;


public class MeiTuanCouponDishView extends LinearLayout {
        TextView mCouponNameTV;
        TextView mDishRelateTV;
        LinearLayout mDishLL;
        TextView mDishsTV;
    boolean isOpen = false;
    MeituanDishItemVo mDishItemVo;
    IGroupBuyingCoupon mCouponInfo;

    public MeiTuanCouponDishView(Context context) {
        this(context, null);
    }

    public MeiTuanCouponDishView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeiTuanCouponDishView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MeiTuanCouponDishView(Context context, IGroupBuyingCoupon meituanCouponDetail, MeituanDishItemVo dishs) {
        this(context);
        inflate(context, R.layout.pay_meituan_coupon_dishview_layout, this);
        this.mDishItemVo = dishs;
        this.mCouponInfo = meituanCouponDetail;
        mCouponNameTV = (TextView) findViewById(R.id.ticket_name);
        mDishRelateTV = (TextView) findViewById(R.id.ticket_dish_relate);
        mDishLL = (LinearLayout) findViewById(R.id.dish_list_ll);
        mDishsTV = (TextView) findViewById(R.id.ticket_dish_list_tv);
        initView();
    }


    public void initView() {
        mDishRelateTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isOpen = !isOpen;
                mDishRelateTV.setSelected(isOpen);
                if (isOpen) {
                    mDishLL.setVisibility(View.VISIBLE);
                } else {
                    mDishLL.setVisibility(View.GONE);
                }
            }
        });
        mDishLL.setVisibility(View.GONE);
        if (mCouponInfo != null) {
            String name = mCouponInfo.getDealTitle() + "(" + getTime(mCouponInfo.getBeginTime()) + "-" + getTime(mCouponInfo.getEndTime()) + ")";
            if (TextUtils.isEmpty(name)) {
                mCouponNameTV.setText("");
            } else {
                mCouponNameTV.setText(name);
            }
        } else {
            mCouponNameTV.setText("");
        }
                if (mDishItemVo != null && Utils.isNotEmpty(mDishItemVo.getMeituanItemVoList())) {
            mDishRelateTV.setVisibility(View.VISIBLE);
            mDishsTV.setText(getDishsStr(mDishItemVo.getMeituanItemVoList()));
        } else {
            mDishRelateTV.setVisibility(View.GONE);
        }
    }

    private String getTime(Long timeStap) {
        if (timeStap != null)
            return DateTimeUtils.formatDate(timeStap, "yyyy.MM.dd");
        return "";
    }


    private String getDishsStr(List<MeituanDishItem> dishItemList) {

        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (MeituanDishItem dishItem : dishItemList) {
            if (count > 0) {
                sb.append("  /  ");
            }
            sb.append(dishItem.dishName);
            sb.append(" ");
                        if (dishItem.deductionType == 2) {                if (dishItem.deductionAmount != null) {
                    sb.append(Utils.formatPrice(dishItem.deductionAmount.doubleValue()));
                }
            } else {                if (dishItem.price != null) {
                    sb.append(Utils.formatPrice(dishItem.price.doubleValue()));
                }
                if (dishItem.num != null) {
                    sb.append(" * ");
                    sb.append(Utils.transferDot2(dishItem.num.toString()));
                }
            }
                        count++;
        }
        return sb.toString();
    }
}
