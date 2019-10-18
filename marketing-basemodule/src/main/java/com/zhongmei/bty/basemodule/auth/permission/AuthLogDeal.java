package com.zhongmei.bty.basemodule.auth.permission;

import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.auth.permission.message.AuthLogResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.AuthorizedLog;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class AuthLogDeal {

        private static final int RECYLE_TIME = 10 * 60 * 1000;
        private static final int QUERYCOUNT = 30;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private boolean isStart = false;

    public void start() {
        if (!isStart) {
            mTimer = new Timer();
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    deal(false);
                }
            };
            mTimer.schedule(mTimerTask, 0, RECYLE_TIME);
            isStart = true;
        }
    }

    public void stop() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        isStart = false;
    }


    private void deal(final boolean isRetry) {
        List<AuthorizedLog> authList = AuthLogManager.getInstance().queryList(-1);
        if (authList == null || authList.isEmpty()) {
            return;
        }
        TradeOperates operates = OperatesFactory.create(TradeOperates.class);
        operates.batchUploadAuthLog(authList, new ResponseListener<AuthLogResp>() {
            @Override
            public void onResponse(ResponseObject<AuthLogResp> response) {
                if (ResponseObject.isOk(response)) {
                    AuthLogManager.getInstance().deleteLogs(response.getContent());
                }
            }

            @Override
            public void onError(VolleyError error) {
                                if (!isRetry) {
                    deal(true);
                }
            }
        });
    }

}
