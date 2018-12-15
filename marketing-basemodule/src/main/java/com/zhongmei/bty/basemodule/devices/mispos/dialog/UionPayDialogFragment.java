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

/**
 * 银行卡刷卡功能实现的DialogFragment，功能：1.银行卡刷卡支付功能。2.银行卡刷卡退钱功能。3.
 * 会员卡刷卡读取卡号。 当调用UionPayDialogFragment的时候。此界面屏蔽了HOME键和BACK键。
 * UionPayDialogFragment会执行刷卡的整个流程。 重要：当界面隐藏的时候才回调监听
 * 隐藏有两种方式：1.当刷卡成功界面或者刷卡失败界面显示出来后,2S后自动隐藏。
 * 2.点击close按钮隐藏。隐藏一定会调用回调。
 *
 * @Date：2016-1-13 上午9:21:41
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */

public class UionPayDialogFragment extends BasicDialogFragment implements OnClickListener, OnKeyListener {

    private final String TAG = UionPayDialogFragment.class.getSimpleName();

    public static final int ICON_READY = R.drawable.commonmodule_uion_card_ready;

    public static final int ICON_SUCCESS = R.drawable.commonmodule_uion_card_success;

    public static final int ICON_FULL = R.drawable.mispos_uion_card_full;

    public static final int ICON_FAILED = R.drawable.commonmodule_uion_card_failed;

    protected TextView reminder_msg_tv;// 刷卡提示信息

    protected TextView pay_money_tv;// 显示金额的TextView

    protected Button close_button;// 关闭按钮。

    protected ImageView center_iv;// 中间显示图片

    protected TextView show_info_tv;// 显示结果信息的TextView

    protected Button try_again_button;// 再次尝试BUTTON

    OnClickListener mcloseListener;// 关闭按钮监听器(外部传入的点击监听器)

    OnClickListener mtryAgainListener;// 再次尝试按钮监听器(外部传入的点击监听器)

    PosOvereCallback mPosOvereCallback;// 由外部传入的监听器。用来返回刷卡的结果。

    TradePaymentVo mTradePaymentVo;// 交易信息数据。

    BigDecimal currentmoney;// 当前交易金额(外部传入的金额)

    PosTransLog currentTransLog;// 当前传入的TransLog(外部传入的金额)

    private String tradeNo;    //订单号

    String currentposTraceNo;// 当前传入的刷卡交易号。

    String currentRefundUuid;// 当前传入的退货使用的payment的uuid

    private UionPayStaus currentpayStaus = UionPayStaus.PAY;// 当前刷卡模式(外部传入的支付模式)

    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    UiHandler handler;// 自动隐藏Dialog使用的Handler

    OnTransListener onTransListener;// 刷卡功能管理类监听器

    private boolean isSuccess = false;// 判断是否刷卡成功返回结果

    private PosTransLog resultTranLog;// 刷卡返回的结果

    private NewLDResponse errLDResponse;// 刷卡返回的错误信息

    private long delaytime = 2000;// 自动隐藏刷卡成功或者失败界面的时间。默认为2S

    private String deviceNum = "";

    //private static UionPayDialogFragment fragment = new UionPayDialogFragment();

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
        reminder_msg_tv = (TextView) view.findViewById(R.id.reminder_msg_tv);// 提示信息的TextView

        pay_money_tv = (TextView) view.findViewById(R.id.pay_money_tv);// 显示金额的TextView

        close_button = (Button) view.findViewById(R.id.close_button);// 关闭按钮。

        center_iv = (ImageView) view.findViewById(R.id.center_iv);// 中间显示图片

        show_info_tv = (TextView) view.findViewById(R.id.show_info_tv);// 显示结果信息的TextView

        try_again_button = (Button) view.findViewById(R.id.try_again_button);// 再次尝试BUTTON
        getDialog().getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(false);

        show_info_tv.getPaint().setFakeBoldText(true);// 设置文字粗体

        close_button.setOnClickListener(this);

        try_again_button.setOnClickListener(this);

        getDialog().setOnKeyListener(this);
        doPos();

    }

    /**
     * 进行刷卡操作。
     *
     * @Title: doPos
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    private void doPos() {
        isSuccess = false;
        resultTranLog = null;
        errLDResponse = null;

        changeViewByType(UionViewStaus.Credit_card_Ready, Utils.formatPrice(Math.abs(currentmoney.doubleValue())));// 准备刷卡操作，界面需要展示金钱所以请传递金钱
        startCountDown();// 开始倒计时


        onTransListener = new OnTransListener() {

            @Override
            public void onActive() {
                Log.d(TAG, TAG + "----------------onActive()");
            }

            @Override
            public void onStart() {
                Log.d(TAG, TAG + "----------------onStart()");
                changeViewByType(UionViewStaus.Credit_card_operating,
                        Utils.formatPrice(Math.abs(currentmoney.doubleValue())));// 界面需要展示金钱所以请传递金钱
                stopCountDown();
            }

            @Override
            public void onFailure(NewLDResponse ldResponse) {
                Log.d(TAG, TAG + "----------------onFailure()");
                isSuccess = false;
                resultTranLog = null;
                errLDResponse = ldResponse;
                changeViewByType(UionViewStaus.Credit_card_fail,
                        Utils.formatPrice(Math.abs(currentmoney.doubleValue())));//
                stopCountDown();
            }

            @Override
            public void onConfirm(PosTransLog log) {
                Log.d(TAG, TAG + "----------------onConfirm()");
                stopCountDown();
                SharedPreferenceUtil.getSpUtil().putString(Constant.SP_POS_DEVICIE_ID, log.getTerminalNumber());// 保存每次交易的终端号。
                Log.d(TAG,
                        TAG + "onConfirm---SP_POS_DEVICIE_ID"
                                + SharedPreferenceUtil.getSpUtil().getString(Constant.SP_POS_DEVICIE_ID, "默认"));
                log.setTradeNo(tradeNo);
                isSuccess = true;
                resultTranLog = log;// 设置结果，成功赋值。
                errLDResponse = null;
                changeViewByType(UionViewStaus.Credit_card_success,
                        Utils.formatPrice(Math.abs(currentmoney.doubleValue())));
//				IPrintContentQueue.Holder.getInstance().PrintLDPosTicket(log, new OnSimplePrintListener(PrintTicketTypeEnum.BANK));// 打印银行
                //解耦后的打印
                //IPrintHelper.Holder.getInstance().printLDPosTicket(log, new PRTOnSimplePrintListener(PrintTicketTypeEnum.BANK));// 打印银行
            }

        };
        Log.d(TAG, TAG + ":doPos()");
        if (currentpayStaus == UionPayStaus.PAY) {
            NewLiandiposManager.getInstance().startPay(currentmoney, onTransListener);// 刷卡支付
        } else if (currentpayStaus == UionPayStaus.RETURN) {
            NewLiandiposManager.getInstance().startRefund(currentRefundUuid, currentposTraceNo, onTransListener);// 刷卡退货
        } else if (currentpayStaus == UionPayStaus.CANCLE) {
            NewLiandiposManager.getInstance().cancelPay(currentTransLog, onTransListener);// 刷卡撤消
        }
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

        //private Bundle mBundle;
        String mDeviceNum;
        OnClickListener mbuilderCloseListener;

        OnClickListener mbuilderTryAgainListener;

        public UionPayDialogFragmentBuilder() {
            //mBundle = new Bundle();
        }

        // 建立支付刷卡的UionPayDialogFragment（支付刷卡只用传金额）
        public UionPayDialogFragment buildPay(BigDecimal money, String no, PosOvereCallback overeCallback) {
            return build(UionPayStaus.PAY, money, no, null, null, null, overeCallback);

        }

        // 建立退货刷卡的UionPayDialogFragment（退货刷卡传金额和PosTransLog
        public UionPayDialogFragment buildReturn(BigDecimal money, String no, String refundUuid, String posTraceNo,
                                                 PosOvereCallback overeCallback) {
            return build(UionPayStaus.RETURN, money, no, refundUuid, posTraceNo, null, overeCallback);
        }

        // 建立撤消刷卡的UionPayDialogFragment(撤消刷卡传PosTransLog)
        public UionPayDialogFragment buildCancle(PosTransLog transLog, PosOvereCallback overeCallback) {
            BigDecimal bigDecimal = NewLiandiposManager.getRealMoney(transLog.getAmount());

            return build(UionPayStaus.CANCLE, bigDecimal, null, uuid(), null, transLog, overeCallback);
        }

        private UionPayDialogFragment build(UionPayStaus payStaus, BigDecimal money, String tradeNo, String refundUuid,
                                            String posTraceNo, PosTransLog transLog, PosOvereCallback overeCallback) {

            //fragment.setArguments(mBundle);
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

        /**
         * 设置卡号NUM
         *
         * @Title: setNum 设置刷卡设备号。为了在不同设备刷卡失败的时候返回错误信息
         * @Description: TODO
         * @Param @param num
         * @Param @return TODO
         * @Return UionPayDialogFragment 返回类型
         */
        public UionPayDialogFragmentBuilder setNum(String num) {
            mDeviceNum = num;
            //fragment.setDeviceNum(num);
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
                handler.removeCallbacksAndMessages(null);// 重试的时候，应该移除隐藏Dialog
            }
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

    /**
     * 展示UI都应该调用这个方法
     *
     * @Title: changeViewByType
     * @Description: TODO
     * @Param @param staus
     * @Param @param money TODO 在UionViewStaus=
     * Credit_card_Ready或者Credit_card_operating时候显示金额
     * ，为其它值的时候没作用
     * @Return void 返回类型
     */
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

    /**
     * 显示准备刷卡的UI
     *
     * @Title: show_credit_card_Ready
     * @Description: TODO
     * @Param @param showinfo 显示结果信息
     * @Param @param money TODO 显示金额
     * @Return void 返回类型
     */

    private void show_credit_card_Ready(String showinfo, String money) {

        //DisplayServiceManager.doUpdateUnionPay(getActivity(), DisplayPayUnion.COMMAND_UNION_PRE, showinfo, money,false);
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

    /**
     * 显示正在刷卡中的UI
     *
     * @Title: show_credit_card_operating
     * @Description: TODO
     * @Param @param showinfo 显示信息
     * @Param @param money TODO 显示刷卡金额
     * @Return void 返回类型
     */
    private void show_credit_card_operating(String showinfo, String money) {
        //DisplayServiceManager.doUpdateUnionPay(getActivity(), DisplayPayUnion.COMMAND_UNION_DOING, showinfo, money);
        center_iv.setBackgroundResource(ICON_READY);
        pay_money_tv.setVisibility(View.VISIBLE);
        pay_money_tv.setText(money);
        close_button.setVisibility(View.INVISIBLE);
        show_info_tv.setVisibility(View.VISIBLE);
        show_info_tv.setText(showinfo);
        reminder_msg_tv.setVisibility(View.GONE);
        try_again_button.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示刷卡成功的UI
     *
     * @Title: show_credit_card_success
     * @Description: TODO
     * @Param @param showinfo 显示展示信息
     * @Param @param money TODO 显示金额，这里金额的View会隐藏起来 所以可以不用填写
     * @Return void 返回类型
     */

    private void show_credit_card_success(String showinfo, String money) {
        //DisplayServiceManager.doUpdateUnionPay(getActivity(), DisplayPayUnion.COMMAND_UNION_SUCCESS, showinfo, money);
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

    /**
     * 显示刷卡失败的UI
     *
     * @Title: show_credit_card_fail
     * @Description: TODO
     * @Param @param showinfo 显示展示信息
     * @Param @param money TODO 显示金额，这里金额的View会隐藏起来 所以可以不用填写
     * @Return void 返回类型
     */
    private void show_credit_card_fail(String showinfo, String money, boolean isHideDialog) {
        //DisplayServiceManager.doUpdateUnionPay(getActivity(), DisplayPayUnion.COMMAND_UNION_FAIL, showinfo, money);
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

    /**
     * 设置当前弹出框的是支付模式还是退货模式
     *
     * @Title: setCurrentpayStaus
     * @Description: TODO
     * @Param @param currentpayStaus TODO
     * @Return void 返回类型
     */
    public void setCurrentpayStaus(UionPayStaus currentpayStaus) {
        this.currentpayStaus = currentpayStaus;
    }

    /**
     * 设置刷卡失败的时候的设备号
     *
     * @Title: setDeviceNum
     * @Description: TODO
     * @Param @param deviceNum TODO
     * @Return void 返回类型
     */
    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    /**
     * 当弹出刷卡成功或者刷卡失败的时候，自动在2S后隐藏Dialog
     *
     * @Title: hideDialogDelayed
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    private void hideDialogDelayed() {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(0, delaytime);
        }

    }

    /**
     * 隐藏Dialog 当点击Close按钮的时候隐藏并回调方法，2后自动隐藏会回调该方法
     *
     * @Title: dimissUionDialog
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    private void dimissUionDialog() {
        dismissAllowingStateLoss();
        DisplayServiceManager.doCancel(getActivity());
        if (mPosOvereCallback != null) {
            if (!isSuccess) {// 根据isSuccess的值来判断是否成功
                if (errLDResponse == null) {// 如果未空的时候。

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
        PAY, // 刷卡支付
        RETURN, // 刷卡退货
        CANCLE, // 撤消
    }

    private enum UionViewStaus {
        Credit_card_Ready, // 请进行刷卡操作（准备进行刷卡）
        Credit_card_operating, // 刷卡进行中
        Credit_card_success, // 刷卡成功
        Credit_card_fail // 刷卡失败

    }

    private static String uuid() {
        return SystemUtils.genOnlyIdentifier();
    }

    /**
     * 开始倒计时
     *
     * @Title: startCountDown
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    private void startCountDown() {
        if (countDownHandler != null) {
            countDownHandler.sendEmptyMessageDelayed(0, 1000);
        }

    }

    /**
     * 停止倒计时
     *
     * @Title: stopCountDown
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    private void stopCountDown() {
        readyCountDown = 30;
        if (countDownHandler != null) {
            countDownHandler.removeCallbacksAndMessages(null);
        }

    }

    /**
     * 倒计时30秒的Handler
     *
     * @Date：2016-4-6 下午4:25:48
     * @Description: TODO
     * @Version: 1.0
     * <p>
     * rights reserved.
     */
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
