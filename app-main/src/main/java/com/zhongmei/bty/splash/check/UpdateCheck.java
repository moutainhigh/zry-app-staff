package com.zhongmei.bty.splash.check;

import android.content.Context;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.init.sync.Check;
import com.zhongmei.yunfu.monitor.EventListener;
import com.zhongmei.OSLog;
import com.zhongmei.yunfu.net.volley.Request.Method;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.context.data.VersionInfo;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.http.CalmStringRequest;

import java.util.Set;


public abstract class UpdateCheck extends Check {

    private String checkUrl;

    public UpdateCheck(Context context, String title, String checkUrl, boolean canContinue) {

        super(context, title, canContinue);
        this.checkUrl = checkUrl;
    }

    @Override
    public void running(Set<String> modules) {
        update(mContext.getString(R.string.update_check));
        CalmStringRequest quest1 = new CalmStringRequest(mContext, Method.GET,
                checkUrl,
                responseListener(), errorListener());
        quest1.executeRequest("2");    }

    private Response.Listener<String> responseListener() {
        return new EventListener<String>(UserActionEvent.INIT_PROCESS) {
            @Override
            public void onResponse(String response) {
                if (null == response) {
                    OSLog.error("onResponse is null");
                    onError(mContext.getString(R.string.update_check_failed));
                } else {
                    OSLog.info("onResponse =" + response);


                    VersionInfo versionInfo = VersionInfo.create(response);
                    if (versionInfo != null) {
                        onSuccess(versionInfo);
                    } else {
                        onError(mContext.getString(R.string.update_check_failed));
                    }
                }

            }
        };
    }

    protected void onSuccess(VersionInfo response) {

        success(mContext.getString(R.string.update_check_success));
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError(error.getMessage());
            }
        };
    }

    protected void onError(String errorMsg) {
        OSLog.error("onErrorResponse,error=" + errorMsg);
        error(mContext.getString(R.string.update_check_error, errorMsg));
    }
}
