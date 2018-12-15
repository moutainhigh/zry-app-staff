package com.zhongmei.yunfu.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.ui.R;

public class CalmToastView extends Toast {

    private Context mContext;
    private String mMessageStr;
    private int mleftPadding, mTopPadding, mRightPadding, mBottomPadding;

    public CalmToastView(Context context) {
        super(context);
        mContext = context;
        initDefaultPadding(context);
    }

    private void initDefaultPadding(Context context) {
        mleftPadding = DensityUtil.dip2px(context, 7);
        mTopPadding = DensityUtil.dip2px(context, 5);
        mRightPadding = DensityUtil.dip2px(context, 7);
        mBottomPadding = DensityUtil.dip2px(context, 5);
    }

    public void setTextPadding(int left, int top, int right, int bottom) {
        this.mleftPadding = left;
        this.mTopPadding = top;
        this.mRightPadding = right;
        this.mBottomPadding = bottom;

    }

    @Override
    public void show() {
        setView(getView());
        super.show();
    }

    @Override
    public View getView() {
        LayoutInflater m = LayoutInflater.from(mContext);
        View view = m.inflate(R.layout.commonmodule_calm_toast_layout, null);

        TextView messageTV = (TextView) view.findViewById(R.id.toast_message);
        if (TextUtils.isEmpty(mMessageStr)) {
            messageTV.setText("");
        } else {
            messageTV.setText(mMessageStr);
        }
        setGravity(Gravity.CENTER, 0, 0);
        messageTV.setPadding(mleftPadding, mTopPadding, mRightPadding, mBottomPadding);
        return view;
    }

    public void setMessage(String msg) {
        mMessageStr = msg;
    }

    public void setMessage(int msgRes) {
        mMessageStr = mContext.getString(msgRes);
    }

}
