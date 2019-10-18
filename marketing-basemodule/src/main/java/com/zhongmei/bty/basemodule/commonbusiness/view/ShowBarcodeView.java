package com.zhongmei.bty.basemodule.commonbusiness.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;



public class ShowBarcodeView extends RelativeLayout implements View.OnClickListener {

        public static final int SHOW_QR_CODE_ING = 0;

        public static final int SHOW_QR_CODE_FAIL = 1;

        public static final int SHOW_QR_CODE_INVALID = 2;

        public static final int SHOW_SCAN_SUCCESS = 3;

        public static final int SHOW_SCAN_TO_CUSTOMER = 4;

        public static final int SHOW_QR_NOT_ASSIGN = 5;

    Context context;

    ImageView mShowBarcode;

    ImageView mErrorView;

    LinearLayout mErrorLayout;

    TextView mShowBarcodeHintTitle;

    Button mBtRetry;

    OnChickRetryListener chickRetryListener;

    public void setChickRetryListener(OnChickRetryListener chickRetryListener) {
        this.chickRetryListener = chickRetryListener;
    }

    public ShowBarcodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.pay_show_bar_code_view_layout, this, false);
        mShowBarcode = (ImageView) view.findViewById(R.id.showBarcode);
        mShowBarcodeHintTitle = (TextView) view.findViewById(R.id.show_barcode_hint_title);
        mBtRetry = (Button) view.findViewById(R.id.bt_retry);
        mErrorView = (ImageView) view.findViewById(R.id.iv_error);
        mErrorLayout = (LinearLayout) view.findViewById(R.id.ll_error);
        mBtRetry.setOnClickListener(this);
        this.addView(view);
    }

    public void setShowType(int showType) {
        switchShow(showType);
    }

    public void setShowQR(Bitmap bitmap) {
        mErrorLayout.setVisibility(View.GONE);
        mShowBarcode.setVisibility(View.VISIBLE);
        mShowBarcode.setImageBitmap(bitmap);
    }

    void switchShow(int type) {
        mErrorLayout.setVisibility(View.VISIBLE);
        mShowBarcode.setVisibility(View.GONE);
        switch (type) {
            case SHOW_QR_CODE_ING:
                mErrorView.setImageResource(R.drawable.get_bg_loading);
                mShowBarcodeHintTitle.setText(R.string.show_barcode_hint_qr_code_generating);
                mBtRetry.setVisibility(View.GONE);
                break;
            case SHOW_QR_CODE_FAIL:
                mErrorView.setImageResource(R.drawable.get_bq_fail);
                mShowBarcodeHintTitle.setText(R.string.show_barcode_hint_qr_code_generation_fails);
                mBtRetry.setVisibility(View.VISIBLE);
                break;
            case SHOW_QR_CODE_INVALID:
                mErrorView.setImageResource(R.drawable.pay_qr_code_timeout);
                mShowBarcodeHintTitle.setText(R.string.show_barcode_hint_qr_code_Invalid);
                mBtRetry.setVisibility(View.VISIBLE);
                break;
            case SHOW_SCAN_SUCCESS:
                mErrorView.setImageResource(R.drawable.pay_online_scan_over_icon);
                mShowBarcodeHintTitle.setText(context.getString(R.string.show_barcode_hint_scan_success) + "\n" + context.getString(R.string.show_barcode_hint_waiting));
                mBtRetry.setVisibility(View.GONE);
                break;
            case SHOW_SCAN_TO_CUSTOMER:
                mErrorView.setImageResource(R.drawable.scan_gun);
                mShowBarcodeHintTitle.setText(context.getString(R.string.show_barcode_hint_use_scannar) + "\n" + context.getString(R.string.show_barcode_hint_scan));
                mBtRetry.setVisibility(View.GONE);
                break;
            case SHOW_QR_NOT_ASSIGN:
                mErrorView.setImageResource(R.drawable.mobile_pay_not_assign_type_img);
                mShowBarcodeHintTitle.setText(R.string.pay_mobile_pay_not_assign_type_hint);
                mBtRetry.setVisibility(View.GONE);
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) return;
        if (chickRetryListener != null) {
            chickRetryListener.retry();
        }
    }

    public interface OnChickRetryListener {
        void retry();
    }
}
