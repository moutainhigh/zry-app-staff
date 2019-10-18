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




public class ReadKeyboardDialogFragment extends BasicDialogFragment implements OnClickListener, OnKeyListener {

    private final String TAG = ReadKeyboardDialogFragment.class.getSimpleName();

    public static final int ICON_READY_SINGLE = R.drawable.mispos_uion_password_ready;

    public static final int ICON_SUCCESS = R.drawable.mispos_uion_read_card_id_success;

    public static final int ICON_FAILED = R.drawable.mispos_uion_password_fail;

    protected TextView reminder_msg_tv;
    protected Button close_button;
    protected ImageView center_iv;
    protected TextView show_info_tv;
    protected Button try_again_button;
    OnClickListener mcloseListener;
    OnClickListener mtryAgainListener;
    CardOvereCallback mPosOvereCallback;
    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    UiHandler handler;
    NewLiandiposManager.OnTransListener onTransListener;
    private boolean isSuccess = false;
    private PosTransLog resultTranLog;
    private NewLDResponse errLDResponse;
    private long delaytime = 3 * 1000;
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
        reminder_msg_tv = (TextView) view.findViewById(R.id.reminder_msg_tv);
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

        changeViewByType(UionViewStaus.Credit_card_Ready, "");        startCountDown();
        onTransListener = new NewLiandiposManager.OnTransListener() {

            @Override
            public void onActive() {
                Log.d(TAG, TAG + "----------------onActive()");
            }

            @Override
            public void onStart() {
                Log.d(TAG, TAG + "----------------onStart()");
                changeViewByType(UionViewStaus.Credit_card_operating, "");                stopCountDown();
            }

            @Override
            public void onFailure(NewLDResponse ldResponse) {
                Log.d(TAG, TAG + "----------------onFailure()");
                isSuccess = false;
                resultTranLog = null;
                errLDResponse = ldResponse;
                changeViewByType(UionViewStaus.Credit_card_fail, "");                stopCountDown();

            }

            @Override
            public void onConfirm(PosTransLog log) {
                Log.d(TAG, TAG + "----------------onConfirm()");
                stopCountDown();

                isSuccess = true;
                resultTranLog = log;                errLDResponse = null;
                                                mPosOvereCallback.onSuccess(resultTranLog.getKeyValue());

            }

        };
        Log.d(TAG, TAG + ":doPos()");

        NewLiandiposManager.getInstance().startReadKeyboardNum(getActivity().getApplicationContext(), onTransListener);
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

                        dismissAllowingStateLoss();
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

    public interface CardOvereCallback {

        public void onSuccess(String keybord);
        public void onFail(NewLDResponse ldResponse);
    }


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

                show_credit_card_fail(showinfo, "");

                break;

            default:

                break;
        }

    }



    private void show_credit_card_Ready(String showinfo, String money) {

        showReadyCenterView();
        close_button.setVisibility(View.GONE);
        show_info_tv.setVisibility(View.VISIBLE);
        show_info_tv.setText(showinfo);
        reminder_msg_tv.setText(R.string.uion_credit_card_ready_remind);
        reminder_msg_tv.setVisibility(View.VISIBLE);
        try_again_button.setVisibility(View.INVISIBLE);
    }


    private void show_credit_card_operating(String showinfo, String money) {
        center_iv.setVisibility(View.VISIBLE);
        center_iv.setBackgroundResource(ICON_READY_SINGLE);
        close_button.setVisibility(View.INVISIBLE);
        show_info_tv.setVisibility(View.VISIBLE);
        show_info_tv.setText(showinfo);
        reminder_msg_tv.setVisibility(View.GONE);
        try_again_button.setVisibility(View.INVISIBLE);
    }



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


    private void showReadyCenterView() {

        center_iv.setVisibility(View.VISIBLE);
        center_iv.setBackgroundResource(ICON_READY_SINGLE);

    }


    private void hideDialogDelayed() {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(0, delaytime);
        }

    }



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
        Credit_card_Ready,         Credit_card_operating,         Credit_card_success,         Credit_card_fail
    }


    private void startCountDown() {
        if (countDownHandler != null) {
            countDownHandler.sendEmptyMessageDelayed(0, 1000);
        }

    }


    private void stopCountDown() {
        readyCountDown = 120;
        if (countDownHandler != null) {
            countDownHandler.removeCallbacksAndMessages(null);
        }

    }


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
