package com.zhongmei.yunfu.sync;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;


public class ShopDataInitCheck extends AbsInitCheck {

    private CheckUtil mCheckUtil;

    public ShopDataInitCheck() {
        super(CheckType.SyncInitCheck);
    }

    @Override
    public void handleCheck(InitCheckCallback callback) {
        initSyncData();
    }

    private void initSyncData() {
        checkProgress(this, 10, MainApplication.getInstance().getString(R.string.init));
        mCheckUtil = new CheckUtil(MainApplication.getInstance(), callBack);
        mCheckUtil.start(null);
    }

    @Override
    public void cancel() {
        super.cancel();
        if (mCheckUtil != null) {
            mCheckUtil.cancelCheck();
            mCheckUtil = null;
        }
    }

    private CheckCallback callBack = new CheckCallback() {

        @Override
        public void onCheckStart() {
        }

        @Override
        public void onCheckRunning(String hint, int process) {
            checkProgress(ShopDataInitCheck.this, Math.max(1, process), hint);
        }

        @Override
        public void onCheckOver(int type, final String hint, Throwable err) {
            switch (type) {
                case CheckCallback.CHECK_RESULT_RESTART:
                    checkComplete(ShopDataInitCheck.this, false, hint, err);
                    break;
                case CheckCallback.CHECK_RESULT_SUCCESS:
                    completeCheck();
                    break;
            }
        }
    };

    private void completeCheck() {
        checkProgress(this, 100, MainApplication.getInstance().getString(R.string.login_check_config));
    }

    @Override
    protected void onInit() {
        super.onInit();
    }
}
