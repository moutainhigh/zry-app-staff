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

/**
 * Created by demo on 2018/12/15
 */
public class DinnerCustomerLoginBasicDialogFragment extends BasicDialogFragment {

    public static final String TAG = DinnerCustomerLoginBasicDialogFragment.class.getSimpleName();

    public boolean[] settings;

    public String requestUuid;

    private int barcodeWH = 180;// 微信二维码宽度

    // 生成二维码
    Bitmap mBitmap = null;

    public static final int UI_TYPE_LOGIN = 0;

    public static final int UI_TYPE_FACE_ERROR = 2;

    public static final int UI_TYPE_REGISTER = 1;

    public int uiType = UI_TYPE_LOGIN;

    private DinnerCustomerLoginFragment customerLoginFragment;

    private DinnerCustomerRegisterFragment customerRegisterFragment;

    private Map<String, ErpCurrency> erpCurrencyMap;

    private ErpCommercialRelationDal mErpDal;

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

    /**
     * 控制副二屏的显示
     *
     * @param phone
     */
    public void showSecondDisPlay(String phone) {
        if (settings == null) {
            /*DisplayUserInfo dUserInfo =
                    DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_ACCOUNT_INPUT, "", null, 0, true, 0);
            DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
            return;
        }
        boolean secondDisPlayQrCode = true;
        if (settings[0]) {//开通微信公众号
            if (uiType == UI_TYPE_REGISTER && !settings[1]) {//关注即会员开关关闭
                secondDisPlayQrCode = false;
            }
        } else {
            secondDisPlayQrCode = false;
        }
        try {
            if (secondDisPlayQrCode) {
                //扫描二维码后请求地址
                if (mBitmap == null) {
                    requestUuid = SystemUtils.genOnlyIdentifier();
                    String url = ServerAddressUtil.getInstance().getOpenIdUrl() + requestUuid;
                    mBitmap = EncodingHandler.createQRCode(url, barcodeWH);
                }
            }

        } catch (WriterException e) {
            Log.e(TAG, "", e);
        }
        if (secondDisPlayQrCode) {//有二维码
            //DisplayServiceManager.doUpdateLoginInfo(getActivity(), DisPlayLoginInfo.COMMAND_NORMAL, phone, mBitmap, uiType == UI_TYPE_REGISTER);
        } else {
            if (uiType == UI_TYPE_REGISTER) {//注册页没有二维码展示广告
                DisplayServiceManager.doCancel(getActivity());
            } else {//展示手机号输入
                /*DisplayUserInfo dUserInfo =
                        DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_ACCOUNT_INPUT, phone, null, 0, true, 0);
                DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
            }
        }
    }

    public interface CallbackListener {
        void setType(int uiType);
    }
}
