package com.zhongmei.yunfu.context.util.manager;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.util.NetworkUtil;


public class SwitchServerManager {

    private final static String TAG = SwitchServerManager.class.getSimpleName();

    private final static int COUNT_TO_SWITCH_SERVER = 3;
    private static SwitchServerManager instance;

    private int retryFailCount = 0;

        private boolean isBackupSyncUrlEnabled = false;
    private String syncUrl;
    private String backSyncUrl;

    private SwitchServerManager() {
    }

    public SwitchServerManager(String syncUrl, String backSyncUrl) {
        this.syncUrl = syncUrl;
        this.backSyncUrl = backSyncUrl;
    }

    public static synchronized SwitchServerManager getInstance() {
        if (instance == null) {
            instance = new SwitchServerManager();
        }
        return instance;
    }

    public static void init(String syncUrl, String backSyncUrl) {
        instance = new SwitchServerManager(syncUrl, backSyncUrl);
    }

    public static SyncRetryPolicy newRetryPolicy() {
        return new SyncRetryPolicy();
    }


    public boolean isServerError(Exception error) {
        return true;    }

    public boolean isServerException(Exception e) {
        return true;    }


    public synchronized void retryFailCount() {
        retryFailCount++;
        Log.i(TAG, "连接失败次数：" + retryFailCount);
        if (retryFailCount >= COUNT_TO_SWITCH_SERVER) {
            isBackupSyncUrlEnabled = !isBackupSyncUrlEnabled;

                        reset();
        }
    }


    public synchronized void reset() {
        retryFailCount = 0;
    }

    public boolean isBackupSyncUrlEnabled() {
        return isBackupSyncUrlEnabled;
    }

    public String getServerKey() {
        String serverKey = ShopInfoManager.REMOTE_SERVER_HOST;
        return serverKey;
    }

    public String getAddress(String syncUrl, String backSyncUrl) {
        if (isBackupSyncUrlEnabled && !TextUtils.isEmpty(backSyncUrl)) {
            return backSyncUrl;
        }

        return syncUrl;
    }

    static public class SyncRetryPolicy {
        public static final int DEFAULT_MAX_RETRIES = 3;         private int retryCount = 0;


        public synchronized void retry(Exception error) throws Exception {
            if (NetworkUtil.isNetworkConnected()) {
                retryCount++;
                if (retryCount <= DEFAULT_MAX_RETRIES) {
                    Log.i(TAG, "Sync连接失败次数：" + retryCount);
                    SwitchServerManager serverManager = SwitchServerManager.getInstance();
                    serverManager.retryFailCount();
                    return;
                }
            }

            throw error;
        }
    }
}
