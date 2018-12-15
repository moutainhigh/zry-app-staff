package com.zhongmei.yunfu.init.sync;

import android.content.Context;

import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.yunfu.init.sync.bean.SyncModule;
import com.zhongmei.yunfu.net.HttpConstant;
import com.zhongmei.yunfu.context.util.manager.SwitchServerManager;
import com.zhongmei.yunfu.init.sync.bean.SyncContent;
import com.zhongmei.yunfu.init.sync.bean.SyncRequest;
import com.zhongmei.yunfu.init.sync.bean.SyncResponse;

import java.util.List;
import java.util.Map;

/**
 * 同步数据任务
 *
 * @created 2017/5/5
 */
public class SyncRequestTask implements Runnable {

    private Context context;
    private SyncRequestManager requestManager;
    private SyncModule syncModule;
    private List<String> modules;
    private boolean isNotify;
    private boolean isSyncModule;
    private volatile boolean done;
    private volatile boolean cancelled;
    private volatile Throwable throwable;

    public SyncRequestTask(Context context, SyncRequestManager syncRequestManager, List<String> modules, boolean isNotify, boolean isSyncModule) {
        this.context = context;
        this.requestManager = syncRequestManager;
        this.syncModule = syncRequestManager.getSyncModule();
        this.modules = modules;
        this.isNotify = isNotify;
        this.isSyncModule = isSyncModule;
    }

    @Override
    public void run() {
        try {
            synServerModule(context, modules, isNotify);
            SyncServiceUtil.log("同步完成");
            requestManager.notifyChildThread(this, null);
        } catch (Throwable e) {
            SyncServiceUtil.error(e, "同步出错：%s", e.getMessage());
            requestManager.notifyChildThread(this, e);
        }
    }

    private void synServerModule(Context context, List<String> modules, boolean notify) throws Exception {
        SwitchServerManager.SyncRetryPolicy retryPolicy = SwitchServerManager.newRetryPolicy();
        SyncSession session = new SyncSession();
        while (!session.isSyncSuccess() && modules.size() > 0) {
            checkCancelled();

            String url = ServerAddressUtil.getInstance().getOwns();
            SyncHttpCaller caller = new SyncHttpCaller(url);
            try {
                SyncServiceUtil.log("开始同步[%s]: %s", caller.getHttpProperty(HttpConstant.YF_API_MSG_ID), url);
                final Map<String, String> syncMarkMap = SyncDatabaseOps.getSyncMarkMap();
                SyncContent content = SyncDatabaseOps.getInstance().querySyncContent(modules, syncMarkMap);
                SyncRequest syncRequest = SyncRequest.create(context, content, notify);
                SyncResponse syncResponse = caller.call(syncRequest);
                if (SyncResponse.isOk(syncResponse)) {
                    session.commit(syncModule, modules, syncMarkMap, syncResponse);
                } else {
                    String messageId = syncResponse != null ? syncResponse.getMessageId() : null;
                    String message = syncResponse != null ? syncResponse.getMessage() : "SyncRequest is null";
                    throw new SyncException(messageId, message);
                }
            } catch (Exception e) {
                SyncServiceUtil.log(e, "同步出錯msg[%s]：%s", caller.getHttpProperty(HttpConstant.YF_API_MSG_ID), e.getMessage());
                retryPolicy.retry(e);
            }
        }
    }

    private void checkCancelled() throws SyncCancelException {
        if (cancelled) {
            throw new SyncCancelException("sync child cancel");
        }
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(Throwable error) {
        done = true;
        throwable = error;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
