package com.zhongmei.bty.basemodule.devices.mispos.dialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLDResponse;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLiandiposManager;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLiandiposManager.OnTransListener;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.commonmodule.database.entity.local.PosTransLog;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;



public class UionPayDialogFragment extends BasicDialogFragment implements OnClickListener, OnKeyListener {

    private final String TAG = UionPayDialogFragment.class.getSimpleName();

    public static final int ICON_READY = R.drawable.commonmodule_uion_card_ready;

    public static final int ICON_SUCCESS = R.drawable.commonmodule_uion_card_success;

    public static final int ICON_FULL = R.drawable.mispos_uion_card_full;

    public static final int ICON_FAILED = R.drawable.commonmodule_uion_card_failed;

    protected TextView reminder_msg_tv;
    protected TextView pay_money_tv;
    protected Button close_button;
    protected ImageView center_iv;
    protected TextView show_info_tv;
    protected Button try_again_button;
    OnClickListener mcloseListener;
    OnClickListener mtryAgainListener;
    PosOvereCallback mPosOvereCallback;
    TradePaymentVo mTradePaymentVo;
    BigDecimal currentmoney;
    PosTransLog currentTransLog;
    private String tradeNo;
    String currentposTraceNo;
    String currentRefundUuid;
    private UionPayStaus currentpayStaus = UionPayStaus.PAY;
    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    UiHandler handler;
    OnTransListener onTransListener;
    private boolean isSuccess = false;
    private PosTransLog resultTranLog;
    private NewLDResponse errLDResponse;
    private long delaytime = 2000;
    private String deviceNum = "";


    private int readyCountDown = 30;

    private UionCountDownHandler countDownHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG + "onCreate()");
        handler = new UiHandler(this);
        countDownHandler = new UionCountDownHandler(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, TAG + "onCreateView()");
        View view = inflater.inflate(R.layout.mispos_union_pay_dialog_layout, container);
        initViewById(view);

        return view;
    }

    @Override
    public void onDestroy() {
        onTransListener = null;

        Log.d(TAG, TAG + "onDestroy()");
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        stopCountDown();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        Log.d(TAG, TAG + "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, TAG + "onResume()");
        super.onResume();

    }

    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();

    }

    private void initViewById(View view) {
        reminder_msg_tv = (TextView) view.findViewById(R.id.reminder_msg_tv);
        pay_money_tv = (TextView) view.findViewById(R.id.pay_money_tv);
        close_button = (Button) view.findViewById(R.id.close_button);
        center_iv = (ImageView) view.findViewById(R.id.center_iv);
        show_info_tv = (TextView) view.findViewById(R.id.show_info_tv);
        try_again_button = (Button) view.findViewById(R.id.try_again_button);        getDialog().getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(false);

        show_info_tv.getPaint().setFakeBoldText(true);
        close_button.setOnClickListener(this);

        try_again_button.setOnClickListener(this);

        getDialog().setOnKeyListener(this);
        doPos();

    }


    private void doPos() {
        isSuccess = false;
        resultTranLog = null;
        errLDResponse = null;

        changeViewByType(UionViewStaus.Credit_card_Ready, Utils.formatPrice(Math.abs(currentmoney.doubleValue())));        startCountDown();

        onTransListener = new OnTransListener() {

            @Override
            public void onActive() {
                Log.d(TAG, TAG + "----------------onActive()");
            }

            @Override
            public void onStart() {
                Log.d(TAG, TAG + "----------------onStart()");
                changeViewByType(UionViewStaus.Credit_card_operating,
                        Utils.formatPrice(Math.abs(currentmoney.doubleValue())));                stopCountDown();
            }

            @Override
            public void onFailure(NewLDResponse ldResponse) {
                Log.d(TAG, TAG + "----------------onFailure()");
                isSuccess = false;
                resultTranLog = null;
                errLDResponse = ldResponse;
                changeViewByType(UionViewStaus.Credit_card_fail,
                        Utils.formatPrice(Math.abs(currentmoney.doubleValue())));                stopCountDown();
            }

            @Override
            public void onConfirm(PosTransLog log) {
                Log.d(TAG, TAG + "----------------onConfirm()");
                stopCountDown();
                SharedPreferenceUtil.getSpUtil().putString(Constant.SP_POS_DEVICIE_ID, log.getTerminalNumber());                Log.d(TAG,
                        TAG + "onConfirm---SP_POS_DEVICIE_ID"
                                + SharedPreferenceUtil.getSpUtil().getString(Constant.SP_POS_DEVICIE_ID, "默认"));
                log.setTradeNo(tradeNo);
                isSuccess = true;
                resultTranLog = log;                errLDResponse = null;
                changeViewByType(UionViewStaus.Credit_card_success,
                        Utils.formatPrice(Math.abs(currentmoney.doubleValue())));
                                            }

        };
        Log.d(TAG, TAG + ":doPos()");
        if (currentpayStaus == UionPayStaus.PAY) {
            NewLiandiposManager.getInstance().startPay(currentmoney, onTransListener);        } else if (currentpayStaus == UionPayStaus.RETURN) {
            NewLiandiposManager.getInstance().startRefund(currentRefundUuid, currentposTraceNo, onTransListener);        } else if (currentpayStaus == UionPayStaus.CANCLE) {
            NewLiandiposManager.getInstance().cancelPay(currentTransLog, onTransListener);        }
    }

    public void setCloseListener(OnClickListener listener) {
        mcloseListener = listener;
    }

    public void setTryAgainListener(OnClickListener listener) {
        mtryAgainListener = listener;
    }

    public void setPosOvereCallback(PosOvereCallback mPosOvereCallback) {
        this.mPosOvereCallback = mPosOvereCallback;
    }

    public void setCurrentmoney(BigDecimal currentmoney) {
        this.currentmoney = currentmoney;
    }

    public void setCurrentTransLog(PosTransLog currentTransLog) {
        this.currentTransLog = currentTransLog;
    }

    public void setCurrentposTraceNo(String currentposTraceNo) {
        this.currentposTraceNo = currentposTraceNo;
    }

    public void setCurrentRefundUuid(String currentRefundUuid) {
        this.currentRefundUuid = currentRefundUuid;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public static class UionPayDialogFragmentBuilder {

                String mDeviceNum;
        OnClickListener mbuilderCloseListener;

        OnClickListener mbuilderTryAgainListener;

        public UionPayDialogFragmentBuilder() {
                    }

                public UionPayDialogFragment buildPay(BigDecimal money, String no, PosOvereCallback overeCallback) {
            return build(UionPayStaus.PAY, money, no, null, null, null, overeCallback);

        }

                public UionPayDialogFragment buildReturn(BigDecimal money, String no, String refundUuid, String posTraceNo,
                                                 PosOvereCallback overeCallback) {
            return build(UionPayStaus.RETURN, money, no, refundUuid, posTraceNo, null, overeCallback);
        }

                public UionPayDialogFragment buildCancle(PosTransLog transLog, PosOvereCallback overeCallback) {
            BigDecimal bigDecimal = NewLiandiposManager.getRealMoney(transLog.getAmount());

            return build(UionPayStaus.CANCLE, bigDecimal, null, uuid(), null, transLog, overeCallback);
        }

        private UionPayDialogFragment build(UionPayStaus payStaus, BigDecimal money, String tradeNo, String refundUuid,
                                            String posTraceNo, PosTransLog transLog, PosOvereCallback overeCallback) {

                        UionPayDialogFragment fragment = new UionPayDialogFragment();
            fragment.setCurrentpayStaus(payStaus);
            if (mDeviceNum != null) {
                fragment.setDeviceNum(mDeviceNum);
            }
            if (mbuilderCloseListener != null) {
                fragment.setCloseListener(mbuilderCloseListener);
            }
            if (mbuilderTryAgainListener != null) {
                fragment.setTryAgainListener(mbuilderTryAgainListener);
            }
            if (overeCallback != null) {
                fragment.setPosOvereCallback(overeCallback);
            }

            fragment.setCurrentmoney(money);
            fragment.setCurrentposTraceNo(posTraceNo);
            fragment.setCurrentRefundUuid(refundUuid);
            fragment.setCurrentTransLog(transLog);
            fragment.setTradeNo(tradeNo);

            return fragment;
        }


        public UionPayDialogFragmentBuilder setNum(String num) {
            mDeviceNum = num;
                        return this;
        }

        public UionPayDialogFragmentBuilder closeListener(OnClickListener listener) {
            mbuilderCloseListener = listener;
            return this;
        }

        public UionPayDialogFragmentBuilder tryagainListener(OnClickListener listener) {
            mbuilderTryAgainListener = listener;
            return this;
        }

    }

    @Override
    public void onClick(View view) {
        if (view.equals(close_button)) {
            if (mcloseListener != null) {
                mcloseListener.onClick(view);

            }

            dimissUionDialog();
        }
        if (view.equals(try_again_button)) {
            doPos();
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);            }
            if (mtryAgainListener != null) {
                mtryAgainListener.onClick(view);

            }
        }

    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Log.d(TAG, TAG + "this.onKey------------");
            return true;
        }
        return false;
    }

    public interface PosOvereCallback {

        public void onFinished(UionPayStaus status, boolean issuccess, PosTransLog log, NewLDResponse ldResponse);
    }


    public void changeViewByType(UionViewStaus staus, String money) {
        if (getActivity() == null) {
            return;
        }

        String showinfo = "";
        String showmoney = "";
        switch (staus) {
            case Credit_card_Ready:

                if (currentpayStaus == UionPayStaus.PAY) {
                    showinfo = getString(R.string.uion_credit_card_ready, readyCountDown);
                    showmoney = String.format(getString(R.string.uion_credit_card_money), money);
                } else if (currentpayStaus == UionPayStaus.RETURN || currentpayStaus == UionPayStaus.CANCLE) {
                    showinfo = getString(R.string.uion_credit_card_ready, readyCountDown);
                    showmoney = String.format(getString(R.string.uion_return_money), money);
                }

                show_credit_card_Ready(showinfo, showmoney);

                break;
            case Credit_card_operating:

                if (currentpayStaus == UionPayStaus.PAY) {
                    showinfo = getString(R.string.uion_credit_card_operating);
                    showmoney = String.format(getString(R.string.uion_credit_card_money), money);
                } else if (currentpayStaus == UionPayStaus.RETURN || currentpayStaus == UionPayStaus.CANCLE) {
                    showinfo = getString(R.string.uion_credit_card_operating);
                    showmoney = String.format(getString(R.string.uion_return_money), money);
                }
                show_credit_card_operating(showinfo, showmoney);

                break;
            case Credit_card_success:
                if (currentpayStaus == UionPayStaus.PAY) {
                    showinfo = getString(R.string.uion_credit_card_success);
                    showmoney = String.format(getString(R.string.uion_credit_card_money), money);
                } else if (currentpayStaus == UionPayStaus.RETURN || currentpayStaus == UionPayStaus.CANCLE) {
                    showinfo = getString(R.string.uion_return_success);
                    showmoney = String.format(getString(R.string.uion_return_money), money);
                }

                show_credit_card_success(showinfo, showmoney);

                break;
            case Credit_card_fail:
                String errorInfo = "";
                if (errLDResponse != null) {
                    errorInfo = errLDResponse.getRejCodeExplain();
                    Log.d(TAG, TAG + "rejCode" + errLDResponse.getRejCode() + errorInfo);
                }
                if (errLDResponse != null && errLDResponse.getRejCode().equals("XB")) {
                    errorInfo = String.format(getString(R.string.pay_on_specify_device), deviceNum);
                }

                if (currentpayStaus == UionPayStaus.PAY) {
                    showinfo = getString(R.string.uion_credit_card_fail, errorInfo);
                    showmoney = String.format(getString(R.string.uion_credit_card_money), money);
                } else if (currentpayStaus == UionPayStaus.RETURN || currentpayStaus == UionPayStaus.CANCLE) {
                    showinfo = getString(R.string.uion_return_fail, errorInfo);
                    showmoney = String.format(getString(R.string.uion_return_money), money);
                }

                show_credit_card_fail(showinfo, showmoney, false);

                break;

            default:

                break;
        }

    }



    private void show_credit_card_Ready(String showinfo, String money) {

                center_iv.setBackgroundResource(ICON_READY);
        pay_money_tv.setVisibility(View.VISIBLE);
        pay_money_tv.setText(money);
        close_button.setVisibility(View.GONE);
        show_info_tv.setVisibility(View.VISIBLE);
        show_info_tv.setText(showinfo);
        reminder_msg_tv.setText(R.string.uion_credit_card_ready_remind);
        reminder_msg_tv.setVisibility(View.VISIBLE);
        try_again_button.setVisibility(View.INVISIBLE);
    }


    private void show_credit_card_operating(String showinfo, String money) {
                center_iv.setBackgroundResource(ICON_READY);
        pay_money_tv.setVisibility(View.VISIBLE);
        pay_money_tv.setText(money);
        close_button.setVisibility(View.INVISIBLE);
        show_info_tv.setVisibility(View.VISIBLE);
        show_info_tv.setText(showinfo);
        reminder_msg_tv.setVisibility(View.GONE);
        try_again_button.setVisibility(View.INVISIBLE);
    }



    private void show_credit_card_success(String showinfo, String money) {
                center_iv.setBackgroundResource(ICON_SUCCESS);
        pay_money_tv.setVisibility(View.VISIBLE);
        pay_money_tv.setText(money);
        close_button.setVisibility(View.INVISIBLE);
        show_info_tv.setVisibility(View.VISIBLE);
        show_info_tv.setText(showinfo);
        reminder_msg_tv.setVisibility(View.GONE);
        try_again_button.setVisibility(View.INVISIBLE);
        hideDialogDelayed();
    }


    private void show_credit_card_fail(String showinfo, String money, boolean isHideDialog) {
                center_iv.setBackgroundResource(ICON_FAILED);
        pay_money_tv.setVisibility(View.INVISIBLE);
        pay_money_tv.setText(money);
        close_button.setVisibility(View.VISIBLE);
        show_info_tv.setVisibility(View.VISIBLE);
        show_info_tv.setText(showinfo);
        reminder_msg_tv.setVisibility(View.GONE);
        try_again_button.setVisibility(View.VISIBLE);
        try_again_button.setText(getString(R.string.try_again));
        if (isHideDialog)
            hideDialogDelayed();
    }


    public void setCurrentpayStaus(UionPayStaus currentpayStaus) {
        this.currentpayStaus = currentpayStaus;
    }


    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }


    private void hideDialogDelayed() {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(0, delaytime);
        }

    }


    private void dimissUionDialog() {
        dismissAllowingStateLoss();
        DisplayServiceManager.doCancel(getActivity());
        if (mPosOvereCallback != null) {
            if (!isSuccess) {                if (errLDResponse == null) {
                }
                mPosOvereCallback.onFinished(currentpayStaus, false, null, errLDResponse);
            } else {
                mPosOvereCallback.onFinished(currentpayStaus, true, resultTranLog, null);
            }

        }

    }

    public static class UiHandler extends Handler {
        private SoftReference<UionPayDialogFragment> weakReference;

        public UiHandler(UionPayDialogFragment dialogFragment) {
            weakReference = new SoftReference<UionPayDialogFragment>(dialogFragment);
        }

        @Override
        public void handleMessage(android.os.Message msg) {

            UionPayDialogFragment dialogFragment = weakReference.get();
            switch (msg.what) {
                case 0:
                    if (dialogFragment != null) {
                        dialogFragment.dimissUionDialog();
                    }

                    break;
                default:
                    break;
            }

        }

    }

    public enum UionPayStaus {
        PAY,         RETURN,         CANCLE,     }

    private enum UionViewStaus {
        Credit_card_Ready,         Credit_card_operating,         Credit_card_success,         Credit_card_fail
    }

    private static String uuid() {
        return SystemUtils.genOnlyIdentifier();
    }


    private void startCountDown() {
        if (countDownHandler != null) {
            countDownHandler.sendEmptyMessageDelayed(0, 1000);
        }

    }


    private void stopCountDown() {
        readyCountDown = 30;
        if (countDownHandler != null) {
            countDownHandler.removeCallbacksAndMessages(null);
        }

    }


    private static class UionCountDownHandler extends Handler {
        private WeakReference<UionPayDialogFragment> weakReference;

        public UionCountDownHandler(UionPayDialogFragment dialogFragment) {
            weakReference = new WeakReference<UionPayDialogFragment>(dialogFragment);
        }

        @Override
        public void handleMessage(android.os.Message msg) {

            UionPayDialogFragment dialogFragment = weakReference.get();
            switch (msg.what) {
                case 0:
                    if (dialogFragment != null) {
                        dialogFragment.readyCountDown = dialogFragment.readyCountDown - 1;

                        if (dialogFragment.readyCountDown <= 0) {
                            dialogFragment.readyCountDown = 0;

                        }

                        dialogFragment.changeViewByType(UionViewStaus.Credit_card_Ready, Utils.formatPrice(Math.abs(dialogFragment.currentmoney.doubleValue())));
                    }
                    sendEmptyMessageDelayed(0, 1000);

                    break;
                default:
                    break;
            }
        }
    }
}
