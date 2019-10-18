package com.zhongmei.yunfu.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
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


@EFragment(R.layout.dialog_download_hint)
public class DownloadHintDialog extends DialogFragment implements DialogInterface.OnKeyListener,View.OnClickListener{

    @ViewById(R.id.pb_round)
   public RoundProgressBar pb_round;

    @ViewById(R.id.tv_message)
   public TextView tv_message;

    @ViewById(R.id.btn_negative)
    public Button btn_negative;

    @ViewById(R.id.btn_positive)
    public Button btn_positive;


    @FragmentArg("message")
    protected String message;

    @FragmentArg("max")
    protected Long max;

    @FragmentArg("negative")
    protected String negative;

    @FragmentArg("positive")
    protected String positive;

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

        if (!TextUtils.isEmpty(message)) {
            setMessage(message);
        }
        if(max!=null){
            pb_round.setMax(max);
            pb_round.setProgress(0);
        }
        getDialog().setOnKeyListener(this);
        if (!TextUtils.isEmpty(negative)) {
            btn_negative.setText(negative);
            btn_negative.setOnClickListener(this);
            btn_negative.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(positive)) {
            btn_positive.setText(positive);
            btn_positive.setOnClickListener(this);
            btn_positive.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_HOME) {
            if (getActivity() != null) {
                getActivity().onKeyDown(keyCode, event);
            }

            return false;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_negative:
                if(onNegativeListener!=null){
                    onNegativeListener.onClick(view);
                }
                dismissAllowingStateLoss();
                break;
            case R.id.btn_positive:
                if(onPositiveListener!=null){
                    onPositiveListener.onClick(view);
                }
                dismissAllowingStateLoss();
                break;
        }
    }

    public static class DownloadHintDialogBuilder {
        private Context context;

        protected Bundle mBundle;

        protected View.OnClickListener mNegativeListener;

        protected View.OnClickListener mpositiveLinstner;

        public DownloadHintDialogBuilder(Context context) {
            this.context = context;
            mBundle = new Bundle();
        }

        public DownloadHintDialog build() {
            DownloadHintDialog fragment = new DownloadHintDialog_();
            fragment.setArguments(mBundle);
            if (mNegativeListener != null) {
                fragment.setOnNegativeListener(mNegativeListener);
            }
            if (mpositiveLinstner != null) {
                fragment.setOnPositiveListener(mpositiveLinstner);
            }
            return fragment;
        }

        public DownloadHintDialog.DownloadHintDialogBuilder negativeLisnter(
                View.OnClickListener listener) {
            mNegativeListener = listener;
            return this;
        }

        public DownloadHintDialog.DownloadHintDialogBuilder positiveLinstner(
                View.OnClickListener listener) {
            mpositiveLinstner = listener;
            return this;
        }

        public DownloadHintDialog.DownloadHintDialogBuilder max(long max) {
             mBundle.putLong("max", max);
             return this;
        }

        public DownloadHintDialog.DownloadHintDialogBuilder message(int title) {
            return message(context.getResources()
                    .getString(title));
        }

        public DownloadHintDialog.DownloadHintDialogBuilder message(CharSequence message) {
            mBundle.putCharSequence("message", message);
            return this;
        }

        public DownloadHintDialog.DownloadHintDialogBuilder negativeText(String negative) {
            mBundle.putString("negative", negative);
            return this;
        }

        public DownloadHintDialog.DownloadHintDialogBuilder positiveText(String positive) {
            mBundle.putString("positive", positive);
            return this;
        }

        public DownloadHintDialog.DownloadHintDialogBuilder negativeText(int negative) {
            return negativeText(context.getResources()
                    .getString(negative));
        }

        public DownloadHintDialog.DownloadHintDialogBuilder positiveText(int positive) {
            return positiveText(context.getResources()
                    .getString(positive));
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
        tv_message.setText(message);
    }

    public void setProgress(Long progress){
        pb_round.setProgress(progress);
    }


}
