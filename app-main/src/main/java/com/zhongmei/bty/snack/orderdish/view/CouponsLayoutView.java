package com.zhongmei.bty.snack.orderdish.view;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.snack.orderdish.adapter.CustomerCouponsAdapter;
import com.zhongmei.bty.snack.orderdish.adapter.CustomerCouponsAdapter2;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;
import com.zhongmei.yunfu.db.enums.CouponType;
import com.zhongmei.yunfu.util.ToastUtil;

@EViewGroup(R.layout.customer_coupons_layout_view)
public class CouponsLayoutView extends LinearLayout {
    @ViewById
    TextView titleTV;

    @ViewById
    CouponsGridView gridView;

    private Context mContext;

    private CouponType mType;

    private BaseAdapter mAdapter;

    private List<CouponVo> mData;

    private CouponSelectedCallback mCallback;

    public CouponsLayoutView(Context context, CouponSelectedCallback callback, CouponType type) {
        super(context);
        mContext = context;
        mType = type;
        mCallback = callback;
    }

    public CouponsLayoutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public CouponsLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @AfterViews
    public void init() {
        switch (this.mType) {

            case REBATE:// 满减券
                titleTV.setText(mContext.getString(R.string.coupon_type_rebate));
                break;
            case DISCOUNT:// 折扣券
                titleTV.setText(mContext.getString(R.string.coupon_type_discount));
                break;
            case GIFT:// 礼品券
                titleTV.setText(mContext.getString(R.string.coupon_type_gift));
                break;
            case CASH:// 现金券
                titleTV.setText(mContext.getString(R.string.coupon_type_cash));
                break;
            default:
                break;
        }
    }

    // 可以设置显示列数
    public void setData(List<CouponVo> data, int numColumns) {
        if (gridView != null) {
            gridView.setNumColumns(numColumns);
            setData(data);
        }
    }

    // 默认显示3列
    public void setData(List<CouponVo> data) {
        mData = data;
        if (mType.equals(CouponType.GIFT)) {
            mAdapter = new CustomerCouponsAdapter2(mContext, mData, mType);
        } else {
            mAdapter = new CustomerCouponsAdapter(mContext, mData, mType);
        }

        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mData != null) {
                    CouponVo vo = mData.get(position);
                    if (!vo.isEnabled())
                        return;

                    if (vo.getCoupon() == null) {
                        ToastUtil.showShortToast(R.string.beauty_coupon_enable_user);
                        return;
                    }
                    if (vo.isSelected()) {
                        vo.setSelected(false);
                    } else {
                        vo.setSelected(true);
                    }
                    if (mCallback != null)
                        mCallback.onCouponSelected(vo, mType);
                }
            }
        });

    }

    public void removeGiftCoupon(Long id) {
        for (CouponVo couponVo : mData) {
            if (id.compareTo(couponVo.getCouponInfo().getId()) == 0)
                couponVo.setSelected(false);
        }
        updateViews();
    }

    /**
     * 移除所有选中的券
     */
    public void removeAllSelectCoupon() {
        for (CouponVo couponVo : mData) {
            couponVo.setSelected(false);
        }
        updateViews();
    }


    /**
     * 批量移券
     */
    public void removeCoupns(List<Long> ids) {
        if (ids != null) {
            for (CouponVo couponVo : mData) {
                for (Long id : ids) {
                    if (id.compareTo(couponVo.getCouponInfo().getId()) == 0)
                        couponVo.setSelected(false);
                }

            }
            updateViews();
        }

    }

    public void updateViews() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public CouponSelectedCallback getmCallback() {
        return mCallback;
    }

    public void setmCallback(CouponSelectedCallback mCallback) {
        this.mCallback = mCallback;
    }

    public interface CouponSelectedCallback {

        public void onCouponSelected(CouponVo vo, CouponType type);

    }
}
