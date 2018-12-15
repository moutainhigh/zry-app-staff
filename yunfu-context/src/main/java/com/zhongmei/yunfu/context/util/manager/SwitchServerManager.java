package com.zhongmei.yunfu.context.util.manager;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.util.NetworkUtil;

/**
 * 服务器切换manager
 * Created by demo on 2018/12/15
 */
public class SwitchServerManager {

    private final static String TAG = SwitchServerManager.class.getSimpleName();

    private final static int COUNT_TO_SWITCH_SERVER = 3;//需要切换服务器的失败次数

    private static SwitchServerManager instance;

    private int retryFailCount = 0;

    //private String serverKey = ShopInfo.SERVER_KEY;
    private boolean isBackupSyncUrlEnabled = false; //是否启用备用的同步地址

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

    /**
     * 判断volley返回的error是否为服务器类型错误
     *
     * @param error
     * @return
     */
    public boolean isServerError(Exception error) {
//        return error instanceof NetworkError || error instanceof ServerError || error instanceof TimeoutError;
        return true;//目前统一认为是服务器异常
    }

    public boolean isServerException(Exception e) {
//        return e instanceof ConnectException || e instanceof SocketException || e instanceof SocketTimeoutException
//                || e instanceof IOException;
        return true;//目前统一认为是服务器异常
    }

    /**
     * 同步服务器失败次数＋1
     */
    public synchronized void retryFailCount() {
        retryFailCount++;
        Log.i(TAG, "连接失败次数：" + retryFailCount);
        if (retryFailCount >= COUNT_TO_SWITCH_SERVER) {
            isBackupSyncUrlEnabled = !isBackupSyncUrlEnabled;

            //切换后，将失败次数置为0
            reset();
        }
    }

    /**
     * 重置服务器失败次数
     */
    public synchronized void reset() {
        retryFailCount = 0;
    }

    public boolean isBackupSyncUrlEnabled() {
        return isBackupSyncUrlEnabled;
    }

    public String getServerKey() {
        String serverKey = ShopInfoManager.REMOTE_SERVER_HOST;
//        String serverKey = getAddress(syncUrl, backSyncUrl);
//        //补齐https前缀
//        if (!TextUtils.isEmpty(serverKey)) {//add 20171204
//            if (!serverKey.startsWith("http")) {
//                serverKey = "https://" + serverKey;
//            } else {
//                serverKey = serverKey.replace("http://", "https://");
//            }
//        }
        return serverKey;
    }

    public String getAddress(String syncUrl, String backSyncUrl) {
        if (isBackupSyncUrlEnabled && !TextUtils.isEmpty(backSyncUrl)) {
            return backSyncUrl;
        }

        return syncUrl;
    }

    static public class SyncRetryPolicy {
        public static final int DEFAULT_MAX_RETRIES = 3; //重试次数
        private int retryCount = 0;

        /**
         * 是否重试
         *
         * @return
         */
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
