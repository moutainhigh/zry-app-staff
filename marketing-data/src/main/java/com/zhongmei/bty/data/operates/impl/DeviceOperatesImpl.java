package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.http.CalmNetWorkRequest;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.data.operates.DeviceOperates;
import com.zhongmei.bty.data.operates.message.content.CheckAuthReq;
import com.zhongmei.bty.data.operates.message.content.CheckAuthResp;
import com.zhongmei.bty.data.operates.message.content.CheckVersionsAreaReq;
import com.zhongmei.bty.data.operates.message.content.CheckVersionsAreaResp;
import com.zhongmei.bty.data.operates.message.content.DeviceAuthReq;
import com.zhongmei.bty.data.operates.message.content.DeviceAuthResp;

/**
 * 设备相关
 * Created by demo on 2018/12/15
 */
public class DeviceOperatesImpl extends AbstractOpeartesImpl implements DeviceOperates {


    public DeviceOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void getDeviceAuth(DeviceAuthReq mDeviceAuthReq, ResponseListener<DeviceAuthResp> listener) {
        String url = ServerAddressUtil.getInstance().getDeviceAuth();
        OpsRequest.Executor<DeviceAuthReq, DeviceAuthResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(mDeviceAuthReq)
                .responseClass(DeviceAuthResp.class)
                .interceptEnable(true)
                .execute(listener, "deviceAuth");
    }

    @Override
    public void checkVersionsArea(CheckVersionsAreaReq req, CalmResponseListener<ResponseObject<CheckVersionsAreaResp>> listener) {
        String url = ServerAddressUtil.getInstance().checkVersionsAreaInfo() +
                "versionCode=" + req.getVersionCode() +
                "&brandId=" + req.getBrandId() +
                "&appType=" + req.getAppType();
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(url)
                .requestContent(req)
                .responseClass(CheckVersionsAreaResp.class)
                .interceptEnable(true)
                .successListener(listener)
                .errorListener(listener)
                .tag("checkVersionsArea")
                .create();
    }

    @Override
    public void checkAuth(CheckAuthReq req, CalmResponseListener<ResponseObject<CheckAuthResp>> listener) {
        String url = ServerAddressUtil.getInstance().checkAuth() + "?"
                + "brandId=" + req.getBrandId()
                + "&commercialId=" + req.getCommercialId();
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(url)
                .requestContent(req)
                .responseClass(CheckAuthResp.class)
                .interceptEnable(true)
                .successListener(listener)
                .errorListener(listener)
                .tag("checkAuth")
                .create();
    }
}
