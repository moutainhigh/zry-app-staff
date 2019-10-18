package com.zhongmei.bty.dinner.cash;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.google.zxing.WriterException;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.erp.operates.ErpCommercialRelationDal;
import com.zhongmei.yunfu.context.util.EncodingHandler;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import java.util.Map;


public class DinnerCustomerLoginBasicDialogFragment extends BasicDialogFragment {

    public static final String TAG = DinnerCustomerLoginBasicDialogFragment.class.getSimpleName();

    public boolean[] settings;

    public String requestUuid;

    private int barcodeWH = 180;
        Bitmap mBitmap = null;

    public static final int UI_TYPE_LOGIN = 0;

    public static final int UI_TYPE_FACE_ERROR = 2;

    public static final int UI_TYPE_REGISTER = 1;

    public int uiType = UI_TYPE_LOGIN;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_LAYOUT_FLAGS | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        Window window = dialog.getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        return dialog;
    }


    public interface CallbackListener {
        void setType(int uiType);
    }
}
