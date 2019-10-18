package com.zhongmei.yunfu.monitor;

import com.zhongmei.yunfu.net.builder.NetError;


public class CalmSyncResponseListener extends CalmResponseListener {

    private CalmResponseListener calmResponseListener;
    private boolean isCancelled = false;

    public CalmSyncResponseListener(CalmResponseListener calmResponseListener) {
        this.calmResponseListener = calmResponseListener;
    }

    public static CalmSyncResponseListener newCalmSync(CalmResponseListener listener) {
        return new CalmSyncResponseListener(listener);
    }

    public void cancel() {
        isCancelled = true;
    }

    @Override
    public void onSuccess(Object data) {
        if (isCancelled) {
            return;
        }

        if (calmResponseListener != null) {
            calmResponseListener.onSuccess(data);
        }
    }

    @Override
    public void onError(NetError error) {
        if (isCancelled) {
            return;
        }

        if (calmResponseListener != null) {
            calmResponseListener.onError(error);
        }
    }
}
