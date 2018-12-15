package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.yunfu.http.CalmNetWorkRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.data.operates.PushOperates;
import com.zhongmei.bty.sync.push.SysCmdResponse;

/**
 * Created by demo on 2018/12/15
 */
public class PushOperatesImpl extends AbstractOpeartesImpl implements PushOperates {

    public PushOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void getSysCmdConfig(CalmResponseListener<ResponseObject<SysCmdResponse>> listener) {
        String url = ServerAddressUtil.getInstance().getSysCmdConfig();
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(url)
                .requestContent(new Object())
                .responseClass(SysCmdResponse.class)
                .successListener(listener)
                .errorListener(listener)
                .tag("getPushConfig")
                .create();
    }

    @Override
    public void onlineSwitch(boolean onlineSwitch, CalmResponseListener<ResponseObject<SysCmdResponse>> listener) {
        String url = ServerAddressUtil.getInstance().getOnlineSwitch();
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(url)
                .requestContent(new OnlineSwitchRequest(onlineSwitch ? OnlineSwitchRequest.Online : OnlineSwitchRequest.offline))
                .responseClass(SysCmdResponse.class)
                .successListener(listener)
                .errorListener(listener)
                .tag("onlineSwitch")
                .create();
    }

    static class OnlineSwitchRequest {
        static final int Online = 2; // 2.上线
        static final int offline = 3; //3.下线

        int type;

        public OnlineSwitchRequest(int type) {
            this.type = type;
        }
    }
}
