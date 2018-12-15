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

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.devices.mispos.event.EventReadKeyboard;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLDResponse;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLiandiposManager;
import com.zhongmei.bty.commonmodule.database.entity.local.PosTransLog;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import de.greenrobot.event.EventBus;


/**
 * 实现读取得DialogFrament
 *
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */

public class ReadKeyboardDialogFragment extends BasicDialogFragment implements OnClickListener, OnKeyListener {

    private final String TAG = ReadKeyboardDialogFragment.class.getSimpleName();

    public static final int ICON_READY_SINGLE = R.drawable.mispos_uion_password_ready;

    public static final int ICON_SUCCESS = R.drawable.mispos_uion_read_card_id_success;

    public static final int ICON_FAILED = R.drawable.mispos_uion_password_fail;

    protected TextView reminder_msg_tv;// 刷卡提示信息

    protected Button close_button;// 关闭按钮。

    protected ImageView center_iv;// 中间显示图片

    protected TextView show_info_tv;// 显示结果信息的TextView

    protected Button try_again_button;// 再次尝试BUTTON

    OnClickListener mcloseListener;// 关闭按钮监听器(外部传入的点击监听器)

    OnClickListener mtryAgainListener;// 再次尝试按钮监听器(外部传入的点击监听器)

    CardOvereCallback mPosOvereCallback;// 由外部传入的监听器。用来返回刷卡的结果。

    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    UiHandler handler;// 自动隐藏Dialog使用的Handler

    NewLiandiposManager.OnTransListener onTransListener;// 刷卡功能管理类监听器

    private boolean isSuccess = false;// 判断是否刷卡成功返回结果

    private PosTransLog resultTranLog;// 刷卡返回的结果

    private NewLDResponse errLDResponse;// 刷卡返回的错误信息

    private long delaytime = 3 * 1000;// 自动隐藏刷卡成功或者失败界面的时间。默认为2S

    private int readyCountDown = 120;

    private CountDownHandler countDownHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG + "onCreate()");
        handler = new UiHandler(this);
        EventBus.getDefault().register(this);
        countDownHandler = new CountDownHandler(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, TAG + "onCreateView()");
        View view = inflater.inflate(R.layout.mispos_union_key_board_layout, container);
        initViewById(view);

        return view;
    }

    @Override
    public void onDestroy() {
        onTransListener = null;
        EventBus.getDefault().unregister(this);
        Log.d(TAG, TAG + "onDestroy()");
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
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

        changeViewByType(UionViewStaus.Credit_card_Ready, "");// 准备刷卡操作
        startCountDown();// 开始倒计时

        onTransListener = new NewLiandiposManager.OnTransListener() {

            @Override
            public void onActive() {
                Log.d(TAG, TAG + "----------------onActive()");
            }

            @Override
            public void onStart() {
                Log.d(TAG, TAG + "----------------onStart()");
                changeViewByType(UionViewStaus.Credit_card_operating, "");// 界面需要展示
                stopCountDown();
            }

            @Override
            public void onFailure(NewLDResponse ldResponse) {
                Log.d(TAG, TAG + "----------------onFailure()");
                isSuccess = false;
                resultTranLog = null;
                errLDResponse = ldResponse;
                changeViewByType(UionViewStaus.Credit_card_fail, "");//
                stopCountDown();

            }

            @Override
            public void onConfirm(PosTransLog log) {
                Log.d(TAG, TAG + "----------------onConfirm()");
                stopCountDown();

                isSuccess = true;
                resultTranLog = log;// 设置结果，成功赋值。
                errLDResponse = null;
                // changeViewByType(UionViewStaus.Credit_card_success,
                // "");
                mPosOvereCallback.onSuccess(resultTranLog.getKeyValue());
                //取到resultTranLog.getKeyValue() 是（小写32位）MD5加密的

            }

        };
        Log.d(TAG, TAG + ":doPos()");

        NewLiandiposManager.getInstance().startReadKeyboardNum(getActivity().getApplicationContext(), onTransListener);// 读取键盘值

    }

    public void setCloseListener(OnClickListener listener) {
        mcloseListener = listener;
    }

    public void setTryAgainListener(OnClickListener listener) {
        mtryAgainListener = listener;
    }

    public void setPosOvereCallback(CardOvereCallback mPosOvereCallback) {
        this.mPosOvereCallback = mPosOvereCallback;
    }

    public static class ReadKeyboardFragmentBuilder {

        private Bundle mBundle;

        OnClickListener mbuilderCloseListener;

        OnClickListener mbuilderTryAgainListener;

        ReadKeyboardDialogFragment fragment;

        public ReadKeyboardFragmentBuilder() {
            mBundle = new Bundle();
        }

        public ReadKeyboardDialogFragment build() {
            return build(null);
        }

        public ReadKeyboardDialogFragment build(CardOvereCallback overeCallback) {
            fragment = new ReadKeyboardDialogFragment();
            fragment.setArguments(mBundle);

            if (mbuilderCloseListener != null) {
                fragment.setCloseListener(mbuilderCloseListener);
            }
            if (mbuilderTryAgainListener != null) {
                fragment.setTryAgainListener(mbuilderTryAgainListener);
            }
            if (overeCallback != null) {
                fragment.setPosOvereCallback(overeCallback);
            }

            return fragment;
        }

        public void setPosOverCallback(CardOvereCallback overeCallback) {
            fragment.setPosOvereCallback(overeCallback);
        }

        public ReadKeyboardFragmentBuilder closeListener(OnClickListener listener) {
            mbuilderCloseListener = listener;
            return this;
        }

        public ReadKeyboardFragmentBuilder tryagainListener(OnClickListener listener) {
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

            // dimissUionDialog();
            dismissAllowingStateLoss();
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

    public interface CardOvereCallback {

        public void onSuccess(String keybord);// 返回的key值

        public void onFail(NewLDResponse ldResponse);
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

        switch (staus) {
            case Credit_card_Ready:

                showinfo = getString(R.string.uion_key_bord_ready, readyCountDown);
                show_credit_card_Ready(showinfo, "");

                break;
            case Credit_card_operating:

                showinfo = getString(R.string.uion_key_bord_operating);

                show_credit_card_operating(showinfo, "");

                break;
            case Credit_card_success:

                showinfo = getString(R.string.uion_key_bord_success);

                show_credit_card_success(showinfo, "");

                break;
            case Credit_card_fail:
                String errorInfo = "";
                if (errLDResponse != null) {
                    errorInfo = errLDResponse.getRejCodeExplain();
                    Log.d(TAG, TAG + "rejCode" + errLDResponse.getRejCode());
                }

                showinfo = errorInfo;
//				if (!PosConnectManager.isPosConnected()) {
//					showinfo = getString(R.string.card_device_no_connent);
//				}

                show_credit_card_fail(showinfo, "");

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

        showReadyCenterView();
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
        center_iv.setVisibility(View.VISIBLE);
        center_iv.setBackgroundResource(ICON_READY_SINGLE);
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
        center_iv.setVisibility(View.VISIBLE);
        center_iv.setBackgroundResource(ICON_SUCCESS);
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
    private void show_credit_card_fail(String showinfo, String money) {
        center_iv.setVisibility(View.VISIBLE);
        center_iv.setBackgroundResource(ICON_FAILED);
        close_button.setVisibility(View.VISIBLE);
        show_info_tv.setVisibility(View.VISIBLE);
        show_info_tv.setText(showinfo);
        reminder_msg_tv.setVisibility(View.GONE);
        try_again_button.setVisibility(View.VISIBLE);
        try_again_button.setText(getString(R.string.uion_input_again));
        hideDialogDelayed();
    }

    /**
     * 根据状态显示中间图片的方法
     *
     * @Title: showReadyCenter
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    private void showReadyCenterView() {

        center_iv.setVisibility(View.VISIBLE);
        center_iv.setBackgroundResource(ICON_READY_SINGLE);

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

    // /**
    // * 隐藏Dialog 当点击Close按钮的时候隐藏并回调方法，2后自动隐藏会回调该方法
    // *

    // * @Title: dimissUionDialog
    // * @Return void 返回类型
    // */
    // private void dimissUionDialog() {
    // dismissAllowingStateLoss();
    // if (mPosOvereCallback != null) {
    // if (!isSuccess) {// 根据isSuccess的值来判断是否成功
    // if (errLDResponse == null) {// 如果未空的时候。
    //
    // }
    // mPosOvereCallback.onFail(errLDResponse);
    // } else {
    // mPosOvereCallback.onSuccess(resultTranLog.getKeyValue());
    // }
    //
    // }
    //
    // }

    public static class UiHandler extends Handler {
        private SoftReference<ReadKeyboardDialogFragment> weakReference;

        public UiHandler(ReadKeyboardDialogFragment dialogFragment) {
            weakReference = new SoftReference<ReadKeyboardDialogFragment>(dialogFragment);
        }

        @Override
        public void handleMessage(android.os.Message msg) {

            ReadKeyboardDialogFragment dialogFragment = weakReference.get();
            switch (msg.what) {
                case 0:
                    if (dialogFragment != null) {
                        dialogFragment.dismissAllowingStateLoss();
                    }
                    break;

                default:
                    break;
            }
        }

    }

    private enum UionViewStaus {
        Credit_card_Ready, // 请进行刷卡操作（准备进行刷卡）
        Credit_card_operating, // 刷卡进行中
        Credit_card_success, // 刷卡成功
        Credit_card_fail // 刷卡失败

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
        readyCountDown = 120;
        if (countDownHandler != null) {
            countDownHandler.removeCallbacksAndMessages(null);
        }

    }

    /**
     * 倒计时120秒的Handler
     *
     * @Date：2016-4-6 下午4:25:48
     * @Description: TODO
     * @Version: 1.0
     * <p>
     * rights reserved.
     */
    private static class CountDownHandler extends Handler {
        private WeakReference<ReadKeyboardDialogFragment> weakReference;

        public CountDownHandler(ReadKeyboardDialogFragment dialogFragment) {
            weakReference = new WeakReference<ReadKeyboardDialogFragment>(dialogFragment);
        }

        @Override
        public void handleMessage(android.os.Message msg) {

            ReadKeyboardDialogFragment dialogFragment = weakReference.get();
            switch (msg.what) {
                case 0:
                    if (dialogFragment != null) {
                        dialogFragment.readyCountDown = dialogFragment.readyCountDown - 1;

                        if (dialogFragment.readyCountDown <= 0) {
                            dialogFragment.readyCountDown = 0;
                        }

                        dialogFragment.changeViewByType(UionViewStaus.Credit_card_Ready, "");
                    }
                    sendEmptyMessageDelayed(0, 1000);

                    break;

                default:
                    break;
            }

        }

    }

    public void onEventMainThread(EventReadKeyboard event) {
        if (event.isIssuccess()) {
            dismissAllowingStateLoss();
        } else {
            show_credit_card_fail(event.errorinfo, "");
        }

    }

}
