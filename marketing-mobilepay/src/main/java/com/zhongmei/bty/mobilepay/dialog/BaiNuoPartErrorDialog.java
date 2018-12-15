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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.PayResultTool;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.context.util.Utils;

/**
 * 百糯券部分成功部分失败界面
 * Created by demo on 2018/12/15
 */

public class BaiNuoPartErrorDialog extends Dialog implements View.OnClickListener {
    private String TAG = BaiNuoPartErrorDialog.class.getSimpleName();
    private final static int itemtextcolor = Color.parseColor("#999999");
    private TextView mPayAmountTV;//显示 本次付款
    private ImageView mCloseBT;//关闭按钮
    private LinearLayout mOkList;//成功列表
    private LinearLayout mErrorList;//失败列表
    private PaymentVo mPaymentVo;//支付信息
    private PayResp mPayResp;//支付返回信息
    private IPayOverCallback mCallBack;
    private IPaymentInfo mPaymentInfo;//
    private Context mContext;

    private BaiNuoPartErrorDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pay_part_bainuo_error_dialog_layout);//新收银失败界面
    }

    public BaiNuoPartErrorDialog(Activity context, IPaymentInfo paymentInfo, PaymentVo paymentVo, PayResp payResp, IPayOverCallback callBack) {
        this(context, R.style.custom_alert_dialog);
        this.mPaymentVo = paymentVo;
        this.mPayResp = payResp;
        this.mCallBack = callBack;
        this.mPaymentInfo = paymentInfo;
        initView();

    }

    private void initView() {
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPayAmountTV = (TextView) findViewById(R.id.pay_ok_amount);
        mCloseBT = (ImageView) findViewById(R.id.pay_error_close_button);
        mOkList = (LinearLayout) findViewById(R.id.pay_ok_list);
        mErrorList = (LinearLayout) findViewById(R.id.pay_error_list);
        mCloseBT.setOnClickListener(this);
        this.setOnDismissListener(dismisslistener);
        PayResultTool resultTool = new PayResultTool(mPaymentVo, mPayResp);
        mPayAmountTV.setText(CashInfoManager.formatCash(resultTool.getPayOkAmount()));
        String couponNoLabel = getContext().getString(R.string.coupon_number) + "：";
        //成功列表
        if (!Utils.isEmpty(resultTool.getPaymentItemList(true))) {
            for (PaymentItem item : resultTool.getPaymentItemList(true)) {
                mOkList.addView(getItemView(getContext(), couponNoLabel + item.getRelateId(), CashInfoManager.formatCash(item.getUsefulAmount().doubleValue())));
            }
        }
        //失败列表
        if (!Utils.isEmpty(resultTool.getPaymentItemList(false))) {
            for (PaymentItem item : resultTool.getPaymentItemList(false)) {
                PayResp.PItemResults result = resultTool.getPItemResultsByPaymentItemUUid(item.getUuid());
                if (result != null && result.getResultMsg() != null)
                    mErrorList.addView(getErrorItemView(getContext(), couponNoLabel + item.getRelateId(), result.getResultMsg()));
            }
        }
        /*PayMessage payMessage = DisplayServiceManager.buildPayMessage(PayMessage.PAY_STATE_FAIL, "");
        payMessage.setLabel(this.getContext().getString(R.string.coupon_part_error_title));
        DisplayServiceManager.updateDisplay(this.getContext().getApplicationContext(), payMessage);*/
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.pay_error_close_button) {
            this.dismiss();
        }
    }

    public static View getErrorItemView(Context context, String couponNo, String errorMs) {
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 40));
        layoutParams.setMargins(0, 0, 0, 0);

        linearLayout.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        String content = couponNo + "   " + errorMs;

        TextView textView = new TextView(context);
        textView.setTextSize(DensityUtil.sp2px(context, 16));
        textView.setTextColor(itemtextcolor);
        textView.setText(content);
        textView.setLayoutParams(layoutParams1);

        linearLayout.addView(textView);

        return linearLayout;
    }

    public View getItemView(Context context, String text0, String text1) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 40));
        layoutParams.setMargins(0, 0, 0, 0);
        RelativeLayout relativeLayout = new RelativeLayout(context);

        relativeLayout.setLayoutParams(layoutParams);

        for (int i = 0; i < 2; i++) {
            RelativeLayout.LayoutParams layoutParams1 =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(context);
            textView.setTextSize(DensityUtil.sp2px(context, 16));
            textView.setTextColor(itemtextcolor);
            switch (i) {
                case 0:
                    layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                    layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    if (text0 == null) {
                        textView.setVisibility(View.GONE);
                    } else {
                        textView.setText(text0);
                    }
                    textView.setLayoutParams(layoutParams1);
                    relativeLayout.addView(textView);
                    break;
                case 1:
                    layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                    layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    if (text1 == null) {
                        textView.setVisibility(View.GONE);
                    } else {
                        textView.setPadding(0, 0, DensityUtil.dip2px(context, 10), 0);
                        textView.setText(text1);
                    }
                    textView.setLayoutParams(layoutParams1);
                    relativeLayout.addView(textView);
                    break;
                default:
                    break;
            }
        }
        return relativeLayout;
    }

    OnDismissListener dismisslistener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            try {
                //刷新副2屏应收
                DisplayServiceManager.updateDisplayPay(getContext().getApplicationContext(), mPaymentInfo.getActualAmount());
                if (mCallBack != null) {
                    mCallBack.onFinished(true, 1000);
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    };
}
