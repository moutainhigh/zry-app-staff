package com.zhongmei.yunfu.net;

import android.content.Context;

import com.zhongmei.yunfu.net.volley.Request;
import com.zhongmei.OSLog;

import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public class RequestManagerCompat {

    private static final OSLog LOG = OSLog.getLog("http");

    public static void log(String msg, Object... args) {
        LOG.log(msg, args);
    }

    public static void addRequest(Context context, Request<?> request, Object tag) {
        try {
            LOG.log("[%s]%s heads:%s, body: %s",
                    request.getHeaders().get(HttpConstant.YF_API_MSG_ID),
                    request.getUrl(),
                    getHeadersNoError(request),
                    getBodyString(request));
            RequestManager.addRequest(context, request, tag);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public static String getBodyString(Request<?> request) {
        try {
            return new String(request.getBody(), "utf-8");
        } catch (Exception error) {
            return null;
        }
    }

    public static Map<String, String> getHeadersNoError(Request<?> request) {
        try {
            return request.getHeaders();
        } catch (Exception error) {
            return null;
        }
    }
}
