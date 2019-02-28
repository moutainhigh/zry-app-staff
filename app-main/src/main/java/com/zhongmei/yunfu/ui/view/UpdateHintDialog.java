package com.zhongmei.yunfu.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * Created by dingzb on 2019/2/28.
 */
@EFragment(R.layout.dialog_update_hint)
public class UpdateHintDialog extends DialogFragment implements DialogInterface.OnKeyListener{

    @ViewById(R.id.common_dialog_title)
    protected TextView tv_title;

    @ViewById(R.id.common_dialog_message)
    protected TextView tv_message;

    @ViewById(R.id.positive_button)
    protected Button btn_positive;

    @ViewById(R.id.negative_button)
    protected Button btn_negative;


    @FragmentArg("title")
    protected String title;

    @FragmentArg("message")
    protected String message;

    @FragmentArg("negative")
    protected String negative;

    @FragmentArg("positive")
    protected String positive;

    @FragmentArg("positiveBgRes")
    protected int positiveBgRes = -1;

    @FragmentArg("negativeBgRes")
    protected int negativeBgRes = -1;

    private View.OnClickListener onPositiveListener;
    private View.OnClickListener onNegativeListener;

    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    @AfterViews
    protected void init(){
        getDialog().getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,
                FLAG_HOMEKEY_DISPATCHED);
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDialog().setCanceledOnTouchOutside(false);

        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        if (!TextUtils.isEmpty(message)) {
            setMessage(message);
        }
        getDialog().setOnKeyListener(this);
        if (!TextUtils.isEmpty(negative)) {
            btn_negative.setText(negative);
            btn_negative.setOnClickListener(onNegativeListener);
            btn_negative.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(positive)) {
            btn_positive.setText(positive);
            btn_positive.setOnClickListener(onPositiveListener);
            btn_positive.setVisibility(View.VISIBLE);
        }

        if (negativeBgRes > -1) {
            btn_negative.setBackgroundResource(negativeBgRes);
        }

        if (positiveBgRes > -1) {
            btn_positive.setBackgroundResource(positiveBgRes);
        }

        if (btn_negative.getVisibility() == View.GONE) {
            btn_positive
                    .setBackgroundResource(com.zhongmei.yunfu.ui.R.drawable.commonmodule_dialog_positive_only);
        } else if (btn_positive.getVisibility() == View.GONE) {
            btn_negative
                    .setBackgroundResource(com.zhongmei.yunfu.ui.R.drawable.commonmodule_dialog_positive_only);
        } else if (btn_positive.getVisibility() == View.GONE
                && btn_negative.getVisibility() == View.GONE) {
            throw new IllegalArgumentException(
                    "no botton dialog??please check your builder!!!");
        }
    }


    @Override
    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_HOME) {
//            if (mCancelWithHomeKey) {
//            dismiss();
//            }
            if (getActivity() != null) {
                getActivity().onKeyDown(keyCode, event);
            }

            return false;
        }
        return false;
    }

    public static class UpdateHintDialogBuilder {
        private Context context;

        protected Bundle mBundle;

        protected View.OnClickListener mNegativeListener;

        protected View.OnClickListener mpositiveLinstner;

        public UpdateHintDialogBuilder(Context context) {
            this.context = context;
            mBundle = new Bundle();
        }

        public UpdateHintDialog build() {
            UpdateHintDialog fragment = new UpdateHintDialog_();
            fragment.setArguments(mBundle);
            if (mNegativeListener != null) {
                fragment.setOnNegativeListener(mNegativeListener);
            }
            if (mpositiveLinstner != null) {
                fragment.setOnPositiveListener(mpositiveLinstner);
            }
            return fragment;
        }

        public UpdateHintDialog.UpdateHintDialogBuilder negativeLisnter(
                View.OnClickListener listener) {
            mNegativeListener = listener;
            return this;
        }

        public UpdateHintDialog.UpdateHintDialogBuilder positiveLinstner(
                View.OnClickListener listener) {
            mpositiveLinstner = listener;
            return this;
        }

        public UpdateHintDialog.UpdateHintDialogBuilder title(int title) {
            return title(context.getResources()
                    .getString(title));
        }

        public UpdateHintDialog.UpdateHintDialogBuilder title(String title) {
            mBundle.putString("title", title);
            return this;
        }

        public UpdateHintDialog.UpdateHintDialogBuilder title(CharSequence title) {
            mBundle.putCharSequence("title", title);
            return this;
        }


        public UpdateHintDialog.UpdateHintDialogBuilder message(int title) {
            return message(context.getResources()
                    .getString(title));
        }

        public UpdateHintDialog.UpdateHintDialogBuilder message(CharSequence message) {
            mBundle.putCharSequence("message", message);
            return this;
        }

        public UpdateHintDialog.UpdateHintDialogBuilder negativeText(String negative) {
            mBundle.putString("negative", negative);
            return this;
        }

        public UpdateHintDialog.UpdateHintDialogBuilder positiveText(String positive) {
            mBundle.putString("positive", positive);
            return this;
        }

        public UpdateHintDialog.UpdateHintDialogBuilder negativeText(int negative) {
            return negativeText(context.getResources()
                    .getString(negative));
        }

        public UpdateHintDialog.UpdateHintDialogBuilder positiveText(int positive) {
            return positiveText(context.getResources()
                    .getString(positive));
        }


        public UpdateHintDialog.UpdateHintDialogBuilder negativeBtnRes(int res) {
            mBundle.putInt("negativeBgRes", res);
            return this;
        }

        public UpdateHintDialog.UpdateHintDialogBuilder positiveBtnRes(int res) {
            mBundle.putInt("positiveBgRes", res);
            return this;
        }

    }


    public void setOnPositiveListener(View.OnClickListener onPositiveListener) {
        this.onPositiveListener = onPositiveListener;
    }

    public void setOnNegativeListener(View.OnClickListener onNegativeListener) {
        this.onNegativeListener = onNegativeListener;
    }

    public void setMessage(String message) {
        tv_message.setVisibility(View.VISIBLE);
        tv_message.setText(Html.fromHtml(message));
        tv_message.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}
