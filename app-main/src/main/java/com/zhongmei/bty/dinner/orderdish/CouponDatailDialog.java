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
        setContentView(R.layout.dialog_coupon_detail_layout);//券详情界面
    }

    public CouponDatailDialog(Context context, CouponVo vo) {
        this(context, R.style.custom_alert_dialog);
        this.mContext = context;
        this.mCouponVo = vo;
        this.initView();
    }


    private void initView() {
        mNumber = (TextView) findViewById(R.id.tv_number);//券码
        mName = (TextView) findViewById(R.id.tv_name);//券名称
        mType = (TextView) findViewById(R.id.tv_type);//券类型
        mFaceAmount = (TextView) findViewById(R.id.tv_face_amount);//券面额
        mDate = (TextView) findViewById(R.id.tv_date);//券有效期
        mAmountLimit = (TextView) findViewById(R.id.tv_amount_limit);//金额条件
        mTime = (TextView) findViewById(R.id.tv_time);//券可用时段
        mDishInfo = (TextView) findViewById(R.id.tv_dish_info);//券参数商品
        mGifName = (TextView) findViewById(R.id.tv_gift_name);//礼品名称
        LinearLayout numberLL = (LinearLayout) findViewById(R.id.ll_number);//券码行
        LinearLayout timeLL = (LinearLayout) findViewById(R.id.ll_time);//可用是的行
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (mCouponVo != null) {
            //券名称
            mName.setText(mCouponVo.getCouponInfo().getName());

            //有效期
            mDate.setText(DateUtil.format(mCouponVo.getCouponInfo().getEndTime()));
            //使用限制
            String valueLimit = " ";//金额限制
            if (mCouponVo.getCouponInfo().getFullValue() != null) {
                valueLimit = mContext.getString(R.string.full) + " ¥" + mCouponVo.getCouponInfo().getFullValue().toString();
            }
            mAmountLimit.setText(valueLimit);

            if (mCouponVo.getCouponInfo() != null) {

                switch (mCouponVo.getCouponInfo().getCouponType()) {
                    case CASH:// 代金券
                        mType.setText(mContext.getString(R.string.coupon_type_cash));
                        mFaceAmount.setText("¥" + mCouponVo.getCouponInfo().getDiscountValue());
                        mGifName.setText("无");
                        break;
                    case DISCOUNT:// 折扣
                        mType.setText(mContext.getString(R.string.coupon_type_discount));
                        mFaceAmount.setText(mCouponVo.getCouponInfo().getDiscountValue().toString() + "折");
                        mGifName.setText("无");
                        break;
                    case REBATE:// 满减
                        mType.setText(mContext.getString(R.string.coupon_type_rebate));
                        mFaceAmount.setText("¥" + mCouponVo.getCouponInfo().getDiscountValue());
                        mGifName.setText("无");
                        break;
                    case GIFT:// 礼品券
                        mType.setText(mContext.getString(R.string.coupon_type_gift));
                        // 礼品名称
                        mGifName.setText(mCouponVo.getCouponInfo().getDishName() + "  1");
                        break;
                    default:

                        break;
                }
            }
        }
    }
}