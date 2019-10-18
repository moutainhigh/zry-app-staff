package com.zhongmei.bty.settings.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;


@EFragment(R.layout.settings_scan)
public class ScannerFragment extends BasicFragment {

    @ViewById(R.id.scan_barcode)
    ImageView mScanBarcode;

    @ViewById(R.id.scan_test)
    TextView mScanTest;

    @ViewById(R.id.barcode_id)
    EditText mBarcodeId;

    private boolean finished = false;

    @AfterViews
    void init() {
        forbiddenSoftKeyboard(mBarcodeId);
        mBarcodeId.requestFocus();
        mBarcodeId.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    mBarcodeId.setInputType(InputType.TYPE_NULL);
                    Log.e("et text", mBarcodeId.getText().toString());
                    String text = mBarcodeId.getText().toString();
                    mBarcodeId.setText("");
                    if (verifyCode(text)) {
                        goOk();
                    }
                }
                return false;
            }
        });
    }

    private boolean verifyCode(String text) {
        return TextUtils.equals(text, getActivity().getResources().getString(R.string.scan_test_value));
    }

    private void goOk() {
        mScanBarcode.setImageResource(R.drawable.settings_scan_success);
        mScanBarcode.setPadding(DensityUtil.dip2px(MainApplication.getInstance(), 50), DensityUtil.dip2px(MainApplication.getInstance(), 40), DensityUtil.dip2px(MainApplication.getInstance(), 50), 0);
        mScanTest.setTextColor(getActivity().getResources().getColor(R.color.settings_grayword));
        returnScan();
    }

    @UiThread(delay = 5000)
    protected void returnScan() {
        if (!finished) {
            mScanBarcode.setImageResource(R.drawable.settings_scan_test);
            mScanBarcode.setPadding(DensityUtil.dip2px(MainApplication.getInstance(), 50), DensityUtil.dip2px(MainApplication.getInstance(), 80), DensityUtil.dip2px(MainApplication.getInstance(), 50), 0);
            mScanTest.setTextColor(getActivity().getResources().getColor(R.color.print_orange));
        }
    }

    @Override
    public void onDestroyView() {
        finished = true;
        super.onDestroyView();
    }

}
