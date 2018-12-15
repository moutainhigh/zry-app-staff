package com.zhongmei.bty.basemodule.devices.mispos.watchdata.readcard;

import android.content.Context;

/**
 * 读卡公共接口
 */

public interface IReadCard {
    /**
     * 连接读卡设备，并开始读卡任务
     */
    void connectDevice(Context context, ReadCardResponseListener listener, boolean isDelay);

    /**
     * 断开读卡设备
     */
    void disconnectDevice();
}
