package com.zhongmei.bty.mobilepay.fragment.mobilepay;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.commonbusiness.view.ShowBarcodeView;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.basemodule.pay.event.PushPayRespEvent;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.commonmodule.view.CurrencyTextView;
import com.zhongmei.bty.mobilepay.IOnlinePayBreakCallback;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.MobliePayMenuTool;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.core.OnlinePayApi;
import com.zhongmei.bty.mobilepay.core.OnlinePayCallback;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.yunfu.net.RequestManager;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.util.ToastUtil;

import java.util.Timer;


public class MobilePayDialog extends BasicDialogFragment implements CompoundButton.OnCheckedChangeListener, IPayConstParame, View.OnClickListener {

    public static final String TAG = MobilePayDialog.class.getSimpleName();

    protected ImageButton mIbClose;

    protected TextView mTvTitle;

    protected CurrencyTextView mTvAmount;

    protected RadioGroup mRgLayout;

    protected RadioButton mRbScan;

    protected RadioButton mRbCode;

    protected View mVLine;

    private MobilePayScanFragment scanPayFragment;

    private MobilePayCodeFragment codePayFragment;

    private int mCurrentScanType = ONLIEN_SCAN_TYPE_ACTIVE;

    private static final int INTERVALTIME = 3000;

    private volatile int mMaxTimeoutMs = 0;
    private static final int TIME_OUT_MS = 291;
    private static final int WHAT_PAYSTATUS = 1;

    private static final int WHAT_UPDATE_TIME = 2;

    private static final int WHAT_BARCODE_TIMEOUT = 3;
    private static final int WHAT_SHOW_PAYSTATUS_BUTTON = 5;
    private static int BARCODE_FAIL = 2;
        private static int BARCODE_PAYING = 3;

    private boolean isBQ = false;
    private boolean isOnPaying = false;
    private Timer mUpdateTimeTimer;
    private int requestPayStatusCount = 0;
    private long mCurrentPaymentItemId;

    private String mCurrentPaymentItemUuid = null;

    private IOnlinePayBreakCallback mOnPayStopListener = null;

    private OnlinePayApi mDoPayApi;

    private IPaymentInfo mPaymentInfo;

    private PayModelItem mPayModelItem;

    private String mTimeOutFormat;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_LAYOUT_FLAGS | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        Window window = dialog.getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        return dialog;
    }

    public static MobilePayDialog newInstance(DoPayApi doPayApi, IPaymentInfo info, PayModelItem payModelItem, int scanType) {
        MobilePayDialog mobilePayDialogFragment = new MobilePayDialog();
        mobilePayDialogFragment.mCurrentScanType = scanType;
        mobilePayDialogFragment.mDoPayApi = doPayApi;
        mobilePayDialogFragment.mPaymentInfo = info;
        mobilePayDialogFragment.mPayModelItem = payModelItem;
        return mobilePayDialogFragment;
    }

    public void setDoPayApi(DoPayApi doPayApi) {
        this.mDoPayApi = doPayApi;
    }

    public void setInfo(IPaymentInfo info) {
        this.mPaymentInfo = info;
    }

    public void setPayModelItem(PayModelItem payModelItem) {
        this.mPayModelItem = payModelItem;
    }

    public void setOnPayStopListener(IOnlinePayBreakCallback onPayStopListener) {
        this.mOnPayStopListener = onPayStopListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay_mobile_pay_dialog_fragment_layout, container, false);
        bindView(view);
        chooseShowGroupView();
        switchUI();
        if (mPayModelItem != null) {
            mTvAmount.setText(getString(R.string.pay_for) + " " + CashInfoManager.formatCash(mPayModelItem.getUsedValue().doubleValue()));
        }
        mTimeOutFormat = getResources().getString(R.string.alipaytimeout);        this.registerEventBus();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DoPayApi.OnlineDialogShowing = true;
    }

    @Override
    public void onStop() {
        stopUpdateTimeTimer();        super.onStop();
    }

    @Override
    public void onDestroy() {
        DoPayApi.OnlineDialogShowing = false;
        stopGetPayStatus();
        this.unregisterEventBus();
        mHandler.removeCallbacksAndMessages(null);
        if (mOnPayStopListener != null) {
            mOnPayStopListener.onPayStop();
        }
        super.onDestroy();
    }

    void bindView(View container) {
        mDoPayApi.setCurrentPaymentInfoId(mPaymentInfo.getId());        mIbClose = (ImageButton) container.findViewById(R.id.back);
        mTvTitle = (TextView) container.findViewById(R.id.tv_title);
        mTvAmount = (CurrencyTextView) container.findViewById(R.id.tv_amount);
        mRgLayout = (RadioGroup) container.findViewById(R.id.rg_scan_type);
        mRbScan = (RadioButton) container.findViewById(R.id.rb_to_scan);
        mRbCode = (RadioButton) container.findViewById(R.id.rb_recv_scan);
        mVLine = container.findViewById(R.id.split_line);
        mRbScan.setOnCheckedChangeListener(this);
        mIbClose.setOnClickListener(this);
    }

    void chooseShowGroupView() {
        if (MobliePayMenuTool.isSetScanCode() && MobliePayMenuTool.isSetShowCode()) {
            mRbScan.setVisibility(View.VISIBLE);
            mRbCode.setVisibility(View.VISIBLE);
        } else if (MobliePayMenuTool.isSetScanCode()) {
            mRbScan.setVisibility(View.VISIBLE);
            mCurrentScanType = ONLIEN_SCAN_TYPE_ACTIVE;
        } else if (MobliePayMenuTool.isSetShowCode()) {
            mRbCode.setVisibility(View.VISIBLE);
            mCurrentScanType = ONLIEN_SCAN_TYPE_UNACTIVE;
        } else {
            mRgLayout.setVisibility(View.GONE);
        }
        if (mCurrentScanType == ONLIEN_SCAN_TYPE_UNACTIVE) {
            mRbScan.setChecked(false);
            mRbCode.setChecked(true);
        } else {            mRbScan.setChecked(true);
            mRbCode.setChecked(false);
        }
    }


    private void showHintDialog() {
        String html = "<b>" + getString(R.string.pay_online_dialog_title) + "</b><br/>";
        html += "<small>" + getString(R.string.pay_online_exit_scan_dialog_hint1) + "</small><br/>";
        html += "<small>" + getString(R.string.pay_online_exit_scan_dialog_hint2) + "</small>";
        CommonDialogFragment.CommonDialogFragmentBuilder cb = new CommonDialogFragment.CommonDialogFragmentBuilder(getActivity());
        cb.iconType(CommonDialogFragment.ICON_WARNING)
                .title(Html.fromHtml(html))
                .negativeText(R.string.pay_online_dialog_close)
                .positiveText(R.string.pay_online_dialog_wait)
                .negativeLisnter(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        try {
                            DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
                            dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .build()
                .show(getFragmentManager(), "OnlinePayDialogFragment");
    }

    private void showGetPayStateBT() {
        if (mCurrentPaymentItemId != 0) {
            if (mCurrentScanType == ONLIEN_SCAN_TYPE_ACTIVE) {
                scanPayFragment.showPayStatusButton();
            } else {
                codePayFragment.showPayStatusButton();
            }
        }
    }

    private void switchUI() {
        if (mCurrentScanType == ONLIEN_SCAN_TYPE_ACTIVE) {
            mRbScan.setVisibility(View.VISIBLE);
            scanPayFragment = MobilePayScanFragment.newInstance(mPaymentInfo, mobilePayCallBack);
            replaceChildFragment(R.id.fl_fragment, scanPayFragment, MobilePayScanFragment.TAG);

        } else if (mCurrentScanType == ONLIEN_SCAN_TYPE_UNACTIVE) {
            stopUpdateTimeTimer();
            mRbCode.setVisibility(View.VISIBLE);
            codePayFragment = MobilePayCodeFragment.newInstance(mPaymentInfo, mobilePayCallBack);
            replaceChildFragment(R.id.fl_fragment, codePayFragment, MobilePayCodeFragment.TAG);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mCurrentScanType = ONLIEN_SCAN_TYPE_ACTIVE;
        } else {
            mCurrentScanType = ONLIEN_SCAN_TYPE_UNACTIVE;
        }
        switchUI();
    }

    @Override
    public void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) return;
        if (v.getId() == R.id.back) {
            if (isBQ || isOnPaying) {
                showHintDialog();
            } else {
                dismiss();
                DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
            }
        }
    }

        public void clearBarCode() {
        if (mCurrentScanType == ONLIEN_SCAN_TYPE_UNACTIVE && isBQ) {
            Message message = new Message();
            message.what = WHAT_BARCODE_TIMEOUT;
            mHandler.removeMessages(WHAT_BARCODE_TIMEOUT);
            mHandler.sendMessage(message);
            isBQ = false;
        }
    }


    public void onEventMainThread(ExemptEventUpdate event) {
        if (this.isAdded() && !this.isHidden()) {
            if (mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_SHOP) {
                clearBarCode();
            }
        }
    }


    public void onEventMainThread(PushPayRespEvent event) {
                try {
                        PayResp result = event.getPushPayMent().getContent();
                        mDoPayApi.doVerifyPayResp(getActivity(), mPaymentInfo, result, onlinePayCallback);        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void stopGetPayStatus() {
        RequestManager.cancelAll("paystatus");        if (mHandler != null) {
            mHandler.removeMessages(WHAT_PAYSTATUS);
        }
    }


    private void sendGetPayStatusMessage() {
        Message message = new Message();
        message.what = WHAT_PAYSTATUS;
        mHandler.removeMessages(WHAT_PAYSTATUS);

        if (requestPayStatusCount >= 50) {

            mHandler.sendMessageDelayed(message, 10000);

        } else if (50 > requestPayStatusCount && requestPayStatusCount > 40) {

            mHandler.sendMessageDelayed(message, 5000);

        } else if (requestPayStatusCount == 0) {
                        if (mCurrentScanType == ONLIEN_SCAN_TYPE_ACTIVE) {
                mHandler.sendMessageDelayed(message, 2000);
            } else {
                mHandler.sendMessageDelayed(message, INTERVALTIME);
            }
        } else {
                        mHandler.sendMessageDelayed(message, 500);
        }
        requestPayStatusCount++;
    }

    public void createPayBarcode(PayModeId payModeId) {
        final TradeVo tradeVo = mPaymentInfo.getTradeVo();
        if (tradeVo == null || getActivity() == null) {
            return;
        }
        mCurrentPaymentItemId = 0;
        mCurrentPaymentItemUuid = null;
        mPayModelItem.setPayMode(payModeId);
        mPayModelItem.setUuid(SystemUtils.genOnlyIdentifier());
        mPayModelItem.setPayType(PayType.QCODE);
        mPaymentInfo.getOtherPay().clear();
        mPaymentInfo.getOtherPay().addPayModelItem(mPayModelItem);
        mCurrentPaymentItemUuid = mPayModelItem.getUuid();
        mDoPayApi.setOnlinePaymentItemUuid(mPayModelItem.getUuid());        codePayFragment.showBarcodeType(ShowBarcodeView.SHOW_QR_CODE_ING);
                mDoPayApi.getOnlinePayBarcode(getActivity(), mPaymentInfo, mPayModelItem, onlinePayCallback);
    }

    public void doPayByAuthCode(String authCode, PayModeId payModeId) {
        TradeVo tradeVo = mPaymentInfo.getTradeVo();
        if (tradeVo == null || TextUtils.isEmpty(authCode) || getActivity() == null) {
            ToastUtil.showLongToast(R.string.trade_info_cannot_null);
            return;
        }
        updateMiniDiplay(BARCODE_PAYING, null);
        isOnPaying = true;
        mCurrentPaymentItemId = 0;
        mCurrentPaymentItemUuid = null;
        mPayModelItem.setPayMode(payModeId);
        UserActionEvent.start(mDoPayApi.getUserActionEventName(mPayModelItem.getPayMode()));

        mPayModelItem.setUuid(SystemUtils.genOnlyIdentifier());
        mPayModelItem.setPayType(PayType.SCAN);
        mPayModelItem.setAuthCode(authCode);

        mPaymentInfo.getOtherPay().clear();
        mPaymentInfo.getOtherPay().addPayModelItem(mPayModelItem);
        mCurrentPaymentItemUuid = mPayModelItem.getUuid();
        mDoPayApi.setOnlinePaymentItemUuid(mPayModelItem.getUuid());        mDoPayApi.onlinePayByAuthCode(getActivity(), mPaymentInfo, mPayModelItem, onlinePayCallback);
    }

    private void getResult() {
                if (!DoPayApi.OnlineDialogShowing || mCurrentPaymentItemUuid == null) {
            return;
        }
        mDoPayApi.getOnlinePayState(getActivity(), mPaymentInfo, mCurrentPaymentItemId, mCurrentPaymentItemUuid, onlinePayCallback);
    }

    private void getPayStatus() {
        if (mCurrentPaymentItemId == 0 || !DoPayApi.OnlineDialogShowing) {
            ToastUtil.showShortToast(getString(R.string.toast_pay_error_hint));
            return;
        }
        mDoPayApi.getOnlinePayStateOfThird(getActivity(), mPaymentInfo, mCurrentPaymentItemId, mCurrentPaymentItemUuid, onlinePayCallback);
    }

        private void sendShowGetPayStateButtonMS() {
        Message message = new Message();
        message.what = WHAT_SHOW_PAYSTATUS_BUTTON;
        mHandler.sendMessageDelayed(message, 1000 * 12);    }

    private void sendBarCodeTimeOutMs() {
        requestPayStatusCount = 0;
        Message message = new Message();
        message.what = WHAT_BARCODE_TIMEOUT;
        mHandler.removeMessages(WHAT_BARCODE_TIMEOUT);
        mHandler.sendMessageDelayed(message, 1000 * TIME_OUT_MS);
    }

    private void startGetPayStatus() {
        sendBarCodeTimeOutMs();

        sendGetPayStatusMessage();
                sendShowGetPayStateButtonMS();
    }

        private void startUpdateTimeTimer() {
        stopUpdateTimeTimer();
        mUpdateTimeTimer = new Timer();
        mUpdateTimeTimer.scheduleAtFixedRate(new UpdateTimeTask(), 0, 1000);
    }

        private void stopUpdateTimeTimer() {
        if (mUpdateTimeTimer != null) {
            mUpdateTimeTimer.cancel();
            mHandler.removeMessages(WHAT_UPDATE_TIME);
            mUpdateTimeTimer = null;
        }
    }

        private void updateMiniDiplay(int payWay, Bitmap bitmap) {
        updateMiniDiplayWithUrl(payWay, null, bitmap, false);
    }

    private void updateMiniDiplayWithUrl(int payWay, String codeUrl, Bitmap bitmap, boolean isUseUrl) {

    }


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case WHAT_PAYSTATUS:
                        getResult();
                        break;
                    case WHAT_BARCODE_TIMEOUT:
                                                if (mCurrentScanType == ONLIEN_SCAN_TYPE_UNACTIVE) {
                            stopUpdateTimeTimer();
                            codePayFragment.hidTimeoutAlert();
                            codePayFragment.showBarcodeType(ShowBarcodeView.SHOW_QR_CODE_INVALID);
                            updateMiniDiplay(BARCODE_FAIL, null);
                        }
                        break;

                    case WHAT_UPDATE_TIME:
                        codePayFragment.showTimeoutAlert((SpannableStringBuilder) msg.obj);
                        if (mMaxTimeoutMs == 0) {
                            stopUpdateTimeTimer();
                        }
                        break;
                    case WHAT_SHOW_PAYSTATUS_BUTTON:
                        showGetPayStateBT();
                        break;

                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    OnlinePayCallback onlinePayCallback = new OnlinePayCallback() {

        @Override
        public void onBarcodeScuess(Long paymentItemId, Bitmap bitmap, String codeUrl, boolean isCodeUrl) {
            if (mCurrentScanType == ONLIEN_SCAN_TYPE_UNACTIVE) {
                isBQ = true;
                mCurrentPaymentItemId = paymentItemId;
                                updateMiniDiplayWithUrl(mCurrentScanType, codeUrl, bitmap, true);
                                codePayFragment.showBarcode(bitmap);
                                startGetPayStatus();

                if (mCurrentScanType == ONLIEN_SCAN_TYPE_UNACTIVE) {
                    mMaxTimeoutMs = TIME_OUT_MS;
                    startUpdateTimeTimer();
                }
            }
        }

        @Override
        public void onBarcodeError() {
            if (mCurrentScanType == ONLIEN_SCAN_TYPE_UNACTIVE) {
                codePayFragment.showBarcodeType(ShowBarcodeView.SHOW_QR_CODE_FAIL);
                updateMiniDiplay(BARCODE_FAIL, null);
            }
        }

        @Override
        public void onAuthCodeScuess(Long paymentItemId) {
            isOnPaying = false;
            mCurrentPaymentItemId = paymentItemId;
                        startGetPayStatus();
        }

        @Override
        public void onAuthCodeError() {
            isOnPaying = false;
            if (mCurrentScanType == ONLIEN_SCAN_TYPE_ACTIVE) {
                scanPayFragment.showBarcodeType(ShowBarcodeView.SHOW_SCAN_TO_CUSTOMER);
                updateMiniDiplay(mCurrentScanType, null);
            }
        }

        @Override
        public void onPayResult(Long paymentItemId, int payStatus) {
            if (TradePayStatus.PAID.value() == payStatus) {                DoPayApi.OnlineDialogShowing = false;                stopGetPayStatus();                dismiss();
            } else if (TradePayStatus.PAID_FAIL.value() == payStatus) {                stopGetPayStatus();                if (mCurrentScanType == ONLIEN_SCAN_TYPE_ACTIVE) {
                    scanPayFragment.showBarcodeType(ShowBarcodeView.SHOW_SCAN_TO_CUSTOMER);
                }
            } else {
                sendGetPayStatusMessage();
            }
        }
    };
    private MobilePayCallBack mobilePayCallBack = new MobilePayCallBack() {
        @Override
        public void payByAuthCode(String authCode, PayModeId payModeId) {
            if (!isOnPaying)
                doPayByAuthCode(authCode, payModeId);
        }

        @Override
        public void getPayBarcode(PayModeId payModeId) {
            createPayBarcode(payModeId);
        }

        @Override
        public void getPayState() {
            getPayStatus();
        }

        @Override
        public void stopUpdateOutTime() {
            stopUpdateTimeTimer();
        }
    };


    public interface MobilePayCallBack {

        void payByAuthCode(String authCode, PayModeId payModeId);
        void getPayBarcode(PayModeId payModeId);
        void getPayState();

        void stopUpdateOutTime();
    }


    class UpdateTimeTask extends java.util.TimerTask {
        @Override
        public void run() {
            if (mMaxTimeoutMs > 0) {
                mMaxTimeoutMs--;
                int startIndex = 3;
                int endIndex = startIndex;
                String str = String.valueOf(mMaxTimeoutMs);
                if (str != null && mTimeOutFormat != null) {
                    SpannableStringBuilder builder =
                            new SpannableStringBuilder(String.format(mTimeOutFormat, str));
                    endIndex += str.length();
                    builder.setSpan(new ForegroundColorSpan(Color.RED),
                            startIndex,
                            endIndex,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Message ms = mHandler.obtainMessage();
                    ms.what = WHAT_UPDATE_TIME;
                    ms.obj = builder;
                    mHandler.sendMessage(ms);
                }
            }
        }
    }
}
