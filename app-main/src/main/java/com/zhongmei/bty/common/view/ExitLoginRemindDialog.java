package com.zhongmei.bty.common.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;

/**
 * 自助激活设备
 *
 * @created 2017/7/3
 */
@SuppressLint("ValidFragment")
public class ExitLoginRemindDialog extends CommonDialogFragment {

    TextView common_dialog_title;
    Button negative_button;
    int countDown = 3;
    Handler handler = new Handler();

    public static ExitLoginRemindDialog show(FragmentActivity activity) {
        ExitLoginRemindDialog autoActivateDialog = (ExitLoginRemindDialog) ExitLoginRemindDialog.instantiate(activity, ExitLoginRemindDialog.class.getName());
        autoActivateDialog.show(activity.getSupportFragmentManager(), null);
        return autoActivateDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_exit_login_remind, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        common_dialog_title = findViewById(R.id.common_dialog_title);
        negative_button = findViewById(R.id.negative_button);

        setTitleText(countDown);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (--countDown > 0) {
                    handler.postDelayed(this, 1000);
                    setTitleText(countDown);
                } else {
                    dismissAllowingStateLoss();
                    //LoginActivity.logoutDialog(getActivity());
                }
            }
        }, 1500);

        negative_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
    }

    private void setTitleText(int countDown) {
        common_dialog_title.setText(getString(R.string.dialog_exit_remind_title) + "\n" + countDown + "s");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}
