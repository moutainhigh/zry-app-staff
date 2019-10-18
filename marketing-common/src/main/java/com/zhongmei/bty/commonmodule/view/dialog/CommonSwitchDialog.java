package com.zhongmei.bty.commonmodule.view.dialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.commonmodule.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;


@EFragment(resName = "common_switch_diglog_layout")
public class CommonSwitchDialog extends BasicDialogFragment implements
        View.OnClickListener, DialogInterface.OnKeyListener {
    @FragmentArg("title")
    protected CharSequence title;

    @FragmentArg("message")
    protected CharSequence message;

    @FragmentArg("negative")
    protected String negative;

    @FragmentArg("positive")
    protected String positive;

    @FragmentArg("iconType")
    protected int iconType = R.drawable.commonmodule_dialog_icon_hint;

    @FragmentArg("witchText")
    protected String witchText;

    @FragmentArg("categoryText")
    protected String categoryText;


    @ViewById(resName = "common_dialog_title")
    protected TextView mTitle;

    @ViewById(resName = "common_dialog_message")
    protected TextView mMessage;

    @ViewById(resName = "negative_button")
    protected Button mNegativeButton;

    @ViewById(resName = "positive_button")
    protected Button mPositiveButton;

    @ViewById(resName = "icon")
    protected ImageView mIconView;

    @ViewById(resName = "print_checkedType_cb")
    protected CheckBox mSwitchCB;

    @ViewById(resName = "print_checkedCategory_cb")
    protected CheckBox mCbCategory;

    private boolean mCancelWithHomeKey = true;

    protected View.OnClickListener mNegativeListener;

    protected View.OnClickListener mpositiveLinstner;
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
        if (!TextUtils.isEmpty(witchText)) {
            mSwitchCB.setText(witchText);
        }
        if (!TextUtils.isEmpty(categoryText)) {
            mCbCategory.setVisibility(View.VISIBLE);
            mCbCategory.setText(categoryText);
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

    public void setNegativeListener(View.OnClickListener listener) {
        mNegativeListener = listener;
    }

    public void setpositiveListener(View.OnClickListener listener) {
        mpositiveLinstner = listener;
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setMessage(CharSequence message) {
        mMessage.setVisibility(View.VISIBLE);
        mMessage.setText(message);
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

    public boolean isChecked() {
        if (mSwitchCB == null)
            return false;
        return this.mSwitchCB.isChecked();
    }

    public boolean isCategoryChecked() {
        if (mCbCategory == null) {
            return false;
        }
        return mCbCategory.isChecked();
    }


    public static class CommonSwitchDialogBuilder {

        protected Bundle mBundle;

        protected View.OnClickListener mNegativeListener;

        protected View.OnClickListener mpositiveLinstner;

        public CommonSwitchDialogBuilder() {
            mBundle = new Bundle();
        }

        public CommonSwitchDialog build() {
            CommonSwitchDialog fragment = new CommonSwitchDialog_();
            fragment.setArguments(mBundle);
            if (mNegativeListener != null) {
                fragment.setNegativeListener(mNegativeListener);
            }
            if (mpositiveLinstner != null) {
                fragment.setpositiveListener(mpositiveLinstner);
            }
            return fragment;
        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder negativeLisnter(
                View.OnClickListener listener) {
            mNegativeListener = listener;
            return this;
        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder positiveLinstner(
                View.OnClickListener listener) {
            mpositiveLinstner = listener;
            return this;
        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder title(int title) {
            return title(BaseApplication.sInstance.getResources()
                    .getString(title));
        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder title(String title) {
            mBundle.putString("title", title);
            return this;
        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder title(CharSequence title) {
            mBundle.putCharSequence("title", title);
            return this;
        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder message(int title) {
            return message(BaseApplication.sInstance.getResources()
                    .getString(title));
        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder message(CharSequence message) {
            mBundle.putCharSequence("message", message);
            return this;
        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder negativeText(String negative) {
            mBundle.putString("negative", negative);
            return this;
        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder witchText(String witchText) {
            mBundle.putString("witchText", witchText);
            return this;

        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder witchText(int witchText) {
            return witchText(BaseApplication.sInstance.getResources()
                    .getString(witchText));
        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder witch2Text(String witchText) {
            mBundle.putString("categoryText", witchText);
            return this;

        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder witch2Text(int witchText) {
            return witch2Text(BaseApplication.sInstance.getResources()
                    .getString(witchText));
        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder positiveText(String positive) {
            mBundle.putString("positive", positive);
            return this;
        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder negativeText(int negative) {
            return negativeText(BaseApplication.sInstance.getResources()
                    .getString(negative));
        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder positiveText(int positive) {
            return positiveText(BaseApplication.sInstance.getResources()
                    .getString(positive));
        }

        public CommonSwitchDialog.CommonSwitchDialogBuilder iconType(int iconType) {
            mBundle.putInt("iconType", iconType);
            return this;
        }
    }
}
