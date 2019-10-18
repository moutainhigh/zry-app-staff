package com.zhongmei.bty.basemodule.commonbusiness.view;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.devices.scaner.DeWoScanCode;
import com.zhongmei.bty.basemodule.pay.event.RegisterDeWoCouponScanEvent;
import com.zhongmei.yunfu.resp.UserActionEvent;

import java.lang.reflect.Method;

import de.greenrobot.event.EventBus;


public class CommonCheckCouponsView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = CommonCheckCouponsView.class.getSimpleName();

    private Context mContext;

        private ImageButton mScanBtn;

    private EditText mValue;

        private Button mCheckBtn;

        private boolean isShowSystemKeryBoard = false;

    private CouponsListener mListener = null;

    private ScanPopupWindow scanPopupWindow;

    public CommonCheckCouponsView(Context context) {
        super(context);
        EventBus.getDefault().register(this);
        this.mContext = context;
    }

    public CommonCheckCouponsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        EventBus.getDefault().register(this);
        init(context);
    }

    public CommonCheckCouponsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        EventBus.getDefault().register(this);

    }

    private void resisterDeWoScanCode() {
        DeWoScanCode.getInstance().registerReceiveDataListener(new DeWoScanCode.OnReceiveDataListener() {
            @Override
            public void onReceiveData(String data) {
                mValue.setText(data);
            }
        });
    }


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.comm_check_coupons_layout, this);
        mScanBtn = (ImageButton) findViewById(R.id.scan_btn);
        mScanBtn.setOnClickListener(this);
        mValue = (EditText) findViewById(R.id.value);
        mValue.addTextChangedListener(textWatcher);
        mCheckBtn = (Button) findViewById(R.id.check_btn);
        mCheckBtn.setOnClickListener(this);
        resisterDeWoScanCode();
    }

    public void onEventMainThread(RegisterDeWoCouponScanEvent event) {
        resisterDeWoScanCode();
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (scanPopupWindow != null && scanPopupWindow.isShowing()) {
                scanPopupWindow.dismiss();
            }
        }
    };

    public EditText getEdit() {
        return mValue;
    }


    public void cleanEditText() {
        mValue.setText("");
    }


    public void setKeyBoardValue(boolean flag) {
        this.isShowSystemKeryBoard = flag;
        forbiddenSoftKeyboard();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.scan_btn) {
            mValue.requestFocus();
            startScan();
        } else if (v.getId() == R.id.check_btn) {
            UserActionEvent.start(UserActionEvent.DINNER_PAY_WECHAT_CARD_COUPON);
            if (mListener != null) {
                mListener.getCouponsNo(mValue.getText().toString().trim());
            }
        }
    }



    private void forbiddenSoftKeyboard() {

        try {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            setShowSoftInputOnFocus.setAccessible(true);

            if (isShowSystemKeryBoard) {
                ((Activity) this.mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
                setShowSoftInputOnFocus.invoke(mValue, true);
            } else {
                ((Activity) this.mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                setShowSoftInputOnFocus.invoke(mValue, false);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }


    public void startScan() {
        scanPopupWindow = new ScanPopupWindow(mContext);
        scanPopupWindow.showAtLocation(mScanBtn, Gravity.NO_GRAVITY, 0, 0);
        scanPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

        scanPopupWindow.setOnScanBarcodeCallback(new ScanPopupWindow.ScanBarcodeCallback() {
            @Override
            public void onScanBarcode(String data) {
                if (!TextUtils.isEmpty(data)) {
                    mValue.setText(data);
                    if (scanPopupWindow != null) {
                        scanPopupWindow.dismiss();
                    }
                    if (mListener != null) {                        mListener.getCouponsNo(mValue.getText().toString().trim());
                    }
                }
            }
        });
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    public interface CouponsListener {


        public void getCouponsNo(String ticketNo);
    }


    public void setCouponsListener(CouponsListener vListener) {
        this.mListener = vListener;
    }


}
