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

/**
 * 移动支付主界面，支持一码付
 * Created on 2018/6/4.
 */
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

    private volatile int mMaxTimeoutMs = 0;// 秒表到记时

    private static final int TIME_OUT_MS = 291;// 超时秒数

    private static final int WHAT_PAYSTATUS = 1;

    private static final int WHAT_UPDATE_TIME = 2;

    private static final int WHAT_BARCODE_TIMEOUT = 3;// 二维码超时

    private static final int WHAT_SHOW_PAYSTATUS_BUTTON = 5;//显示获取支付状态按钮

    private static int BARCODE_FAIL = 2;
    //扫描成功后正在支付中
    private static int BARCODE_PAYING = 3;

    private boolean isBQ = false;// 是否已经生成过二维码

    private boolean isOnPaying = false;// 是否正在支付

    private Timer mUpdateTimeTimer;//超时计时器

    private int requestPayStatusCount = 0;// 请求支付状态次数

    private long mCurrentPaymentItemId;

    private String mCurrentPaymentItemUuid = null;

    private IOnlinePayBreakCallback mOnPayStopListener = null;

    private OnlinePayApi mDoPayApi;

    private IPaymentInfo mPaymentInfo;

    private PayModelItem mPayModelItem;

    private String mTimeOutFormat;//add v8.15

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
        mTimeOutFormat = getResources().getString(R.string.alipaytimeout);//二维码倒记时格式
        this.registerEventBus();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DoPayApi.OnlineDialogShowing = true;
    }

    @Override
    public void onStop() {
        stopUpdateTimeTimer();//add v8.15
        super.onStop();
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
        mDoPayApi.setCurrentPaymentInfoId(mPaymentInfo.getId());//设置当前支付id
        mIbClose = (ImageButton) container.findViewById(R.id.back);
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
        } else {//默认扫码枪
            mRbScan.setChecked(true);
            mRbCode.setChecked(false);
        }
    }

    /**
     * 显示注销对话框
     */
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
            // updateMiniDiplay(mCurrentScanType, null);

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

    //让生成的二维码失效
    public void clearBarCode() {
        if (mCurrentScanType == ONLIEN_SCAN_TYPE_UNACTIVE && isBQ) {
            Message message = new Message();
            message.what = WHAT_BARCODE_TIMEOUT;
            mHandler.removeMessages(WHAT_BARCODE_TIMEOUT);
            mHandler.sendMessage(message);
            isBQ = false;
        }
    }

    /**
     * @Description: 刷新未支付或找零
     * @Return void 返回类型
     */
    public void onEventMainThread(ExemptEventUpdate event) {
        if (this.isAdded() && !this.isHidden()) {
            if (mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_SHOP) {
                clearBarCode();
            }
        }
    }

    /**
     * 接收推送消息
     */
    public void onEventMainThread(PushPayRespEvent event) {
        // 收到长连接发送过来的通知。
        try {
            //PLog.d(PLog.TAG_CALLPRINT_KEY, "info:推送v3第三方支付:时间戳:" + System.currentTimeMillis() + ",TradeUuid:" + event.getTradeUuid() + ",position:" + TAG + "->onEventMainThread()");
            PayResp result = event.getPushPayMent().getContent();
            // 3成功
            mDoPayApi.doVerifyPayResp(getActivity(), mPaymentInfo, result, onlinePayCallback);// 处理收银结果
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消查询订单状态
     */
    private void stopGetPayStatus() {
        RequestManager.cancelAll("paystatus");// 移除网络请求
        if (mHandler != null) {
            mHandler.removeMessages(WHAT_PAYSTATUS);
        }
    }

    /**
     * 发送获取订单状态消息
     */
    private void sendGetPayStatusMessage() {
        Message message = new Message();
        message.what = WHAT_PAYSTATUS;
        mHandler.removeMessages(WHAT_PAYSTATUS);

        if (requestPayStatusCount >= 50) {

            mHandler.sendMessageDelayed(message, 10000);

        } else if (50 > requestPayStatusCount && requestPayStatusCount > 40) {

            mHandler.sendMessageDelayed(message, 5000);

        } else if (requestPayStatusCount == 0) {
            //如果是付款码首次轮训等待3秒，如果是被扫等待2秒 modify v8.11
            if (mCurrentScanType == ONLIEN_SCAN_TYPE_ACTIVE) {
                mHandler.sendMessageDelayed(message, 2000);
            } else {
                mHandler.sendMessageDelayed(message, INTERVALTIME);
            }
        } else {
            //前30次轮训间隔半秒 modify v8.11
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
        mDoPayApi.setOnlinePaymentItemUuid(mPayModelItem.getUuid());//add 20170525 默认当前支付的uuid(解决超时情况有推送结果但是没有出票问题)
        codePayFragment.showBarcodeType(ShowBarcodeView.SHOW_QR_CODE_ING);
        //生成二维码
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
        mDoPayApi.setOnlinePaymentItemUuid(mPayModelItem.getUuid());//add 20170525 默认当前支付的uuid(解决超时情况有推送结果但是没有出票问题)
        mDoPayApi.onlinePayByAuthCode(getActivity(), mPaymentInfo, mPayModelItem, onlinePayCallback);
    }

    private void getResult() {
        //没有支付明细ID 或者界面关闭 不轮询
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

    //add 20171024  start 获取支付状态按钮
    private void sendShowGetPayStateButtonMS() {
        Message message = new Message();
        message.what = WHAT_SHOW_PAYSTATUS_BUTTON;
        mHandler.sendMessageDelayed(message, 1000 * 12);//等待10秒显示
    }

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
        //add 20171014 显示获取状态按钮
        sendShowGetPayStateButtonMS();
    }

    //开启超时到计时
    private void startUpdateTimeTimer() {
        stopUpdateTimeTimer();
        mUpdateTimeTimer = new Timer();
        mUpdateTimeTimer.scheduleAtFixedRate(new UpdateTimeTask(), 0, 1000);
    }

    //停止计时
    private void stopUpdateTimeTimer() {
        if (mUpdateTimeTimer != null) {
            mUpdateTimeTimer.cancel();
            mHandler.removeMessages(WHAT_UPDATE_TIME);
            mUpdateTimeTimer = null;
        }
    }

    // 刷新2屏
    private void updateMiniDiplay(int payWay, Bitmap bitmap) {
        updateMiniDiplayWithUrl(payWay, null, bitmap, false);
    }

    private void updateMiniDiplayWithUrl(int payWay, String codeUrl, Bitmap bitmap, boolean isUseUrl) {
        /*try {
            //mLastBarcode = bitmap;
            DisplayBarcode dBarcode =
                    DisplayServiceManager.buildDBarcode(payWay,
                            String.valueOf(mPayModelItem.getUsedValue()),
                            bitmap,
                            mPayModelItem.getPayMode().value());
            dBarcode.setCodeUrl(codeUrl);
            dBarcode.setUseUrl(isUseUrl);
            if (mPayModelItem.getPayMode() == PayModeId.MOBILE_PAY)//如果是移动支付，生成副屏支持的方式
            {
                dBarcode.setPayType(MobliePayMenuTool.getPayTypeByScanType(payWay));
            } else {
                dBarcode.setPayType(MobliePayMenuTool.getPayTypeByMode(mPayModelItem.getPayMode()));
            }
            DisplayServiceManager.updateDisplay(getActivity().getApplicationContext(), dBarcode);
        } catch (Exception e) {
            // Log.e(TAG, "", e);
            e.printStackTrace();
        }*/
    }


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case WHAT_PAYSTATUS:
                        getResult();
                        break;
                    case WHAT_BARCODE_TIMEOUT:
                        //如果扫码模式才刷新UI
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

    OnlinePayCallback onlinePayCallback = new OnlinePayCallback() {//在线支付回调


        @Override
        public void onBarcodeScuess(Long paymentItemId, Bitmap bitmap, String codeUrl, boolean isCodeUrl) {
            if (mCurrentScanType == ONLIEN_SCAN_TYPE_UNACTIVE) {
                isBQ = true;
                mCurrentPaymentItemId = paymentItemId;
                // 副屏显示二维码
                updateMiniDiplayWithUrl(mCurrentScanType, codeUrl, bitmap, true);
                // 显示二维码
                codePayFragment.showBarcode(bitmap);
                // 开始轮训获取支付结果
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
            // 开始轮训获取支付结果
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
            if (TradePayStatus.PAID.value() == payStatus) {//支付成功
                DoPayApi.OnlineDialogShowing = false;//add v8.5及时标记退出在线支付界面
                stopGetPayStatus();// 取消查询订单状态
                dismiss();
            } else if (TradePayStatus.PAID_FAIL.value() == payStatus) {//支付失败
                stopGetPayStatus();// 取消查询订单状态
                if (mCurrentScanType == ONLIEN_SCAN_TYPE_ACTIVE) {
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

    /**
     * 二维码扫码枪界面接口
     */
    public interface MobilePayCallBack {

        void payByAuthCode(String authCode, PayModeId payModeId);//扫码枪事件

        void getPayBarcode(PayModeId payModeId);//

        void getPayState();

        void stopUpdateOutTime();
    }

    /**
     * 二维码到计时
     */
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
