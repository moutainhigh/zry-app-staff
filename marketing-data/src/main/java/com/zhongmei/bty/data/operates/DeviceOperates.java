package com.zhongmei.bty.data.operates;

import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
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
public interface DeviceOperates extends IOperates {

    /**
     * 获取设备认证token
     *
     * @return
     */
    public void getDeviceAuth(DeviceAuthReq mDeviceAuthReq, ResponseListener<DeviceAuthResp> listener);

    public void checkVersionsArea(CheckVersionsAreaReq req, CalmResponseListener<ResponseObject<CheckVersionsAreaResp>> listener);

    void checkAuth(CheckAuthReq req, CalmResponseListener<ResponseObject<CheckAuthResp>> listener);
}
