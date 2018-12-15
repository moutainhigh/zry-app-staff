package com.zhongmei.bty.data.operates;

import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.sync.push.SysCmdResponse;

/**
 * Created by demo on 2018/12/15
 */
public interface PushOperates extends IOperates {

    /**
     * 1. 将一些和客户端底层功能相关的可能经常变动的参数配置到服务器端维护， 客户端开机或初始化之前请求此接口获取一些动态参信息（也可长定时间隔触发请求）
     * 2. 4.29迭代添加PUSH相关参数配置，后续会继续增加如10分钟轮询同步的模块列表等配置
     *
     * @param listener;
     */
    void getSysCmdConfig(CalmResponseListener<ResponseObject<SysCmdResponse>> listener);

    void onlineSwitch(boolean onlineSwitch, CalmResponseListener<ResponseObject<SysCmdResponse>> listener);
}
