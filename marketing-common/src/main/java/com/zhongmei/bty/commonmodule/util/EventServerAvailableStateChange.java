package com.zhongmei.bty.commonmodule.util;



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
