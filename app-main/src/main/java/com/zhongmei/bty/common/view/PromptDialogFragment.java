package com.zhongmei.bty.common.view;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.R;

@EFragment(R.layout.prompt_dailog)
public class PromptDialogFragment extends BasicDialogFragment {

    private static final String DRAWABLEID = "drawableId";

    private static final String MESSAGE = "message";

    @FragmentArg(MESSAGE)
    protected String mMessage;

    @FragmentArg(DRAWABLEID)
    protected int mIconId = 0;

    @ViewById(R.id.message)
    protected TextView mTextView;

    @ViewById(R.id.icon)
    protected ImageView mIcon;

    OnClickListener mListener;

    @Click({R.id.close, R.id.ok})
    public void onClick(View v) {
        this.dismissAllowingStateLoss();
        switch (v.getId()) {
            case R.id.ok:
                if (mListener != null) {
                    mListener.onClick(v);
                } else {
                    getActivity().finish();
                }
                break;
            default:
                break;
        }

    }

    public void setListener(OnClickListener listener) {
        this.mListener = listener;
    }

    @AfterViews
    public void initView() {
        if (mIconId != 0) {
            mIcon.setVisibility(View.VISIBLE);
            mIcon.setImageResource(mIconId);
        }
        mTextView.setText(mMessage);
    }

    @SuppressLint("WrongConstant")
    public static void show(FragmentManager fm, String message, int drawableId, OnClickListener listener) {
        if (fm == null || TextUtils.isEmpty(message)) {
            return;
        }
        PromptDialogFragment dialogFragment =
                PromptDialogFragment_.builder().mIconId(drawableId).mMessage(message).build();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dialogFragment.setListener(listener);
        dialogFragment.show(fm, PromptDialogFragment.class.getSimpleName());
    }
}
