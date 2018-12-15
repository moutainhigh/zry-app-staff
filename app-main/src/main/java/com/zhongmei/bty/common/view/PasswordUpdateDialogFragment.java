package com.zhongmei.bty.common.view;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.input.NumberClickListener;
import com.zhongmei.bty.basemodule.input.NumberKeyBoard;
import com.zhongmei.bty.basemodule.input.Password;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.splash.login.ChangePasswordSuccessEvent;

import de.greenrobot.event.EventBus;

public class PasswordUpdateDialogFragment extends BasicDialogFragment implements NumberClickListener, Password.PassVerify, View.OnClickListener {
    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    private static final int PWD_MAX_VALUE = 6;
    private static final int PW_OLD = 1;
    private static final int PW_NEW = 2;
    private static final int PW_NEW_AGAIN = 3;
    //UI控件
    private Button mCloseBtn;
    private ImageView mIvHeader;
    private TextView mTvName;
    private TextView mTvWarn;
    private Password mPassword;
    private NumberKeyBoard mKeyboard;

    private MyHandler mHandler = new MyHandler();
    private PermissionVerify listener;
    private String oldPasswordNum;
    private String newPasswordNum;
    private int pwPosition = PW_OLD;
    private int verifyCount;

    public interface PermissionVerify {
        void verifySuccess(String newPasswordNum, String oldPasswordNum);
    }

    public void setListener(PermissionVerify listener) {
        this.listener = listener;
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mPassword.deleteText(true);
                    if (pwPosition == PW_NEW_AGAIN && verifyCount == 3) {
                        mTvWarn.setText(getResources().getString(R.string.account_new_password_input));
                        mTvWarn.setTextColor(Color.parseColor("#666666"));
                        pwPosition = PW_NEW;
                    }
                    break;
                case 2:
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    private void initId() {
        mCloseBtn = findViewById(R.id.close_btn);
        mIvHeader = findViewById(R.id.iv_header);
        mTvName = findViewById(R.id.tv_name);
        mTvWarn = findViewById(R.id.tv_warn);
        mPassword = findViewById(R.id.password_edt);
        mKeyboard = findViewById(R.id.software_disk_layout);

        mCloseBtn.setOnClickListener(this);
        mPassword.setListener(this);
        mKeyboard.setListener(this);
        mKeyboard.setShowClean();
    }

    private void initViewsAndEvents() {
        mKeyboard.setCleanContent(getResources().getString(R.string.account_clear));
        String name = Session.getAuthUser().getName();
        if (!TextUtils.isEmpty(name) && name.length() > 3) {
            name = name.substring(name.length() - 3);
        }
        mTvName.setText(name);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.setCancelable(false);
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.layout_password_update_dialog, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initId();
        initViewsAndEvents();
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_btn:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void numberClicked(String number) {
        mPassword.setText(number);
        showWarnInfo();
    }

    @Override
    public void clearClicked() {
        mPassword.deleteText(true);
        showWarnInfo();
    }

    @Override
    public void deleteClicked() {
        mPassword.deleteText(false);
        showWarnInfo();
    }

    @Override
    public void dismiss() {
        pwPosition = PW_OLD;
        oldPasswordNum = null;
        newPasswordNum = null;
        super.dismiss();
    }

    private void showWarnInfo() {
        if (mPassword.getInputPassword().length() == PWD_MAX_VALUE) {
            return;
        }
        switch (pwPosition) {
            case PW_OLD:
                mTvWarn.setText(getResources().getString(R.string.account_old_password_input));
                break;
            case PW_NEW:
                mTvWarn.setText(getResources().getString(R.string.account_new_password_input));
                break;
            case PW_NEW_AGAIN:
                mTvWarn.setText(getResources().getString(R.string.account_new_password_verify));
                break;
            default:
                break;
        }
        mTvWarn.setTextColor(Color.parseColor("#666666"));
    }

    @Override
    public void verifyPass() {
        if (TextUtils.isEmpty(mPassword.getInputPassword())) {
            ToastUtil.showShortToast(R.string.commonmodule_password_empty_hint);
            return;
        }
        switch (pwPosition) {
            case PW_OLD:
                verifyOldPassword();
                break;
            case PW_NEW:
                verifyNewPassword();
                break;
            case PW_NEW_AGAIN:
                verifyNewPasswordAgain();
                break;
            default:
                break;
        }
    }

    private void verifyOldPassword() {
        oldPasswordNum = mPassword.getInputPassword();
        if (Session.getFunc(UserFunc.class).checkPassword(Session.getAuthUser(), oldPasswordNum)) {
            mTvWarn.setText(getResources().getString(R.string.account_new_password_input));
            mTvWarn.setTextColor(Color.parseColor("#666666"));
            mPassword.deleteText(true);
            pwPosition = PW_NEW;
        } else {
            // 密码校验失败
            for (int i = 0; i < PWD_MAX_VALUE; i++) {
                mPassword.getChildAt(i).setBackgroundResource(R.drawable.account_password_dot_error);
            }
            mTvWarn.setText(getResources().getString(R.string.account_password_error));
            mTvWarn.setTextColor(Color.parseColor("#FE6A4D"));
            mPassword.startAnimation(Utils.shakeAnimation(3));
            Message message = Message.obtain();
            message.what = 1;
            mHandler.sendMessageDelayed(message, 600);
        }
    }

    private void verifyNewPassword() {
        newPasswordNum = mPassword.getInputPassword();
        mTvWarn.setText(getResources().getString(R.string.account_new_password_verify));
        mTvWarn.setTextColor(Color.parseColor("#666666"));
        mPassword.deleteText(true);
        pwPosition = PW_NEW_AGAIN;
    }

    private void verifyNewPasswordAgain() {
        String verifyPassword = mPassword.getInputPassword();
        if (!TextUtils.isEmpty(newPasswordNum) && newPasswordNum.equals(verifyPassword)) {
            if (null != listener) {
                listener.verifySuccess(newPasswordNum, oldPasswordNum);
            }
        } else {
            for (int i = 0; i < PWD_MAX_VALUE; i++) {
                mPassword.getChildAt(i).setBackgroundResource(R.drawable.account_password_dot_error);
            }
            mTvWarn.setText(getResources().getString(R.string.account_password_verify_failure));
            mTvWarn.setTextColor(Color.parseColor("#FE6A4D"));
            mPassword.startAnimation(Utils.shakeAnimation(3));
            verifyCount += 1;

            Message msg = Message.obtain();
            msg.what = 1;
            mHandler.sendMessageDelayed(msg, 600);
        }
    }

    public void onEventMainThread(ChangePasswordSuccessEvent event) {
        if (event.success) {
            for (int i = 0; i < PWD_MAX_VALUE; i++) {
                mPassword.getChildAt(i).setBackgroundResource(R.drawable.account_password_dot_pass);
            }
            mTvWarn.setText(R.string.account_change_password_success);
            mTvWarn.setTextColor(Color.parseColor("#40D0AD"));

            Message msg = Message.obtain();
            msg.what = 2;
            mHandler.sendMessageDelayed(msg, 2000);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroyView() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
    }
}
