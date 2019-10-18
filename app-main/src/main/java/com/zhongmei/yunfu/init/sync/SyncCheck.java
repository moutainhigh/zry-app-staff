package com.zhongmei.yunfu.init.sync;

import android.content.Context;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.init.sync.bean.SyncContent;

import java.util.Set;

public class SyncCheck extends Check {

    public SyncCheck(Context context) {
        super(context, context.getString(R.string.init), false);
    }

    @Override
    public void running(Set<String> modules) {
        update(mContext.getString(R.string.init));
        SyncContent.setSyncModuleConfig(null);
        SyncServiceUtil.startService(MainApplication.getInstance());
        SyncServiceManager.addCheckListener(new SyncCheckListener() {
            @Override
            public void onSyncCheck(boolean success, String errorMsg, Throwable err) {
                SyncServiceManager.removeCheckListener(this);
                if (success) {
                    update(mContext.getString(R.string.push_config_init));
                    startPushService(MainApplication.getInstance());
                } else {
                    error(errorMsg, err);
                }
            }
        });
    }

    public void startPushService(Context context) {
        success(mContext.getString(R.string.init_success));
                            }
}
