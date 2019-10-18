package com.zhongmei.bty.mobilepay.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.transition.Scene;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;



public class PayDepositPromptDialog extends Dialog implements View.OnClickListener {
    private static final int MSG_WAT_PAYDEPOSIT = 1;
    private ImageView mCloseBT;    private IPaymentInfo mPaymentInfo;    private TextView mDepositAmountTV;    private Activity mContext;
    private DoPayApi mDoPayApi;

    public static void start(Activity context, DoPayApi doPayApi, IPaymentInfo paymentInfo) {
        PayDepositPromptDialog dialog = new PayDepositPromptDialog(context, doPayApi, paymentInfo);
        dialog.show();
    }

    private PayDepositPromptDialog(Context context, int theme) {
        super(context, theme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pay_deposit_prompt_dialog_layout);
    }

    public PayDepositPromptDialog(Activity context, DoPayApi doPayApi, IPaymentInfo paymentInfo) {
        this(context, R.style.custom_alert_dialog);
        this.mPaymentInfo = paymentInfo;
        this.mContext = context;
        this.mDoPayApi = doPayApi;
        initView();
    }

    private void initView() {
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mCloseBT = (ImageView) findViewById(R.id.close_button);
        mDepositAmountTV = (TextView) findViewById(R.id.amount_text);
        mDepositAmountTV.setText(CashInfoManager.formatCash(mPaymentInfo.getTradeVo().getTradeDeposit().getDepositPay().doubleValue()));
        mCloseBT.setOnClickListener(this);
        this.setOnDismissListener(onDismissListener);
        delayDismiss();
    }

    private void delayDismiss() {
        Message ms = mHandler.obtainMessage();
        ms.what = MSG_WAT_PAYDEPOSIT;
        mHandler.sendMessageDelayed(ms, 2000);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.close_button) {
            mHandler.removeCallbacksAndMessages(null);
            this.dismiss();
        }
    }

    OnDismissListener onDismissListener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            mHandler.removeCallbacksAndMessages(null);
        }
    };
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WAT_PAYDEPOSIT:
                    this.removeCallbacksAndMessages(null);
                    dismiss();
                    PayScene payScene = PayScene.SCENE_CODE_BUFFET_DEPOSIT;
                    if (mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_BAKERY_BOOKING) {
                        payScene = PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT;
                    }
                    DoPayUtils.gotoPayActivity(mContext, mDoPayApi, mPaymentInfo.getTradeVo(), mPaymentInfo.isOrderCenter(), payScene);
                    break;
                default:
                    break;
            }
        }
    };
}
