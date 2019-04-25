package com.zhongmei.yunfu.ui.view;

import android.content.Context;

import com.zhongmei.yunfu.ui.R;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@EFragment(resName = "commonmodule_dialog_layout")
public class CommonDialogFragment extends BasicDialogFragment implements
        OnClickListener, OnKeyListener {

    @FragmentArg("headTitle")
    protected CharSequence headTitle;

    @FragmentArg("title")
    protected CharSequence title;

    @FragmentArg("message")
    protected CharSequence message;

    @FragmentArg("negative")
    protected String negative;

    @FragmentArg("positive")
    protected String positive;

    @FragmentArg("iconType")
    protected int iconType = ICON_ERROR;

    @FragmentArg("positiveBgRes")
    protected int positiveBgRes = -1;

    @FragmentArg("negativeBgRes")
    protected int negativeBgRes = -1;

    public static final int ICON_ERROR = R.drawable.commonmodule_dialog_icon_error;

    public static final int ICON_SUCCESS = R.drawable.commonmodule_dialog_icon_success;

    public static final int ICON_WARNING = R.drawable.commonmodule_dialog_icon_warning;

    public static final int ICON_HINT = R.drawable.commonmodule_dialog_icon_hint;

    public static final int ICON_ASK = R.drawable.commonmodule_dialog_icon_ask;

    @ViewById(resName = "common_dialog_title")
    public TextView mTitle;

    @ViewById(resName = "common_dialog_message")
    public TextView mMessage;

    @ViewById(resName = "negative_button")
    public Button mNegativeButton;

    @ViewById(resName = "positive_button")
    public Button mPositiveButton;

    @ViewById(resName = "icon")
    public ImageView mIconView;

    private boolean mCancelWithHomeKey = true;

    public OnClickListener mNegativeListener;

    public OnClickListener mpositiveLinstner;
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    public void show(FragmentManager manager, String tag) {
        if (manager != null && !manager.isDestroyed()) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    @AfterViews
    protected void initView() {
        getDialog().getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,
                FLAG_HOMEKEY_DISPATCHED);
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(false);
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
        }
        if (!TextUtils.isEmpty(message)) {
            setMessage(message);
        }
        getDialog().setOnKeyListener(this);
        mIconView.setBackgroundResource(iconType);
        if (!TextUtils.isEmpty(negative)) {
            mNegativeButton.setText(negative);
            mNegativeButton.setOnClickListener(this);
            mNegativeButton.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(positive)) {
            mPositiveButton.setText(positive);
            mPositiveButton.setOnClickListener(this);
            mPositiveButton.setVisibility(View.VISIBLE);
        }

        if (negativeBgRes > -1) {
            mNegativeButton.setBackgroundResource(negativeBgRes);
        }

        if (positiveBgRes > -1) {
            mPositiveButton.setBackgroundResource(positiveBgRes);
        }

        if (mNegativeButton.getVisibility() == View.GONE) {
            mPositiveButton
                    .setBackgroundResource(R.drawable.commonmodule_dialog_positive_only);
        } else if (mPositiveButton.getVisibility() == View.GONE) {
            mNegativeButton
                    .setBackgroundResource(R.drawable.commonmodule_dialog_positive_only);
        } else if (mPositiveButton.getVisibility() == View.GONE
                && mNegativeButton.getVisibility() == View.GONE) {
            throw new IllegalArgumentException(
                    "no botton dialog??please check your builder!!!");
        }
    }

    protected void setNegativeListener(OnClickListener listener) {
        mNegativeListener = listener;
    }

    protected void setpositiveListener(OnClickListener listener) {
        mpositiveLinstner = listener;
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setMessage(CharSequence message) {
        mMessage.setVisibility(View.VISIBLE);
        mMessage.setText(message);
    }

    public static class CommonDialogFragmentBuilder {
        private Context context;

        protected Bundle mBundle;

        protected OnClickListener mNegativeListener;

        protected OnClickListener mpositiveLinstner;

        public CommonDialogFragmentBuilder(Context context) {
            this.context = context;
            mBundle = new Bundle();
        }

        public CommonDialogFragment build() {
            CommonDialogFragment fragment = new CommonDialogFragment_();
            fragment.setArguments(mBundle);
            if (mNegativeListener != null) {
                fragment.setNegativeListener(mNegativeListener);
            }
            if (mpositiveLinstner != null) {
                fragment.setpositiveListener(mpositiveLinstner);
            }
            return fragment;
        }

        public CommonDialogFragmentBuilder negativeLisnter(
                OnClickListener listener) {
            mNegativeListener = listener;
            return this;
        }

        public CommonDialogFragmentBuilder positiveLinstner(
                OnClickListener listener) {
            mpositiveLinstner = listener;
            return this;
        }

        public CommonDialogFragmentBuilder title(int title) {
            return title(context.getResources()
                    .getString(title));
        }

        public CommonDialogFragmentBuilder title(String title) {
            mBundle.putString("title", title);
            return this;
        }

        public CommonDialogFragmentBuilder title(CharSequence title) {
            mBundle.putCharSequence("title", title);
            return this;
        }

        public CommonDialogFragmentBuilder headTitle(int headTitle) {
            return headTitle(context.getResources()
                    .getString(headTitle));
        }

        public CommonDialogFragmentBuilder headTitle(String headTitle) {
            mBundle.putString("headTitle", headTitle);
            return this;
        }

        public CommonDialogFragmentBuilder headTitle(CharSequence headTitle) {
            mBundle.putCharSequence("headTitle", headTitle);
            return this;
        }

        public CommonDialogFragmentBuilder message(int title) {
            return message(context.getResources()
                    .getString(title));
        }

        public CommonDialogFragmentBuilder message(CharSequence message) {
            mBundle.putCharSequence("message", message);
            return this;
        }

        public CommonDialogFragmentBuilder negativeText(String negative) {
            mBundle.putString("negative", negative);
            return this;
        }

        public CommonDialogFragmentBuilder positiveText(String positive) {
            mBundle.putString("positive", positive);
            return this;
        }

        public CommonDialogFragmentBuilder negativeText(int negative) {
            return negativeText(context.getResources()
                    .getString(negative));
        }

        public CommonDialogFragmentBuilder positiveText(int positive) {
            return positiveText(context.getResources()
                    .getString(positive));
        }

        public CommonDialogFragmentBuilder iconType(int iconType) {
            mBundle.putInt("iconType", iconType);
            context.getResources();

            return this;
        }

        public CommonDialogFragmentBuilder negativeBtnRes(int res) {
            mBundle.putInt("negativeBgRes", res);
            return this;
        }

        public CommonDialogFragmentBuilder positiveBtnRes(int res) {
            mBundle.putInt("positiveBgRes", res);
            return this;
        }

    }

    public void setCancelWithHomeKey(boolean mCancelWithHomeKey) {
        this.mCancelWithHomeKey = mCancelWithHomeKey;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(mNegativeButton)) {
                if (mNegativeListener != null) {
                    mNegativeListener.onClick(view);
                }
            } else if (view.equals(mPositiveButton)) {
                if (mpositiveLinstner != null) {
                    mpositiveLinstner.onClick(view);
                }
        }
        this.dismissAllowingStateLoss();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_HOME) {
            if (mCancelWithHomeKey) {
                dismiss();
            }
            if (getActivity() != null) {
                getActivity().onKeyDown(keyCode, event);
            }

            return false;
        }
        return false;
    }
}
