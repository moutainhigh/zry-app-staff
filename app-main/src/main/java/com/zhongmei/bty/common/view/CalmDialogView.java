package com.zhongmei.bty.common.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

public class CalmDialogView extends Dialog {

    private Context mContext;

    private Button posBtn, neuBtn, negBtn;
    private OnClickListener posListener, neuListener, negListener;

    private TextView message;

    private String mMessage;
    private Spanned mSpannedMessage;
    private boolean mEnableTitleClose;
    private ProgressBar progressBar;

    private ScrollView scrollView;

    private ScrollView scrollViewForCustom;
    private LinearLayout linearLayout;

    private TextView titleView;
    private String mTitle;
    private CharSequence mSpannedTitle;

    public CalmDialogView(Context context) {
        this(context, R.style.dialog);
    }

    public CalmDialogView(Context context, int theme) {
        super(context, 0);
        mContext = context;
        mEnableTitleClose = true;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.calm_dlg_layout);
        Button close = (Button) findViewById(R.id.close);
        message = (TextView) findViewById(R.id.message);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        titleView = (TextView) findViewById(R.id.title);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollViewForCustom = (ScrollView) findViewById(R.id.scrollViewForCustom);

        linearLayout = (LinearLayout) findViewById(R.id.calm_dlg_relative_layout);
        posBtn = (Button) findViewById(R.id.pos_btn);
        neuBtn = (Button) findViewById(R.id.neu_btn);
        negBtn = (Button) findViewById(R.id.neg_btn);

        close.setOnClickListener(mDlgClickListener);
        posBtn.setOnClickListener(mDlgClickListener);
        neuBtn.setOnClickListener(mDlgClickListener);
        negBtn.setOnClickListener(mDlgClickListener);

        posBtn.setVisibility(View.GONE);
        neuBtn.setVisibility(View.GONE);
        negBtn.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (TextUtils.isEmpty(mMessage)) {
            if (mSpannedMessage == null) {
                message.setText("");
            } else {
                message.setText(mSpannedMessage);
            }
        } else {
            message.setText(mMessage);
        }
    }

    public void setMessage(String msg) {
        mSpannedMessage = null;
        mMessage = msg;
        if (message != null) {
            message.setText(mMessage);
        }
    }

    public void setMessage(int msgResId) {
        mSpannedMessage = null;
        mMessage = mContext.getString(msgResId);
        if (message != null) {
            message.setText(msgResId);
        }
    }

    public void setMessage(Spanned fromHtml) {
        mMessage = null;
        mSpannedMessage = fromHtml;
        if (message != null) {
            message.setText(mSpannedMessage);
        }
    }

    public void setEnableTitleClose(boolean enable) {
        mEnableTitleClose = enable;
    }

    private View.OnClickListener mDlgClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            switch (arg0.getId()) {
                case R.id.close:
                    if (mEnableTitleClose) {
                        CalmDialogView.this.dismiss();
                        if (negListener != null) {
                            negListener.onClick(CalmDialogView.this, R.id.neg_btn);
                        }
                    }
                    break;
                case R.id.pos_btn:
                    if (posListener != null) {
                        posListener.onClick(CalmDialogView.this, R.id.pos_btn);
                    }
                    break;
                case R.id.neu_btn:
                    if (neuListener != null) {
                        neuListener.onClick(CalmDialogView.this, R.id.neu_btn);
                    }
                    break;
                case R.id.neg_btn:
                    if (negListener != null) {
                        negListener.onClick(CalmDialogView.this, R.id.neg_btn);
                    }
                    break;
            }
        }

    };

    public void setNegativeButton(int resId, OnClickListener onClickListener) {
        negBtn.setText(resId);
        negListener = onClickListener;
        negBtn.setVisibility(View.VISIBLE);
    }

    public void setNegativeButton(String text, OnClickListener onClickListener) {
        negBtn.setText(text);
        negListener = onClickListener;
        negBtn.setVisibility(View.VISIBLE);
    }

    public void setPositiveButton(int resId, OnClickListener onClickListener) {
        posBtn.setText(resId);
        posListener = onClickListener;
        posBtn.setVisibility(View.VISIBLE);
    }

    public void setPositiveButton(String text, OnClickListener onClickListener) {
        posBtn.setText(text);
        posListener = onClickListener;
        posBtn.setVisibility(View.VISIBLE);
    }

    public void setNeutralButton(int resId, OnClickListener onClickListener) {
        neuBtn.setText(resId);
        neuListener = onClickListener;
        neuBtn.setVisibility(View.VISIBLE);
    }

    public Button getNegativeButton() {
        return negBtn;
    }

    public Button getPositiveButton() {
        return posBtn;
    }

    public Button getNeutralButton() {
        return neuBtn;
    }

    public void showProgressbar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void dismissProgressbar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    public ScrollView getScrollView() {
        return scrollView;
    }

    public LinearLayout getDialogLinearLayout() {
        return linearLayout;
    }

    public ScrollView getScrollViewForCustom() {
        return scrollViewForCustom;
    }

    @Override
    public void setTitle(CharSequence title) {
        // TODO Auto-generated method stub
        // super.setTitle(title);
        mTitle = null;
        mSpannedTitle = title;
        if (titleView != null) {
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(mSpannedTitle);
        }
    }

    // public void setTitle(Spanned fromHtml){
    // mTitle= null;
    // mSpannedTitle = fromHtml;
    // if (titleView!= null) {
    // titleView.setText(mSpannedTitle);
    // }
    // }

}
