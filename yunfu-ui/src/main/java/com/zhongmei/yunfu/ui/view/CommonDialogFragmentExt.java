package com.zhongmei.yunfu.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.ui.R;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(resName = "commonmodule_dialog_layout_ext")
public class CommonDialogFragmentExt extends BasicDialogFragment implements
        OnClickListener, OnKeyListener {

    @FragmentArg("headTitle")
    protected CharSequence headTitle;

    @FragmentArg("title")
    protected CharSequence title;

    @FragmentArg("message")
    protected CharSequence message;

    @FragmentArg("message_detail")
    protected CharSequence message_detail;

    @FragmentArg("negative")
    protected String negative;

    @FragmentArg("positive")
    protected String positive;

    @FragmentArg("iconType")
    protected int iconType = ICON_ERROR;

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

    @ViewById(resName = "common_dialog_message_detail_layout")
    public ViewGroup common_dialog_message_detail_layout;

    @ViewById(resName = "common_dialog_message_detail_content")
    public TextView common_dialog_message_detail_content;

    private boolean mCancelWithHomeKey = true;

    public OnClickListener mNegativeListener;

    public OnClickListener mpositiveLinstner;
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    private OnClickListener detailTitlelistener;

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
        setMessage(message);
        setMessageDetail(message_detail);

        getDialog().setOnKeyListener(this);
        mIconView.setBackgroundResource(iconType);
        setNegativeButton();
        setPositiveButton();

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

        TextView messageDetailTitle = findViewById(R.id.common_dialog_message_detail);
        if (detailTitlelistener != null) {
            messageDetailTitle.setText(message_detail);
            messageDetailTitle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailTitlelistener.onClick(v);
                }
            });
        } else {
            messageDetailTitle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    common_dialog_message_detail_content.setVisibility(common_dialog_message_detail_content.isShown() ? View.GONE : View.VISIBLE);
                }
            });
        }
    }

    protected void setNegativeListener(OnClickListener listener) {
        mNegativeListener = listener;
    }

    protected void setpositiveListener(OnClickListener listener) {
        mpositiveLinstner = listener;
    }


    private void setDetailTitleListener(OnClickListener listener) {
        detailTitlelistener = listener;
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setMessage(CharSequence message) {
        mMessage.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(message)) {
            mMessage.setVisibility(View.VISIBLE);
            mMessage.setText(message);
        }
    }

    public void setMessageDetail(CharSequence message) {
        common_dialog_message_detail_layout.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(message_detail)) {
            common_dialog_message_detail_layout.setVisibility(View.VISIBLE);
            common_dialog_message_detail_content.setVisibility(View.GONE);
            common_dialog_message_detail_content.setText(message);
        }
    }

    private void setPositiveButton() {
        mPositiveButton.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(positive)) {
            mPositiveButton.setText(positive);
            mPositiveButton.setOnClickListener(this);
            mPositiveButton.setVisibility(View.VISIBLE);
        }
    }

    private void setNegativeButton() {
        mNegativeButton.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(negative)) {
            mNegativeButton.setText(negative);
            mNegativeButton.setOnClickListener(this);
            mNegativeButton.setVisibility(View.VISIBLE);
        }
    }

    public static class CommonDialogFragmentExtBuilder {
        private Context context;

        protected Bundle mBundle;

        protected OnClickListener mNegativeListener;

        protected OnClickListener mpositiveLinstner;

        protected OnClickListener detailTitleListener;

        public CommonDialogFragmentExtBuilder(Context context) {
            this.context = context;
            mBundle = new Bundle();
        }

        public CommonDialogFragmentExt build() {
            CommonDialogFragmentExt fragment = new CommonDialogFragmentExt_();
            fragment.setArguments(mBundle);
            if (mNegativeListener != null) {
                fragment.setNegativeListener(mNegativeListener);
            }
            if (mpositiveLinstner != null) {
                fragment.setpositiveListener(mpositiveLinstner);
            }
            fragment.setDetailTitleListener(detailTitleListener);
            return fragment;
        }

        public CommonDialogFragmentExtBuilder negativeLisnter(
                OnClickListener listener) {
            mNegativeListener = listener;
            return this;
        }

        public CommonDialogFragmentExtBuilder positiveListener(
                OnClickListener listener) {
            mpositiveLinstner = listener;
            return this;
        }

        public CommonDialogFragmentExtBuilder title(int title) {
            return title(context.getResources()
                    .getString(title));
        }

        public CommonDialogFragmentExtBuilder title(String title) {
            mBundle.putString("title", title);
            return this;
        }

        public CommonDialogFragmentExtBuilder title(CharSequence title) {
            mBundle.putCharSequence("title", title);
            return this;
        }

        public CommonDialogFragmentExtBuilder headTitle(int headTitle) {
            return headTitle(context.getResources()
                    .getString(headTitle));
        }

        public CommonDialogFragmentExtBuilder headTitle(String headTitle) {
            mBundle.putString("headTitle", headTitle);
            return this;
        }

        public CommonDialogFragmentExtBuilder headTitle(CharSequence headTitle) {
            mBundle.putCharSequence("headTitle", headTitle);
            return this;
        }

        public CommonDialogFragmentExtBuilder message(int title) {
            return message(context.getResources()
                    .getString(title));
        }

        public CommonDialogFragmentExtBuilder message(CharSequence message) {
            mBundle.putCharSequence("message", message);
            return this;
        }

        public CommonDialogFragmentExtBuilder messageDetail(int title) {
            return messageDetail(context.getResources()
                    .getString(title));
        }

        public CommonDialogFragmentExtBuilder messageDetail(CharSequence detail) {
            messageDetail(detail, null);
            return this;
        }

        public CommonDialogFragmentExtBuilder messageDetail(CharSequence detail, OnClickListener listener) {
            mBundle.putCharSequence("message_detail", detail);
            this.detailTitleListener = listener;
            return this;
        }

        public CommonDialogFragmentExtBuilder negativeText(String negative) {
            mBundle.putString("negative", negative);
            return this;
        }

        public CommonDialogFragmentExtBuilder positiveText(String positive) {
            mBundle.putString("positive", positive);
            return this;
        }

        public CommonDialogFragmentExtBuilder negativeText(int negative) {
            return negativeText(context.getResources()
                    .getString(negative));
        }

        public CommonDialogFragmentExtBuilder positiveText(int positive) {
            return positiveText(context.getResources()
                    .getString(positive));
        }

        public CommonDialogFragmentExtBuilder iconType(int iconType) {
            mBundle.putInt("iconType", iconType);
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
