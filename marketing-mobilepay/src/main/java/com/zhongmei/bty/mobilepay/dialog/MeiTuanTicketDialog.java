package com.zhongmei.bty.mobilepay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.message.TuanGouCouponDetail;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */

public class MeiTuanTicketDialog extends Dialog implements View.OnClickListener {
    private TuanGouCouponDetail mTuanGouCouponDetail;
    private SetMeiTuanCouponsListener mListener;
    private TextView mTitleTV;
    private EditText mCountET;
    private ImageView mMinusBT;
    private ImageView mAddBT;
    private int mUsedCount;
    private double mUnPayValue;
    private Context mContext;
    private boolean isLimitMax = true;//add v8.3限制溢出，默认限制

    public void setLimitMax(boolean limitMax) {
        isLimitMax = limitMax;
    }

    public MeiTuanTicketDialog(Context context, int theme) {
        super(context, theme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.pay_meituan_tickets_dialog_layout);
    }

    public MeiTuanTicketDialog(Context context, int defautlCount, double unPayValue, TuanGouCouponDetail tuanGouCouponDetail, SetMeiTuanCouponsListener listener) {
        this(context, R.style.custom_alert_dialog);
        this.mContext = context;
        this.mTuanGouCouponDetail = tuanGouCouponDetail;
        this.mUsedCount = defautlCount;
        this.mUnPayValue = unPayValue;
        this.mListener = listener;
        this.init();
    }

    private void init() {
        mTitleTV = (TextView) findViewById(R.id.tv_meituan_count_label);
        mCountET = (EditText) findViewById(R.id.coupon_num_tv);
        mMinusBT = (ImageView) findViewById(R.id.minus_coupon_iv);
        mAddBT = (ImageView) findViewById(R.id.add_coupon_iv);
        ((TextView) findViewById(R.id.meituan_coupon_name)).setText(mTuanGouCouponDetail.getDealTitle());
        ((TextView) findViewById(R.id.meituan_coupon_amount)).setText(Utils.formatPrice(mTuanGouCouponDetail.getMarketPrice().doubleValue()));
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        mMinusBT.setOnClickListener(this);
        mAddBT.setOnClickListener(this);
        mCountET.setText(mUsedCount + "");
        String usableCountStr = mTuanGouCouponDetail.getMaxConsume() + "";
        SpannableStringBuilder builder =
                new SpannableStringBuilder(String.format(mContext.getResources().getString(R.string.pay_setting_meituan_count), usableCountStr));
        int startIndex = 4;
        int endIndex = startIndex + usableCountStr.length();
        builder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_red_2)),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTitleTV.setText(builder);
        updateButtonEnable();
    }

    private void minus() {
        if (mUsedCount > mTuanGouCouponDetail.getMinConsume()) {
            mUsedCount--;
            mCountET.setText(mUsedCount + "");
            updateButtonEnable();
        }
    }

    private void add() {
        //
        if (mUsedCount < mTuanGouCouponDetail.getMaxConsume()) {
            mUsedCount++;
            mCountET.setText(mUsedCount + "");
            updateButtonEnable();
        }
    }

    private void updateButtonEnable() {
        if (mUsedCount < mTuanGouCouponDetail.getMaxConsume() && !isPayMore()) {
            mAddBT.setImageResource(R.drawable.ic_number_plus);
            mAddBT.setEnabled(true);
        } else {
            mAddBT.setImageResource(R.drawable.ic_number_plus_gray);
            mAddBT.setEnabled(false);
        }

        if (mUsedCount > mTuanGouCouponDetail.getMinConsume()) {
            mMinusBT.setImageResource(R.drawable.ic_number_minus);
            mMinusBT.setEnabled(true);
        } else {
            mMinusBT.setImageResource(R.drawable.ic_number_minus_gray);
            mMinusBT.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.minus_coupon_iv) {
            minus();
        } else if (vId == R.id.add_coupon_iv) {
            add();
        } else if (vId == R.id.btn_close) {
            this.dismiss();
        } else if (vId == R.id.btn_ok) {
            BigDecimal value = new BigDecimal(mUsedCount).multiply(mTuanGouCouponDetail.getMarketPrice());
            //如果溢收大于券面值,不支持 modify 20170412
            if (isLimitMax && ((value.doubleValue() - this.mUnPayValue) > mTuanGouCouponDetail.getMarketPrice().doubleValue())) {
                ToastUtil.showShortToast(getContext().getString(R.string.toast_excessive_charge_limit));
                return;
            }
            if (mListener != null && mUsedCount > 0) {
                mListener.setMeiTuanCoupons(mTuanGouCouponDetail, mUsedCount);
            }
            this.dismiss();
        }
    }

    private boolean isPayMore() {
        BigDecimal value = new BigDecimal(mUsedCount).multiply(mTuanGouCouponDetail.getMarketPrice());
        return value.doubleValue() >= this.mUnPayValue;
    }

    public interface SetMeiTuanCouponsListener {
        public void setMeiTuanCoupons(TuanGouCouponDetail tuanGouCouponDetail, int usedCount);
    }
}
