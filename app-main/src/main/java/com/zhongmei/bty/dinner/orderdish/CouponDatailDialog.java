package com.zhongmei.bty.dinner.orderdish;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;
import com.zhongmei.yunfu.db.entity.discount.CoupRule;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;

public class CouponDatailDialog extends Dialog {
    private Context mContext;
    private CouponVo mCouponVo;

    private TextView mNumber, mName, mType, mDate, mAmountLimit, mTime, mGifName, mFaceAmount, mDishInfo;

    public static void show(Context context, CouponVo vo) {
        CouponDatailDialog dialog = new CouponDatailDialog(context, vo);
        dialog.show();

    }

    public CouponDatailDialog(Context context, int theme) {
        super(context, theme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_coupon_detail_layout);    }

    public CouponDatailDialog(Context context, CouponVo vo) {
        this(context, R.style.custom_alert_dialog);
        this.mContext = context;
        this.mCouponVo = vo;
        this.initView();
    }


    private void initView() {
        mNumber = (TextView) findViewById(R.id.tv_number);        mName = (TextView) findViewById(R.id.tv_name);        mType = (TextView) findViewById(R.id.tv_type);        mFaceAmount = (TextView) findViewById(R.id.tv_face_amount);        mDate = (TextView) findViewById(R.id.tv_date);        mAmountLimit = (TextView) findViewById(R.id.tv_amount_limit);        mTime = (TextView) findViewById(R.id.tv_time);        mDishInfo = (TextView) findViewById(R.id.tv_dish_info);        mGifName = (TextView) findViewById(R.id.tv_gift_name);        LinearLayout numberLL = (LinearLayout) findViewById(R.id.ll_number);        LinearLayout timeLL = (LinearLayout) findViewById(R.id.ll_time);        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (mCouponVo != null) {
                        mName.setText(mCouponVo.getCouponInfo().getName());

                        mDate.setText(DateUtil.format(mCouponVo.getCouponInfo().getEndTime()));
                        String valueLimit = " ";            if (mCouponVo.getCouponInfo().getFullValue() != null) {
                valueLimit = mContext.getString(R.string.full) + " ¥" + mCouponVo.getCouponInfo().getFullValue().toString();
            }
            mAmountLimit.setText(valueLimit);

            if (mCouponVo.getCouponInfo() != null) {

                switch (mCouponVo.getCouponInfo().getCouponType()) {
                    case CASH:                        mType.setText(mContext.getString(R.string.coupon_type_cash));
                        mFaceAmount.setText("¥" + mCouponVo.getCouponInfo().getDiscountValue());
                        mGifName.setText("无");
                        break;
                    case DISCOUNT:                        mType.setText(mContext.getString(R.string.coupon_type_discount));
                        mFaceAmount.setText(mCouponVo.getCouponInfo().getDiscountValue().toString() + "折");
                        mGifName.setText("无");
                        break;
                    case REBATE:                        mType.setText(mContext.getString(R.string.coupon_type_rebate));
                        mFaceAmount.setText("¥" + mCouponVo.getCouponInfo().getDiscountValue());
                        mGifName.setText("无");
                        break;
                    case GIFT:                        mType.setText(mContext.getString(R.string.coupon_type_gift));
                                                mGifName.setText(mCouponVo.getCouponInfo().getDishName() + "  1");
                        break;
                    default:

                        break;
                }
            }
        }
    }
}