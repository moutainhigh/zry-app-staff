package com.zhongmei.bty.cashier.ordercenter.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.devices.scaner.DeWoScanCode;
import com.zhongmei.bty.basemodule.devices.scaner.DeWoScanCode.OnReceiveDataListener;
import com.zhongmei.bty.basemodule.devices.scaner.ScanCode.ScanCodeReceivedListener;
import com.zhongmei.bty.basemodule.devices.scaner.ScanCodeManager;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.commonmodule.data.operate.IOperates.ImplContext;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.data.operates.impl.TradeOperatesImpl;
import com.zhongmei.bty.data.operates.message.content.KouBeiVerifyResp;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;

/**
 * 口碑核销
 *
 * @since 2018.06.09.
 */
@EFragment(R.layout.dialog_verification_koubei)
public class VerificationDialog extends BasicDialogFragment implements ScanCodeReceivedListener, ImplContext, OnReceiveDataListener {

    @ViewById(R.id.ll_verification_scan)
    LinearLayout mVerificationScan;

    @ViewById(R.id.ll_verification_result)
    LinearLayout mVerificationResult;

    @ViewById(R.id.iv_verification_result_img)
    ImageView mResultImg;

    @ViewById(R.id.tv_verification_result)
    TextView mResultText;

    @ViewById(R.id.et_barcode)
    EditText mBarCode;

    @ViewById(R.id.bt_ok)
    Button mOk;

    @ViewById(R.id.foo_dash_line)
    View mFooDashLine;


    private TradeOperatesImpl operates;

    private ScanCodeManager scanManager;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | SOFT_INPUT_ADJUST_RESIZE);
        return dialog;
    }

    @AfterViews
    public void initialize() {
        operates = new TradeOperatesImpl(this);
        scanManager = new ScanCodeManager(getContext(), mBarCode, true);
        mVerificationScan.setVisibility(View.VISIBLE);
        scanManager.start(this);
        DeWoScanCode.getInstance().registerReceiveDataListener(this);
        DeWoScanCode.getInstance().start();
    }

    /**
     * 切换扫码
     */
    private void switchScan() {
        mVerificationScan.setVisibility(View.VISIBLE);
        mBarCode.setVisibility(View.VISIBLE);
        mOk.setVisibility(View.VISIBLE);
        mFooDashLine.setVisibility(View.VISIBLE);
        mVerificationResult.setVisibility(View.GONE);
    }

    /**
     * 切换结果
     */
    private void switchResult(boolean isSuccess) {
        mVerificationScan.setVisibility(View.GONE);
        mBarCode.setVisibility(View.GONE);
        mOk.setVisibility(View.GONE);
        mFooDashLine.setVisibility(View.GONE);
        mVerificationResult.setVisibility(View.VISIBLE);
        mResultImg.setImageResource(isSuccess ? R.drawable.ic_verification_success : R.drawable.ic_verification_error);
        mResultText.setTextColor(isSuccess ? Color.parseColor("#3FDBB7") : Color.parseColor("#FF513A"));
        mResultText.setText(isSuccess ? R.string.order_center_koubei_verification_success : R.string.order_center_koubei_verification_error);
        if (isSuccess) {
            mBarCode.setText("");
        }
    }

    /**
     * 请求核销
     */
    private void requestVerify(String code) {
        operates.koubeiVerification(code, LoadingResponseListener.ensure(new ResponseListener<KouBeiVerifyResp>() {
            @Override
            public void onResponse(ResponseObject<KouBeiVerifyResp> response) {
                if (VerificationDialog.this.isDetached()) {
                    return;
                }
                if (ResponseObject.isOk(response)) {
                    switchResult(KouBeiVerifyResp.isOk(response.getContent()));
                    countDownTimer.start();
                } else {
                    ToastUtil.showShortToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                if (VerificationDialog.this.isDetached()) {
                    return;
                }
                switchResult(false);
                countDownTimer.start();
                ToastUtil.showShortToast(error.getMessage());
            }
        }, getFragmentManager()));


    }

    /**
     * CountDownTimer 实现倒计时
     */
    private CountDownTimer countDownTimer = new CountDownTimer(3 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            if (isAdded()) {
                switchScan();
            }
        }
    };

    /**
     * 德沃扫码枪
     */
    @Override
    public void onReceiveData(String data) {
        requestVerify(data);
    }

    /**
     * 其他扫码枪
     */
    @Override
    public void onScanCodeReceived(String data) {
        requestVerify(data);
    }

    public void show(FragmentManager fm) {
        show(fm, "VerificationDialog");
    }

    @Click(R.id.iv_back)
    public void onBackClick() {
        dismiss();
    }

    @Click(R.id.bt_ok)
    public void onOkClick() {
        requestVerify(mBarCode.getEditableText().toString());
    }

    @Override
    public void onDestroyView() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scanManager.stop();
        DeWoScanCode.getInstance().stop();
        DeWoScanCode.getInstance().unregisterReceiver();
    }
}
