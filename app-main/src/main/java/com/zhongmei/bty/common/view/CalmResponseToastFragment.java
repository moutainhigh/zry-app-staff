package com.zhongmei.bty.common.view;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Date： 16/5/23
 * @Description:
 * @Version: 1.0
 */
public class CalmResponseToastFragment extends BasicDialogFragment {


    public static final int SUCCESS = 0;
    public static final int FAIL = 1;

    private ImageView mBtnClose;
    private TextView mTvTitle;
    private ImageView mIvStatus;
    private TextView mTvAlertTitle;
    private LinearLayout mLlDetail;
    private TextView mTvDetailMsg;

    private View mView;

    private int mType;
    private String mMsg;
    private String mDetail;

    private Timer timer;

    private int EXCUTETIME = 2000;

    TimerTask task = new TimerTask() {
        public void run() {
            dismiss();
        }
    };

    private void assignViews() {
        mBtnClose = (ImageView) mView.findViewById(R.id.btn_close);
        mTvTitle = (TextView) mView.findViewById(R.id.tv_title);
        mIvStatus = (ImageView) mView.findViewById(R.id.iv_status);
        mTvAlertTitle = (TextView) mView.findViewById(R.id.tv_alert_title);
        mLlDetail = (LinearLayout) mView.findViewById(R.id.ll_detail);
        mTvDetailMsg = (TextView) mView.findViewById(R.id.tv_detail_msg);
    }

    public static CalmResponseToastFragment newInstance(int type, String msg, String detail) {
        CalmResponseToastFragment f = new CalmResponseToastFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("msg", msg);
        bundle.putString("detail", detail);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mType = bundle.getInt("type", SUCCESS);
        mMsg = bundle.getString("msg");
        mDetail = bundle.getString("detail");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mView = inflater.inflate(R.layout.fragment_response_toast_layout, container);
        assignViews();

        if (mType == SUCCESS)
            mIvStatus.setImageResource(R.drawable.pay_ok_icon);
        else
            mIvStatus.setImageResource(R.drawable.pay_error_icon);
        mTvAlertTitle.setText(mMsg);
        if (!TextUtils.isEmpty(mDetail))
            mTvDetailMsg.setText(mDetail);

        timer = new Timer(true);
        timer.schedule(task, EXCUTETIME);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDialogWidthAndHeight(view);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setDialogWidthAndHeight(View view) {
        Window window = getDialog().getWindow();
        view.measure(0, 0);
        Resources resources = getActivity().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int desiredWidth = metrics.widthPixels;
        int desiredHeight = metrics.heightPixels;
        window.setLayout(desiredWidth, desiredHeight);
        window.getAttributes().y = 0;
    }
}
