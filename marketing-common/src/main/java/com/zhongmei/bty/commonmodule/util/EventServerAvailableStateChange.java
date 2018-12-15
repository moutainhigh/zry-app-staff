package com.zhongmei.bty.commonmodule.util;

/**
 * Created by demo on 2018/12/15
 * 服务器心跳状态改变的event,只在连接服务器状态改变时发送
 */

public class EventServerAvailableStateChange {
    private ServerHeartbeat.NetworkState state;

    public EventServerAvailableStateChange(ServerHeartbeat.NetworkState state) {
        this.state = state;
    }

    public ServerHeartbeat.NetworkState getState() {
        return state;
    }

    public void setState(ServerHeartbeat.NetworkState state) {
        this.state = state;
    }
}
