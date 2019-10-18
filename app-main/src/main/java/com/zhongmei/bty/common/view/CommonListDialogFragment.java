package com.zhongmei.bty.common.view;


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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.common_list_dialog_layout)
public class CommonListDialogFragment extends BasicDialogFragment implements OnClickListener, OnKeyListener {


    @FragmentArg("title")
    protected String title;

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

    @ViewById(R.id.common_dialog_title)
    protected TextView mTitle;

    @ViewById(R.id.negative_button)
    protected Button mNegativeButton;

    @ViewById(R.id.positive_button)
    protected Button mPositiveButton;

    @ViewById(R.id.icon)
    protected ImageView mIconView;


    @ViewById(R.id.listview)
    protected ListView mlistView;


    private boolean mCancelWithHomeKey = true;

    OnClickListener mNegativeListener;

    OnClickListener mpositiveLinstner;

    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    public void show(FragmentManager manager, String tag) {
        if (manager != null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    @AfterViews
    protected void initView() {
        getDialog().getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(false);
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
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
            mPositiveButton.setBackgroundResource(R.drawable.commonmodule_dialog_positive_only);
        } else if (mPositiveButton.getVisibility() == View.GONE) {
            mNegativeButton.setBackgroundResource(R.drawable.commonmodule_dialog_positive_only);
        } else if (mPositiveButton.getVisibility() == View.GONE && mNegativeButton.getVisibility() == View.GONE) {
            throw new IllegalArgumentException("no botton dialog??please check your builder!!!");
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





    public static class CommonListDialogFragmentBuilder {

        private Bundle mBundle;

        OnClickListener mNegativeListener;

        OnClickListener mpositiveLinstner;


        public CommonListDialogFragmentBuilder() {
            mBundle = new Bundle();
        }

        public CommonListDialogFragment build() {
            CommonListDialogFragment fragment = new CommonListDialogFragment_();
                        fragment.setArguments(mBundle);
            if (mNegativeListener != null) {
                fragment.setNegativeListener(mNegativeListener);
            }
            if (mpositiveLinstner != null) {
                fragment.setpositiveListener(mpositiveLinstner);
            }
            return fragment;
        }

        public CommonListDialogFragmentBuilder negativeLisnter(OnClickListener listener) {
            mNegativeListener = listener;
            return this;
        }

        public CommonListDialogFragmentBuilder positiveLinstner(OnClickListener listener) {
            mpositiveLinstner = listener;
            return this;
        }

        public CommonListDialogFragmentBuilder title(String title) {
            mBundle.putString("title", title);
            return this;
        }

        public CommonListDialogFragmentBuilder negativeText(String negative) {
            mBundle.putString("negative", negative);
            return this;
        }

        public CommonListDialogFragmentBuilder positiveText(String positive) {
            mBundle.putString("positive", positive);
            return this;
        }

        public CommonListDialogFragmentBuilder title(int title) {
            return title(MainApplication.getInstance().getResources().getString(title));
        }

        public CommonListDialogFragmentBuilder negativeText(int negative) {
            return negativeText(MainApplication.getInstance().getResources().getString(negative));
        }

        public CommonListDialogFragmentBuilder positiveText(int positive) {
            return positiveText(MainApplication.getInstance().getResources().getString(positive));
        }

        public CommonListDialogFragmentBuilder iconType(int iconType) {
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
            getActivity().onKeyDown(keyCode, event);
            return false;
        }
        return false;
    }


}
