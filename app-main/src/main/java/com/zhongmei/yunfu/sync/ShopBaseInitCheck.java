package com.zhongmei.yunfu.sync;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.NetworkUtil;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.init.sync.AppUpdateCheck;
import com.zhongmei.yunfu.init.sync.Check;
import com.zhongmei.yunfu.init.sync.CheckListener;
import com.zhongmei.yunfu.init.sync.ErpCheck;

import java.util.ArrayList;
import java.util.List;


public class ShopBaseInitCheck extends AbsInitCheck {

    public ShopBaseInitCheck() {
        super(CheckType.BaseInitCheck);
    }

    @Override
    public void handleCheck(InitCheckCallback callback) {
        checkProgress(this, 1, MainApplication.getInstance().getString(R.string.login_check_device));
        waitRandom();
        if (!NetworkUtil.isNetworkConnected()) {
            checkComplete(this, false, getConfirmDialogError(InitCheck.ERROR_NETWORK) + MainApplication.getInstance().getString(R.string.login_check_network_error));
            return;
        }

        NetworkUtil.saveIpAddress(BaseApplication.sInstance);
        ErpCheck erpDeviceCheck = new ErpCheck(MainApplication.getInstance());
        erpDeviceCheck.check(new ErpCheck.ErpCheckCallback() {
            @Override
            public void onErpCheck(int errorCode, String errorMsg, Throwable err) {
                if (errorCode > 0) {
                    String errMsg = errorMsg;
                    if (errorCode != InitCheck.ERROR_CODE_OTHER) {
                        errMsg = getConfirmDialogError(errorCode) + errorMsg;
                    }
                    checkComplete(ShopBaseInitCheck.this, false, errMsg, err);
                    return;
                }

                checkProgress(ShopBaseInitCheck.this, 50, errorMsg);
                if (errorCode == -1) {
                    checkUpdateAll();
                }
            }
        });
    }

    private void checkUpdateAll() {
        List<Check> checkList = new ArrayList<>();
        checkList.add(new AppUpdateCheck(MainApplication.getInstance()));
                checkUpdate(checkList, 0);
    }

    private void checkUpdate(final List<Check> checkList, final int index) {
        if (index >= checkList.size()) {
            checkProgress(this, 100, MainApplication.getInstance().getString(R.string.login_check_device));
            return;
        }

        Check check = checkList.get(index);
        check.setCheckListener(new CheckListener() {
            @Override
            public void update(Check check, String hint) {
                checkProgress(ShopBaseInitCheck.this, 1, hint);
            }

            @Override
            public void onSuccess(Check check, String hint) {
                int flag = getCheckUpdate(check);
                boolean isForce = ShopInfoCfg.getInstance().getAppVersionInfo().isForce();
                boolean isNotRemindUpdate = SpHelper.getDefault().getBoolean(SpHelper.APP_UPDATE_NOT_SECOND_REMIND, false);
                if (!CheckManager.isIgnore(flag) && (isUpdateApp(flag))) {
                    if (isNotRemindUpdate && !isForce) {
                        checkUpdate(checkList, index + 1);
                    } else {
                        checkComplete(ShopBaseInitCheck.this, false, getConfirmDialogError(flag));
                    }
                    return;
                }

                checkUpdate(checkList, index + 1);
            }

            @Override
            public void onError(Check check, String errorMsg, Throwable err) {
                                onSuccess(check, errorMsg);
            }
        });
        check.running(null);
    }

    private boolean isUpdateApp(int flag) {
        return flag == InitCheck.CHECK_UPDATE_APP
                && ShopInfoCfg.getInstance().getAppVersionInfo().hasUpdate();
    }

    private int getCheckUpdate(Check check) {
        if (check instanceof AppUpdateCheck) {
            return InitCheck.CHECK_UPDATE_APP;
        }



        return 0;
    }
}
