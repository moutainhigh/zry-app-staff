package com.zhongmei.bty.common.view;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import android.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.R;

@EFragment(R.layout.calm_hint_dialog_layout)
public class CalmHintDialogFragment extends BasicDialogFragment implements
        OnClickListener {
    public static final String MESSAGE = "message";
    public static final String TAG = "CalmHintDialogFragment";
    public static final String HINT_DETAIL = "hint_detail";
    public static final String BUTTON_TEXT = "button_text";
    public static final String CLICKABLE = "clickable";
    @FragmentArg(MESSAGE)
    protected String mTitleString;
    @FragmentArg(HINT_DETAIL)
    protected String mHintDetail;
    @FragmentArg(BUTTON_TEXT)
    protected String mButtonText;
    @ViewById(R.id.calm_hint_title)
    protected TextView mTitle;
    @ViewById(R.id.calm_hint_cancel_btn)
    protected Button mCancelButton;
    @ViewById(R.id.calm_hint_progressbar)
    protected ProgressBar mProgressBar;
    @ViewById(R.id.buttom_linearlayout)
    protected LinearLayout mLinearLayout;
    @ViewById(R.id.calm_hint_text)
    protected TextView mHintText;
    @FragmentArg(CLICKABLE)
    protected boolean mCancelable = true;
    OnClickListener mCancelListener;

    public void setCancelListener(OnClickListener cancelListener) {
        this.mCancelListener = cancelListener;
    }

    @AfterViews
    void initView() {
        if (!TextUtils.isEmpty(mHintDetail)) {
            mHintText.setVisibility(View.VISIBLE);
            mHintText.setText(mHintDetail);
        }
        if (!mCancelable) {
            mCancelButton.setVisibility(View.GONE);
        }
        mCancelButton.setOnClickListener(this);
        if (!TextUtils.isEmpty(mButtonText)) {
            mCancelButton.setText(mButtonText);
        }
        if (!TextUtils.isEmpty(mTitleString)) {
            mTitle.setText(mTitleString);
        } else {
            mTitle.setVisibility(View.GONE);
        }
        this.setCancelable(false);
    }

    public static void show(FragmentManager fm, String message,
                            OnClickListener cancelListener) {
        if (fm == null)
            return;
        CalmHintDialogFragment dialogFragment = CalmHintDialogFragment_
                .builder().mTitleString(message).build();
        dialogFragment.show(fm, cancelListener);
    }

    public static void show(FragmentManager fm, String message,
                            String buttonText, boolean cancelable,
                            OnClickListener cancelListener) {
        if (fm == null)
            return;
        CalmHintDialogFragment dialogFragment = CalmHintDialogFragment_
                .builder().mTitleString(message).mButtonText(buttonText)
                .mCancelable(cancelable).build();
        dialogFragment.show(fm, cancelListener);
    }

    public void show(FragmentManager fm, OnClickListener cancelListener) {
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        setCancelListener(cancelListener);
        show(fm, TAG);
    }

    public void canbeCancel() {
        mCancelButton.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mHintText.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        this.dismissAllowingStateLoss();
        if (mCancelListener != null) {
            mCancelListener.onClick(v);
        }
    }

    public void setTitle(String title) {
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(title);
    }

    public void setButton(String button) {
        mCancelButton.setText(button);
    }

    public void setLinearLayoutBackground(int resid) {
        mLinearLayout.setBackgroundResource(resid);
    }
}
