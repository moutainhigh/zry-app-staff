package com.zhongmei.bty.mobilepay.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.PayResultTool;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.yunfu.context.util.Utils;



public class BaiNuoAllErrorDialog extends Dialog implements View.OnClickListener {
    private String TAG = BaiNuoAllErrorDialog.class.getSimpleName();
    private ImageView mCloseBT;    private LinearLayout mErrorList;    private PaymentVo mPaymentVo;    private PayResp mPayResp;    private IPayOverCallback mCallBack;
    private IPaymentInfo mPaymentInfo;
    private BaiNuoAllErrorDialog(Context context, int theme) {
        super(context, theme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pay_all_bainuo_error_dialog_layout);    }

    public BaiNuoAllErrorDialog(Activity context, IPaymentInfo paymentInfo, PaymentVo paymentVo, PayResp payResp, IPayOverCallback callBack) {
        this(context, R.style.custom_alert_dialog);
        this.mPaymentVo = paymentVo;
        this.mPayResp = payResp;
        this.mCallBack = callBack;
        this.mPaymentInfo = paymentInfo;
        initView();
    }

    private void initView() {
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mErrorList = (LinearLayout) findViewById(R.id.pay_error_list);
        mCloseBT = (ImageView) findViewById(R.id.pay_error_close_button);
        mCloseBT.setOnClickListener(this);
        this.setOnDismissListener(dismisslistener);
        PayResultTool resultTool = new PayResultTool(mPaymentVo, mPayResp);
        if (!Utils.isEmpty(resultTool.getPaymentItemList(false))) {
            String couponNoLabel = getContext().getString(R.string.coupon_number) + "：";
            for (PaymentItem item : resultTool.getPaymentItemList(false)) {
                PayResp.PItemResults result = resultTool.getPItemResultsByPaymentItemUUid(item.getUuid());
                if (result != null && result.getResultMsg() != null)
                    mErrorList.addView(BaiNuoPartErrorDialog.getErrorItemView(getContext(), couponNoLabel + item.getRelateId(), result.getResultMsg()));
            }
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.pay_error_close_button) {            this.dismiss();
        }
    }

    OnDismissListener dismisslistener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            try {
                                DisplayServiceManager.updateDisplayPay(getContext().getApplicationContext(), mPaymentInfo.getActualAmount());
                if (mCallBack != null) {
                    mCallBack.onFinished(true, 0);
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    };
}
