
package com.zhongmei.bty.basemodule.commonbusiness.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.devices.scaner.ScanCode;
import com.zhongmei.bty.basemodule.devices.scaner.ScanCodeManager;


public class ScanPopupWindow extends PopupWindow {

    private Context mContext;
    private TextView mDescription;
    private EditText etBarcode;
    private ScanBarcodeCallback scanBarcodeCallback;
    private ScanCodeManager scanCodeManager;

    public ScanPopupWindow(Context context) {
        this((Activity) context, context.getString(R.string.sacn_coupons_desc));
    }

    public ScanPopupWindow(Activity context, String desc) {
        mContext = context;
        initView();
        mDescription.setText(desc);
        setFocusable(true);
        scanCodeManager = new ScanCodeManager(context, etBarcode, false);
        scanCodeManager.start(new ScanCode.ScanCodeReceivedListener() {
            @Override
            public void onScanCodeReceived(String data) {
                scanGoods(data);
            }
        });
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contentView = inflater.inflate(R.layout.scan_popup_layout, null);
        Button close = (Button) contentView.findViewById(R.id.btn_close);
        mDescription = (TextView) contentView.findViewById(R.id.tv_description);
        etBarcode = (EditText) contentView.findViewById(R.id.et_barcode);
        setContentView(contentView);
        setBackgroundDrawable(new ColorDrawable(0));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                                close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (scanCodeManager != null) {
            scanCodeManager.stop();
        }
    }

    public void setOnScanBarcodeCallback(ScanBarcodeCallback callback) {
        this.scanBarcodeCallback = callback;
    }


    private void scanGoods(String barcode) {
        if (!TextUtils.isEmpty(barcode)) {
            dismiss();
            if (scanBarcodeCallback != null) {
                scanBarcodeCallback.onScanBarcode(barcode);
            }
        }
    }

    public interface ScanBarcodeCallback {
        void onScanBarcode(String barcode);
    }
}
