package com.zhongmei.bty.common.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

/**
 * 公告弹窗
 *
 * @created 2017/7/3
 */
@SuppressLint("ValidFragment")
public class CommonNoticeDialog<T> extends BasicDialogFragment {

    /*public interface OnItemClickListener<T> {
        void OnItemClick(DialogFragment dialog, List<T> item);
    }*/

    TextView tvTitle;
    TextView tvSubTitle;
    ImageView ivClose;
    TextView lvItem;
    Button btnOk;

    View.OnClickListener buttonClickListener;

    String title;
    String buttonText;
    String message;

    public static <T> CommonNoticeDialog show(FragmentActivity activity, int titleResId, String message, int buttonTextResId, View.OnClickListener listener) {
        return CommonNoticeDialog.with(activity)
                .setTitle(titleResId)
                .setMessage(message)
                .setButtonText(buttonTextResId, listener)
                .showDialog();
    }

    public static Builder with(FragmentActivity activity) {
        return new CommonNoticeDialog.Builder(activity);
    }

    static public class Builder<T> {

        FragmentActivity activity;
        CommonNoticeDialog multiChoiceDialog;

        public Builder(FragmentActivity activity) {
            this.activity = activity;
            this.multiChoiceDialog = new CommonNoticeDialog();
        }

        public Builder setTitle(@StringRes int titleResId) {
            return setTitle(activity.getString(titleResId));
        }

        public Builder setTitle(String title) {
            multiChoiceDialog.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            multiChoiceDialog.message = message;
            return this;
        }

        public Builder setButtonText(int buttonTextResId, View.OnClickListener listener) {
            return setButtonText(activity.getString(buttonTextResId), listener);
        }

        public Builder setButtonText(String buttonText, View.OnClickListener listener) {
            multiChoiceDialog.buttonText = buttonText;
            multiChoiceDialog.buttonClickListener = listener;
            return this;
        }

        public CommonNoticeDialog showDialog() {
            multiChoiceDialog.show(activity.getSupportFragmentManager(), "CommonNoticeDialog");
            return multiChoiceDialog;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_common_notice, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        tvTitle = findViewById(R.id.tvTitle);
        tvSubTitle = findViewById(R.id.tvSubTitle);
        ivClose = findViewById(R.id.ivClose);
        lvItem = findViewById(R.id.lvItem);
        btnOk = findViewById(R.id.btnOk);

        tvTitle.setText(title);
        btnOk.setText(buttonText);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
        lvItem.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
                if (buttonClickListener != null) {
                    buttonClickListener.onClick(v);
                }
            }
        });
    }

}
