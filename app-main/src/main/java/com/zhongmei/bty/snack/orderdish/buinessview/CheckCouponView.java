package com.zhongmei.bty.snack.orderdish.buinessview;

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
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.view.ScanPopupWindow;
import com.zhongmei.bty.basemodule.devices.scaner.DeWoScanCode;

import java.lang.reflect.Method;


public class CheckCouponView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = CheckCouponView.class.getSimpleName();

    private Context mContext;

        private LinearLayout mScanBtn;

    private EditText mValue;

        private Button mCheckBtn;

        private boolean isShowSystemKeryBoard = true;

    private CouponsListener mListener = null;

    private ScanPopupWindow scanPopupWindow;

    public CheckCouponView(Context context) {
        super(context);
        this.mContext = context;
    }

    public CheckCouponView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context);
    }

    public CheckCouponView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.comm_check_coupons_vertical_layout, this);
        mScanBtn = (LinearLayout) findViewById(R.id.scan_btn);
        mScanBtn.setOnClickListener(this);
        mValue = (EditText) findViewById(R.id.value);
        mValue.addTextChangedListener(textWatcher);
        mCheckBtn = (Button) findViewById(R.id.check_btn);
        mCheckBtn.setOnClickListener(this);
        setKeyBoardValue(isShowSystemKeryBoard);
        mCheckBtn.setEnabled(false);

        DeWoScanCode.getInstance().registerReceiveDataListener(new DeWoScanCode.OnReceiveDataListener() {
            @Override
            public void onReceiveData(String data) {
                mValue.setText(data);
            }
        });
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
            if (TextUtils.isEmpty(s.toString())) {
                mCheckBtn.setEnabled(false);
            } else {
                mCheckBtn.setEnabled(true);
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
        switch (v.getId()) {
            case R.id.scan_btn:
                mValue.requestFocus();
                startScan();
                break;
            case R.id.check_btn:
                if (mListener != null) {
                    mListener.getCouponsNo(mValue.getText().toString().trim());
                }
                break;
            default:
                Log.d(TAG, "No item clicked");
                break;
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


    private void startScan() {
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
                mValue.setText(data);
            }
        });
    }


    public interface CouponsListener {


        public void getCouponsNo(String ticketNo);
    }


    public void setCouponsListener(CouponsListener vListener) {
        this.mListener = vListener;
    }
}

