package com.zhongmei.bty.mobilepay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart;
import com.zhongmei.bty.basemodule.trade.event.ActionCloseOrderDishActivity;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.yunfu.resp.ResponseObject;

import de.greenrobot.event.EventBus;

/**
 * Created by demo on 2018/12/15
 */

public class PayErrorDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = PayErrorDialog.class.getSimpleName();

    private FragmentActivity mContext;
    private TextView mPayAmountTV;//显示 本次付款
    private TextView mErrorReasonTV;// 显示失败原因
    private ImageView mCloseBT;//关闭按钮
    private Button mRePayBT;//再次尝试按钮
    private IPaymentInfo mPaymentInfo;//
    private String errorReason;
    private int errorCode;//支付失败码
    private IPayOverCallback mCallBack;
    private DoPayApi mDoPayApi;//add v8.9 jieou

    public PayErrorDialog(Context context, int theme) {
        super(context, theme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pay_error_dialog_layout);//新收银失败界面
    }

    public PayErrorDialog(FragmentActivity context, DoPayApi doPayApi, IPaymentInfo paymentInfo, String errorReason, IPayOverCallback callBack, int errCode) {
        this(context, R.style.custom_alert_dialog);
        this.mContext = context;
        this.mDoPayApi = doPayApi;
        this.mPaymentInfo = paymentInfo;
        this.errorReason = errorReason;
        this.mCallBack = callBack;
        this.errorCode = errCode;
        initView(paymentInfo);
        this.setOnDismissListener(listener);
        //查询已经支付的记录
        findPaymentVos(paymentInfo);
    }

    private void initView(IPaymentInfo paymentInfo) {
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPayAmountTV = (TextView) findViewById(R.id.pay_amount_text);
        mErrorReasonTV = (TextView) findViewById(R.id.pay_error_reason_text);
        mCloseBT = (ImageView) findViewById(R.id.pay_error_close_button);
        mRePayBT = (Button) findViewById(R.id.pay_error_repay_button);
        if (errorCode == ResponseObject.TRADE_HAS_PAID && mPaymentInfo.isDinner()) {
            if (mPaymentInfo.isOrderCenter()) {
                mRePayBT.setText(mContext.getString(R.string.pay_back_text));//返回
            } else {
                mRePayBT.setText(mContext.getString(R.string.pay_goto_table));//返回桌台
            }
            mCloseBT.setVisibility(View.INVISIBLE);//隐藏关闭按钮
        }
        mCloseBT.setOnClickListener(this);
        mRePayBT.setOnClickListener(this);
        mPayAmountTV.setText(CashInfoManager.formatCash(paymentInfo.getOtherPay().getGroupAmount()));
        mErrorReasonTV.setText(mContext.getString(R.string.pay_error_reason) + this.errorReason);
        /*PayMessage payMessage = DisplayServiceManager.buildPayMessage(PayMessage.PAY_STATE_FAIL, "");
        payMessage.setLabel(this.getContext().getString(R.string.pay_error));
        DisplayServiceManager.updateDisplay(this.getContext().getApplicationContext(), payMessage);*/
    }

    private void findPaymentVos(IPaymentInfo paymentInfo) {
        if (paymentInfo.getActualAmount() > 0 && paymentInfo.getTradeVo() != null && paymentInfo.getTradeVo().getTrade() != null) {
            DoPayUtils.findPaymentVoAsync(paymentInfo);
        }
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.pay_error_close_button) {
            if (mCallBack != null) {
                mCallBack.onFinished(false, 0);
            }
            this.dismiss();
        } else if (vId == R.id.pay_error_repay_button) {
            if (errorCode == ResponseObject.TRADE_HAS_PAID && mPaymentInfo.isDinner()) {
                SeparateShoppingCart.getInstance().clearShoppingCart();
                DinnerShoppingCart.getInstance().clearShoppingCart();
                DinnerShopManager.getInstance().clearCustomer();
                this.dismiss();
                mContext.finish();
                EventBus.getDefault().post(new ActionCloseOrderDishActivity());//返回桌台
            } else {
                if (mDoPayApi != null)
                    mDoPayApi.doPay(this.mContext, this.mPaymentInfo, mCallBack);
                this.dismiss();
            }
        }
    }

    OnDismissListener listener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            try {
                //刷新副2屏应收
                DisplayServiceManager.updateDisplayPay(mContext.getApplicationContext(), mPaymentInfo.getActualAmount());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    };
}
