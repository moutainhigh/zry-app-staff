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

/**
 * Created by demo on 2018/12/15
 * 百度糯米券全部失败界面
 */

public class BaiNuoAllErrorDialog extends Dialog implements View.OnClickListener {
    private String TAG = BaiNuoAllErrorDialog.class.getSimpleName();
    private ImageView mCloseBT;//关闭按钮
    private LinearLayout mErrorList;//失败列表
    private PaymentVo mPaymentVo;//支付信息
    private PayResp mPayResp;//支付返回信息
    private IPayOverCallback mCallBack;
    private IPaymentInfo mPaymentInfo;//

    private BaiNuoAllErrorDialog(Context context, int theme) {
        super(context, theme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pay_all_bainuo_error_dialog_layout);//新收银失败界面
    }

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
        /*PayMessage payMessage = DisplayServiceManager.buildPayMessage(PayMessage.PAY_STATE_FAIL, "");
        payMessage.setLabel(this.getContext().getString(R.string.pay_error));
        DisplayServiceManager.updateDisplay(this.getContext().getApplicationContext(), payMessage);*/
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.pay_error_close_button) {//关闭
            this.dismiss();
        }
    }

    OnDismissListener dismisslistener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            try {
                //刷新副2屏应收
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
