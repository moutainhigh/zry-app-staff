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


/**
 * Created by demo on 2018/12/15
 * 检测服务器的连接状态，当状态改变时，发送消息
 */

public class ServerHeartbeat {
    private static final String TAG = ServerHeartbeat.class.getSimpleName();

    public enum NetworkState {
        NetworkUnavailable,
        NetworkAvailable,
    }

    private String url;
    private NetworkState lastState = NetworkAvailable;
    private static final int INTERVAL_HEARTBEAT_SECOND = 10;
    private static final int TIMES_FAIL_RETRY = 3;//失败重试次数,次数结束才算是测试失败
    private int mTimesLeftFailRetry = TIMES_FAIL_RETRY;
    private boolean hasBeenStarted = false;
    private static ServerHeartbeat netWorkCheckUtil;
    private ScheduledExecutorService executor;

    private ServerHeartbeat() {
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    private void httpRequest() {
        //如果网络检测地址为空，那么不执行请求
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

    /**
     * 停止网络监测
     */
    public void stop() {
        if (!hasBeenStarted) {
            throw new IllegalStateException("server heartbeat not started");
        }
        //SLog.d(SLog.TAG_KEY, "info:停止网络以及服务器监测;position:" + TAG + "->stop()");
        RequestManager.cancelAll("server_heartbeat");
        hasBeenStarted = false;
        executor.shutdownNow();
        netWorkCheckUtil = null;
    }

    /**
     * 开始服务器检测
     */
    public void start(String url) {
        if (hasBeenStarted) {
            throw new IllegalStateException("server heartbeat already started");
        }
        //SLog.d(SLog.TAG_KEY, "info:开始网络以及服务器监测;position:" + TAG + "->start()");
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
        //SLog.d(SLog.TAG_KEY, "info:网络以及服务器监测失败,判断是否超时与剩余重试次数;"+";mTimesLeftFailRetry:"+mTimesLeftFailRetry+ ";position:" + TAG + "->heartbeatFail()");
        if (--mTimesLeftFailRetry <= 0) {
            //SLog.d(SLog.TAG_KEY, "info:网络以及服务器监测失败,判断是否超时与剩余重试次数;lastState:" + lastState +";currentState:"+ NetworkState.NetworkUnavailable+ ";position:" + TAG + "->heartbeatFail()");
            postEventByCurrentState(NetworkState.NetworkUnavailable);
        }
    }


    private void heartbeatSuccess() {
        mTimesLeftFailRetry = TIMES_FAIL_RETRY;
        postEventByCurrentState(NetworkAvailable);
    }

    /**
     * 根据当前状态和上次状态来判断状态是否改变，并更新状态
     *
     * @param currentState 当前状态
     */
    private void postEventByCurrentState(NetworkState currentState) {
        switch (lastState) {
            case NetworkUnavailable:
                if (currentState == NetworkAvailable) {
                    EventBus.getDefault().post(new EventServerAvailableStateChange(currentState));
                }
                break;
            case NetworkAvailable:
                if (currentState == NetworkState.NetworkUnavailable) {
                    //SLog.d(SLog.TAG_KEY, "info:网络以及服务器监测失败,更改网络状态;position:" + TAG + "->heartbeatFail()");
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

    /**
     * 返回当前网络状态
     */
    public synchronized NetworkState getNetworkState() {
        return lastState;
    }

}
