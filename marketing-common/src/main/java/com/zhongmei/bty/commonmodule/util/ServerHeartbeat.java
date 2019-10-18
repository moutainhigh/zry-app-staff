package com.zhongmei.bty.commonmodule.util;


import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.http.CalmStringRequest;
import com.zhongmei.yunfu.net.RequestManager;
import com.zhongmei.yunfu.net.RequestManagerCompat;
import com.zhongmei.yunfu.net.request.StringRequest;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.context.base.BaseApplication;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

import static com.zhongmei.bty.commonmodule.util.ServerHeartbeat.NetworkState.NetworkAvailable;




public class ServerHeartbeat {
    private static final String TAG = ServerHeartbeat.class.getSimpleName();

    public enum NetworkState {
        NetworkUnavailable,
        NetworkAvailable,
    }

    private String url;
    private NetworkState lastState = NetworkAvailable;
    private static final int INTERVAL_HEARTBEAT_SECOND = 10;
    private static final int TIMES_FAIL_RETRY = 3;    private int mTimesLeftFailRetry = TIMES_FAIL_RETRY;
    private boolean hasBeenStarted = false;
    private static ServerHeartbeat netWorkCheckUtil;
    private ScheduledExecutorService executor;

    private ServerHeartbeat() {
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    private void httpRequest() {
                if (TextUtils.isEmpty(url)) {
            return;
        }

        StringRequest request = new CalmStringRequest.BusinessStringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: server heartbeat success");
                if (response == null) {
                    heartbeatFail();
                } else {
                    heartbeatSuccess();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: server heartbeat fail");
                heartbeatFail();
            }
        });
        RequestManagerCompat.addRequest(BaseApplication.sInstance, request, "server_heartbeat");
    }


    public void stop() {
        if (!hasBeenStarted) {
            throw new IllegalStateException("server heartbeat not started");
        }
                RequestManager.cancelAll("server_heartbeat");
        hasBeenStarted = false;
        executor.shutdownNow();
        netWorkCheckUtil = null;
    }


    public void start(String url) {
        if (hasBeenStarted) {
            throw new IllegalStateException("server heartbeat already started");
        }
                hasBeenStarted = true;
        this.url = url;
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "server heartbeat is  running");
                httpRequest();
            }
        }, 0, INTERVAL_HEARTBEAT_SECOND, TimeUnit.SECONDS);
    }

    private void heartbeatFail() {
                if (--mTimesLeftFailRetry <= 0) {
                        postEventByCurrentState(NetworkState.NetworkUnavailable);
        }
    }


    private void heartbeatSuccess() {
        mTimesLeftFailRetry = TIMES_FAIL_RETRY;
        postEventByCurrentState(NetworkAvailable);
    }


    private void postEventByCurrentState(NetworkState currentState) {
        switch (lastState) {
            case NetworkUnavailable:
                if (currentState == NetworkAvailable) {
                    EventBus.getDefault().post(new EventServerAvailableStateChange(currentState));
                }
                break;
            case NetworkAvailable:
                if (currentState == NetworkState.NetworkUnavailable) {
                                        EventBus.getDefault().post(new EventServerAvailableStateChange(currentState));
                }
                break;
            default:
                break;
        }
        lastState = currentState;
    }

    public static ServerHeartbeat getInstance() {
        if (netWorkCheckUtil == null) {
            netWorkCheckUtil = new ServerHeartbeat();
        }
        return netWorkCheckUtil;
    }


    public synchronized NetworkState getNetworkState() {
        return lastState;
    }

}
