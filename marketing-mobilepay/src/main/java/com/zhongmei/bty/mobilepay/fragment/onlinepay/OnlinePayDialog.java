package com.zhongmei.bty.mobilepay.fragment.onlinepay;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.commonbusiness.enums.PasswordType;
import com.zhongmei.bty.basemodule.commonbusiness.view.ShowBarcodeView;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.scaner.DeWoScanCode;
import com.zhongmei.bty.basemodule.devices.scaner.ScanDataReceivedListener;
import com.zhongmei.bty.basemodule.devices.scaner.innerscanner.IScannerManager;
import com.zhongmei.bty.basemodule.devices.scaner.innerscanner.InnerScannerManager1;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.basemodule.pay.event.PushPayRespEvent;
import com.zhongmei.bty.basemodule.pay.event.RegisterDeWoOnlinePayScanEvent;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.event.ActionCloseOrderDishActivity;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.commonmodule.view.NumberInputdialog;
import com.zhongmei.bty.mobilepay.IOnlinePayBreakCallback;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.core.OnlinePayCallback;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;
import com.zhongmei.bty.mobilepay.event.StopPayStatusTimer;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.yunfu.net.RequestManager;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.ViewUtil;

import java.math.BigDecimal;
import java.util.Timer;

import de.greenrobot.event.EventBus;


public class OnlinePayDialog extends BasicDialogFragment implements View.OnClickListener, TextView.OnEditorActionListener, DialogInterface.OnKeyListener, ScanDataReceivedListener, IPayConstParame, ShowBarcodeView.OnChickRetryListener {

    private final static String TAG = OnlinePayDialog.class.getSimpleName();

    private Button mBack;

    private RadioGroup mRgScanType;

    private RadioButton mRbRecvScan;

    private TextView mPaymodelalter;
    private TextView mPaytimeoutAlert;
    private View splitView;
    private ShowBarcodeView mShowBarcode;
    private TextView mTvAmount;
    private LinearLayout mUnDisCountLayout;    private EditText mWechatIdET;    private TextView mUnDisCountValueInput;    private TextView mGetPayStautsTV;    private IPaymentInfo mPaymentInfo;
    private PayModeId mCurrentPayModeId = PayModeId.WEIXIN_PAY;

    private volatile int mMaxTimeoutMs = 0;
    private static final int TIME_OUT_MS = 291;
    private static final int INTERVALTIME = 2500;

    private static final int WHAT_PAYSTATUS = 1;

    private static final int WHAT_UPDATE_TIME = 2;

    private static final int WHAT_BARCODE_TIMEOUT = 3;
    private static final int WHAT_SHOW_PAYSTATUS_BUTTON = 5;
    private boolean isBQ = false;
    private boolean isBQTimeOut = false;
    private int requestPayStatusCount = 0;
    private IScannerManager mScannerManager;
    private int mCurrentScanType = ONLIEN_SCAN_TYPE_ACTIVE;
    private static int BARCODE_FAIL = 2;
        private static int BARCODE_PAYING = 3;
    private boolean isOnPaying = false;
    private Timer mUpdateTimeTimer;
    private long mCurrentPaymentItemId;

    private String mCurrentPaymentItemUuid = null;

    private PayModelItem mPayModelItem = null;

    private IOnlinePayBreakCallback mOnPayStopListener = null;

    private DoPayApi mDoPayApi;

    public static OnlinePayDialog newInstance(DoPayApi doPayApi, IPaymentInfo info, PayModelItem payModelItem, int scanType) {
        OnlinePayDialog f = new OnlinePayDialog();
        f.setCancelable(false);
        f.setDoPayApi(doPayApi);
        f.setPaymentInfo(info);
        Bundle bundle = new Bundle();
        bundle.putSerializable("payModelItem", payModelItem);
        bundle.putInt(EXTRA_SCAN_TYPE, scanType);        f.setArguments(bundle);
        return f;
    }

    public void setDoPayApi(DoPayApi doPayApi) {
        this.mDoPayApi = doPayApi;
    }

    private void setPaymentInfo(IPaymentInfo paymentInfo) {
        this.mPaymentInfo = paymentInfo;

    }

    public void setOnPayStopListener(IOnlinePayBreakCallback onPayStopListener) {
        this.mOnPayStopListener = onPayStopListener;
    }


    private void initDialog() {
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getDialog().setCancelable(false);
        this.getDialog().setCanceledOnTouchOutside(false);
        this.getDialog().setOnKeyListener(this);    }

    private void assignViews(View view) {
        mBack = (Button) view.findViewById(R.id.back);
        mRgScanType = (RadioGroup) view.findViewById(R.id.rg_scan_type);
        mRbRecvScan = (RadioButton) view.findViewById(R.id.rb_recv_scan);
        mPaymodelalter = (TextView) view.findViewById(R.id.paymodelalter);
        mPaytimeoutAlert = (TextView) view.findViewById(R.id.paytimeoutAlert);
        mShowBarcode = (ShowBarcodeView) view.findViewById(R.id.showBarcode);
        mTvAmount = (TextView) view.findViewById(R.id.tv_amount);
        mUnDisCountLayout = (LinearLayout) view.findViewById(R.id.pay_online_undiscount_layout);
        mWechatIdET = (EditText) view.findViewById(R.id.pay_online_wechat_id);
        mUnDisCountValueInput = (TextView) view.findViewById(R.id.pay_online_undicount_value);
        mGetPayStautsTV = (TextView) view.findViewById(R.id.tv_refresh_pay_state);
        splitView = view.findViewById(R.id.split_line);
    }

    private void initViewListener() {
        mBack.setOnClickListener(this);
                mBack.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e("OnlinePayDialog", "onFocusChange");
                mWechatIdET.requestFocus();
            }
        });
        mGetPayStautsTV.setOnClickListener(this);
        mUnDisCountValueInput.setOnClickListener(this);
        this.registerEventBus();
        mWechatIdET.setOnEditorActionListener(this);
        mWechatIdET.setInputType(InputType.TYPE_NULL);
        mWechatIdET.requestFocus();
        mWechatIdET.requestFocusFromTouch();
        mShowBarcode.setChickRetryListener(this);

        if (mCurrentScanType == ONLIEN_SCAN_TYPE_UNACTIVE) {
            mRgScanType.check(R.id.rb_recv_scan);
        } else {
            mRgScanType.check(R.id.rb_to_scan);
        }
        mRgScanType.setOnCheckedChangeListener(mRadioGroupOnCheckListener);    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPayModelItem = (PayModelItem) getArguments().getSerializable("payModelItem");
        mCurrentScanType = getArguments().getInt(EXTRA_SCAN_TYPE);
        mCurrentPayModeId = mPayModelItem.getPayMode();        mDoPayApi.setCurrentPaymentInfoId(mPaymentInfo.getId());    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initDialog();
        View view = inflater.inflate(R.layout.pay_online_pay_dialog_layout, container);
        assignViews(view);
        initViewListener();
        mScannerManager = InnerScannerManager1.newInstance(this.getActivity(), this);                updateMiniDiplay(mCurrentScanType, null);
        mTvAmount.setText(getString(R.string.pay_for) + " " + CashInfoManager.formatCash(mPayModelItem.getUsedValue().doubleValue()));
        switch (mCurrentPayModeId) {
            case WEIXIN_PAY:
                mTvAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.weixinpay_icon, 0, 0, 0);
                break;
            case ALIPAY:
                mTvAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.alpay_icon, 0, 0, 0);
                break;
            case BAIFUBAO:
                mTvAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baidupay_icon, 0, 0, 0);
                break;
            case UNIONPAY_CLOUD_PAY:                mTvAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pay_cloud_icon, 0, 0, 0);
                break;
            case ICBC_E_PAY:                mTvAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pay_epay_icon, 0, 0, 0);
                break;
            case MEITUAN_FASTPAY:
                mTvAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sanpay_icon, 0, 0, 0);
                break;
            case JIN_CHENG:
                mTvAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jin_cheng_pay_icon, 0, 0, 0);
                break;
            case MEMBER_CARD:
                mTvAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.member_card_pay_icon, 0, 0, 0);
                break;
            case JIN_CHENG_VALUE_CARD:
                mTvAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jincheng_value_card_pay_icon, 0, 0, 0);
                                splitView.setVisibility(View.GONE);
                mRbRecvScan.setVisibility(View.GONE);
                break;
            case DIANXIN_YIPAY:                mTvAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pay_yipay_icon, 0, 0, 0);
                break;
            default:
                break;
        }
                if (mCurrentPayModeId == PayModeId.MEITUAN_FASTPAY) {
            mUnDisCountLayout.setVisibility(View.VISIBLE);
            mRgScanType.setVisibility(View.INVISIBLE);
            if (mPaymentInfo.getTradeVo().getNoJoinDiscount() != null && mPaymentInfo.getTradeVo().getNoJoinDiscount().compareTo(BigDecimal.ZERO) > 0) {
                                if (mPaymentInfo.getTradeVo().getNoJoinDiscount().compareTo(mPaymentInfo.getTradeVo().getTrade().getTradeAmount()) < 0) {
                    mUnDisCountValueInput.setText(CashInfoManager.formatCash(mPaymentInfo.getTradeVo().getNoJoinDiscount().doubleValue()));
                } else {
                    mUnDisCountValueInput.setText(CashInfoManager.formatCash(mPaymentInfo.getTradeVo().getTrade().getTradeAmount().doubleValue()));
                }
            }
        } else {
            mUnDisCountLayout.setVisibility(View.GONE);
            mRgScanType.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            if (isBQ || isOnPaying) {
                showHintDialog();
            } else {
                dismiss();
                DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
            }
        } else if (v.getId() == R.id.pay_online_undicount_value) {
            showNumberInputDialog();

        } else if (v.getId() == R.id.tv_refresh_pay_state) {
            this.getPayStateOfThird();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        registerDewoScan();
        DoPayApi.OnlineDialogShowing = true;
        switchPayWay(mCurrentScanType);    }

    private void registerDewoScan() {
        if (mCurrentScanType == ONLIEN_SCAN_TYPE_ACTIVE && isAdded()) {
            DeWoScanCode.getInstance().registerReceiveDataListener(new DeWoScanCode.OnReceiveDataListener() {
                @Override
                public void onReceiveData(String data) {
                    if (mCurrentScanType == ONLIEN_SCAN_TYPE_ACTIVE && getActivity() != null && isAdded()) {
                        doPayByAuthCode(data);
                    }
                }
            });
        }
    }

    private void unRegisterDewoScan() {
        if (isAdded()) {
            DeWoScanCode.getInstance().unRegisterReceiveDataListener();
        }
    }

    public void onEventMainThread(RegisterDeWoOnlinePayScanEvent event) {
        registerDewoScan();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mScannerManager != null) {
            mScannerManager.stop();
        }
        unRegisterDewoScan();
    }

    @Override
    public void onDestroy() {
        this.unregisterEventBus();
        stopGetPayStatus();
        mHandler.removeCallbacksAndMessages(null);
        if (mScannerManager != null) {
            mScannerManager.destory();
        }
        if (mOnPayStopListener != null) {
            mOnPayStopListener.onPayStop();
        }
        DoPayApi.OnlineDialogShowing = false;
        showLoadingProgressDialogHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
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
                .show(getFragmentManager(), "OnliinePayDialog");
    }


    public void onEventMainThread(StopPayStatusTimer stop) {
        if (stop.isStop()) {
            stopGetPayStatus();
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

    private void startGetPayStatus() {
        sendBarCodeTimeOutMs();
        sendGetPayStatusMessage();
    }

    private void sendBarCodeTimeOutMs() {
        isBQTimeOut = false;
        requestPayStatusCount = 0;
        Message message = new Message();
        message.what = WHAT_BARCODE_TIMEOUT;
        mHandler.removeMessages(WHAT_BARCODE_TIMEOUT);
        mHandler.sendMessageDelayed(message, 1000 * TIME_OUT_MS);
    }


    private void sendGetPayStatusMessage() {
        if (!isBQTimeOut) {
            Message message = new Message();
            message.what = WHAT_PAYSTATUS;
            mHandler.removeMessages(WHAT_PAYSTATUS);

            if (requestPayStatusCount >= 10) {
                mHandler.sendMessageDelayed(message, INTERVALTIME * 2);
            } else {

                mHandler.sendMessageDelayed(message, INTERVALTIME);
                            }
            requestPayStatusCount++;
        }
    }


    private void stopGetPayStatus() {
        stopUpdateTimeTimer();
        RequestManager.cancelAll("paystatus");        if (mHandler != null) {
            mHandler.removeMessages(WHAT_PAYSTATUS);
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


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case WHAT_PAYSTATUS:
                        getPayState();
                        break;

                    case WHAT_UPDATE_TIME:
                        mPaytimeoutAlert.setText((SpannableStringBuilder) msg.obj);
                        if (mMaxTimeoutMs == 0) {
                            stopUpdateTimeTimer();
                        }
                        break;

                    case WHAT_BARCODE_TIMEOUT:
                        if (mCurrentScanType == ONLIEN_SCAN_TYPE_UNACTIVE) {                            onBarcodeTimeOut();
                            if (mCurrentPayModeId != PayModeId.ALIPAY) {
                                isBQTimeOut = true;                             }
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

        private void onBarcodeTimeOut() {
        stopUpdateTimeTimer();
        mPaymodelalter.setVisibility(View.GONE);
        mPaytimeoutAlert.setVisibility(View.GONE);
        mShowBarcode.setShowType(ShowBarcodeView.SHOW_QR_CODE_INVALID);
        updateMiniDiplay(BARCODE_FAIL, null);
    }

        private void generateBarcodeing() {
        mShowBarcode.setShowType(ShowBarcodeView.SHOW_QR_CODE_ING);
        mPaytimeoutAlert.setVisibility(View.GONE);
        stopUpdateTimeTimer();
        saveTradeAndGetBarcode();    }

        private void generateBarcodeingFail() {
        if (mPaymentInfo.getActualAmount() != null) {
            updateMiniDiplay(BARCODE_FAIL, null);
        }
        mPaymodelalter.setVisibility(View.INVISIBLE);
        mPaytimeoutAlert.setVisibility(View.GONE);
        mShowBarcode.setShowType(ShowBarcodeView.SHOW_QR_CODE_FAIL);
    }

        private void showScanOverIcon() {
        mShowBarcode.setShowType(ShowBarcodeView.SHOW_SCAN_SUCCESS);
    }


    private void updatePayModelAlert() {
        if (mCurrentScanType == ONLIEN_SCAN_TYPE_UNACTIVE) {
            mPaymodelalter.setVisibility(View.VISIBLE);
            switch (mCurrentPayModeId) {
                case WEIXIN_PAY:
                    mPaymodelalter.setText(getActivity().getResources().getString(R.string.wechatscantoPay));
                    break;
                case ALIPAY:
                    mPaymodelalter.setText(getActivity().getResources().getString(R.string.alipayscantoPay));
                    break;
                case BAIFUBAO:
                    mPaymodelalter.setText(getActivity().getResources().getString(R.string.wechatpayAlert));
                    break;
                case MEITUAN_FASTPAY:
                    mPaymodelalter.setText(R.string.please_use_dianping_pay);
                    break;
                case JIN_CHENG:
                    mPaymodelalter.setText(R.string.please_use_jin_cheng_pay);
                    break;
                case MEMBER_CARD:
                    mPaymodelalter.setText(getActivity().getResources().getString(R.string.wechatpayAlert));
                    break;
                default:
                    break;

            }
        } else {            stopUpdateTimeTimer();
            mPaymodelalter.setVisibility(View.INVISIBLE);
            mPaytimeoutAlert.setVisibility(View.GONE);
            mPaymodelalter.setText("");
            mPaytimeoutAlert.setText("");
        }
                if (mCurrentPayModeId == PayModeId.MEITUAN_FASTPAY) {
            mUnDisCountLayout.setVisibility(View.VISIBLE);
        } else {
            mUnDisCountLayout.setVisibility(View.GONE);
        }
    }

        private void generateBarcodedSuccess() {
        updatePayModelAlert();
        mPaymodelalter.setVisibility(View.VISIBLE);
                if (mCurrentScanType == ONLIEN_SCAN_TYPE_UNACTIVE) {
            mMaxTimeoutMs = TIME_OUT_MS;
            startUpdateTimeTimer();
            mPaytimeoutAlert.setVisibility(View.VISIBLE);
        }
    }

        RadioGroup.OnCheckedChangeListener mRadioGroupOnCheckListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.rb_to_scan) {
                switchPayWay(ONLIEN_SCAN_TYPE_ACTIVE);
                MobclickAgentEvent.onEvent(getActivity(), MobclickAgentEvent.OnlinePayClickBeisaoCheckLabel);
            } else {
                switchPayWay(ONLIEN_SCAN_TYPE_UNACTIVE);
                MobclickAgentEvent.onEvent(getActivity(), MobclickAgentEvent.OnlinePayClickZhusaoCheckLabel);
            }
        }
    };

    @Override
    public void retry() {
        generateBarcodeing();
    }


    class UpdateTimeTask extends java.util.TimerTask {
        @Override
        public void run() {
            if (mMaxTimeoutMs > 0) {
                mMaxTimeoutMs--;
                int startIndex = 3;
                int endIndex = startIndex;
                String str = String.valueOf(mMaxTimeoutMs);
                if (str != null) {
                    SpannableStringBuilder builder =
                            new SpannableStringBuilder(String.format(getResources().getString(R.string.alipaytimeout),
                                    str));
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

            NumberInputdialog.InputOverListener mInputOverListener = new NumberInputdialog.InputOverListener() {
        @Override
        public void afterInputOver(String inputContent) {
            if (inputContent != null) {
                double vl = Double.valueOf(inputContent);
                mUnDisCountValueInput.setText(CashInfoManager.formatCash(vl));
                if (mCurrentScanType == ONLIEN_SCAN_TYPE_UNACTIVE && mCurrentPayModeId == PayModeId.MEITUAN_FASTPAY) {
                    generateBarcodeing();
                }
            }
        }
    };

        void showNumberInputDialog() {
        if (!ClickManager.getInstance().isClicked()) {
            double maxValue = mPaymentInfo.getTradeAmount();
            String defaultInput = getUnDiscountInputValue();
            NumberInputdialog numberDialog =
                    new NumberInputdialog(this.getActivity(), getString(R.string.pay_cannot_discount_amount), getString(R.string.input_price_str), defaultInput, maxValue, mInputOverListener);
            numberDialog.setNumberType(NumberInputdialog.NUMBER_TYPE_FLOAT).show();
        }
    }

        private String getUnDiscountInputValue() {
        String content = mUnDisCountValueInput.getText().toString();
        if (TextUtils.isEmpty(content)) {
            return null;
        } else {
            return content.replace(ShopInfoCfg.getInstance().getCurrencySymbol(), "").trim();
        }
    }

        private void switchPayWay(int scanType) {
        if (scanType == ONLIEN_SCAN_TYPE_UNACTIVE) {            mCurrentScanType = ONLIEN_SCAN_TYPE_UNACTIVE;
            if (mCurrentPayModeId.value() != 0) {
                generateBarcodeing();
            }
            if (mScannerManager != null) {
                mScannerManager.stop();
            }
        } else {            mCurrentScanType = ONLIEN_SCAN_TYPE_ACTIVE;
            scanGunRequestFocus();
            ViewUtil.hiddenSoftKeyboard(mWechatIdET);
            mShowBarcode.setShowType(ShowBarcodeView.SHOW_SCAN_TO_CUSTOMER);
            if (mScannerManager != null) {
                mScannerManager.start();
            }
        }
        updatePayModelAlert();
        updateMiniDiplay(mCurrentScanType, null);
    }

        private void scanGunRequestFocus() {
        mWechatIdET.setVisibility(View.VISIBLE);
        mWechatIdET.setText("");
        mWechatIdET.setSelection(0);
        mWechatIdET.requestFocus();
        mWechatIdET.requestFocusFromTouch();
    }

        @Override
    public void onDataReceivedOver(String authCode) {
        try {
                    } catch (Exception e) {
            e.printStackTrace();
        }
                if (!TextUtils.isEmpty(authCode)) {
            doPayByAuthCode(authCode);
        } else {
            ToastUtil.showLongToast(R.string.pay_authcode_can_not_empty);
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String authCode = mWechatIdET.getText().toString();
        try {
                    } catch (Exception e) {
            e.printStackTrace();
        }
                if (!TextUtils.isEmpty(authCode)) {
            doPayByAuthCode(authCode);
            mWechatIdET.setText("");
            mWechatIdET.requestFocus();
            ViewUtil.hiddenSoftKeyboard(mWechatIdET);
        } else {
            ToastUtil.showLongToast(R.string.pay_authcode_can_not_empty);
        }
        return false;
    }

    private void doPayByAuthCode(String authCode) {
        if (mCurrentScanType == ONLIEN_SCAN_TYPE_ACTIVE) {
            if (!isOnPaying) {
                updateMiniDiplay(BARCODE_PAYING, null);
                if (mCurrentPayModeId == PayModeId.JIN_CHENG_VALUE_CARD) {
                    saveTradeAndPayByAuthCode(authCode);
                } else if (mCurrentPayModeId == PayModeId.MEMBER_CARD) {
                    doWeiXinCustomerScanPay(authCode);
                } else {
                    saveTradeAndPayByAuthCode(authCode);
                }
                            } else {
                ToastUtil.showLongToast(R.string.is_paying);
            }
        }
    }


    private void saveTradeAndGetBarcode() {
        final TradeVo tradeVo = mPaymentInfo.getTradeVo();
        if (tradeVo == null) {
            return;
        }
        mCurrentPaymentItemId = 0;
        mCurrentPaymentItemUuid = null;
        mPayModelItem.setUuid(SystemUtils.genOnlyIdentifier());
        mPayModelItem.setPayType(PayType.QCODE);
        mPaymentInfo.getOtherPay().clear();
        mPaymentInfo.getOtherPay().addPayModelItem(mPayModelItem);
        mDoPayApi.setOnlinePaymentItemUuid(mPayModelItem.getUuid());        mCurrentPaymentItemUuid = mPayModelItem.getUuid();
                mDoPayApi.getOnlinePayBarcode(getActivity(), mPaymentInfo, mPayModelItem, onlinePayCallback);
    }

    OnlinePayCallback onlinePayCallback = new OnlinePayCallback() {

        @Override
        public void onBarcodeScuess(Long paymentItemId, Bitmap bitmap, String codeUrl, boolean isCodeUrl) {
            if (mCurrentScanType == ONLIEN_SCAN_TYPE_UNACTIVE) {
                isBQ = true;
                mCurrentPaymentItemId = paymentItemId;
                generateBarcodedSuccess();
                                updateMiniDiplayWithUrl(mCurrentScanType, codeUrl, bitmap, true);
                                mShowBarcode.setShowQR(bitmap);
                                startGetPayStatus();

                sendShowGetPayStateButtonMS();            }
        }

        @Override
        public void onBarcodeError() {
            generateBarcodeingFail();
        }

        @Override
        public void onAuthCodeScuess(Long paymentItemId) {
            isOnPaying = false;
            if (paymentItemId != null)
                mCurrentPaymentItemId = paymentItemId;
                        startGetPayStatus();

                        showGetPayStateBT();
        }

        @Override
        public void onAuthCodeError() {
            isOnPaying = false;
            if (mCurrentScanType == ONLIEN_SCAN_TYPE_ACTIVE) {
                mShowBarcode.setShowType(ShowBarcodeView.SHOW_SCAN_TO_CUSTOMER);
                updateMiniDiplay(mCurrentScanType, null);
            }
            scanGunRequestFocus();
        }

        @Override
        public void onPayResult(Long paymentItemId, int payStatus) {
            dismissLoadingProgressDialog();
            if (TradePayStatus.PAID.value() == payStatus) {                EventBus.getDefault().post(new ActionCloseOrderDishActivity());
                DoPayApi.OnlineDialogShowing = false;                stopGetPayStatus();                dismiss();
            } else if (TradePayStatus.PAID_FAIL.value() == payStatus) {                stopGetPayStatus();            } else {
                sendGetPayStatusMessage();
            }
        }
    };



    private void saveTradeAndPayByAuthCode(final String authCode) {
        TradeVo tradeVo = mPaymentInfo.getTradeVo();
        if (tradeVo == null || TextUtils.isEmpty(authCode)) {
            ToastUtil.showLongToast(R.string.trade_info_cannot_null);
            return;
        }
        isOnPaying = true;
        mCurrentPaymentItemId = 0;
        mCurrentPaymentItemUuid = null;
        UserActionEvent.start(mDoPayApi.getUserActionEventName(mCurrentPayModeId));

        mPayModelItem.setUuid(SystemUtils.genOnlyIdentifier());
        mPayModelItem.setPayType(PayType.SCAN);
        mPayModelItem.setAuthCode(authCode);
        String disAmount = getUnDiscountInputValue();
        if (!TextUtils.isEmpty(disAmount)) {
            double tmp = Double.valueOf(disAmount);
            mPayModelItem.setNoDiscountAmount(BigDecimal.valueOf(tmp));
        }

        mPaymentInfo.getOtherPay().clear();
        mPaymentInfo.getOtherPay().addPayModelItem(mPayModelItem);
        mDoPayApi.setOnlinePaymentItemUuid(mPayModelItem.getUuid());        mDoPayApi.onlinePayByAuthCode(getActivity(), mPaymentInfo, mPayModelItem, onlinePayCallback);

        mCurrentPaymentItemUuid = mPayModelItem.getUuid();
        showScanOverIcon();            }

        IPayOverCallback mPayOverCallback = new IPayOverCallback() {

        @Override
        public void onFinished(boolean isOK, int statusCode) {
            try {
                if (isOK) {
                    isOnPaying = false;
                    dismiss();
                } else {
                    isOnPaying = false;
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    };

    private void doWeiXinCustomerScanPay(String tokenJson) {
        if (!TextUtils.isEmpty(tokenJson) && tokenJson.contains(":") && tokenJson.length() > 5) {
            String tokens[] = tokenJson.split(":");
            if (tokens != null) {
                String customerId = tokens[0];
                String token = tokens[1];
                if (!TextUtils.isEmpty(customerId) && Utils.isNum(customerId) && !TextUtils.isEmpty(token)) {
                    UserActionEvent.start(UserActionEvent.DINNER_PAY_SETTLE_STORE);
                    mPayModelItem.setPasswordType(PasswordType.TOKEN_CODE);
                    mPayModelItem.setPayType(null);
                    mPayModelItem.setUuid(SystemUtils.genOnlyIdentifier());
                    try {
                        this.mPaymentInfo.setCustomerId(Long.valueOf(customerId).longValue());
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                    this.mPaymentInfo.setMemberPassword(token);
                    this.mPaymentInfo.getOtherPay().clear();
                    this.mPaymentInfo.getOtherPay().addPayModelItem(mPayModelItem);                    isOnPaying = true;
                    if (mDoPayApi != null)
                        mDoPayApi.doPay(this.getActivity(), this.mPaymentInfo, mPayOverCallback);
                } else {
                    ToastUtil.showShortToast(R.string.pay_data_format_not_ok);
                }
            }
        } else {
            ToastUtil.showShortToast(R.string.pay_data_format_not_ok);
        }
    }





    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
                        WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = DensityUtil.dip2px(getActivity(), 460);
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(attributes);
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

        private void sendShowGetPayStateButtonMS() {
        Message message = new Message();
        message.what = WHAT_SHOW_PAYSTATUS_BUTTON;
        mHandler.sendMessageDelayed(message, 10 * 1000);    }

    private void showGetPayStateBT() {
        if (mCurrentPaymentItemId != 0) {
            this.mGetPayStautsTV.setVisibility(View.VISIBLE);
        }
    }

    private void getPayState() {
                if (!DoPayApi.OnlineDialogShowing || mCurrentPaymentItemUuid == null) {
            return;
        }
        mDoPayApi.getOnlinePayState(getActivity(), mPaymentInfo, mCurrentPaymentItemId, mCurrentPaymentItemUuid, onlinePayCallback);
    }

    private void getPayStateOfThird() {
        if (mCurrentPaymentItemId == 0 || !DoPayApi.OnlineDialogShowing) {
            ToastUtil.showShortToast(getString(R.string.toast_pay_error_hint));
            return;
        }
        showLoadingProgressDialog(2500);
        mDoPayApi.getOnlinePayStateOfThird(getActivity(), mPaymentInfo, mCurrentPaymentItemId, mCurrentPaymentItemUuid, onlinePayCallback);
    }

    Handler showLoadingProgressDialogHandler = new Handler();
    long showLoadingProgressDialogTime = 0;

    public void showLoadingProgressDialog(long delayMillis) {
        showLoadingProgressDialogTime = System.currentTimeMillis() + delayMillis;
        showLoadingProgressDialog();
    }

    @Override
    public void dismissLoadingProgressDialog() {
        long time = System.currentTimeMillis() - showLoadingProgressDialogTime;
        if (time > 0) {
            super.dismissLoadingProgressDialog();
        } else {
            showLoadingProgressDialogHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissLoadingProgressDialog();
                }
            }, time);
        }
    }
}
