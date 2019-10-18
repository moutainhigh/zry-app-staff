package com.zhongmei.yunfu.init.sync;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.erp.operates.ErpOperates;
import com.zhongmei.bty.basemodule.session.SessionImpl;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.sync.InitCheck;
import com.zhongmei.OSLog;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.ShopInfo;
import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.monitor.ResponseStringListener;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;


public class ErpCheck {

    private static final String TAG = ErpCheck.class.getSimpleName();
    private Context mContext;
    private ErpCheckCallback checkCallback;

    public ErpCheck(Context context) {
        this.mContext = context;
            }

    public void check(ErpCheckCallback callback) {
        this.checkCallback = callback;
        update(mContext.getString(R.string.request_server_address));
        ErpOperates erpOperates = OperatesFactory.create(ErpOperates.class);
        erpOperates.getErp(responseListener(), errorListener());
    }

    private ResponseStringListener<YFResponse<ShopInfo>> responseListener() {
        return new ResponseStringListener<YFResponse<ShopInfo>>() {
            @Override
            public void onResponse(YFResponse<ShopInfo> response) {
                if (null == response) {
                    Log.i(TAG, "onResponse is null");
                    error(mContext.getString(R.string.get_server_fail));
                    return;
                }

                if (!response.isOk()) {
                    error(response.getMessage());
                    return;
                }

                ShopInfoManager.init(response.getContent());
                if (response.getContent().getShopId() == null) {
                    Log.i(TAG, mContext.getString(R.string.get_shop_fail));
                    error(mContext.getString(R.string.get_shop_fail));
                    return;
                }

                if (TextUtils.isEmpty(ShopInfoManager.getInstance().getShopInfo().getSyncUrl())) {
                    Log.i(TAG, mContext.getString(R.string.request_server_address_failed));
                    error(mContext.getString(R.string.request_server_address_failed));
                    return;
                }



                Session.init(new SessionImpl(MainApplication.getInstance(), SessionImpl.AV1));
                success(mContext.getString(R.string.request_server_address_success));
            }
        };
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                OSLog.error("onErrorResponse,error=" + error.toString());
                error(error.getMessage());
            }
        };
    }

    private void update(String errorMsg) {
        error(0, errorMsg);
    }

    private void success(String errorMsg) {
        error(-1, errorMsg);
    }

    public void error(String errorMsg) {
        error(InitCheck.ERROR_CODE_OTHER, errorMsg);
    }

    private void error(int errorCode, String errorMsg) {
        error(errorCode, errorMsg, null);
    }

    private void error(int errorCode, String errorMsg, Throwable err) {
        if (checkCallback != null) {
            checkCallback.onErpCheck(errorCode, errorMsg, err);
        }
    }

    public interface ErpCheckCallback {

        void onErpCheck(int errorCode, String errorMsg, Throwable err);
    }
}
