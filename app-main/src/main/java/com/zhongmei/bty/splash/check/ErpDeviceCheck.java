package com.zhongmei.bty.splash.check;

import android.content.Context;

import com.zhongmei.OSLog;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.data.operates.CommercialDal;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.sync.CheckManager;
import com.zhongmei.yunfu.sync.InitCheck;

import java.util.List;

/**
 * 检查设备配置的商户信息
 *
 * @created 2017/05/09
 */
public class ErpDeviceCheck {

    private static final String TAG = ErpDeviceCheck.class.getSimpleName();
    private Context mContext;
    private ErpCheckCallback checkCallback;

    public ErpDeviceCheck(Context context) {
        this.mContext = context;
        //super(context, context.getString(R.string.get_server_address), false);
    }

    public void check(ErpCheckCallback callback) {
        this.checkCallback = callback;
        update("[ERP]" + mContext.getString(R.string.request_server_address));
        checkHaveDirtyData();
    }

    public void checkHaveDirtyData() {
        CommercialDal mCommercialDal = OperatesFactory.create(CommercialDal.class);
        List<Long> listShopId = mCommercialDal.queryAllShopIdenty();
        String newShopId = ShopInfoCfg.getInstance().shopId;
        if (listShopId != null && listShopId.size() > 1) {
            removeDBFile();
        } else if (listShopId != null && listShopId.size() == 1) {
            Boolean haveOldData = false;
            for (Long shopId : listShopId) {
                if (!newShopId.equals(Long.toString(shopId))) {
                    haveOldData = true;
                    removeDBFile();
                    break;
                }
            }
            if (!haveOldData) {
                checkAutoSetFlag();
            }
        } else {
            checkAutoSetFlag();
        }
    }

    private void removeDBFile() {
        //ResetFragment.show(this, ResetFragment.VERSION);
        error(InitCheck.ERROR_CODE_VERSION, mContext.getResources().getString(R.string.dataError));
    }

    /**
     * 自动配置向导检查成功失败不受影响
     */
    public void checkAutoSetFlag() {
        if (CheckManager.isIgnore(InitCheck.ERROR_CODE_AUTO_CONFIG)) {
            success(mContext.getString(R.string.request_server_address_success));
            return;
        }

        /*PrintSettingDal dal = OperatesFactory.create(PrintSettingDal.class);
        dal.queryAutoSetFlag(new EventResponseListener<MindTransferResp<Integer>>(UserActionEvent.INIT_PROCESS) {
            @Override
            public void onResponse(ResponseObject<MindTransferResp<Integer>> response) {
                MindTransferResp<Integer> content = response.getContent();
                if (MindTransferResp.isOk(content)) {
                    Integer flag = content.getData();
                    if (Utils.equals(flag, 0)) {
                        error(InitCheck.ERROR_CODE_AUTO_CONFIG, mContext.getString(R.string.confirm_goto_autoset));
                        return;
                    }
                }

                success(mContext.getString(R.string.request_server_address_success));
            }

            @Override
            public void onError(VolleyError error) {
                success(mContext.getString(R.string.request_server_address_success));
            }
        });*/
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                OSLog.error("onErrorResponse,error=" + error.toString());
                error(InitCheck.ERROR_CODE_OTHER, "[ERP]" + mContext.getString(R.string.get_server_info_failed), error);
            }
        };
    }

    private void update(String errorMsg) {
        error(0, errorMsg, null);
    }

    private void success(String errorMsg) {
        error(-1, errorMsg, null);
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
        /**
         * Erp检查回调
         *
         * @param errorCode 0正在执行 -1检查完成 其它为错误状态
         * @param errorMsg
         */
        void onErpCheck(int errorCode, String errorMsg, Throwable err);
    }
}
